package pl.utp.scrumban.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.utp.scrumban.dto.TaskDto;
import pl.utp.scrumban.model.Column;
import pl.utp.scrumban.model.Project;
import pl.utp.scrumban.model.Task;
import pl.utp.scrumban.model.User;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "id", source = "taskDto.id")
    @Mapping(target = "name", source = "taskDto.name")
    @Mapping(target = "description", source = "taskDto.description")
    @Mapping(target = "startedLocalDate", source = "taskDto.startedLocalDate")
    @Mapping(target = "finishedLocalDate", source = "taskDto.finishedLocalDate")
    @Mapping(target = "column", source = "column")
    @Mapping(target = "project", source = "project")
    @Mapping(target = "users", source = "users")
    Task mapToTask(TaskDto taskDto, Project project, Column column, Set<User> users);


    @Mapping(target = "columnId", source = "task.column.id")
    @Mapping(target = "columnName", source = "task.column.name")
    @Mapping(target = "projectId", source = "task.project.id")
    @Mapping(target = "projectName", source = "task.project.name")
    TaskDto mapToTaskDto(Task task);
}
