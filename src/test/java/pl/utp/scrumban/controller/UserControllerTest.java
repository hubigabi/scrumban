package pl.utp.scrumban.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.utp.scrumban.model.User;
import pl.utp.scrumban.service.JwtService;
import pl.utp.scrumban.service.UserDetailsServiceImpl;
import pl.utp.scrumban.service.UserService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private static final String USER_API_URL = "/api/user";

    @Test
    void getUser() throws Exception {
        long userID = 1L;
        User user = new User("JohnSmith@gmail.com", "John Smith", "JohnSmith", LocalDate.now().minusDays(17));
        user.setId(userID);

        Mockito.when(userService.getUser(userID)).thenReturn(user);

        ResultActions resultActions = mockMvc.perform(get(USER_API_URL + "/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) userID)));

        checkUserJSONPath(resultActions, user);
    }

    @Test
    void getAllUsers() throws Exception {
        User u1 = new User("JohnSmith@gmail.com", "John Smith", "JohnSmith", LocalDate.now().minusDays(17));
        User u2 = new User("RileyTaylor@gmail.com", "Riley Taylor", "RileyTaylor", LocalDate.now().minusDays(26));
        List<User> users = Arrays.asList(u1, u2);

        Mockito.when(userService.getAllUsers()).thenReturn(users);

        ResultActions resultActions = mockMvc.perform(get(USER_API_URL + "/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(users.size())));

        checkUsersJSONPath(resultActions, users);
    }

    @Test
    void getUserByEmail() throws Exception {
        String email = "JohnSmith@gmail.com";
        User user = new User(email, "John Smith", "JohnSmith", LocalDate.now().minusDays(17));

        Mockito.when(userService.getUserByEmail(email)).thenReturn(user);

        ResultActions resultActions = mockMvc.perform(get(USER_API_URL + "/email/{email}", email)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(email)));

        checkUserJSONPath(resultActions, user);
    }

    @Test
    void createUser() throws Exception {
        User user = new User("JohnSmith@gmail.com", "John Smith", "JohnSmith", LocalDate.now().minusDays(17));

        Mockito.when(userService.createUser(any(User.class))).thenReturn(user);

        ResultActions resultActions = mockMvc.perform(post(USER_API_URL)
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        checkUserJSONPath(resultActions, user);
    }

    @Test
    void updateUser() throws Exception {
        User user = new User("JohnSmith@gmail.com", "John Smith", "JohnSmith", LocalDate.now().minusDays(17));

        Mockito.when(userService.updateUser(any(User.class))).thenReturn(user);

        ResultActions resultActions = mockMvc.perform(put(USER_API_URL)
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        checkUserJSONPath(resultActions, user);
    }

    void checkUserJSONPath(ResultActions resultActions, User user) throws Exception {
        resultActions
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.registrationDate", is(user.getRegistrationDate().toString())));

    }

    void checkUsersJSONPath(ResultActions resultActions, List<User> users) throws Exception {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            String jsonIndexPath = "[" + i + "]";

            resultActions
                    .andExpect(jsonPath("$" + jsonIndexPath + ".email", is(user.getEmail())))
                    .andExpect(jsonPath("$" + jsonIndexPath + ".name", is(user.getName())))
                    .andExpect(jsonPath("$" + jsonIndexPath + ".password", is(user.getPassword())))
                    .andExpect(jsonPath("$" + jsonIndexPath + ".registrationDate", is(user.getRegistrationDate().toString())));
        }
    }

}
