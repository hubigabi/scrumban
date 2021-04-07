package pl.utp.scrumban.mapper;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import pl.utp.scrumban.dto.ColumnDto;
import pl.utp.scrumban.model.Column;
import pl.utp.scrumban.model.Project;

@Mapper(componentModel = "spring")
public interface ColumnMapper {

    @Mapping(target = "id", source = "column.id")
    @Mapping(target = "name", source = "column.name")
    @Mapping(target = "description", source = "column.description")
    Column mapToColumn(ColumnDto column, Project project);
    
    ColumnDto mapToColumnDto(Column column);
}
