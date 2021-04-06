package pl.utp.scrumban.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.utp.scrumban.dto.ProjectDto;
import pl.utp.scrumban.dto.UserDto;
import pl.utp.scrumban.exception.NotExistsException;
import pl.utp.scrumban.mapper.ProjectMapper;
import pl.utp.scrumban.model.*;
import pl.utp.scrumban.repositiory.ColumnRepository;
import pl.utp.scrumban.repositiory.ProjectRepository;
import pl.utp.scrumban.repositiory.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ColumnRepository columnRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ColumnRepository columnRepository,
                          UserRepository userRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.columnRepository = columnRepository;
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

    public List<ProjectDto> findAllByLeaderUser_Id(Long id) {
        return projectRepository.findAllByLeaderUser_Id(id)
                .stream()
                .map(projectMapper::mapToProjectDto)
                .collect(Collectors.toList());
    }

    public List<ProjectDto> findAllByUser_Id(Long id) {
        return projectRepository.findAllByUsers_Id(id)
                .stream()
                .map(projectMapper::mapToProjectDto)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        Optional<Project> projectOptional = projectRepository.findById(id);

        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            project.setUsers(new HashSet<>());
            projectRepository.save(project);
            projectRepository.deleteById(id);
        }
    }

}
