package pl.utp.scrumban.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.utp.scrumban.dto.CommentDto;
import pl.utp.scrumban.exception.NotExistsException;
import pl.utp.scrumban.mapper.CommentMapper;
import pl.utp.scrumban.model.Comment;
import pl.utp.scrumban.model.Task;
import pl.utp.scrumban.model.User;
import pl.utp.scrumban.repositiory.CommentRepository;
import pl.utp.scrumban.repositiory.TaskRepository;
import pl.utp.scrumban.repositiory.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, TaskRepository taskRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.commentMapper = commentMapper;
    }

    public List<CommentDto> getAllComments() {
        return commentRepository.findAll(Sort.by(Sort.Order.desc("id")))
                .stream()
                .map(commentMapper::mapToCommentDto)
                .collect(Collectors.toList());
    }

    public CommentDto getComment(long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        return commentMapper.mapToCommentDto(comment);
    }

    public CommentDto createComment(CommentDto commentDto) {
        User user = userRepository.findById(commentDto.getUserId())
                .orElseThrow(() -> new NotExistsException("User does not exist"));
        Task task = taskRepository.findById(commentDto.getTaskId())
                .orElseThrow(() -> new NotExistsException("Task does not exist"));

        Comment comment = commentMapper.mapToComment(commentDto, user, task);
        comment.setLocalDateTime(LocalDateTime.now());

        comment = commentRepository.save(comment);
        return commentMapper.mapToCommentDto(comment);
    }

    public CommentDto updateComment(CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentDto.getId())
                .orElseThrow(() -> new NotExistsException("Comment does not exist"));

        comment.setCommentText(commentDto.getCommentText());
        comment.setLocalDateTime(commentDto.getLocalDateTime());

        comment = commentRepository.save(comment);
        return commentMapper.mapToCommentDto(comment);
    }


    public List<CommentDto> findAllByTask_Id(Long id) {
        return commentRepository.findAllByTask_IdOrderByLocalDateTimeDesc(id)
                .stream()
                .map(commentMapper::mapToCommentDto)
                .collect(Collectors.toList());
    }

    public List<Comment> saveAll(Iterable<Comment> comments) {
        return commentRepository.saveAll(comments);
    }

}
