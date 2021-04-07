package pl.utp.scrumban.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import pl.utp.scrumban.dto.ColumnDto;
import pl.utp.scrumban.dto.CommentDto;
import pl.utp.scrumban.dto.ProjectDto;
import pl.utp.scrumban.dto.TaskDto;
import pl.utp.scrumban.service.ColumnService;
import pl.utp.scrumban.service.CommentService;
import pl.utp.scrumban.service.ProjectService;
import pl.utp.scrumban.service.TaskService;

@RestController
@CrossOrigin
@Slf4j
public class WebSocketController {

    private final TaskService taskService;
    private final ColumnService columnService;
    private final ProjectService projectService;
    private final CommentService commentService;
    private final SimpMessagingTemplate simpMessagingTemplate;

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
    public TaskDto saveTask(@DestinationVariable String project_id, TaskDto taskDto) {
        return taskService.updateTask(taskDto);
    }

    @MessageMapping("/deleteTask/{project_id}")
    public void deleteTask(@DestinationVariable String project_id, TaskDto taskDto) {
        try {
            taskService.deleteById(taskDto.getId());
            simpMessagingTemplate.convertAndSend("/deletedTask/" + project_id, taskDto);
        } catch (Exception ex) {
            log.info("Could not delete task: " + taskDto.getName());
        }
    }

    @MessageMapping("/saveProject/{project_id}")
    @SendTo("/project/{project_id}")
    public ProjectDto saveProject(@DestinationVariable String project_id, ProjectDto projectDto) {
        return projectService.updateProject(projectDto);
    }

    @MessageMapping("/deleteProject/{project_id}")
    public void deleteProject(@DestinationVariable String project_id) {
        try {
            projectService.deleteProject(Long.parseLong(project_id));
            simpMessagingTemplate.convertAndSend("/deletedProject/" + project_id, Long.valueOf(project_id));
        } catch (Exception ex) {
            log.info("Could not delete project: " + project_id);
        }
    }

    @MessageMapping("/saveComment/{task_id}")
    @SendTo("/comment/{task_id}")
    public CommentDto saveComment(@DestinationVariable String task_id, CommentDto commentDto) {
        return commentService.createComment(commentDto);
    }

    @MessageMapping("/saveColumn/{project_id}")
    @SendTo("/column/{project_id}")
    public ColumnDto saveColumn(@DestinationVariable Long project_id, ColumnDto columnDto) {
        return columnService.saveColumn(columnDto, project_id);
    }

    @MessageMapping("/saveColumnNoChangeInOrder/{project_id}")
    public void saveColumnNoChangeInOrder(@DestinationVariable Long project_id, ColumnDto columnDto) {
        columnDto = columnService.saveColumnNoChangeInOrder(columnDto, project_id);
        simpMessagingTemplate.convertAndSend("/columnNoChangeInOrder/" + project_id, columnDto);
    }

    @MessageMapping("/deleteColumn/{project_id}")
    public void deleteColumn(@DestinationVariable Long project_id, ColumnDto columnDto) {
        try {
            if (columnService.deleteColumnModifyingOthers(columnDto)) {
                simpMessagingTemplate.convertAndSend("/deletedColumn/" + project_id, columnDto);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Could not delete column: " + columnDto.getName());
        }
    }

}