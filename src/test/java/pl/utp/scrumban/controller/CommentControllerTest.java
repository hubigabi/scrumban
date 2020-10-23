package pl.utp.scrumban.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import pl.utp.scrumban.model.Comment;
import pl.utp.scrumban.model.Task;
import pl.utp.scrumban.model.User;
import pl.utp.scrumban.service.CommentService;

import java.time.LocalDateTime;
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
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String COMMENT_API_URL = "/api/comment";

    @Test
    void getComment() throws Exception {
        long commentID = 1L;
        Comment comment = new Comment(commentID, "Lorem ipsum", LocalDateTime.now().minusDays(1), new Task(), new User());
        comment.setId(commentID);

        Mockito.when(commentService.getComment(commentID)).thenReturn(comment);

        ResultActions resultActions = mockMvc.perform(get(COMMENT_API_URL + "/{id}", comment.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) commentID)));

        checkCommentJSONPath(resultActions, comment);
    }

    @Test
    void getAllComments() throws Exception {
        Comment c1 = new Comment(1L, "Lorem ipsum 1", LocalDateTime.now().minusDays(5), new Task(), new User());
        Comment c2 = new Comment(2L, "Lorem ipsum 2", LocalDateTime.now().minusDays(2), new Task(), new User());
        List<Comment> comments = Arrays.asList(c1, c2);

        Mockito.when(commentService.getAllComments()).thenReturn(comments);

        ResultActions resultActions = mockMvc.perform(get(COMMENT_API_URL + "/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(comments.size())));

        checkCommentsJSONPath(resultActions, comments);
    }


    @Test
    void createComment() throws Exception {
        Comment comment = new Comment(1L, "Lorem ipsum", LocalDateTime.now().minusDays(1), new Task(), new User());

        Mockito.when(commentService.createComment(any(Comment.class))).thenReturn(comment);

        ResultActions resultActions = mockMvc.perform(post(COMMENT_API_URL)
                .content(objectMapper.writeValueAsString(comment))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());

        checkCommentJSONPath(resultActions, comment);
    }

    @Test
    void updateComment() throws Exception {
        Comment comment = new Comment(1L, "Lorem ipsum", LocalDateTime.now().minusDays(1), new Task(), new User());

        Mockito.when(commentService.updateComment(any(Comment.class))).thenReturn(comment);

        ResultActions resultActions = mockMvc.perform(put(COMMENT_API_URL)
                .content(objectMapper.writeValueAsString(comment))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        checkCommentJSONPath(resultActions, comment);
    }

    @Test
    void findAllByTask_Id() throws Exception {
        Long taskID = 1L;
        Task task = new Task();
        task.setId(taskID);

        Comment c1 = new Comment(1L, "Lorem ipsum 1", LocalDateTime.now().minusDays(5), task, new User());
        Comment c2 = new Comment(2L, "Lorem ipsum 2", LocalDateTime.now().minusDays(2), task, new User());
        List<Comment> comments = Arrays.asList(c1, c2);

        Mockito.when(commentService.findAllByTask_Id(taskID)).thenReturn(comments);

        ResultActions resultActions = mockMvc.perform(get(COMMENT_API_URL + "/allByTask/{id}", taskID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(comments.size())))
                .andDo(MockMvcResultHandlers.print());

        checkCommentsJSONPath(resultActions, comments);
        checkTaskInCommentsJSONPath(resultActions, comments);
    }

    void checkCommentJSONPath(ResultActions resultActions, Comment comment) throws Exception {
        resultActions
                .andExpect(jsonPath("$.commentText", is(comment.getCommentText())));
    }

    void checkCommentsJSONPath(ResultActions resultActions, List<Comment> comments) throws Exception {
        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            String jsonIndexPath = "[" + i + "]";

            resultActions
                    .andExpect(jsonPath("$" + jsonIndexPath + ".commentText", is(comment.getCommentText())));
        }
    }

    void checkTaskInCommentsJSONPath(ResultActions resultActions, List<Comment> comments) throws Exception {
        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            String jsonIndexPath = "[" + i + "]";

            resultActions
                    .andExpect(jsonPath("$" + jsonIndexPath + ".task.id", is(comment.getTask().getId().intValue())));
        }
    }
}
