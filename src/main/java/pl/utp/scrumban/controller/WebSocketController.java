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
import pl.utp.scrumban.mapper.ColumnMapper;
import pl.utp.scrumban.model.Column;
import pl.utp.scrumban.model.Project;
import pl.utp.scrumban.model.Task;
import pl.utp.scrumban.service.ColumnService;
import pl.utp.scrumban.service.CommentService;
import pl.utp.scrumban.service.ProjectService;
import pl.utp.scrumban.service.TaskService;

import java.util.List;

@RestController
@CrossOrigin
@Slf4j
public class WebSocketController {

    private final TaskService taskService;
    private final ColumnService columnService;
    private final ProjectService projectService;
    private final CommentService commentService;
    private final ColumnMapper columnMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WebSocketController(TaskService taskService, ColumnService columnService, ProjectService projectService,
                               CommentService commentService, ColumnMapper columnMapper, SimpMessagingTemplate simpMessagingTemplate) {
        this.taskService = taskService;
        this.columnService = columnService;
        this.projectService = projectService;
        this.commentService = commentService;
        this.columnMapper = columnMapper;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/saveTask/{project_id}")
    @SendTo("/task/{project_id}")
    public TaskDto saveTask(@DestinationVariable String project_id, TaskDto task) {
        return taskService.updateTask(task);
    }

    @MessageMapping("/deleteTask/{project_id}")
    public void deleteTask(@DestinationVariable String project_id, TaskDto task) {
        try {
            taskService.deleteById(task.getId());
            simpMessagingTemplate.convertAndSend("/deletedTask/" + project_id, task);
        } catch (Exception ex) {
            log.info("Could not delete task: " + task.getName());
        }
    }

    @MessageMapping("/saveProject/{project_id}")
    @SendTo("/project/{project_id}")
    public ProjectDto saveProject(@DestinationVariable String project_id, ProjectDto project) {
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
    public CommentDto saveComment(@DestinationVariable String task_id, CommentDto comment) {
        return commentService.createComment(comment);
    }

    @MessageMapping("/saveColumn/{project_id}")
    @SendTo("/column/{project_id}")
    public ColumnDto saveColumn(@DestinationVariable Long project_id, ColumnDto columnDto) {
        Project project = projectService.getProject(project_id);
        Column column = columnMapper.mapToColumn(columnDto, project);

        if (columnService.existsById(column.getId())) {
            return columnMapper.mapToColumnDto(columnService.updateColumnModifyingOthers(column));
        } else {
            return columnMapper.mapToColumnDto(columnService.createColumnModifyingOthers(column));
        }
    }

    @MessageMapping("/saveColumnNoChangeInOrder/{project_id}")
    public void saveColumnNoChangeInOrder(@DestinationVariable Long project_id, ColumnDto columnDto) {
        Project project = projectService.getProject(project_id);
        Column column = columnMapper.mapToColumn(columnDto, project);

        if (columnService.existsById(column.getId())) {
            column = columnService.saveColumnNoChangeInOrder(column);
            simpMessagingTemplate.convertAndSend("/columnNoChangeInOrder/" + project_id, columnMapper.mapToColumnDto(column));
        }
    }

    @MessageMapping("/deleteColumn/{project_id}")
    public void deleteColumn(@DestinationVariable Long project_id, ColumnDto columnDto) {
        try {
            Project project = projectService.getProject(project_id);
            Column column = columnMapper.mapToColumn(columnDto, project);

            if (columnService.deleteColumnModifyingOthers(column)) {
                simpMessagingTemplate.convertAndSend("/deletedColumn/" + project_id, columnMapper.mapToColumnDto(column));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Could not delete column: " + columnDto.getName());
        }
    }

}