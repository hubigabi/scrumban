package pl.utp.scrumban.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

    private Long id;
    private String name;
    private String description;
    private Integer priority;
    private LocalDate startedLocalDate;
    private LocalDate finishedLocalDate;
    private Long columnId;
    private String columnName;
    private Long projectId;
    private String projectName;
    private Set<UserDto> users;
}
