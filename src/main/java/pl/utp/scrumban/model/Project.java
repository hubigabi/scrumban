package pl.utp.scrumban.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3)
    private String name;

    private String description;
    private LocalDate startedLocalDate;
    private LocalDate finishedLocalDate;

    @ManyToOne
    @JoinColumn(name = "leader_user_id")
    private User leaderUser;

    @ManyToMany
    @JoinTable(
            name = "projects_users",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    public void addUser(User user) {
        this.users.add(user);
        user.getProjects().add(this);
    }

    public void removeUser(User user) {
        this.users.remove(user);
        user.getProjects().remove(this);
    }


    public Project(@NotNull @Size(min = 3) String name, String description,
                   LocalDate startedLocalDate, LocalDate finishedLocalDate, User leaderUser) {
        this.name = name;
        this.description = description;
        this.startedLocalDate = startedLocalDate;
        this.finishedLocalDate = finishedLocalDate;
        this.leaderUser = leaderUser;
    }

}