package pl.utp.scrumban.repositiory;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import pl.utp.scrumban.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:test.properties")
@AutoConfigureTestDatabase(replace = NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private List<User> users;

    @BeforeAll
    public void init() {
        users = new ArrayList<>();
        User u1 = new User("JohnSmith@gmail.com", "John Smith", "JohnSmith", LocalDate.now().minusDays(17));
        User u2 = new User("RileyTaylor@gmail.com", "Riley Taylor", "RileyTaylor", LocalDate.now().minusDays(26));

        users = userRepository.saveAll(Arrays.asList(u1, u2));
    }

    @AfterAll
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void findOne() {
        User expected = users.get(0);
        User actual = userRepository.findById(expected.getId()).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    void findAll() {
        List<User> expected = users;
        List<User> actual = userRepository.findAll();

        MatcherAssert.assertThat("List equality without order",
                actual, Matchers.containsInAnyOrder(expected.toArray()));
    }

    @Test
    void save() {
        Integer expectedSize = userRepository.findAll().size() + 1;

        User user = new User("JohnSmith2@gmail.com", "John Smith2", "JohnSmith2", LocalDate.now().minusDays(15));
        userRepository.save(user);
        Integer actualSize = userRepository.findAll().size();

        assertEquals(expectedSize, actualSize);
    }


    @Test
    void findByEmail() {
        User expected = users.get(0);
        User actual = userRepository.findByEmail(expected.getEmail());

        assertEquals(expected, actual);
    }
}