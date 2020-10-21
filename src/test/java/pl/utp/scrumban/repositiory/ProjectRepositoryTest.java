package pl.utp.scrumban.repositiory;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import pl.utp.scrumban.model.Project;
import pl.utp.scrumban.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:test.properties")
@AutoConfigureTestDatabase(replace = NONE)
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    private static List<User> users;
    private static List<Project> projects;

    @BeforeAll
//    https://stackoverflow.com/questions/29340286/how-to-autowire-field-in-static-beforeclass
    public static void init(@Autowired ProjectRepository projectRepository,
                            @Autowired UserRepository userRepository) {
        users = new ArrayList<>();
        projects = new ArrayList<>();

        User u1 = new User("JohnSmith@gmail.com", "John Smith", "JohnSmith", LocalDate.now().minusDays(17));
        User u2 = new User("RileyTaylor@gmail.com", "Riley Taylor", "RileyTaylor", LocalDate.now().minusDays(26));

        u1 = userRepository.save(u1);
        u2 = userRepository.save(u2);
        users = userRepository.saveAll(Arrays.asList(u1, u2));

        Project p1 = new Project("Hotel", "Web application for hotel", 4, LocalDate.now().minusDays(15), null, u1);
        Project p2 = new Project("Shop", "Web application for shop", 5, LocalDate.now().minusDays(13), null, u2);
        projectRepository.saveAll(Arrays.asList(p1, p2));

        p1.addUser(u1);
        p1.addUser(u2);
        p2.addUser(u2);
        projects = projectRepository.saveAll(Arrays.asList(p1, p2));
    }

    @Test
    void findOne() {
        Project expected = projects.get(0);
        Project actual = projectRepository.findById(expected.getId()).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    void findAll() {
        List<Project> expected = projects;
        List<Project> actual = projectRepository.findAll();

        MatcherAssert.assertThat("List equality without order",
                actual, Matchers.containsInAnyOrder(expected.toArray()));
    }

    @Test
    void save() {
        Integer expectedSize = projectRepository.findAll().size() + 1;

        Project project = new Project("Hotel", "Web application for hotel", 4, LocalDate.now().minusDays(15), null, users.get(0));
        projectRepository.save(project);
        Integer actualSize = projectRepository.findAll().size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    void findAllByLeaderUser_Id() {
        Long leaderID = users.get(0).getId();

        List<Project> expected = projects
                .stream()
                .filter(project -> project.getLeaderUser().getId().equals(leaderID))
                .collect(Collectors.toList());

        List<Project> actual = projectRepository.findAllByLeaderUser_Id(leaderID);

        MatcherAssert.assertThat("List equality without order",
                actual, Matchers.containsInAnyOrder(expected.toArray()));
    }

    @Test
    void findAllByUsers_Id() {
        Long userID = users.get(1).getId();

        List<Project> expected = projects
                .stream()
                .filter(project -> project.getUsers()
                        .stream()
                        .anyMatch(user -> user.getId().equals(userID)))
                .collect(Collectors.toList());

        List<Project> actual = projectRepository.findAllByUsers_Id(userID);

        MatcherAssert.assertThat("List equality without order",
                actual, Matchers.containsInAnyOrder(expected.toArray()));
    }
}