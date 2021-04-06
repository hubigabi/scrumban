package pl.utp.scrumban.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {

    private Long id;
    private String name;
    private String description;
    private LocalDate startedLocalDate;
    private LocalDate finishedLocalDate;
    private Long leaderUserId;
    private Set<UserDto> users;
}
