//package pl.utp.scrumban.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import pl.utp.scrumban.model.Project;
//import pl.utp.scrumban.model.User;
//import pl.utp.scrumban.service.JwtService;
//import pl.utp.scrumban.service.ProjectService;
//import pl.utp.scrumban.service.UserDetailsServiceImpl;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
//import static org.hamcrest.core.Is.is;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(ProjectController.class)
//@AutoConfigureMockMvc(addFilters = false)
//class ProjectControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private ProjectService projectService;
//
//    @MockBean
//    private JwtService jwtService;
//
//    @MockBean
//    private UserDetailsServiceImpl userDetailsService;
//
//    private static final String PROJECT_API_URL = "/api/project";
//
//    @Test
//    void getProject() throws Exception {
//        long projectID = 1L;
//        Project project = new Project("Hotel", "Web application for hotel", LocalDate.now().minusDays(15), null, new User());
//        project.setId(projectID);
//
//        Mockito.when(projectService.getProject(projectID)).thenReturn(project);
//
//        ResultActions resultActions = mockMvc.perform(get(PROJECT_API_URL + "/{id}", project.getId())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is((int) projectID)));
//
//        checkProjectJSONPath(resultActions, project);
//    }
//
//    @Test
//    void getAllProjects() throws Exception {
//        Project p1 = new Project("Hotel", "Web application for hotel", LocalDate.now().minusDays(15), null, new User());
//        Project p2 = new Project("Shop", "Web application for shop", LocalDate.now().minusDays(13), null, new User());
//        List<Project> projects = Arrays.asList(p1, p2);
//
//        Mockito.when(projectService.getAllProjects()).thenReturn(projects);
//
//        ResultActions resultActions = mockMvc.perform(get(PROJECT_API_URL + "/all")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(projects.size())));
//
//        checkProjectsJSONPath(resultActions, projects);
//    }
//
//    @Test
//    void createProject() throws Exception {
//        Project project = new Project("Hotel", "Web application for hotel", LocalDate.now().minusDays(15), null, new User());
//
//        Mockito.when(projectService.createDefaultProject(any(Project.class))).thenReturn(project);
//
//        ResultActions resultActions = mockMvc.perform(post(PROJECT_API_URL)
//                .content(objectMapper.writeValueAsString(project))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//
//        checkProjectJSONPath(resultActions, project);
//    }
//
//    @Test
//    void updateProject() throws Exception {
//        Project project = new Project("Hotel", "Web application for hotel", LocalDate.now().minusDays(15), null, new User());
//
//        Mockito.when(projectService.updateProject(any(Project.class))).thenReturn(project);
//
//        ResultActions resultActions = mockMvc.perform(put(PROJECT_API_URL)
//                .content(objectMapper.writeValueAsString(project))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        checkProjectJSONPath(resultActions, project);
//    }
//
//    @Test
//    void findAllProjectsByLeaderUser_Id() throws Exception {
//        Long leaderUserID = 1L;
//        User leaderUser = new User("JohnSmith@gmail.com", "John Smith", "JohnSmith", LocalDate.now().minusDays(17));
//        leaderUser.setId(leaderUserID);
//
//        Project p1 = new Project("Hotel", "Web application for hotel", LocalDate.now().minusDays(15), null, leaderUser);
//        Project p2 = new Project("Shop", "Web application for shop", LocalDate.now().minusDays(13), null, leaderUser);
//        List<Project> projects = Arrays.asList(p1, p2);
//
//        Mockito.when(projectService.findAllByLeaderUser_Id(leaderUserID)).thenReturn(projects);
//
//        ResultActions resultActions = mockMvc.perform(get(PROJECT_API_URL + "/allByLeaderUser/{id}", leaderUserID)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(projects.size())));
//
//        checkProjectsJSONPath(resultActions, projects);
//        checkLeaderUserInProjectsJSONPath(resultActions, projects);
//    }
//
//    @Test
//    void findAllProjectsByUser_Id() throws Exception {
//        Long userID = 1L;
//        User user = new User("JohnSmith@gmail.com", "John Smith", "JohnSmith", LocalDate.now().minusDays(17));
//        user.setId(userID);
//
//        Project p1 = new Project("Hotel", "Web application for hotel", LocalDate.now().minusDays(15), null, new User());
//        p1.addUser(user);
//        Project p2 = new Project("Shop", "Web application for shop", LocalDate.now().minusDays(13), null, new User());
//        p2.addUser(user);
//        List<Project> projects = Arrays.asList(p1, p2);
//
//        Mockito.when(projectService.findAllByUser_Id(userID)).thenReturn(projects);
//
//        ResultActions resultActions = mockMvc.perform(get(PROJECT_API_URL + "/allByUser/{id}", userID)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(projects.size())));
//
//        checkProjectsJSONPath(resultActions, projects);
//    }
//
//    void checkProjectJSONPath(ResultActions resultActions, Project project) throws Exception {
//        resultActions
//                .andExpect(jsonPath("$.name", is(project.getName())))
//                .andExpect(jsonPath("$.description", is(project.getDescription())))
//                .andExpect(jsonPath("$.startedLocalDate", is(project.getStartedLocalDate().toString())));
//
//    }
//
//    void checkProjectsJSONPath(ResultActions resultActions, List<Project> projects) throws Exception {
//        for (int i = 0; i < projects.size(); i++) {
//            Project project = projects.get(i);
//            String jsonIndexPath = "[" + i + "]";
//
//            resultActions
//                    .andExpect(jsonPath("$" + jsonIndexPath + ".name", is(project.getName())))
//                    .andExpect(jsonPath("$" + jsonIndexPath + ".description", is(project.getDescription())))
//                    .andExpect(jsonPath("$" + jsonIndexPath + ".startedLocalDate", is(project.getStartedLocalDate().toString())));
//        }
//    }
//
//    void checkLeaderUserInProjectsJSONPath(ResultActions resultActions, List<Project> projects) throws Exception {
//        for (int i = 0; i < projects.size(); i++) {
//            Project project = projects.get(i);
//            String jsonIndexPath = "[" + i + "]";
//
//            resultActions
//                    .andExpect(jsonPath("$" + jsonIndexPath + ".leaderUser.id", is(project.getLeaderUser().getId().intValue())));
//        }
//    }
//
//}
