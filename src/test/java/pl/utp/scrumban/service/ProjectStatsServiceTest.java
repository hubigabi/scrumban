//package pl.utp.scrumban.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import pl.utp.scrumban.model.*;
//
//import java.time.LocalDate;
//import java.time.temporal.ChronoUnit;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//
//@ExtendWith(MockitoExtension.class)
//class ProjectStatsServiceTest {
//
//    @Mock
//    private ProjectService projectService;
//
//    @Mock
//    private TaskService taskService;
//
//    @InjectMocks
//    private ProjectStatsService projectStatsService;
//
//    @BeforeEach
//    public void setup() {
//        Project project = new Project("Hotel", "Web application for hotel", 4, LocalDate.now().minusDays(15), null, new User());
//
//        Task t1 = new Task("Backend", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", 3, Progress.BACKLOG, LocalDate.now().minusDays(14), null, project);
//        Task t2 = new Task("Frontend", "Ut ac quam a tellus dictum pretium eget ac neque.", 2, Progress.QA, LocalDate.now().minusDays(7), null, project);
//        Task t3 = new Task("Database", "Aenean a tortor eget elit scelerisque aliquam.", 2, Progress.DEVELOPMENT, LocalDate.now().minusDays(9), null, project);
//        Task t4 = new Task("Login", "Sed vitae diam eleifend, vestibulum eros sed, malesuada sapien.", 1, Progress.TEST, LocalDate.now().minusDays(13), null, project);
//        Task t5 = new Task("Sign up", "Curabitur vel sollicitudin sem, ut rutrum magna.", 1, Progress.DEPLOYMENT, LocalDate.now().minusDays(8), null, project);
//        Task t6 = new Task("Web sockets", "Nam auctor enim at erat porta, ut elementum nibh ultrices.", 2, Progress.DONE, LocalDate.now().minusDays(11), LocalDate.now().minusDays(6), project);
//        List<Task> tasks = Arrays.asList(t1, t2, t3, t4, t5, t6);
//
//        Mockito.when(projectService.getProject(any(Long.class))).thenReturn(project);
//        Mockito.when(taskService.findAllByProject_Id(any(Long.class))).thenReturn(tasks);
//    }
//
//    @Test
//    void localDateTest() {
//        List<LocalDate> localDateListActual = projectStatsService.getProjectStats(1L)
//                .stream()
//                .map(ProjectStats::getLocalDate)
//                .collect(Collectors.toList());
//
//        LocalDate startDate = LocalDate.now().minusDays(15);
//        LocalDate endDate = LocalDate.now();
//        List<LocalDate> localDateListExpected = Stream
//                .iterate(startDate, date -> date.plusDays(1))
//                .limit(ChronoUnit.DAYS.between(startDate, endDate.plusDays(1)))
//                .collect(Collectors.toList());
//
//        assertEquals(localDateListExpected.size(), localDateListActual.size());
//        assertEquals(localDateListExpected, localDateListActual);
//    }
//
//    @Test
//    void startedTasksTest() {
//        List<Integer> startedTasksListActual = projectStatsService.getProjectStats(1L)
//                .stream()
//                .map(projectStats -> (int) projectStats.getStartedTasks())
//                .collect(Collectors.toList());
//
//        List<Integer> startedTasksListExpected = Arrays.asList(0, 1, 2, 2, 3, 3, 4, 5, 6, 6, 6, 6, 6, 6, 6, 6);
//
//        assertEquals(startedTasksListExpected.size(), startedTasksListActual.size());
//        assertEquals(startedTasksListExpected, startedTasksListActual);
//    }
//
//    @Test
//    void activeTasksTest() {
//        List<Integer> activeTasksListActual = projectStatsService.getProjectStats(1L)
//                .stream()
//                .map(projectStats -> (int) projectStats.getActiveTasks())
//                .collect(Collectors.toList());
//
//        List<Integer> activeTasksListExpected = Arrays.asList(0, 1, 2, 2, 3, 3, 4, 5, 6, 5, 5, 5, 5, 5, 5, 5);
//
//        assertEquals(activeTasksListExpected.size(), activeTasksListActual.size());
//        assertEquals(activeTasksListExpected, activeTasksListActual);
//    }
//
//    @Test
//    void finishedTasksTest() {
//        List<Integer> finishedTasksListActual = projectStatsService.getProjectStats(1L)
//                .stream()
//                .map(projectStats -> (int) projectStats.getFinishedTasks())
//                .collect(Collectors.toList());
//
//        List<Integer> finishedTasksListExpected = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1);
//
//        assertEquals(finishedTasksListExpected.size(), finishedTasksListActual.size());
//        assertEquals(finishedTasksListExpected, finishedTasksListActual);
//    }
//
//    @Test
//    void throughputTest() {
//        double[] throughputArrayActual = projectStatsService.getProjectStats(1L)
//                .stream()
//                .mapToDouble(ProjectStats::getThroughput)
//                .toArray();
//
//        double[] throughputArrayExpected = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.11, 0.1, 0.09, 0.083, 0.07, 0.07, 0.06};
//
//        assertEquals(throughputArrayExpected.length, throughputArrayActual.length);
//        assertArrayEquals(throughputArrayExpected, throughputArrayActual, 0.01);
//    }
//
//    @Test
//    void leadTimeTest() {
//        double[] leadTimeArrayActual = projectStatsService.getProjectStats(1L)
//                .stream()
//                .mapToDouble(ProjectStats::getLeadTime)
//                .toArray();
//
//        double[] leadTimeArrayExpected = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0};
//
//        assertEquals(leadTimeArrayExpected.length, leadTimeArrayActual.length);
//        assertArrayEquals(leadTimeArrayExpected, leadTimeArrayActual, 0.01);
//    }
//
//    @Test
//    void wipTest() {
//        double[] wipArrayActual = projectStatsService.getProjectStats(1L)
//                .stream()
//                .mapToDouble(ProjectStats::getWIP)
//                .toArray();
//
//        double[] wipArrayExpected = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.55, 0.5, 0.45, 0.41, 0.38, 0.35, 0.33};
//
//        assertEquals(wipArrayExpected.length, wipArrayActual.length);
//        assertArrayEquals(wipArrayExpected, wipArrayActual, 0.01);
//    }
//}