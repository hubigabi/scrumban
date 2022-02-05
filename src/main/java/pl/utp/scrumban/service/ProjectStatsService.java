package pl.utp.scrumban.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.utp.scrumban.dto.stats.FinishedTasksColumn;
import pl.utp.scrumban.dto.stats.ProjectCumulativeStats;
import pl.utp.scrumban.model.Column;
import pl.utp.scrumban.model.Project;
import pl.utp.scrumban.dto.stats.ProjectChartStats;
import pl.utp.scrumban.model.Task;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class ProjectStatsService {

    private final ProjectService projectService;
    private final ColumnService columnService;
    private final TaskService taskService;
    private final AuditReader auditReader;

    @Autowired
    public ProjectStatsService(ProjectService projectService, ColumnService columnService,
                               TaskService taskService, AuditReader auditReader) {
        this.projectService = projectService;
        this.columnService = columnService;
        this.taskService = taskService;
        this.auditReader = auditReader;
    }

    //    Little's law
//    https://kanbanzone.com/littles-law/
    public List<ProjectChartStats> getProjectChartStats(Long projectID) {
        Project project = projectService.getProject(projectID);
        List<Task> tasks = taskService.findAllByProject_Id(projectID);
        List<ProjectChartStats> projectChartStats = new ArrayList<>();

        LocalDate startedProject = project.getStartedLocalDate();
        LocalDate finishedProject = project.getFinishedLocalDate() != null ? project.getFinishedLocalDate() : LocalDate.now();

//        if (finishedProject.isBefore(startedProject))
//            finishedProject = startedProject;
        final Integer MAX_COLUMN_NUMBER_ORDER = columnService.getMaxColumnNumberOrder(projectID);
        double activeTasks = 0;
        double dayCounter = 0;
        int startedTaskCounter = 0;
        int finishedTaskCounter = 0;
        double finishedTaskDayCounter = 0;

        for (LocalDate date = startedProject; !date.isAfter(finishedProject); date = date.plusDays(1)) {
            final LocalDate currentDate = date;

            int startedTaskToday = (int) tasks.stream()
                    .filter(task -> task.getStartedLocalDate() != null && task.getStartedLocalDate().equals(currentDate))
                    .count();
            startedTaskCounter += startedTaskToday;

            List<Task> finishedTaskToday = tasks.stream()
                    .filter(task -> task.getFinishedLocalDate() != null && task.getFinishedLocalDate().equals(currentDate)
                            && task.getColumn().getNumberOrder().equals(MAX_COLUMN_NUMBER_ORDER)
                    )
                    .collect(Collectors.toList());
            int finishedTaskTodayCounter = finishedTaskToday.size();
            finishedTaskCounter += finishedTaskTodayCounter;

            int finishedTaskTodayDayCounter = finishedTaskToday.stream()
                    .mapToInt(task -> (int) ChronoUnit.DAYS.between(task.getStartedLocalDate(), task.getFinishedLocalDate()))
                    .sum();
            finishedTaskDayCounter += finishedTaskTodayDayCounter;

            double throughput;
            if (dayCounter != 0) {
                throughput = finishedTaskCounter / dayCounter;
            } else {
                throughput = 0;
            }

            double leadTime;
            if (finishedTaskCounter != 0) {
                leadTime = finishedTaskDayCounter / finishedTaskCounter;
            } else {
                leadTime = 0;
            }

            double WIP = throughput * leadTime;
            activeTasks = activeTasks + startedTaskToday - finishedTaskTodayCounter;

            projectChartStats.add(new ProjectChartStats(currentDate, startedTaskCounter, activeTasks, finishedTaskCounter, throughput, leadTime, WIP));
            dayCounter++;
        }

        return projectChartStats;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class TaskRevision {
        private long id;
        private long columnId;
        private String columnName;
        private int columnOrder;
        private long timestamp;
        private RevisionType revisionType;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class TaskStatusStartDate {
        // taskId for testing purposes
        private long taskId;
        private long columnId;
        private String columnName;
        private int columnOrder;
        private LocalDate startLocalDate;
    }

    public ProjectCumulativeStats getProjectCumulativeStats(long projectId) {
        List<Long> tasksIdInProject = taskService.findAllByProject_Id(projectId).stream()
                .map(Task::getId)
                .collect(Collectors.toList());

        List<Column> columns = columnService.findAllByProject_Id(projectId)
                .stream()
                .sorted(Comparator.comparingInt(Column::getNumberOrder).reversed())
                .collect(Collectors.toList());

        AuditQuery query = auditReader.createQuery()
                .forRevisionsOfEntity(Task.class, false, false)
                .add(AuditEntity.id().in(tasksIdInProject));
        List<Object[]> resultList = query.getResultList();

        List<TaskRevision> taskRevisionsAll = resultList.stream()
                .map(ProjectStatsService::mapToTaskRevision)
                .collect(Collectors.toList());
        setTaskRevisionsColumnOrder(taskRevisionsAll, columns);

        Map<Long, List<TaskRevision>> taskRevisionMap = taskRevisionsAll.stream()
                .collect(groupingBy(TaskRevision::getId));

        ArrayList<TaskStatusStartDate> taskStatusStartDateList = new ArrayList<>();

        for (var entry : taskRevisionMap.entrySet()) {
            List<TaskRevision> taskRevisions = entry.getValue();
            taskRevisions = getOnlyNotOutdatedTaskRevisions(taskRevisions);

            Optional<LocalDate> taskStatusStartDate = Optional.empty();
            for (Column column : columns) {
                OptionalLong currentTaskStatusStartTimestamp = taskRevisions.stream()
                        .filter(taskRevision -> taskRevision.getColumnId() == column.getId())
                        .mapToLong(TaskRevision::getTimestamp)
                        .max();

                if (currentTaskStatusStartTimestamp.isPresent()) {
                    taskStatusStartDate = Optional.of(Instant.ofEpochMilli(currentTaskStatusStartTimestamp.getAsLong())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate());
                }

                taskStatusStartDate.ifPresent(localDate -> taskStatusStartDateList.add(
                        new TaskStatusStartDate(entry.getKey(), column.getId(),
                                column.getName(), column.getNumberOrder(), localDate)));
            }
        }

        LocalDate firstProjectStatsDay = taskStatusStartDateList.stream()
                .map(TaskStatusStartDate::getStartLocalDate)
                .min(LocalDate::compareTo)
                .orElse(LocalDate.now());

        Map<Long, List<TaskStatusStartDate>> taskStatusStartMap = taskStatusStartDateList.stream().
                collect(groupingBy(TaskStatusStartDate::getColumnId));

        ProjectCumulativeStats projectCumulativeStats = new ProjectCumulativeStats();
        projectCumulativeStats.setDays(firstProjectStatsDay.datesUntil(LocalDate.now().plusDays(1), Period.ofDays(1))
                .collect(Collectors.toList()));
        projectCumulativeStats.setFinishedTasksColumns(new ArrayList<>());
        for (var entry : taskStatusStartMap.entrySet()) {
            FinishedTasksColumn finishedTasksColumn = new FinishedTasksColumn();
            finishedTasksColumn.setColumnId(entry.getKey());
            finishedTasksColumn.setColumnName(entry.getValue().get(0).getColumnName());
            finishedTasksColumn.setColumnOrder(entry.getValue().get(0).getColumnOrder());
            finishedTasksColumn.setFinishedTaskStatus(new ArrayList<>());
            int finishedTaskStatusCounter = 0;
            for (LocalDate date = firstProjectStatsDay; !date.isAfter(LocalDate.now()); date = date.plusDays(1)) {
                final LocalDate currentDate = date;

                long currentDateFinishedTaskStatus = entry.getValue().stream()
                        .map(TaskStatusStartDate::getStartLocalDate)
                        .filter(localDate -> localDate.equals(currentDate))
                        .count();
                finishedTaskStatusCounter += currentDateFinishedTaskStatus;

                finishedTasksColumn.getFinishedTaskStatus().add(finishedTaskStatusCounter);
            }
            projectCumulativeStats.getFinishedTasksColumns().add(finishedTasksColumn);
        }
        fillProjectCumulativeStatsWithRemainingColumns(projectCumulativeStats, columns);

        projectCumulativeStats.getFinishedTasksColumns()
                .sort(Comparator.comparingInt(FinishedTasksColumn::getColumnOrder));

        return projectCumulativeStats;
    }

    private ArrayList<TaskRevision> getOnlyNotOutdatedTaskRevisions(List<TaskRevision> outdatedTaskRevisions) {
        outdatedTaskRevisions.sort(Comparator.comparingInt(TaskRevision::getColumnOrder));
        ArrayList<TaskRevision> actualTaskRevisions = new ArrayList<>();
        while (outdatedTaskRevisions.size() > 0) {
            TaskRevision mostRecentTaskRevision = outdatedTaskRevisions.stream()
                    .max(Comparator.comparingLong(TaskRevision::getTimestamp))
                    .get();

            int startIndexToRemove = outdatedTaskRevisions.indexOf(mostRecentTaskRevision);
            outdatedTaskRevisions.subList(startIndexToRemove, outdatedTaskRevisions.size()).clear();
            actualTaskRevisions.add(mostRecentTaskRevision);
        }
        return actualTaskRevisions;
    }

    private void fillProjectCumulativeStatsWithRemainingColumns(ProjectCumulativeStats projectCumulativeStats,
                                                           List<Column> columns) {
        List<Long> columnsIdsFromGeneratedStats = projectCumulativeStats.getFinishedTasksColumns()
                .stream()
                .map(FinishedTasksColumn::getColumnId)
                .collect(Collectors.toList());

        int daysNumber = projectCumulativeStats.getDays().size();
        List<Integer> zeros = new ArrayList<>(Collections.nCopies(daysNumber, 0));

        for (Column column : columns) {
            if (!columnsIdsFromGeneratedStats.contains(column.getId())) {
                FinishedTasksColumn finishedTasksColumn = new FinishedTasksColumn();
                finishedTasksColumn.setColumnId(column.getId());
                finishedTasksColumn.setColumnName(column.getName());
                finishedTasksColumn.setColumnOrder(column.getNumberOrder());
                finishedTasksColumn.setFinishedTaskStatus(zeros);
                projectCumulativeStats.getFinishedTasksColumns().add(finishedTasksColumn);
            }
        }
    }

    private static TaskRevision mapToTaskRevision(Object[] o) {
        TaskRevision taskRevision = new TaskRevision();
        Task task = (Task) o[0];
        taskRevision.setId(task.getId());
        taskRevision.setColumnId(task.getColumn().getId());
        DefaultRevisionEntity revisionEntity = (DefaultRevisionEntity) o[1];
        taskRevision.setTimestamp(revisionEntity.getTimestamp());
        taskRevision.setRevisionType((RevisionType) o[2]);
        return taskRevision;
    }

    private void setTaskRevisionsColumnOrder(List<TaskRevision> taskRevisions, List<Column> columns) {
        for (TaskRevision taskRevision : taskRevisions) {
            columns.stream()
                    .filter(column -> column.getId() == taskRevision.getColumnId())
                    .findFirst()
                    .ifPresent(column -> {
                        taskRevision.setColumnName(column.getName());
                        taskRevision.setColumnOrder(column.getNumberOrder());
                    });
        }
    }

}
