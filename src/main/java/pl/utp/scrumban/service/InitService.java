package pl.utp.scrumban.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.utp.scrumban.model.Progress;
import pl.utp.scrumban.model.Project;
import pl.utp.scrumban.model.Task;
import pl.utp.scrumban.model.User;

import java.time.LocalDate;

@Service
@Slf4j
public class InitService {

    private UserService userService;
    private ProjectService projectService;
    private TaskService taskService;

    @Autowired
    public InitService(UserService userService, ProjectService projectService, TaskService taskService) {
        this.userService = userService;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        User u1 = new User("JohnSmith@gmail.com", "John Smith", "JohnSmith", LocalDate.now().minusDays(10));
        User u2 = new User("RileyTaylor@gmail.com", "Riley Taylor", "RileyTaylor", LocalDate.now().minusDays(26));
        User u3 = new User("LaraBaxter@gmail.com", "Lara Baxter", "LaraBaxter", LocalDate.now().minusDays(15));
        User u4 = new User("AlexTyson@gmail.com", "Alex Tyson", "AlexTyson", LocalDate.now().minusDays(18));
        User u5 = new User("SamWright@gmail.com", "Sam Wright", "SamWright", LocalDate.now().minusDays(22));

        u1 = userService.createUser(u1);
        u2 = userService.createUser(u2);
        u3 = userService.createUser(u3);
        u4 = userService.createUser(u4);
        u5 = userService.createUser(u5);

        Project p1 = new Project("Hotel", "Web application for hotel", 6, u1);
        Project p2 = new Project("Shop", "Web application for shop", 8, u2);

        p1 = projectService.createProject(p1);
        p2 = projectService.createProject(p2);

        p1.addUser(u1);
        p1.addUser(u3);
        p1.addUser(u4);

        p2.addUser(u2);
        p2.addUser(u3);
        p2.addUser(u4);
        p2.addUser(u5);

        p1 = projectService.updateProject(p1);
        p2 = projectService.updateProject(p2);

        Task t11 = new Task("Backend" , "Create backend", 3 , Progress.BACKLOG, p1);
        Task t12 = new Task("Frontend" , "Create frontend", 2 , Progress.QA, p1);
        Task t13 = new Task("Database" , "Create database", 2 , Progress.DEVELOPMENT, p1);
        Task t14 = new Task("Login" , "Create login form", 1 , Progress.TEST, p1);
        Task t15 = new Task("Sign up" , "Create sign up form", 1 , Progress.DEPLOYMENT, p1);
        Task t16 = new Task("Init project" , "Init project", 2 , Progress.DONE, p1);

        t11 = taskService.createTask(t11);
        t12 = taskService.createTask(t12);
        t13 = taskService.createTask(t13);
        t14 = taskService.createTask(t14);
        t15 = taskService.createTask(t15);
        t16 = taskService.createTask(t16);

        t12.addUser(u1);
        t12.addUser(u3);
        t13.addUser(u3);
        t14.addUser(u3);
        t15.addUser(u4);
        t16.addUser(u4);

        t12 = taskService.updateTask(t12);
        t13 = taskService.updateTask(t13);
        t14 = taskService.updateTask(t14);
        t15 = taskService.updateTask(t15);
        t16 = taskService.updateTask(t16);

        Task t21 = new Task("Backend" , "Create backend", 3 , Progress.BACKLOG, p2);
        Task t22 = new Task("Frontend" , "Create frontend", 3 , Progress.BACKLOG, p2);
        Task t23 = new Task("Database" , "Create database", 2 , Progress.DEVELOPMENT, p2);
        Task t24 = new Task("Login" , "Create login form", 1 , Progress.TEST, p2);
        Task t25 = new Task("Sign up" , "Create sign up form", 1 , Progress.DEPLOYMENT, p2);
        Task t26 = new Task("Init project" , "Init project", 3 , Progress.DONE, p2);

        t21 = taskService.createTask(t21);
        t22 = taskService.createTask(t22);
        t23 = taskService.createTask(t23);
        t24 = taskService.createTask(t24);
        t25 = taskService.createTask(t25);
        t26 = taskService.createTask(t26);

        t23.addUser(u2);
        t23.addUser(u3);
        t24.addUser(u3);
        t25.addUser(u4);
        t26.addUser(u4);
        t26.addUser(u5);

        t23 = taskService.updateTask(t23);
        t24 = taskService.updateTask(t24);
        t25 = taskService.updateTask(t25);
        t26 = taskService.updateTask(t26);

        log.info("Finished adding data");
    }
}
