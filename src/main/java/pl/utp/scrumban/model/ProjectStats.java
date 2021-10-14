package pl.utp.scrumban.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "projectStats")
public class ProjectStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @NotNull
    private LocalDate localDate;

    @Min(0)
    private double startedTasks;

    @Min(0)
    private double activeTasks;

    @Min(0)
    private double finishedTasks;

    @Min(0)
    private double throughput;

    @Min(0)
    private double leadTime;

    @Min(0)
    private double wip;

}
