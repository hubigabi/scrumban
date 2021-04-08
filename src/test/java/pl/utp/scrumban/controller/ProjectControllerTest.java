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
import pl.utp.scrumban.dto.ProjectDto;
import pl.utp.scrumban.dto.UserDto;
import pl.utp.scrumban.service.JwtService;
import pl.utp.scrumban.service.ProjectService;
import pl.utp.scrumban.service.UserDetailsServiceImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private static final String PROJECT_API_URL = "/api/project";

    @Test
    void getProjectDto() throws Exception {
        long projectID = 1L;
        ProjectDto project = new ProjectDto(projectID, "Hotel", "Web application for hotel", LocalDate.now().minusDays(15), null, 1L, new HashSet<>());

        Mockito.when(projectService.getProjectDto(projectID)).thenReturn(project);

        ResultActions resultActions = mockMvc.perform(get(PROJECT_API_URL + "/{id}", project.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) projectID)));

        checkProjectJSONPath(resultActions, project);
    }

    @Test
    void getAllProjectsDto() throws Exception {
        ProjectDto p1 = new ProjectDto(1L, "Hotel", "Web application for hotel", LocalDate.now().minusDays(15), null, 1L, new HashSet<>());
        ProjectDto p2 = new ProjectDto(2L, "Shop", "Web application for shop", LocalDate.now().minusDays(13), null, 2L, new HashSet<>());
        List<ProjectDto> projects = Arrays.asList(p1, p2);

        Mockito.when(projectService.getAllProjectsDto()).thenReturn(projects);

        ResultActions resultActions = mockMvc.perform(get(PROJECT_API_URL + "/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(projects.size())));

        checkProjectsJSONPath(resultActions, projects);
    }

    @Test
    void createDefaultProject() throws Exception {
        ProjectDto project = new ProjectDto(1L, "Hotel", "Web application for hotel", LocalDate.now().minusDays(15), null, 1L, new HashSet<>());

        Mockito.when(projectService.createDefaultProject(any(ProjectDto.class))).thenReturn(project);

        ResultActions resultActions = mockMvc.perform(post(PROJECT_API_URL)
                .content(objectMapper.writeValueAsString(project))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        checkProjectJSONPath(resultActions, project);
    }

    @Test
    void updateProject() throws Exception {
        ProjectDto project = new ProjectDto(1L, "Hotel", "Web application for hotel", LocalDate.now().minusDays(15), null, 1L, new HashSet<>());

        Mockito.when(projectService.updateProject(any(ProjectDto.class))).thenReturn(project);

        ResultActions resultActions = mockMvc.perform(put(PROJECT_API_URL)
                .content(objectMapper.writeValueAsString(project))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        checkProjectJSONPath(resultActions, project);
    }

    @Test
    void findAllDtoByLeaderUser_Id() throws Exception {
        Long leaderUserID = 1L;
        ProjectDto p1 = new ProjectDto(1L, "Hotel", "Web application for hotel", LocalDate.now().minusDays(15), null, leaderUserID, new HashSet<>());
        ProjectDto p2 = new ProjectDto(2L, "Shop", "Web application for shop", LocalDate.now().minusDays(13), null, leaderUserID, new HashSet<>());
        List<ProjectDto> projects = Arrays.asList(p1, p2);

        Mockito.when(projectService.findAllDtoByLeaderUser_Id(leaderUserID)).thenReturn(projects);

        ResultActions resultActions = mockMvc.perform(get(PROJECT_API_URL + "/allByLeaderUser/{id}", leaderUserID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(projects.size())));

        checkProjectsJSONPath(resultActions, projects);
        checkLeaderUserInProjectsJSONPath(resultActions, projects);
    }

    @Test
    void findAllDtoByUser_Id() throws Exception {
        Long userID = 1L;
        UserDto user = new UserDto(userID, "JohnSmith@gmail.com", "John Smith", LocalDate.now().minusDays(17));

        ProjectDto p1 = new ProjectDto(1L, "Hotel", "Web application for hotel", LocalDate.now().minusDays(15), null, 1L, new HashSet<>(Arrays.asList(user)));
        ProjectDto p2 = new ProjectDto(2L, "Shop", "Web application for shop", LocalDate.now().minusDays(13), null, 2L, new HashSet<>(Arrays.asList(user)));
        List<ProjectDto> projects = Arrays.asList(p1, p2);

        Mockito.when(projectService.findAllDtoByUser_Id(userID)).thenReturn(projects);

        ResultActions resultActions = mockMvc.perform(get(PROJECT_API_URL + "/allByUser/{id}", userID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(projects.size())));

        checkProjectsJSONPath(resultActions, projects);
    }

    void checkProjectJSONPath(ResultActions resultActions, ProjectDto project) throws Exception {
        resultActions
                .andExpect(jsonPath("$.name", is(project.getName())))
                .andExpect(jsonPath("$.description", is(project.getDescription())))
                .andExpect(jsonPath("$.startedLocalDate", is(project.getStartedLocalDate().toString())));

    }

    void checkProjectsJSONPath(ResultActions resultActions, List<ProjectDto> projects) throws Exception {
        for (int i = 0; i < projects.size(); i++) {
            ProjectDto project = projects.get(i);
            String jsonIndexPath = "[" + i + "]";

            resultActions
                    .andExpect(jsonPath("$" + jsonIndexPath + ".name", is(project.getName())))
                    .andExpect(jsonPath("$" + jsonIndexPath + ".description", is(project.getDescription())))
                    .andExpect(jsonPath("$" + jsonIndexPath + ".startedLocalDate", is(project.getStartedLocalDate().toString())));
        }
    }

    void checkLeaderUserInProjectsJSONPath(ResultActions resultActions, List<ProjectDto> projects) throws Exception {
        for (int i = 0; i < projects.size(); i++) {
            ProjectDto project = projects.get(i);
            String jsonIndexPath = "[" + i + "]";

            resultActions
                    .andExpect(jsonPath("$" + jsonIndexPath + ".leaderUserId", is(project.getLeaderUserId().intValue())));
        }
    }

}
