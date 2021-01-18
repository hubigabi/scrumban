package pl.utp.scrumban.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import pl.utp.scrumban.model.Column;
import pl.utp.scrumban.model.Comment;
import pl.utp.scrumban.model.Project;
import pl.utp.scrumban.model.Task;
import pl.utp.scrumban.service.ColumnService;
import pl.utp.scrumban.service.CommentService;
import pl.utp.scrumban.service.ProjectService;
import pl.utp.scrumban.service.TaskService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin
@Slf4j
public class WebSocketController {

    private TaskService taskService;
    private ColumnService columnService;
    private ProjectService projectService;
    private CommentService commentService;
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WebSocketController(TaskService taskService, ColumnService columnService, ProjectService projectService,
                               CommentService commentService, SimpMessagingTemplate simpMessagingTemplate) {
        this.taskService = taskService;
        this.columnService = columnService;
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
        } catch (Exception ex) {
            log.info("Could not delete task: " + task.getName());
        }
    }

    @MessageMapping("/saveProject/{project_id}")
    @SendTo("/project/{project_id}")
    public Project saveProject(@DestinationVariable String project_id, Project project) {
        return projectService.updateProject(project);
    }

    @MessageMapping("/deleteProject/{project_id}")
    public void deleteProject(@DestinationVariable String project_id) {
        try {
            List<Task> projectTasks = taskService.findAllByProject_Id(Long.valueOf(project_id));
            projectTasks.forEach(task ->
                    taskService.deleteById(task.getId())
            );
            List<Column> projectColumns = columnService.findAllByProject_Id(Long.valueOf(project_id));
            projectColumns.forEach(column ->
                    columnService.deleteById(column.getId())
            );

            projectService.deleteById(Long.valueOf(project_id));
            simpMessagingTemplate.convertAndSend("/deletedProject/" + project_id, Long.valueOf(project_id));
        } catch (Exception ex) {
            log.info("Could not delete project: " + project_id);
        }
    }

    @MessageMapping("/saveComment/{task_id}")
    @SendTo("/comment/{task_id}")
    public Comment saveComment(@DestinationVariable String task_id, Comment comment) {
        comment.setLocalDateTime(LocalDateTime.now());
        return commentService.createComment(comment);
    }

}