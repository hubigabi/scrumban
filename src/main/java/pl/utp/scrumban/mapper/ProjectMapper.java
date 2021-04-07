package pl.utp.scrumban.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.utp.scrumban.dto.ProjectDto;
import pl.utp.scrumban.model.Project;
import pl.utp.scrumban.model.User;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(target = "id", source = "projectDto.id")
    @Mapping(target = "name", source = "projectDto.name")
    Project mapToProject(ProjectDto projectDto, Set<User> users, User leaderUser);

    @Mapping(target = "leaderUserId", expression = "java(project.getLeaderUser().getId())")
    ProjectDto mapToProjectDto(Project project);
}
