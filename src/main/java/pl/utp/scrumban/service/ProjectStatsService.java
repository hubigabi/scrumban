package pl.utp.scrumban.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.utp.scrumban.model.Project;
import pl.utp.scrumban.dto.ProjectStatsDto;
import pl.utp.scrumban.model.ProjectStats;
import pl.utp.scrumban.model.Task;
import pl.utp.scrumban.repositiory.ProjectStatsRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProjectStatsService {

    private final ProjectService projectService;
    private final ColumnService columnService;
    private final TaskService taskService;
    private final ProjectStatsRepository projectStatsRepository;

    @Autowired
    public ProjectStatsService(ProjectService projectService, ColumnService columnService,
                               TaskService taskService, ProjectStatsRepository projectStatsRepository) {
        this.projectService = projectService;
        this.columnService = columnService;
        this.taskService = taskService;
        this.projectStatsRepository = projectStatsRepository;
    }

    //    Little's law
//    https://kanbanzone.com/littles-law/
    public List<ProjectStatsDto> getProjectStatsDtoList(Long projectID) {
        Project project = projectService.getProject(projectID);
        List<Task> tasks = taskService.findAllByProject_Id(projectID);
        List<ProjectStatsDto> projectStatsDtoList = new ArrayList<>();

        LocalDate startedProject = project.getStartedLocalDate();
        LocalDate finishedProject = project.getFinishedLocalDate() != null ? project.getFinishedLocalDate() : LocalDate.now();

//        if (finishedProject.isBefore(startedProject))
//            finishedProject = startedProject;
        final Integer MAX_COLUMN_NUMBER_ORDER = columnService.getMaxColumnNumberOrder(projectID);
        int activeTasks = 0;
        int dayCounter = 0;
        int startedTaskCounter = 0;
        int finishedTaskCounter = 0;
        int finishedTaskDayCounter = 0;

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
                throughput = (double) finishedTaskCounter / dayCounter;
            } else {
                throughput = 0;
            }

            double leadTime;
            if (finishedTaskCounter != 0) {
                leadTime = (double) finishedTaskDayCounter / finishedTaskCounter;
            } else {
                leadTime = 0;
            }

            double wip = throughput * leadTime;
            activeTasks = activeTasks + startedTaskToday - finishedTaskTodayCounter;

            projectStatsDtoList.add(new ProjectStatsDto(currentDate, startedTaskCounter, activeTasks, finishedTaskCounter, throughput, leadTime, wip));
            dayCounter++;
        }

        return projectStatsDtoList;
    }

    //    @Scheduled(cron = "0 * * * * *")
    @Scheduled(initialDelay = 10 * 1000, fixedRate = 24 * 60 * 60 * 1000)
    public void generateProjectStats() {
        LocalDate currentDate = LocalDate.now();
        List<Project> projects = projectService.getAllProjects();
        List<ProjectStats> projectStatsList = new ArrayList<>();

        for (Project project : projects) {
            ProjectStats projectStats = generateStatsForProject(project, currentDate);
            projectStatsList.add(projectStats);
        }
        projectStatsRepository.saveAll(projectStatsList);

        log.info("Finished generating statistics for projects");
    }

    private ProjectStats generateStatsForProject(Project project, LocalDate currentDate) {
        List<Task> tasks = taskService.findAllByProject_Id(project.getId());
        final Integer FINISHED_COLUMN_NUMBER = columnService.getMaxColumnNumberOrder(project.getId());
        int projectDaysNumber = (int) ChronoUnit.DAYS.between(project.getStartedLocalDate(), currentDate);

        int startedTasks = tasks.size();
        int finishedTasks = (int) tasks.stream()
                .filter(task -> task.getColumn().getNumberOrder().equals(FINISHED_COLUMN_NUMBER))
                .count();
        int activeTasks = startedTasks - finishedTasks;

        double throughput;
        if (projectDaysNumber != 0) {
            throughput = (double) finishedTasks / projectDaysNumber;
        } else {
            throughput = 0;
        }

        int finishedTaskDayCounter = tasks.stream()
                .filter(task -> task.getColumn().getNumberOrder().equals(FINISHED_COLUMN_NUMBER))
                .mapToInt(task -> (int) ChronoUnit.DAYS.between(task.getStartedLocalDate(), task.getFinishedLocalDate()))
                .sum();

        double leadTime;
        if (finishedTasks != 0) {
            leadTime = (double) finishedTaskDayCounter / finishedTasks;
        } else {
            leadTime = 0;
        }

        double wip = throughput * leadTime;

        return new ProjectStats(0L, project, currentDate, startedTasks,
                activeTasks, finishedTasks, throughput, leadTime, wip);
    }

}
