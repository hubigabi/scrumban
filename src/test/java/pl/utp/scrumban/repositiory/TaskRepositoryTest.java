//package pl.utp.scrumban.repositiory;
//
//import org.hamcrest.MatcherAssert;
//import org.hamcrest.Matchers;
//import org.junit.jupiter.api.*;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringRunner;
//import pl.utp.scrumban.model.Progress;
//import pl.utp.scrumban.model.Project;
//import pl.utp.scrumban.model.Task;
//import pl.utp.scrumban.model.User;
//
//import java.time.LocalDate;
//import java.util.*;
//import java.util.stream.Collectors;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
//
//@RunWith(SpringRunner.class)
//@DataJpaTest
//@TestPropertySource(locations = "classpath:test.properties")
//@AutoConfigureTestDatabase(replace = NONE)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class TaskRepositoryTest {
//
//    @Autowired
//    private TaskRepository taskRepository;
//
//    @Autowired
//    private ProjectRepository projectRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    private List<Task> tasks;
//    private List<Project> projects;
//    private List<User> users;
//
//    @BeforeAll
//    public void initDB() {
//        tasks = new ArrayList<>();
//        projects = new ArrayList<>();
//        users = new ArrayList<>();
//
//        User u1 = new User("JohnSmith@gmail.com", "John Smith", "JohnSmith", LocalDate.now().minusDays(17));
//        User u2 = new User("RileyTaylor@gmail.com", "Riley Taylor", "RileyTaylor", LocalDate.now().minusDays(26));
//        User u3 = new User("LaraBaxter@gmail.com", "Lara Baxter", "LaraBaxter", LocalDate.now().minusDays(15));
//        User u4 = new User("AlexTyson@gmail.com", "Alex Tyson", "AlexTyson", LocalDate.now().minusDays(18));
//        User u5 = new User("SamWright@gmail.com", "Sam Wright", "SamWright", LocalDate.now().minusDays(22));
//
//        u1 = userRepository.save(u1);
//        u2 = userRepository.save(u2);
//        u3 = userRepository.save(u3);
//        u4 = userRepository.save(u4);
//        u5 = userRepository.save(u5);
//
//        Project p1 = new Project("Hotel", "Web application for hotel", 4, LocalDate.now().minusDays(15), null, u1);
//        Project p2 = new Project("Shop", "Web application for shop", 5, LocalDate.now().minusDays(13), null, u2);
//
//        p1 = projectRepository.save(p1);
//        p2 = projectRepository.save(p2);
//
//        p1.addUser(u1);
//        p1.addUser(u3);
//        p1.addUser(u4);
//
//        p2.addUser(u2);
//        p2.addUser(u3);
//        p2.addUser(u4);
//        p2.addUser(u5);
//
//        p1 = projectRepository.save(p1);
//        p2 = projectRepository.save(p2);
//
//        Task t11 = new Task("Backend", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", 3, Progress.BACKLOG, LocalDate.now().minusDays(14), null, p1);
//        Task t12 = new Task("Frontend", "Ut ac quam a tellus dictum pretium eget ac neque.", 2, Progress.QA, LocalDate.now().minusDays(7), null, p1);
//        Task t13 = new Task("Database", "Aenean a tortor eget elit scelerisque aliquam.", 2, Progress.DEVELOPMENT, LocalDate.now().minusDays(9), null, p1);
//        Task t14 = new Task("Login", "Sed vitae diam eleifend, vestibulum eros sed, malesuada sapien.", 1, Progress.TEST, LocalDate.now().minusDays(13), null, p1);
//        Task t15 = new Task("Sign up", "Curabitur vel sollicitudin sem, ut rutrum magna.", 1, Progress.DEPLOYMENT, LocalDate.now().minusDays(8), null, p1);
//        Task t16 = new Task("Web sockets", "Nam auctor enim at erat porta, ut elementum nibh ultrices.", 2, Progress.DONE, LocalDate.now().minusDays(11), LocalDate.now().minusDays(1), p1);
//
//        t11 = taskRepository.save(t11);
//        t12 = taskRepository.save(t12);
//        t13 = taskRepository.save(t13);
//        t14 = taskRepository.save(t14);
//        t15 = taskRepository.save(t15);
//        t16 = taskRepository.save(t16);
//
//        t12.addUser(u1);
//        t12.addUser(u3);
//        t13.addUser(u3);
//        t14.addUser(u3);
//        t15.addUser(u4);
//        t16.addUser(u4);
//
//        t12 = taskRepository.save(t12);
//        t13 = taskRepository.save(t13);
//        t14 = taskRepository.save(t14);
//        t15 = taskRepository.save(t15);
//        t16 = taskRepository.save(t16);
//
//        Task t21 = new Task("Backend", "Aenean at augue euismod, ultrices mi vitae, ultrices nisi.", 3, Progress.BACKLOG, LocalDate.now().minusDays(12), null, p2);
//        Task t22 = new Task("Frontend", "Aliquam sit amet neque non lorem imperdiet maximus a convallis mi.", 3, Progress.BACKLOG, LocalDate.now().minusDays(12), null, p2);
//        Task t23 = new Task("Database", "Duis diam sem, bibendum sit amet leo eu, facilisis iaculis tortor.", 2, Progress.DEVELOPMENT, LocalDate.now().minusDays(10), null, p2);
//        Task t24 = new Task("Login", "Vivamus sem nisi, auctor nec finibus id, ultricies eget ligula. Donec eu tellus.", 1, Progress.TEST, LocalDate.now().minusDays(7), null, p2);
//        Task t25 = new Task("Sign up", "Curabitur interdum hendrerit urna, auctor commodo mi rhoncus dictum.", 1, Progress.DEPLOYMENT, LocalDate.now().minusDays(8), null, p2);
//        Task t26 = new Task("Web sockets", "Curabitur nec leo faucibus, posuere augue at, eleifend diam. ", 3, Progress.DONE, LocalDate.now().minusDays(10), LocalDate.now().minusDays(3), p2);
//
//        t21 = taskRepository.save(t21);
//        t22 = taskRepository.save(t22);
//        t23 = taskRepository.save(t23);
//        t24 = taskRepository.save(t24);
//        t25 = taskRepository.save(t25);
//        t26 = taskRepository.save(t26);
//
//        t23.addUser(u2);
//        t23.addUser(u3);
//        t24.addUser(u3);
//        t25.addUser(u4);
//        t26.addUser(u4);
//        t26.addUser(u5);
//
//        t23 = taskRepository.save(t23);
//        t24 = taskRepository.save(t24);
//        t25 = taskRepository.save(t25);
//        t26 = taskRepository.save(t26);
//    }
//
//
//    @BeforeEach
//    public void init() {
//        users = userRepository.findAll();
//        projects = projectRepository.findAll();
//        tasks = taskRepository.findAll();
//    }
//
//    @AfterAll
//    void tearDown() {
//        taskRepository.deleteAll();
//        projectRepository.deleteAll();
//        userRepository.deleteAll();
//    }
//
//    @Test
//    void save() {
//        Integer expectedSize = taskRepository.findAll().size() + 1;
//
//        Project project = projects.get(0);
//        Task task = new Task("New task", "New task description", 1, Progress.DEVELOPMENT, LocalDate.now().minusDays(10), null, project);
//        taskRepository.save(task);
//        Integer actualSize = taskRepository.findAll().size();
//
//        assertEquals(expectedSize, actualSize);
//    }
//
//    @Test
//    void findOne() {
//        Task expected = tasks.get(0);
//        Task actual = taskRepository.findById(expected.getId()).orElse(null);
//
//        assertNotNull(actual);
//        assertTrue(tasksEqual(expected, actual));
//    }
//
//    @Test
//    void findAll() {
//        List<Task> expected = tasks;
//        List<Task> actual = taskRepository.findAll();
//
//        assertTrue(taskListEqual(expected, actual));
//    }
//
//    @Test
//    void findAllByUsers_Id() {
//        Long userID = users.get(3).getId();
//
//        List<Task> expected = tasks.stream()
//                .filter(task -> task.getUsers()
//                        .stream()
//                        .anyMatch(user ->
//                                user.getId().equals(userID)
//                        ))
//                .collect(Collectors.toList());
//        List<Task> actual = taskRepository.findAllByUsers_Id(userID);
//
//        assertEquals(expected.size(), actual.size());
//        assertTrue(taskListEqual(expected, actual));
//    }
//
//    @Test
//    void findAllByProject_Id() {
//        Long projectID = projects.get(0).getId();
//
//        List<Task> expected = tasks.stream()
//                .filter(task -> task.getProject()
//                        .getId().equals(projectID)
//                )
//                .collect(Collectors.toList());
//        List<Task> actual = taskRepository.findAllByProject_Id(projectID);
//
//        assertEquals(expected.size(), actual.size());
//        assertTrue(taskListEqual(expected, actual));
//    }
//
//    @Test
//    void findAllByProject_IdAndUsers_Id() {
//        Long projectID = projects.get(0).getId();
//        Long userID = users.get(3).getId();
//
//        List<Task> expected = tasks.stream()
//                .filter(task -> task.getUsers()
//                        .stream()
//                        .anyMatch(user ->
//                                user.getId().equals(userID)
//                        )
//                        && task.getProject().getId().equals(projectID)
//                )
//                .collect(Collectors.toList());
//        List<Task> actual = taskRepository.findAllByProject_IdAndUsers_Id(projectID, userID);
//
//        assertEquals(expected.size(), actual.size());
//        assertTrue(taskListEqual(expected, actual));
//    }
//
//    @Test
//    void deleteById() {
//        Long taskID = tasks.get(0).getId();
//
//        assertNotNull(taskRepository.findById(taskID).orElse(null));
//        taskRepository.deleteById(taskID);
//        assertNull(taskRepository.findById(taskID).orElse(null));
//    }
//
//    public boolean taskListEqual(List<Task> taskList1, List<Task> taskList2) {
//        if (taskList1 == null && taskList2 == null) {
//            return true;
//        }
//
//        if ((taskList1 == null && taskList2 != null)
//                || taskList1 != null && taskList2 == null
//                || taskList1.size() != taskList2.size()) {
//            return false;
//        }
//
//        taskList1.sort(Comparator.comparing(Task::getId));
//        taskList2.sort(Comparator.comparing(Task::getId));
//
//        for (int i = 0; i < taskList1.size(); i++) {
//            if (!tasksEqual(taskList1.get(i), taskList2.get(i)))
//                return false;
//        }
//
//        return true;
//    }
//
//    public boolean tasksEqual(Task task1, Task task2) {
//        return Objects.equals(task1.getId(), task2.getId()) &&
//                Objects.equals(task1.getName(), task2.getName()) &&
//                Objects.equals(task1.getDescription(), task2.getDescription()) &&
//                Objects.equals(task1.getPriority(), task2.getPriority()) &&
//                task1.getProgress() == task2.getProgress() &&
//                Objects.equals(task1.getStartedLocalDate(), task2.getStartedLocalDate()) &&
//                Objects.equals(task1.getFinishedLocalDate(), task2.getFinishedLocalDate());
//    }
//
//}