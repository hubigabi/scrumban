package pl.utp.scrumban.mapper;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import pl.utp.scrumban.dto.ColumnDto;
import pl.utp.scrumban.model.Column;
import pl.utp.scrumban.model.Project;

@Mapper(componentModel = "spring")
public interface ColumnMapper {

    @Mapping(target = "id", source = "columnDto.id")
    @Mapping(target = "name", source = "columnDto.name")
    @Mapping(target = "description", source = "columnDto.description")
    Column mapToColumn(ColumnDto columnDto, Project project);
    
    ColumnDto mapToColumnDto(Column column);
}
