package pl.utp.scrumban.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.utp.scrumban.dto.CommentDto;
import pl.utp.scrumban.model.Comment;
import pl.utp.scrumban.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@CrossOrigin
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CommentDto>> getAllComments() {
        List<CommentDto> allComments = commentService.getAllComments();

        return new ResponseEntity<>(allComments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getComment(@PathVariable("id") long id) {
        CommentDto comment = commentService.getComment(id);

        if (comment != null) {
            return new ResponseEntity<>(comment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto comment) {
        comment = commentService.createComment(comment);

        if (comment != null) {
            return new ResponseEntity<>(comment, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping()
    public ResponseEntity<CommentDto> updateComment(@RequestBody CommentDto comment) {
        comment = commentService.updateComment(comment);

        if (comment != null) {
            return new ResponseEntity<>(comment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/allByTask/{id}")
    public ResponseEntity<List<CommentDto>> findAllByTask_Id(@PathVariable("id") long id) {
        List<CommentDto> comments = commentService.findAllByTask_Id(id);

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

}