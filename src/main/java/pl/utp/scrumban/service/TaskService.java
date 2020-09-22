package pl.utp.scrumban.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.utp.scrumban.model.Project;
import pl.utp.scrumban.model.Task;
import pl.utp.scrumban.repositiory.TaskRepository;

import java.util.List;

@Service
public class TaskService {

    private TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll(Sort.by(Sort.Order.desc("id")));
    }

    public Task getTask(long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> findAllByUsers_Id(Long id){
        return taskRepository.findAllByUsers_Id(id);
    }

    public List<Task> findAllByProject_Id(Long id){
        return taskRepository.findAllByProject_Id(id);
    }

    public List<Task> findAllByProject_IdAndUsers_Id(Long project_ID, Long user_ID){
        return taskRepository.findAllByProject_IdAndUsers_Id(project_ID, user_ID);
    }
}
