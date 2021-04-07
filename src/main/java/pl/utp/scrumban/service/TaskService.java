package pl.utp.scrumban.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.utp.scrumban.dto.TaskDto;
import pl.utp.scrumban.dto.UserDto;
import pl.utp.scrumban.exception.NotExistsException;
import pl.utp.scrumban.mapper.TaskMapper;
import pl.utp.scrumban.model.Column;
import pl.utp.scrumban.model.Project;
import pl.utp.scrumban.model.Task;
import pl.utp.scrumban.model.User;
import pl.utp.scrumban.repositiory.ColumnRepository;
import pl.utp.scrumban.repositiory.ProjectRepository;
import pl.utp.scrumban.repositiory.TaskRepository;
import pl.utp.scrumban.repositiory.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final ColumnRepository columnRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, ColumnRepository columnRepository,
                       UserRepository userRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.columnRepository = columnRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
    }

    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll(Sort.by(Sort.Order.desc("id")))
                .stream()
                .map(taskMapper::mapToTaskDto)
                .collect(Collectors.toList());
    }

    public TaskDto getTask(long id) {
        Task task = taskRepository.findById(id).orElse(null);
        return taskMapper.mapToTaskDto(task);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    public TaskDto createTask(TaskDto taskDto) {
        Project project = projectRepository.findById(taskDto.getProjectId())
                .orElseThrow(() -> new NotExistsException("Project does not exist"));
        Column column = columnRepository.findById(taskDto.getColumnId())
                .orElseThrow(() -> new NotExistsException("Column does not exist"));
        Set<User> users = userRepository.findByIdIn(taskDto.getUsers()
                .stream()
                .mapToLong(UserDto::getId)
                .boxed()
                .collect(Collectors.toSet())
        );

        Task task = taskMapper.mapToTask(taskDto, project, column, users);
        task = taskRepository.save(task);

        return taskMapper.mapToTaskDto(task);
    }

    public TaskDto updateTask(TaskDto taskDto) {
        Project project = projectRepository.findById(taskDto.getProjectId())
                .orElseThrow(() -> new NotExistsException("Project does not exist"));
        Column column = columnRepository.findById(taskDto.getColumnId())
                .orElseThrow(() -> new NotExistsException("Column does not exist"));
        Set<User> users = userRepository.findByIdIn(taskDto.getUsers()
                .stream()
                .mapToLong(UserDto::getId)
                .boxed()
                .collect(Collectors.toSet())
        );

        Task task = taskMapper.mapToTask(taskDto, project, column, users);
        task = taskRepository.save(task);

        return taskMapper.mapToTaskDto(task);
    }

    public List<TaskDto> findAllByUsers_Id(Long id) {
        return taskRepository.findAllByUsers_Id(id)
                .stream()
                .map(taskMapper::mapToTaskDto)
                .collect(Collectors.toList());
    }

    public List<Task> findAllByProject_Id(Long id) {
        return taskRepository.findAllByProject_Id(id);
    }

    public List<TaskDto> findAllDtoByProject_Id(Long id) {
        return taskRepository.findAllByProject_Id(id)
                .stream()
                .map(taskMapper::mapToTaskDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> findAllByProject_IdAndUsers_Id(Long project_ID, Long user_ID) {
        return taskRepository.findAllByProject_IdAndUsers_Id(project_ID, user_ID)
                .stream()
                .map(taskMapper::mapToTaskDto)
                .collect(Collectors.toList());
    }

    public List<Task> saveAll(Iterable<Task> tasks) {
        return taskRepository.saveAll(tasks);
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }
}
