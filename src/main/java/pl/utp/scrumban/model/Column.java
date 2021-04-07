package pl.utp.scrumban.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "columns")
public class Column {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1)
    private String name;

    private String description;

    private Boolean isWIP;

    @Min(0)
    private Integer numberWIP;

    @Min(0)
    @NotNull
    private Integer numberOrder;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public Column(@NotNull @Size(min = 1) String name, String description, Boolean isWIP,
                  @Min(0) Integer numberWIP, @Min(0) Integer numberOrder, Project project) {
        this.name = name;
        this.description = description;
        this.isWIP = isWIP;
        this.numberWIP = numberWIP;
        this.numberOrder = numberOrder;
        this.project = project;
    }

    public static List<Column> getDefaultColumns(Project project) {
        Column c1 = new Column("Backlog", "Column for Backlog", false, 0, 0, project);
        Column c2 = new Column("QA", "Column for QA", true, 3, 1, project);
        Column c3 = new Column("Development", "Column for Development", true, 3, 2, project);
        Column c4 = new Column("Test", "Column for Test", true, 3, 3, project);
        Column c5 = new Column("Deployment", "Column for Deployment", true, 3, 4, project);
        Column c6 = new Column("Done", "Column for Done", false, 0, 5, project);

        List<Column> columns = new ArrayList<>(Arrays.asList(c1, c2, c3, c4, c5, c6));
        return columns;
    }
}