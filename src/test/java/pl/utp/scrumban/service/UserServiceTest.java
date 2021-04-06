//package pl.utp.scrumban.service;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Sort;
//import pl.utp.scrumban.model.User;
//import pl.utp.scrumban.repositiory.UserRepository;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private UserService userService;
//
//    @Test
//    void getUser() {
//        Long userID = 1L;
//        User user = new User("JohnSmith@gmail.com", "John Smith", "JohnSmith", LocalDate.now().minusDays(17));
//        user.setId(userID);
//
//        Mockito.when(userRepository.findById(userID)).thenReturn(Optional.of(user));
//        User actual = userService.getUser(userID);
//
//        assertEquals(user, actual);
//    }
//
//    @Test
//    void getAllUsers() {
//        User u1 = new User("JohnSmith@gmail.com", "John Smith", "JohnSmith", LocalDate.now().minusDays(17));
//        User u2 = new User("RileyTaylor@gmail.com", "Riley Taylor", "RileyTaylor", LocalDate.now().minusDays(26));
//        List<User> users = Arrays.asList(u1, u2);
//
//        Mockito.when(userRepository.findAll(Sort.by(Sort.Order.desc("id")))).thenReturn(users);
//
//        List<User> actual = userService.getAllUsers();
//
//        assertEquals(users, actual);
//    }
//
//    @Test
//    void getUserByEmail() {
//        String email = "JohnSmith@gmail.com";
//        User user = new User(email, "John Smith", "JohnSmith", LocalDate.now().minusDays(17));
//
//        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);
//        User actual = userService.getUserByEmail(email);
//
//        assertEquals(user, actual);
//    }
//
//    @Test
//    void createUser() {
//        User user = new User("JohnSmith@gmail.com", "John Smith", "JohnSmith", LocalDate.now().minusDays(17));
//
//        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
//        User actual = userService.createUser(user);
//
//        assertEquals(user, actual);
//    }
//
//    @Test
//    void updateUser() {
//        User user = new User("JohnSmith@gmail.com", "John Smith", "JohnSmith", LocalDate.now().minusDays(17));
//
//        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
//        User actual = userService.updateUser(user);
//
//        assertEquals(user, actual);
//    }
//
//}
