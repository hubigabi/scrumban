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
import pl.utp.scrumban.dto.UserDto;
import pl.utp.scrumban.service.JwtService;
import pl.utp.scrumban.service.UserDetailsServiceImpl;
import pl.utp.scrumban.service.UserService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private static final String USER_API_URL = "/api/user";

    @Test
    void getUserDto() throws Exception {
        long userID = 1L;
        UserDto user = new UserDto(userID, "JohnSmith@gmail.com", "John Smith", LocalDate.now().minusDays(17));

        Mockito.when(userService.getUserDto(userID)).thenReturn(user);

        ResultActions resultActions = mockMvc.perform(get(USER_API_URL + "/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) userID)));

        checkUserDtoJSONPath(resultActions, user);
    }

    @Test
    void getAllUsersDto() throws Exception {
        UserDto u1 = new UserDto(1L, "JohnSmith@gmail.com", "John Smith", LocalDate.now().minusDays(17));
        UserDto u2 = new UserDto(2L, "RileyTaylor@gmail.com", "Riley Taylor", LocalDate.now().minusDays(26));
        List<UserDto> users = Arrays.asList(u1, u2);

        Mockito.when(userService.getAllUsersDto()).thenReturn(users);

        ResultActions resultActions = mockMvc.perform(get(USER_API_URL + "/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(users.size())));

        checkUserDtosJSONPath(resultActions, users);
    }

    @Test
    void getUserDtoByEmail() throws Exception {
        String email = "JohnSmith@gmail.com";
        UserDto user = new UserDto(1L, email, "John Smith", LocalDate.now().minusDays(17));

        Mockito.when(userService.getUserDtoByEmail(email)).thenReturn(user);

        ResultActions resultActions = mockMvc.perform(get(USER_API_URL + "/email/{email}", email)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(email)));

        checkUserDtoJSONPath(resultActions, user);
    }

    void checkUserDtoJSONPath(ResultActions resultActions, UserDto user) throws Exception {
        resultActions
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.registrationDate", is(user.getRegistrationDate().toString())));

    }

    void checkUserDtosJSONPath(ResultActions resultActions, List<UserDto> users) throws Exception {
        for (int i = 0; i < users.size(); i++) {
            UserDto user = users.get(i);
            String jsonIndexPath = "[" + i + "]";

            resultActions
                    .andExpect(jsonPath("$" + jsonIndexPath + ".email", is(user.getEmail())))
                    .andExpect(jsonPath("$" + jsonIndexPath + ".name", is(user.getName())))
                    .andExpect(jsonPath("$" + jsonIndexPath + ".registrationDate", is(user.getRegistrationDate().toString())));
        }
    }

}
