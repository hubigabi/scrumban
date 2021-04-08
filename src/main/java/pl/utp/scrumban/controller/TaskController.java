package pl.utp.scrumban.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.utp.scrumban.dto.TaskDto;
import pl.utp.scrumban.model.Task;
import pl.utp.scrumban.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/task")
@CrossOrigin
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<TaskDto> allTasks = taskService.getAllTasksDto();

        return new ResponseEntity<>(allTasks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable("id") long id) {
        TaskDto taskDto = taskService.getTaskDto(id);

        if (taskDto != null) {
            return new ResponseEntity<>(taskDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto) {
        taskDto = taskService.createTask(taskDto);

        if (taskDto != null) {
            return new ResponseEntity<>(taskDto, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    public ResponseEntity<TaskDto> updateTask(@RequestBody TaskDto taskDto) {
        taskDto = taskService.updateTask(taskDto);

        if (taskDto != null) {
            return new ResponseEntity<>(taskDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/allByUser/{id}")
    public ResponseEntity<List<TaskDto>> findAllByUsers_Id(@PathVariable("id") long id) {
        List<TaskDto> tasks = taskService.findAllDtoByUsers_Id(id);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/allByProject/{id}")
    public ResponseEntity<List<TaskDto>> findAllByProject_Id(@PathVariable("id") long id) {
        List<TaskDto> tasks = taskService.findAllDtoByProject_Id(id);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/allByProjectAndUser/{project_ID}/{user_ID}")
    public ResponseEntity<List<TaskDto>> findAllByProject_IdAndUsers_Id(@PathVariable("project_ID") long project_ID, @PathVariable("user_ID") long user_ID) {
        List<TaskDto> tasks = taskService.findAllDtoByProject_IdAndUsers_Id(project_ID, user_ID);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
}