package pl.utp.scrumban.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.utp.scrumban.dto.TaskDto;
import pl.utp.scrumban.dto.UserDto;
import pl.utp.scrumban.service.JwtService;
import pl.utp.scrumban.service.TaskService;
import pl.utp.scrumban.service.UserDetailsServiceImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private static final String TASK_API_URL = "/api/task";

    @Test
    void getTaskDto() throws Exception {
        long taskID = 1L;
        TaskDto task = new TaskDto(taskID, "Backend", "Lorem ipsum", 3, LocalDate.now().minusDays(5), null,
                1L, "column1", 1L, "project1", new HashSet<>());

        Mockito.when(taskService.getTaskDto(taskID)).thenReturn(task);

        ResultActions resultActions = mockMvc.perform(get(TASK_API_URL + "/{id}", task.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) taskID)));

        checkTaskJSONPath(resultActions, task);
    }

    @Test
    void getAllTasksDto() throws Exception {
        TaskDto t1 = new TaskDto(1L, "Task 1", "Lorem ipsum 1", 1, LocalDate.now().minusDays(5), null,
                1L, "column1", 1L, "project1", new HashSet<>());
        TaskDto t2 = new TaskDto(2L, "Task 2", "Lorem ipsum 2", 2, LocalDate.now().minusDays(2), null,
                1L, "column1", 1L, "project1", new HashSet<>());
        List<TaskDto> tasks = Arrays.asList(t1, t2);

        Mockito.when(taskService.getAllTasksDto()).thenReturn(tasks);

        ResultActions resultActions = mockMvc.perform(get(TASK_API_URL + "/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(tasks.size())));

        checkTasksJSONPath(resultActions, tasks);
    }


    @Test
    void createTask() throws Exception {
        TaskDto task = new TaskDto(1L, "Backend", "Lorem ipsum", 3, LocalDate.now().minusDays(5), null,
                1L, "column1", 1L, "project1", new HashSet<>());

        Mockito.when(taskService.createTask(any(TaskDto.class))).thenReturn(task);

        ResultActions resultActions = mockMvc.perform(post(TASK_API_URL)
                .content(objectMapper.writeValueAsString(task))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        checkTaskJSONPath(resultActions, task);
    }

    @Test
    void updateTask() throws Exception {
        TaskDto task = new TaskDto(1L, "Backend", "Lorem ipsum", 3, LocalDate.now().minusDays(5), null,
                1L, "column1", 1L, "project1", new HashSet<>());

        Mockito.when(taskService.updateTask(any(TaskDto.class))).thenReturn(task);

        ResultActions resultActions = mockMvc.perform(put(TASK_API_URL)
                .content(objectMapper.writeValueAsString(task))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        checkTaskJSONPath(resultActions, task);
    }

    @Test
    void findAllDtoByUsers_Id() throws Exception {
        Long userID = 1L;
        UserDto user = new UserDto(userID, "JohnSmith@gmail.com", "John Smith", LocalDate.now().minusDays(17));

        TaskDto t1 = new TaskDto(1L, "Task 1", "Lorem ipsum 1", 1, LocalDate.now().minusDays(5), null,
                1L, "column1", 1L, "project1", new HashSet<>(Arrays.asList(user)));
        TaskDto t2 = new TaskDto(2L, "Task 2", "Lorem ipsum 2", 2, LocalDate.now().minusDays(2), null,
                1L, "column1", 1L, "project1", new HashSet<>(Arrays.asList(user)));
        List<TaskDto> tasks = Arrays.asList(t1, t2);

        Mockito.when(taskService.findAllDtoByUsers_Id(userID)).thenReturn(tasks);

        ResultActions resultActions = mockMvc.perform(get(TASK_API_URL + "/allByUser/{id}", userID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(tasks.size())));

        checkTasksJSONPath(resultActions, tasks);
    }

    @Test
    void findAllDtoByProject_Id() throws Exception {
        Long projectID = 1L;

        TaskDto t1 = new TaskDto(1L, "Task 1", "Lorem ipsum 1", 1, LocalDate.now().minusDays(5), null,
                1L, "column1", projectID, "project1", new HashSet<>());
        TaskDto t2 = new TaskDto(2L, "Task 2", "Lorem ipsum 2", 2, LocalDate.now().minusDays(2), null,
                1L, "column1", projectID, "project1", new HashSet<>());
        List<TaskDto> tasks = Arrays.asList(t1, t2);

        Mockito.when(taskService.findAllDtoByProject_Id(projectID)).thenReturn(tasks);

        ResultActions resultActions = mockMvc.perform(get(TASK_API_URL + "/allByProject/{id}", projectID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(tasks.size())));

        checkTasksJSONPath(resultActions, tasks);
        checkProjectInTasksJSONPath(resultActions, tasks);
    }

    @Test
    void findAllDtoByProject_IdAndUsers_Id() throws Exception {
        Long projectID = 1L;
        Long userID = 1L;
        UserDto user = new UserDto(userID, "JohnSmith@gmail.com", "John Smith", LocalDate.now().minusDays(17));

        TaskDto t1 = new TaskDto(1L, "Task 1", "Lorem ipsum 1", 1, LocalDate.now().minusDays(5), null,
                1L, "column1", projectID, "project1", new HashSet<>(Arrays.asList(user)));
        TaskDto t2 = new TaskDto(2L, "Task 2", "Lorem ipsum 2", 2, LocalDate.now().minusDays(2), null,
                1L, "column1", projectID, "project1", new HashSet<>(Arrays.asList(user)));
        List<TaskDto> tasks = Arrays.asList(t1, t2);

        Mockito.when(taskService.findAllDtoByProject_IdAndUsers_Id(projectID, userID)).thenReturn(tasks);

        ResultActions resultActions = mockMvc.perform(get(TASK_API_URL + "/allByProjectAndUser/{project_ID}/{user_ID}", projectID, userID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(tasks.size())));

        checkTasksJSONPath(resultActions, tasks);
        checkProjectInTasksJSONPath(resultActions, tasks);
    }

    void checkTaskJSONPath(ResultActions resultActions, TaskDto task) throws Exception {
        resultActions
                .andExpect(jsonPath("$.name", is(task.getName())))
                .andExpect(jsonPath("$.description", is(task.getDescription())))
                .andExpect(jsonPath("$.priority", is(task.getPriority())))
                .andExpect(jsonPath("$.startedLocalDate", is(task.getStartedLocalDate().toString())));

    }

    void checkTasksJSONPath(ResultActions resultActions, List<TaskDto> tasks) throws Exception {
        for (int i = 0; i < tasks.size(); i++) {
            TaskDto task = tasks.get(i);
            String jsonIndexPath = "[" + i + "]";

            resultActions
                    .andExpect(jsonPath("$" + jsonIndexPath + ".name", is(task.getName())))
                    .andExpect(jsonPath("$" + jsonIndexPath + ".description", is(task.getDescription())))
                    .andExpect(jsonPath("$" + jsonIndexPath + ".priority", is(task.getPriority())))
                    .andExpect(jsonPath("$" + jsonIndexPath + ".startedLocalDate", is(task.getStartedLocalDate().toString())));
        }
    }

    private void checkProjectInTasksJSONPath(ResultActions resultActions, List<TaskDto> tasks) throws Exception {
        for (int i = 0; i < tasks.size(); i++) {
            TaskDto task = tasks.get(i);
            String jsonIndexPath = "[" + i + "]";

            resultActions
                    .andExpect(jsonPath("$" + jsonIndexPath + ".projectId", is(task.getProjectId().intValue())));
        }

    }

}
