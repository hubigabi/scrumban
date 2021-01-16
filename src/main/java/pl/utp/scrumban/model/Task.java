package pl.utp.scrumban.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3)
    private String name;

    private String description;

    @Min(0)
    @Max(3)
    private Integer priority;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startedLocalDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate finishedLocalDate;

    @ManyToOne
    @JoinColumn(name = "column_id", nullable = false)
    @JsonIgnoreProperties({"project"})
    private Column column;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToMany
    @JoinTable(
            name = "tasks_users",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    public void addUser(User user) {
        this.users.add(user);
        user.getTasks().add(this);
    }

    public void removeUser(User user) {
        this.users.remove(user);
        user.getTasks().remove(this);
    }


    public Task(@NotNull @Size(min = 3) String name, String description,
                @Min(0) @Max(3) Integer priority, LocalDate startedLocalDate,
                LocalDate finishedLocalDate, Column column, Project project) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.startedLocalDate = startedLocalDate;
        this.finishedLocalDate = finishedLocalDate;
        this.column = column;
        this.project = project;
    }
}