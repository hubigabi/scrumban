package pl.utp.scrumban.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import pl.utp.scrumban.model.Comment;
import pl.utp.scrumban.model.Project;
import pl.utp.scrumban.model.Task;
import pl.utp.scrumban.service.CommentService;
import pl.utp.scrumban.service.ProjectService;
import pl.utp.scrumban.service.TaskService;

import java.time.LocalDateTime;

@RestController
@CrossOrigin
@Slf4j
public class WebSocketController {

    private TaskService taskService;
    private ProjectService projectService;
    private CommentService commentService;
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WebSocketController(TaskService taskService, ProjectService projectService,
                               CommentService commentService, SimpMessagingTemplate simpMessagingTemplate) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.commentService = commentService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/saveTask/{project_id}")
    @SendTo("/task/{project_id}")
    public Task saveTask(@DestinationVariable String project_id, Task task) {
        return taskService.updateTask(task);
    }

    @MessageMapping("/deleteTask/{project_id}")
    public void deleteTask(@DestinationVariable String project_id, Task task) {
        try {
            taskService.deleteById(task.getId());
            simpMessagingTemplate.convertAndSend("/deletedTask/" + project_id, task);
        } catch (EmptyResultDataAccessException ex) {
            log.info("Task to delete doesn't exist");
        }
    }

    @MessageMapping("/saveProject/{project_id}")
    @SendTo("/project/{project_id}")
    public Project saveProject(@DestinationVariable String project_id, Project project) {
        return projectService.updateProject(project);
    }

    @MessageMapping("/saveComment/{task_id}")
    @SendTo("/comment/{task_id}")
    public Comment saveComment(@DestinationVariable String task_id, Comment comment) {
        comment.setLocalDateTime(LocalDateTime.now());
        return commentService.createComment(comment);
    }

}