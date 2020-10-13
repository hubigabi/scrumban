package pl.utp.scrumban.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.utp.scrumban.model.Comment;
import pl.utp.scrumban.repositiory.CommentRepository;

import java.util.List;

@Service
public class CommentService {

    private CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll(Sort.by(Sort.Order.desc("id")));
    }

    public Comment getComment(long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment updateComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> saveAll(Iterable<Comment> comments) {
        return commentRepository.saveAll(comments);
    }

    public List<Comment> findAllByTask_Id(Long id) {
        return commentRepository.findAllByTask_IdOrderByLocalDateTimeDesc(id);
    }

}
