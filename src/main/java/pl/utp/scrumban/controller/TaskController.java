package pl.utp.scrumban.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.utp.scrumban.model.Task;
import pl.utp.scrumban.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/task")
@CrossOrigin
public class TaskController {

    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> allTasks = taskService.getAllTasks();

        return new ResponseEntity<>(allTasks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable("id") long id) {
        Task task = taskService.getTask(id);

        if (task != null) {
            return new ResponseEntity<>(task, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody Task task){
         task = taskService.createTask(task);

        if (task != null) {
            return new ResponseEntity<>(task, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    public ResponseEntity<Task> updateTask(@RequestBody Task task){
        task = taskService.updateTask(task);

        if (task != null) {
            return new ResponseEntity<>(task, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/allByUser/{id}")
    public ResponseEntity<List<Task>> findAllByUsers_Id(@PathVariable("id") long id) {
        List<Task> tasks = taskService.findAllByUsers_Id(id);

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/allByProject/{id}")
    public ResponseEntity<List<Task>> findAllByProject_Id(@PathVariable("id") long id) {
        List<Task> tasks = taskService.findAllByProject_Id(id);

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/allByProjectAndUser/{project_ID}/{user_ID}")
    public ResponseEntity<List<Task>> findAllByProject_IdAndUsers_Id(@PathVariable("project_ID") long project_ID, @PathVariable("user_ID") long user_ID) {
        List<Task> tasks = taskService.findAllByProject_IdAndUsers_Id(project_ID, user_ID);

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
}