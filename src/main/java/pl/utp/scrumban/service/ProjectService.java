package pl.utp.scrumban.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.utp.scrumban.dto.ProjectDto;
import pl.utp.scrumban.dto.UserDto;
import pl.utp.scrumban.exception.NotExistsException;
import pl.utp.scrumban.mapper.ProjectMapper;
import pl.utp.scrumban.model.*;
import pl.utp.scrumban.repositiory.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ColumnRepository columnRepository;
    private final TaskRepository taskRepository;
    private final ProjectStatsRepository projectStatsRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ColumnRepository columnRepository, TaskRepository taskRepository,
                          ProjectStatsRepository projectStatsRepository, UserRepository userRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.columnRepository = columnRepository;
        this.taskRepository = taskRepository;
        this.projectStatsRepository = projectStatsRepository;
        this.userRepository = userRepository;
        this.projectMapper = projectMapper;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll(Sort.by(Sort.Order.desc("id")));
    }

    public List<ProjectDto> getAllProjectsDto() {
        return projectRepository.findAll(Sort.by(Sort.Order.desc("id"))).stream()
                .map(projectMapper::mapToProjectDto)
                .collect(Collectors.toList());
    }

    public Project getProject(long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public ProjectDto getProjectDto(long id) {
        Project project = projectRepository.findById(id).orElse(null);
        return projectMapper.mapToProjectDto(project);
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public ProjectDto createDefaultProject(ProjectDto projectDto) {
        User leaderUser = userRepository.findById(projectDto.getLeaderUserId())
                .orElseThrow(() -> new NotExistsException("Leader user does not exist"));
        Set<User> users = userRepository.findByIdIn(projectDto.getUsers()
                .stream()
                .mapToLong(UserDto::getId)
                .boxed()
                .collect(Collectors.toSet())
        );

        Project project = projectMapper.mapToProject(projectDto, users, leaderUser);
        project = projectRepository.save(project);

        List<Column> columns = Column.getDefaultColumns(project);
        columnRepository.saveAll(columns);

        return projectMapper.mapToProjectDto(project);
    }

    public Project updateProject(Project project) {
        return projectRepository.save(project);
    }

    public ProjectDto updateProject(ProjectDto projectDto) {
        User leaderUser = userRepository.findById(projectDto.getLeaderUserId())
                .orElseThrow(() -> new NotExistsException("Leader user does not exist"));
        Set<User> users = userRepository.findByIdIn(projectDto.getUsers()
                .stream()
                .mapToLong(UserDto::getId)
                .boxed()
                .collect(Collectors.toSet())
        );

        Project project = projectMapper.mapToProject(projectDto, users, leaderUser);
        project = projectRepository.save(project);

        return projectMapper.mapToProjectDto(project);
    }

    public List<Project> findAllByLeaderUser_Id(Long id) {
        return projectRepository.findAllByLeaderUser_Id(id);
    }

    public List<ProjectDto> findAllDtoByLeaderUser_Id(Long id) {
        return projectRepository.findAllByLeaderUser_Id(id)
                .stream()
                .map(projectMapper::mapToProjectDto)
                .collect(Collectors.toList());
    }

    public List<Project> findAllByUser_Id(Long id) {
        return projectRepository.findAllByUsers_Id(id);
    }

    public List<ProjectDto> findAllDtoByUser_Id(Long id) {
        return projectRepository.findAllByUsers_Id(id)
                .stream()
                .map(projectMapper::mapToProjectDto)
                .collect(Collectors.toList());
    }


    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotExistsException("Project does not exist"));

        List<ProjectStats> projectStatsList = projectStatsRepository.findAllByProject_Id(projectId);
        projectStatsList.forEach(projectStats ->
                projectStatsRepository.deleteById(projectStats.getId())
        );

        List<Task> projectTasks = taskRepository.findAllByProject_Id(projectId);
        projectTasks.forEach(column ->
                taskRepository.deleteById(column.getId())
        );

        List<Column> projectColumns = columnRepository.findAllByProject_Id(projectId);
        projectColumns.forEach(column ->
                columnRepository.deleteById(column.getId())
        );

        projectRepository.deleteById(projectId);
    }


}
