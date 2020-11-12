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
@Entity(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 30)
    private String name;
    private String description;

    @Min(3)
    @Max(15)
    private Integer numberWIP;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startedLocalDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate finishedLocalDate;


    @ManyToOne
    @JoinColumn(name = "leader_user_id")
    private User leaderUser;

    @ManyToMany(
            fetch = FetchType.EAGER
    )
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


    public Project(@NotNull @Size(min = 3, max = 30) String name, String description,
                   Integer numberWIP, LocalDate startedLocalDate, LocalDate finishedLocalDate, User leaderUser) {
        this.name = name;
        this.description = description;
        this.numberWIP = numberWIP;
        this.startedLocalDate = startedLocalDate;
        this.finishedLocalDate = finishedLocalDate;
        this.leaderUser = leaderUser;
    }

}