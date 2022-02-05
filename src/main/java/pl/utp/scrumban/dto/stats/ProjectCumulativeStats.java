package pl.utp.scrumban.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCumulativeStats {

    private List<LocalDate> days;
    private List<FinishedTasksColumn> finishedTasksColumns;

}