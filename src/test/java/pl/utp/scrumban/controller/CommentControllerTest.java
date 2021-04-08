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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import pl.utp.scrumban.dto.CommentDto;
import pl.utp.scrumban.service.CommentService;
import pl.utp.scrumban.service.JwtService;
import pl.utp.scrumban.service.UserDetailsServiceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private static final String COMMENT_API_URL = "/api/comment";

    @Test
    void getCommentDto() throws Exception {
        long commentID = 1L;
        CommentDto comment = new CommentDto(1L, "Lorem ipsum 1", LocalDateTime.now().minusDays(5), 1L, 1L, "username1");

        Mockito.when(commentService.getCommentDto(commentID)).thenReturn(comment);

        ResultActions resultActions = mockMvc.perform(get(COMMENT_API_URL + "/{id}", comment.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) commentID)));

        checkCommentJSONPath(resultActions, comment);
    }

    @Test
    void getAllCommentsDto() throws Exception {
        CommentDto c1 = new CommentDto(1L, "Lorem ipsum 1", LocalDateTime.now().minusDays(5), 1L, 1L, "username1");
        CommentDto c2 = new CommentDto(2L, "Lorem ipsum 2", LocalDateTime.now().minusDays(2), 2L, 2L, "username2");
        List<CommentDto> comments = Arrays.asList(c1, c2);

        Mockito.when(commentService.getAllCommentsDto()).thenReturn(comments);

        ResultActions resultActions = mockMvc.perform(get(COMMENT_API_URL + "/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(comments.size())));

        checkCommentsJSONPath(resultActions, comments);
    }

    @Test
    void createComment() throws Exception {
        CommentDto comment = new CommentDto(1L, "Lorem ipsum 1", LocalDateTime.now().minusDays(5), 1L, 1L, "username1");

        Mockito.when(commentService.createComment(any(CommentDto.class))).thenReturn(comment);

        ResultActions resultActions = mockMvc.perform(post(COMMENT_API_URL)
                .content(objectMapper.writeValueAsString(comment))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());

        checkCommentJSONPath(resultActions, comment);
    }

    @Test
    void updateComment() throws Exception {
        CommentDto comment = new CommentDto(1L, "Lorem ipsum 1", LocalDateTime.now().minusDays(5), 1L, 1L, "username1");

        Mockito.when(commentService.updateComment(any(CommentDto.class))).thenReturn(comment);

        ResultActions resultActions = mockMvc.perform(put(COMMENT_API_URL)
                .content(objectMapper.writeValueAsString(comment))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        checkCommentJSONPath(resultActions, comment);
    }

    @Test
    void findAllDtoByTask_Id() throws Exception {
        Long taskId = 1L;

        CommentDto c1 = new CommentDto(1L, "Lorem ipsum 1", LocalDateTime.now().minusDays(5), taskId, 1L, "username1");
        CommentDto c2 = new CommentDto(2L, "Lorem ipsum 2", LocalDateTime.now().minusDays(2), taskId, 2L, "username2");
        List<CommentDto> comments = Arrays.asList(c1, c2);

        Mockito.when(commentService.findAllDtoByTask_Id(taskId)).thenReturn(comments);

        ResultActions resultActions = mockMvc.perform(get(COMMENT_API_URL + "/allByTask/{id}", taskId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(comments.size())))
                .andDo(MockMvcResultHandlers.print());

        checkCommentsJSONPath(resultActions, comments);
        checkTaskInCommentsJSONPath(resultActions, comments);
    }

    void checkCommentJSONPath(ResultActions resultActions, CommentDto comment) throws Exception {
        resultActions
                .andExpect(jsonPath("$.commentText", is(comment.getCommentText())));
    }

    void checkCommentsJSONPath(ResultActions resultActions, List<CommentDto> comments) throws Exception {
        for (int i = 0; i < comments.size(); i++) {
            CommentDto comment = comments.get(i);
            String jsonIndexPath = "[" + i + "]";

            resultActions
                    .andExpect(jsonPath("$" + jsonIndexPath + ".commentText", is(comment.getCommentText())));
        }
    }

    void checkTaskInCommentsJSONPath(ResultActions resultActions, List<CommentDto> comments) throws Exception {
        for (int i = 0; i < comments.size(); i++) {
            CommentDto comment = comments.get(i);
            String jsonIndexPath = "[" + i + "]";

            resultActions
                    .andExpect(jsonPath("$" + jsonIndexPath + ".taskId", is(comment.getTaskId().intValue())));
        }
    }
}
