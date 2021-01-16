//package pl.utp.scrumban.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import pl.utp.scrumban.model.Progress;
//import pl.utp.scrumban.model.Project;
//import pl.utp.scrumban.model.Task;
//import pl.utp.scrumban.model.User;
//import pl.utp.scrumban.service.JwtService;
//import pl.utp.scrumban.service.TaskService;
//import pl.utp.scrumban.service.UserDetailsServiceImpl;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
//import static org.hamcrest.core.Is.is;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(TaskController.class)
//@AutoConfigureMockMvc(addFilters = false)
//class TaskControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private TaskService taskService;
//
//    @MockBean
//    private JwtService jwtService;
//
//    @MockBean
//    private UserDetailsServiceImpl userDetailsService;
//
//    private static final String TASK_API_URL = "/api/task";
//
//    @Test
//    void getTask() throws Exception {
//        long taskID = 1L;
//        Task task = new Task("Backend", "Lorem ipsum", 3, Progress.BACKLOG, LocalDate.now().minusDays(5), null, new Project());
//        task.setId(taskID);
//
//        Mockito.when(taskService.getTask(taskID)).thenReturn(task);
//
//        ResultActions resultActions = mockMvc.perform(get(TASK_API_URL + "/{id}", task.getId())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is((int) taskID)));
//
//        checkTaskJSONPath(resultActions, task);
//    }
//
//    @Test
//    void getAllTasks() throws Exception {
//        Task t1 = new Task("Task 1", "Lorem ipsum 1", 1, Progress.DEPLOYMENT, LocalDate.now().minusDays(5), null, new Project());
//        Task t2 = new Task("Task 2", "Lorem ipsum 2", 2, Progress.QA, LocalDate.now().minusDays(2), null, new Project());
//        List<Task> tasks = Arrays.asList(t1, t2);
//
//        Mockito.when(taskService.getAllTasks()).thenReturn(tasks);
//
//        ResultActions resultActions = mockMvc.perform(get(TASK_API_URL + "/all")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(tasks.size())));
//
//        checkTasksJSONPath(resultActions, tasks);
//    }
//
//
//    @Test
//    void createTask() throws Exception {
//        Task task = new Task("Backend", "Lorem ipsum", 3, Progress.BACKLOG, LocalDate.now().minusDays(5), null, new Project());
//
//        Mockito.when(taskService.createTask(any(Task.class))).thenReturn(task);
//
//        ResultActions resultActions = mockMvc.perform(post(TASK_API_URL)
//                .content(objectMapper.writeValueAsString(task))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//
//        checkTaskJSONPath(resultActions, task);
//    }
//
//    @Test
//    void updateTask() throws Exception {
//        Task task = new Task("Backend", "Lorem ipsum", 3, Progress.BACKLOG, LocalDate.now().minusDays(5), null, new Project());
//
//        Mockito.when(taskService.updateTask(any(Task.class))).thenReturn(task);
//
//        ResultActions resultActions = mockMvc.perform(put(TASK_API_URL)
//                .content(objectMapper.writeValueAsString(task))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        checkTaskJSONPath(resultActions, task);
//    }
//
//    @Test
//    void findAllByUsers_Id() throws Exception {
//        Long userID = 1L;
//        User user = new User("JohnSmith@gmail.com", "John Smith", "JohnSmith", LocalDate.now().minusDays(17));
//        user.setId(userID);
//
//        Task t1 = new Task("Task 1", "Lorem ipsum 1", 1, Progress.DEPLOYMENT, LocalDate.now().minusDays(5), null, new Project());
//        t1.addUser(user);
//        Task t2 = new Task("Task 2", "Lorem ipsum 2", 2, Progress.QA, LocalDate.now().minusDays(2), null, new Project());
//        t2.addUser(user);
//        List<Task> tasks = Arrays.asList(t1, t2);
//
//        Mockito.when(taskService.findAllByUsers_Id(userID)).thenReturn(tasks);
//
//        ResultActions resultActions = mockMvc.perform(get(TASK_API_URL + "/allByUser/{id}", userID)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(tasks.size())));
//
//        checkTasksJSONPath(resultActions, tasks);
//    }
//
//    @Test
//    void findAllByProject_Id() throws Exception {
//        Long projectID = 1L;
//        Project project = new Project("Hotel", "Web application for hotel", 4, LocalDate.now().minusDays(15), null, new User());
//        project.setId(projectID);
//
//        Task t1 = new Task("Task 1", "Lorem ipsum 1", 1, Progress.DEPLOYMENT, LocalDate.now().minusDays(5), null, project);
//        Task t2 = new Task("Task 2", "Lorem ipsum 2", 2, Progress.QA, LocalDate.now().minusDays(2), null, project);
//        List<Task> tasks = Arrays.asList(t1, t2);
//
//        Mockito.when(taskService.findAllByProject_Id(projectID)).thenReturn(tasks);
//
//        ResultActions resultActions = mockMvc.perform(get(TASK_API_URL + "/allByProject/{id}", projectID)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(tasks.size())));
//
//        checkTasksJSONPath(resultActions, tasks);
//        checkProjectInTasksJSONPath(resultActions, tasks);
//    }
//
//    @Test
//    void findAllByProject_IdAndUsers_Id() throws Exception {
//        Long projectID = 1L;
//        Project project = new Project("Hotel", "Web application for hotel", 4, LocalDate.now().minusDays(15), null, new User());
//        project.setId(projectID);
//
//        Long userID = 1L;
//        User user = new User("JohnSmith@gmail.com", "John Smith", "JohnSmith", LocalDate.now().minusDays(17));
//        user.setId(userID);
//
//        Task t1 = new Task("Task 1", "Lorem ipsum 1", 1, Progress.DEPLOYMENT, LocalDate.now().minusDays(5), null, project);
//        t1.addUser(user);
//        Task t2 = new Task("Task 2", "Lorem ipsum 2", 2, Progress.QA, LocalDate.now().minusDays(2), null, project);
//        t2.addUser(user);
//        List<Task> tasks = Arrays.asList(t1, t2);
//
//        Mockito.when(taskService.findAllByProject_IdAndUsers_Id(projectID, userID)).thenReturn(tasks);
//
//        ResultActions resultActions = mockMvc.perform(get(TASK_API_URL + "/allByProjectAndUser/{project_ID}/{user_ID}", projectID, userID)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(tasks.size())));
//
//        checkTasksJSONPath(resultActions, tasks);
//        checkProjectInTasksJSONPath(resultActions, tasks);
//    }
//
//    void checkTaskJSONPath(ResultActions resultActions, Task task) throws Exception {
//        resultActions
//                .andExpect(jsonPath("$.name", is(task.getName())))
//                .andExpect(jsonPath("$.description", is(task.getDescription())))
//                .andExpect(jsonPath("$.priority", is(task.getPriority())))
//                .andExpect(jsonPath("$.progress", is(task.getProgress().toString())))
//                .andExpect(jsonPath("$.startedLocalDate", is(task.getStartedLocalDate().toString())));
//
//    }
//
//    void checkTasksJSONPath(ResultActions resultActions, List<Task> tasks) throws Exception {
//        for (int i = 0; i < tasks.size(); i++) {
//            Task task = tasks.get(i);
//            String jsonIndexPath = "[" + i + "]";
//
//            resultActions
//                    .andExpect(jsonPath("$" + jsonIndexPath + ".name", is(task.getName())))
//                    .andExpect(jsonPath("$" + jsonIndexPath + ".description", is(task.getDescription())))
//                    .andExpect(jsonPath("$" + jsonIndexPath + ".priority", is(task.getPriority())))
//                    .andExpect(jsonPath("$" + jsonIndexPath + ".progress", is(task.getProgress().toString())))
//                    .andExpect(jsonPath("$" + jsonIndexPath + ".startedLocalDate", is(task.getStartedLocalDate().toString())));
//        }
//    }
//
//    private void checkProjectInTasksJSONPath(ResultActions resultActions, List<Task> tasks) throws Exception {
//        for (int i = 0; i < tasks.size(); i++) {
//            Task task = tasks.get(i);
//            String jsonIndexPath = "[" + i + "]";
//
//            resultActions
//                    .andExpect(jsonPath("$" + jsonIndexPath + ".project.id", is(task.getProject().getId().intValue())));
//        }
//
//    }
//
//}
