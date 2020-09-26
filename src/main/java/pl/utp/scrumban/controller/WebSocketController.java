package pl.utp.scrumban.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import pl.utp.scrumban.model.Task;
import pl.utp.scrumban.service.TaskService;

@RestController
@CrossOrigin
public class WebSocketController {

    private TaskService taskService;

    @Autowired
    public WebSocketController(TaskService taskService) {
        this.taskService = taskService;
    }



    @MessageMapping("/task/{project_id}")
    @SendTo("/updatedTask/{project_id}")
    public Task updateTask(@DestinationVariable String project_id, Task task)  {
        task = taskService.updateTask(task);
        System.out.println(task.getDescription());
        System.out.println(task);
        return task;
    }

}