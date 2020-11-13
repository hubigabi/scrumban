package pl.utp.scrumban.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.utp.scrumban.model.Project;
import pl.utp.scrumban.repositiory.ProjectRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll(Sort.by(Sort.Order.desc("id")));
    }

    public Project getProject(long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project updateProject(Project project) {
        return projectRepository.save(project);
    }

    public List<Project> findAllByLeaderUser_Id(Long id) {
        return projectRepository.findAllByLeaderUser_Id(id);
    }

    public List<Project> findAllByUser_Id(Long id) {
        return projectRepository.findAllByUsers_Id(id);
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
