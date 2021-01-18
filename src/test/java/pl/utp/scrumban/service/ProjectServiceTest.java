package pl.utp.scrumban.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import pl.utp.scrumban.model.Project;
import pl.utp.scrumban.model.User;
import pl.utp.scrumban.repositiory.ProjectRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void getProject() {
        Long userID = 1L;
        Project project = new Project("Hotel", "Web application for hotel", LocalDate.now().minusDays(15), null, new User());
        project.setId(userID);

        Mockito.when(projectRepository.findById(userID)).thenReturn(Optional.of(project));
        Project actual = projectService.getProject(userID);

        assertEquals(project, actual);
    }

    @Test
    void getAllProjects() {
        Project p1 = new Project("Hotel", "Web application for hotel", LocalDate.now().minusDays(15), null, new User());
        Project p2 = new Project("Shop", "Web application for shop", LocalDate.now().minusDays(13), null, new User());
        List<Project> projects = Arrays.asList(p1, p2);

        Mockito.when(projectRepository.findAll(Sort.by(Sort.Order.desc("id")))).thenReturn(projects);

        List<Project> actual = projectService.getAllProjects();

        assertEquals(projects, actual);
    }

    @Test
    void createProject() {
        Project project = new Project("Hotel", "Web application for hotel", LocalDate.now().minusDays(15), null, new User());

        Mockito.when(projectRepository.save(any(Project.class))).thenReturn(project);
        Project actual = projectService.createProject(project);

        assertEquals(project, actual);
    }

    @Test
    void updateProject() {
        Project project = new Project("Hotel", "Web application for hotel", LocalDate.now().minusDays(15), null, new User());

        Mockito.when(projectRepository.save(any(Project.class))).thenReturn(project);
        Project actual = projectService.updateProject(project);

        assertEquals(project, actual);
    }

    @Test
    void findAllByLeaderUser_Id() {
        Long leaderUserID = 1L;
        User leaderUser = new User("JohnSmith@gmail.com", "John Smith", "JohnSmith", LocalDate.now().minusDays(17));
        leaderUser.setId(leaderUserID);

        Project p1 = new Project("Hotel", "Web application for hotel", LocalDate.now().minusDays(15), null, leaderUser);
        Project p2 = new Project("Shop", "Web application for shop", LocalDate.now().minusDays(13), null, leaderUser);
        List<Project> projects = Arrays.asList(p1, p2);

        Mockito.when(projectRepository.findAllByLeaderUser_Id(leaderUserID)).thenReturn(projects);

        List<Project> actual = projectService.findAllByLeaderUser_Id(leaderUserID);

        assertEquals(projects, actual);

        for (Project project : actual) {
            assertEquals(project.getLeaderUser().getId(), leaderUserID);
        }
    }

    @Test
    void findAllByUser_Id() {
        Long userID = 1L;
        User user = new User("JohnSmith@gmail.com", "John Smith", "JohnSmith", LocalDate.now().minusDays(17));
        user.setId(userID);

        Project p1 = new Project("Hotel", "Web application for hotel", LocalDate.now().minusDays(15), null, new User());
        p1.addUser(user);
        Project p2 = new Project("Shop", "Web application for shop", LocalDate.now().minusDays(13), null, new User());
        p2.addUser(user);
        List<Project> projects = Arrays.asList(p1, p2);

        Mockito.when(projectRepository.findAllByUsers_Id(userID)).thenReturn(projects);

        List<Project> actual = projectService.findAllByUser_Id(userID);

        assertEquals(projects, actual);

        for (Project project : actual) {
            assertTrue(project.getUsers().stream()
                    .anyMatch(u ->
                            u.getId().equals(userID)
                    )
            );
        }
    }
}