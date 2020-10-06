package pl.utp.scrumban.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import pl.utp.scrumban.model.Project;
import pl.utp.scrumban.model.Task;
import pl.utp.scrumban.service.ProjectService;
import pl.utp.scrumban.service.TaskService;

@RestController
@CrossOrigin
public class WebSocketController {

    private TaskService taskService;
    private ProjectService projectService;

    @Autowired
    public WebSocketController(TaskService taskService, ProjectService projectService) {
        this.taskService = taskService;
        this.projectService = projectService;
    }

    @MessageMapping("/task/{project_id}")
    @SendTo("/updateTask/{project_id}")
    public Task updateTask(@DestinationVariable String project_id, Task task) {
        return taskService.updateTask(task);
    }

    @MessageMapping("/project/{project_id}")
    @SendTo("/updateProject/{project_id}")
    public Project updateProject(@DestinationVariable String project_id, Project project) {
        return projectService.updateProject(project);
    }

}