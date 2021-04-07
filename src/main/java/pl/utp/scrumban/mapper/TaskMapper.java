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

    @Mapping(target = "id", source = "task.id")
    @Mapping(target = "name", source = "task.name")
    @Mapping(target = "description", source = "task.description")
    @Mapping(target = "startedLocalDate", source = "task.startedLocalDate")
    @Mapping(target = "finishedLocalDate", source = "task.finishedLocalDate")
    @Mapping(target = "column", source = "column")
    @Mapping(target = "project", source = "project")
    @Mapping(target = "users", source = "users")
    Task mapToTask(TaskDto task, Project project, Column column, Set<User> users);


    @Mapping(target = "columnId", source = "task.column.id")
    @Mapping(target = "columnName", source = "task.column.name")
    @Mapping(target = "projectId", source = "task.project.id")
    @Mapping(target = "projectName", source = "task.project.name")
    TaskDto mapToTaskDto(Task task);
}
