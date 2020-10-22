package pl.utp.scrumban.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import pl.utp.scrumban.model.Comment;
import pl.utp.scrumban.model.Task;
import pl.utp.scrumban.model.User;
import pl.utp.scrumban.repositiory.CommentRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    void getComment() {
        Long commentID = 1L;
        Comment comment = new Comment(commentID, "Lorem ipsum", LocalDateTime.now().minusDays(1), new Task(), new User());

        Mockito.when(commentRepository.findById(commentID)).thenReturn(Optional.of(comment));
        Comment actual = commentService.getComment(commentID);

        assertEquals(comment, actual);
    }

    @Test
    void getAllComments() {
        Comment c1 = new Comment(1L, "Lorem ipsum 1", LocalDateTime.now().minusDays(5), new Task(), new User());
        Comment c2 = new Comment(2L, "Lorem ipsum 2", LocalDateTime.now().minusDays(2), new Task(), new User());
        List<Comment> comments = Arrays.asList(c1, c2);

        Mockito.when(commentRepository.findAll(Sort.by(Sort.Order.desc("id")))).thenReturn(comments);

        List<Comment> actual = commentService.getAllComments();

        assertEquals(comments, actual);
    }

    @Test
    void createComment() {
        Comment comment = new Comment(1L, "Lorem ipsum", LocalDateTime.now().minusDays(1), new Task(), new User());

        Mockito.when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        Comment actual = commentService.createComment(comment);

        assertEquals(comment, actual);
    }

    @Test
    void updateComment() {
        Comment comment = new Comment(1L, "Lorem ipsum", LocalDateTime.now().minusDays(1), new Task(), new User());

        Mockito.when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        Comment actual = commentService.updateComment(comment);

        assertEquals(comment, actual);
    }

    @Test
    void saveAll() {
        Comment c1 = new Comment(1L, "Lorem ipsum 1", LocalDateTime.now().minusDays(5), new Task(), new User());
        Comment c2 = new Comment(2L, "Lorem ipsum 2", LocalDateTime.now().minusDays(2), new Task(), new User());
        List<Comment> comments = Arrays.asList(c1, c2);

        Mockito.when(commentRepository.saveAll(any(Iterable.class))).thenReturn(comments);

        List<Comment> actual = commentService.saveAll(comments);

        assertEquals(comments, actual);
    }

    @Test
    void findAllByTask_Id() {
        Long taskID = 1L;
        Task task = new Task();
        task.setId(taskID);

        Comment c1 = new Comment(1L, "Lorem ipsum 1", LocalDateTime.now().minusDays(5), task, new User());
        Comment c2 = new Comment(2L, "Lorem ipsum 2", LocalDateTime.now().minusDays(2), task, new User());
        List<Comment> comments = Arrays.asList(c1, c2);

        Mockito.when(commentRepository.findAllByTask_IdOrderByLocalDateTimeDesc(taskID)).thenReturn(comments);

        List<Comment> actual = commentService.findAllByTask_Id(taskID);

        assertEquals(comments, actual);

        for (Comment comment : actual) {
            assertEquals(comment.getTask().getId(), taskID);
        }

        // Checking order
        actual.stream()
                .map(Comment::getLocalDateTime)
                .reduce((localDateTime, localDateTime2) -> {
                    assertTrue(localDateTime.isBefore(localDateTime2));
                    return localDateTime2;
                });
    }
}
