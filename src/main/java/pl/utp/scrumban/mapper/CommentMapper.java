package pl.utp.scrumban.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.utp.scrumban.dto.CommentDto;
import pl.utp.scrumban.model.Comment;
import pl.utp.scrumban.model.Task;
import pl.utp.scrumban.model.User;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", source = "commentDto.id")
    Comment mapToComment(CommentDto commentDto, User user, Task task);

    @Mapping(target = "taskId", expression = "java(comment.getTask().getId())")
    @Mapping(target = "userId", expression = "java(comment.getUser().getId())")
    @Mapping(target = "userName", expression = "java(comment.getUser().getName())")
    CommentDto mapToCommentDto(Comment comment);
}
