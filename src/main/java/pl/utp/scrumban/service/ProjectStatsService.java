package pl.utp.scrumban.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.utp.scrumban.model.Project;
import pl.utp.scrumban.dto.ProjectStatsDto;
import pl.utp.scrumban.model.Task;

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

    @Autowired
    public ProjectStatsService(ProjectService projectService, ColumnService columnService, TaskService taskService) {
        this.projectService = projectService;
        this.columnService = columnService;
        this.taskService = taskService;
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

            projectStatsDtoList.add(new ProjectStatsDto(currentDate, startedTaskCounter, activeTasks, finishedTaskCounter, throughput, leadTime, WIP));
            dayCounter++;
        }

        return projectStatsDtoList;
    }

    @Scheduled(cron = "0 * * * * *")
    public void generateProjectStats() {
        log.info("Finished generating statistics for projects");
    }

}
