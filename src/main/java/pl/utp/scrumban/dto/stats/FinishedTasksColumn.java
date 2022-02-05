package pl.utp.scrumban.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class FinishedTasksColumn {

    private long columnId;
    private String columnName;
    private int columnOrder;
    private List<Integer> finishedTaskStatus;

}
