package pl.utp.scrumban.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectStatsDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate localDate;

    private double startedTasks;
    private double activeTasks;
    private double finishedTasks;
    private double throughput;
    private double leadTime;
    private double wip;
}
