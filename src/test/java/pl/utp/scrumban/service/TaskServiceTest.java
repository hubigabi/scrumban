package pl.utp.scrumban.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import pl.utp.scrumban.model.Column;
import pl.utp.scrumban.model.Project;
import pl.utp.scrumban.model.Task;
import pl.utp.scrumban.model.User;
import pl.utp.scrumban.repositiory.TaskRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;


    @Test
    void getTask() {
        Long taskID = 1L;
        Task task = new Task("Backend", "Lorem ipsum", 3, LocalDate.now().minusDays(5), null, new Column(), new Project());
        task.setId(taskID);

        Mockito.when(taskRepository.findById(taskID)).thenReturn(Optional.of(task));
        Task actual = taskService.getTask(taskID);

        assertEquals(task, actual);
    }

    @Test
    void getAllTasks() {
        Task t1 = new Task("Task 1", "Lorem ipsum 1", 1, LocalDate.now().minusDays(5), null, new Column(), new Project());
        Task t2 = new Task("Task 2", "Lorem ipsum 2", 2, LocalDate.now().minusDays(2), null, new Column(), new Project());
        List<Task> tasks = Arrays.asList(t1, t2);

        Mockito.when(taskRepository.findAll(Sort.by(Sort.Order.desc("id")))).thenReturn(tasks);

        List<Task> actual = taskService.getAllTasks();

        assertEquals(tasks, actual);
    }

    @Test
    void createTask() {
        Task task = new Task("Backend", "Lorem ipsum", 3, LocalDate.now().minusDays(5), null, new Column(), new Project());

        Mockito.when(taskRepository.save(any(Task.class))).thenReturn(task);
        Task actual = taskService.createTask(task);

        assertEquals(task, actual);
    }

    @Test
    void updateTask() {
        Task task = new Task("Backend", "Lorem ipsum", 3, LocalDate.now().minusDays(5), null, new Column(), new Project());

        Mockito.when(taskRepository.save(any(Task.class))).thenReturn(task);
        Task actual = taskService.updateTask(task);

        assertEquals(task, actual);
    }

    @Test
    void findAllByUsers_Id() {
        Long userID = 1L;
        User user = new User("JohnSmith@gmail.com", "John Smith", "JohnSmith", LocalDate.now().minusDays(17));
        user.setId(userID);

        Task t1 = new Task("Task 1", "Lorem ipsum 1", 1, LocalDate.now().minusDays(5), null, new Column(), new Project());
        t1.addUser(user);
        Task t2 = new Task("Task 2", "Lorem ipsum 2", 2, LocalDate.now().minusDays(2), null, new Column(), new Project());
        t2.addUser(user);
        List<Task> tasks = Arrays.asList(t1, t2);

        Mockito.when(taskRepository.findAllByUsers_Id(userID)).thenReturn(tasks);

        List<Task> actual = taskService.findAllByUsers_Id(userID);

        assertEquals(tasks, actual);

        for (Task task : actual) {
            assertTrue(task.getUsers().stream()
                    .anyMatch(u ->
                            u.getId().equals(userID)
                    )
            );
        }
    }

    @Test
    void findAllByProject_Id() {
        Long projectID = 1L;
        Project project = new Project("Hotel", "Web application for hotel", LocalDate.now().minusDays(15), null, new User());
        project.setId(projectID);

        Task t1 = new Task("Task 1", "Lorem ipsum 1", 1, LocalDate.now().minusDays(5), null, new Column(), project);
        Task t2 = new Task("Task 2", "Lorem ipsum 2", 2, LocalDate.now().minusDays(2), null, new Column(), project);
        List<Task> tasks = Arrays.asList(t1, t2);

        Mockito.when(taskRepository.findAllByProject_Id(projectID)).thenReturn(tasks);

        List<Task> actual = taskService.findAllByProject_Id(projectID);

        assertEquals(tasks, actual);

        for (Task task : actual) {
            assertEquals(task.getProject().getId(), projectID);
        }
    }

    @Test
    void findAllByProject_IdAndUsers_Id() {
        Long projectID = 1L;
        Project project = new Project("Hotel", "Web application for hotel", LocalDate.now().minusDays(15), null, new User());
        project.setId(projectID);

        Long userID = 1L;
        User user = new User("JohnSmith@gmail.com", "John Smith", "JohnSmith", LocalDate.now().minusDays(17));
        user.setId(userID);

        Task t1 = new Task("Task 1", "Lorem ipsum 1", 1, LocalDate.now().minusDays(5), null, new Column(), project);
        t1.addUser(user);
        Task t2 = new Task("Task 2", "Lorem ipsum 2", 2, LocalDate.now().minusDays(2), null, new Column(), project);
        t2.addUser(user);
        List<Task> tasks = Arrays.asList(t1, t2);

        Mockito.when(taskRepository.findAllByProject_IdAndUsers_Id(projectID, userID)).thenReturn(tasks);

        List<Task> actual = taskService.findAllByProject_IdAndUsers_Id(projectID, userID);

        assertEquals(tasks, actual);

        for (Task task : actual) {
            assertTrue(task.getUsers().stream()
                    .anyMatch(u ->
                            u.getId().equals(userID)
                    )
            );

            assertEquals(task.getProject().getId(), projectID);
        }
    }

}
