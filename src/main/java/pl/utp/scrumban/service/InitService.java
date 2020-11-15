package pl.utp.scrumban.service;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.utp.scrumban.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class InitService {

    private UserService userService;
    private ProjectService projectService;
    private TaskService taskService;
    private CommentService commentService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public InitService(UserService userService, ProjectService projectService, TaskService taskService,
                       CommentService commentService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.projectService = projectService;
        this.taskService = taskService;
        this.commentService = commentService;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        User u1 = new User("JohnSmith@gmail.com", "John Smith", passwordEncoder.encode("JohnSmith"), LocalDate.now().minusDays(17));
        User u2 = new User("RileyTaylor@gmail.com", "Riley Taylor", passwordEncoder.encode("RileyTaylor"), LocalDate.now().minusDays(26));
        User u3 = new User("LaraBaxter@gmail.com", "Lara Baxter", passwordEncoder.encode("LaraBaxter"), LocalDate.now().minusDays(15));
        User u4 = new User("AlexTyson@gmail.com", "Alex Tyson", passwordEncoder.encode("AlexTyson"), LocalDate.now().minusDays(18));
        User u5 = new User("SamWright@gmail.com", "Sam Wright", passwordEncoder.encode("SamWright"), LocalDate.now().minusDays(22));

        u1 = userService.createUser(u1);
        u2 = userService.createUser(u2);
        u3 = userService.createUser(u3);
        u4 = userService.createUser(u4);
        u5 = userService.createUser(u5);

        Project p1 = new Project("Hotel", "Web application for hotel", 4, LocalDate.now().minusDays(15), null, u1);
        Project p2 = new Project("Shop", "Web application for shop", 5, LocalDate.now().minusDays(13), null, u2);

        p1.addUser(u1);
        p1.addUser(u3);
        p1.addUser(u4);

        p2.addUser(u2);
        p2.addUser(u3);
        p2.addUser(u4);
        p2.addUser(u5);

        p1 = projectService.createProject(p1);
        p2 = projectService.createProject(p2);

        Task t11 = new Task("Backend", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", 3, Progress.BACKLOG, LocalDate.now().minusDays(14), null, p1);
        Task t12 = new Task("Frontend", "Ut ac quam a tellus dictum pretium eget ac neque.", 2, Progress.QA, LocalDate.now().minusDays(7), null, p1);
        Task t13 = new Task("Database", "Aenean a tortor eget elit scelerisque aliquam.", 2, Progress.DEVELOPMENT, LocalDate.now().minusDays(9), null, p1);
        Task t14 = new Task("Login", "Sed vitae diam eleifend, vestibulum eros sed, malesuada sapien.", 1, Progress.TEST, LocalDate.now().minusDays(13), null, p1);
        Task t15 = new Task("Sign up", "Curabitur vel sollicitudin sem, ut rutrum magna.", 1, Progress.DEPLOYMENT, LocalDate.now().minusDays(8), null, p1);
        Task t16 = new Task("Web sockets", "Nam auctor enim at erat porta, ut elementum nibh ultrices.", 2, Progress.DONE, LocalDate.now().minusDays(11), LocalDate.now().minusDays(6), p1);

        t12.addUser(u1);
        t12.addUser(u3);
        t13.addUser(u3);
        t14.addUser(u3);
        t15.addUser(u4);
        t16.addUser(u4);

        t11 = taskService.createTask(t11);
        t12 = taskService.createTask(t12);
        t13 = taskService.createTask(t13);
        t14 = taskService.createTask(t14);
        t15 = taskService.createTask(t15);
        t16 = taskService.createTask(t16);

        Task t21 = new Task("Backend", "Aenean at augue euismod, ultrices mi vitae, ultrices nisi.", 3, Progress.BACKLOG, LocalDate.now().minusDays(12), null, p2);
        Task t22 = new Task("Frontend", "Aliquam sit amet neque non lorem imperdiet maximus a convallis mi.", 3, Progress.BACKLOG, LocalDate.now().minusDays(12), null, p2);
        Task t23 = new Task("Database", "Duis diam sem, bibendum sit amet leo eu, facilisis iaculis tortor.", 2, Progress.DEVELOPMENT, LocalDate.now().minusDays(10), null, p2);
        Task t24 = new Task("Login", "Vivamus sem nisi, auctor nec finibus id, ultricies eget ligula. Donec eu tellus.", 1, Progress.TEST, LocalDate.now().minusDays(7), null, p2);
        Task t25 = new Task("Sign up", "Curabitur interdum hendrerit urna, auctor commodo mi rhoncus dictum.", 1, Progress.DEPLOYMENT, LocalDate.now().minusDays(8), null, p2);
        Task t26 = new Task("Web sockets", "Curabitur nec leo faucibus, posuere augue at, eleifend diam.", 3, Progress.DONE, LocalDate.now().minusDays(10), LocalDate.now().minusDays(7), p2);

        t23.addUser(u2);
        t23.addUser(u3);
        t24.addUser(u3);
        t25.addUser(u4);
        t26.addUser(u4);
        t26.addUser(u5);

        t21 = taskService.createTask(t21);
        t22 = taskService.createTask(t22);
        t23 = taskService.createTask(t23);
        t24 = taskService.createTask(t24);
        t25 = taskService.createTask(t25);
        t26 = taskService.createTask(t26);


        List<User> allUsers = userService.getAllUsers();
        final int PROJECT_NUMBER = 4;

        final int MIN_WIN = 3;
        final int MAX_WIN = 6;

        final int MIN_DAYS_PROJECT = 12;
        final int MAX_DAYS_PROJECT = 15;

        final int USERS_IN_PROJECT_NUMBER = Math.min(allUsers.size(), 4);

        final int MIN_TASK_NUMBER = 4;
        final int MAX_TASK_NUMBER = 8;

        final int MIN_USERS_IN_TASK_NUMBER = 1;
        final int MAX_USERS_IN_TASK_NUMBER = 2;

        final int MIN_PRIORITY = 0;
        final int MAX_PRIORITY = 3;

        final int MIN_DAYS_TASK = 0;
        final int MAX_DAYS_TASK = 6;

        Lorem loremIpsum = LoremIpsum.getInstance();

        for (int i = 0; i < PROJECT_NUMBER; i++) {
            Collections.shuffle(allUsers);
            ArrayList<User> usersInProject = new ArrayList<>(allUsers.subList(0, USERS_IN_PROJECT_NUMBER));

            String projectName = capitalizeFirstLetter(loremIpsum.getWords(2));
            String projectDescription = capitalizeFirstLetter(loremIpsum.getWords(10, 15)) + ".";
            int wip = ThreadLocalRandom.current().nextInt(MIN_WIN, MAX_WIN + 1);
            LocalDate startedLocalDateProject = LocalDate.now().minusDays(ThreadLocalRandom.current().nextInt(MIN_DAYS_PROJECT, MAX_DAYS_PROJECT + 1));

            Project project = new Project(projectName, projectDescription, wip, startedLocalDateProject, null, usersInProject.get(0));
            project.setUsers(new HashSet<>(usersInProject));

            project = projectService.createProject(project);

            int taskNumber = ThreadLocalRandom.current().nextInt(MIN_TASK_NUMBER, MAX_TASK_NUMBER + 1);
            ArrayList<Task> tasksInProject = new ArrayList<>();

            for (int j = 0; j < taskNumber; j++) {
                String taskName = capitalizeFirstLetter(loremIpsum.getWords(1, 4));
                String taskDescription = capitalizeFirstLetter(loremIpsum.getWords(8, 12)) + ".";
                int priority = ThreadLocalRandom.current().nextInt(MIN_PRIORITY, MAX_PRIORITY + 1);
                Progress progress = Progress.values()[ThreadLocalRandom.current().nextInt(Progress.values().length)];
                LocalDate startedLocalDateTask = project.getStartedLocalDate().plusDays(ThreadLocalRandom.current().nextInt(MIN_DAYS_TASK, MAX_DAYS_TASK + 1));

                Task task = new Task(taskName, taskDescription, priority, progress, startedLocalDateTask, null, project);
                if (task.getProgress() == Progress.DONE) {
                    task.setFinishedLocalDate(task.getStartedLocalDate().plusDays(5));
                }

                Collections.shuffle(usersInProject);
                int usersInTaskNumber = ThreadLocalRandom.current().nextInt(MIN_USERS_IN_TASK_NUMBER, MAX_USERS_IN_TASK_NUMBER + 1);
                ArrayList<User> userInTask = new ArrayList<>(usersInProject.subList(0, usersInTaskNumber));

                task.setUsers(new HashSet<>(userInTask));
                tasksInProject.add(task);
            }
            taskService.saveAll(tasksInProject);
        }

        final int MIN_COMMENTS_NUMBER_BY_TASK = 10;
        final int MAX_COMMENTS_NUMBER_BY_TASK = 20;
        final int MAX_COMMENT_LENGTH = 800;

        for (Project project : projectService.getAllProjects()) {
            ArrayList<User> usersInProject = new ArrayList<>(project.getUsers());

            for (Task task : taskService.findAllByProject_Id(project.getId())) {
                ArrayList<Comment> comments = new ArrayList<>();

                int commentsNumber = ThreadLocalRandom.current().nextInt(MIN_COMMENTS_NUMBER_BY_TASK, MAX_COMMENTS_NUMBER_BY_TASK + 1);

                LocalDateTime commentLocalDateTime = task.getStartedLocalDate().atStartOfDay().plusHours(12);
                LocalDateTime maxLocalDateTime = LocalDateTime.now();

                if (task.getFinishedLocalDate() != null) {
                    maxLocalDateTime = task.getFinishedLocalDate().atStartOfDay();
                }
                long minutesIncrementMax = commentLocalDateTime.until(maxLocalDateTime, ChronoUnit.MINUTES) / commentsNumber;

                for (int i = 0; i < commentsNumber; i++) {
                    Comment comment = new Comment();
                    comment.setTask(task);

                    String commentText = loremIpsum.getParagraphs(1, 1);
                    commentText = commentText.substring(0, Math.min(commentText.length(), MAX_COMMENT_LENGTH));
                    comment.setCommentText(commentText);

                    long minutesIncrement = ThreadLocalRandom.current().nextLong(minutesIncrementMax / 2, minutesIncrementMax);
                    commentLocalDateTime = commentLocalDateTime.plusMinutes(minutesIncrement);
                    comment.setLocalDateTime(commentLocalDateTime);

                    int userIndex = ThreadLocalRandom.current().nextInt(0, usersInProject.size());
                    comment.setUser(usersInProject.get(userIndex));

                    comments.add(comment);
                }

                commentService.saveAll(comments);
            }
        }

        log.info("Finished initializing data");
    }

    public String capitalizeFirstLetter(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

}
