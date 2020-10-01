package pl.utp.scrumban.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @Size(min = 3, max = 30)
    private String name;

    @Size(max = 1000)
    private String description;

    @Min(0)
    @Max(3)
    private Integer priority;

    private Progress progress;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startedLocalDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate finishedLocalDate;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToMany(
            cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            fetch = FetchType.EAGER
    )
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

    public Task(@NotNull @Size(min = 3, max = 30) String name, @Size(max = 1000) String description,
                @Min(0) @Max(3) Integer priority, Progress progress, LocalDate startedLocalDate,
                LocalDate finishedLocalDate, Project project) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.progress = progress;
        this.startedLocalDate = startedLocalDate;
        this.finishedLocalDate = finishedLocalDate;
        this.project = project;
    }
}