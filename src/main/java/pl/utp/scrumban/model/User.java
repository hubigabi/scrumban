package pl.utp.scrumban.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"projects", "tasks"})
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
@JsonIgnoreProperties({"projects", "tasks"})
@ToString(exclude = {"projects", "tasks"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    @Size(min = 3, max = 30)
    private String name;

    @NotNull
    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate registrationDate;

    @ManyToMany(mappedBy = "users")
    private Set<Project> projects = new HashSet<>();

    @ManyToMany(mappedBy = "users")
    private Set<Task> tasks = new HashSet<>();

    public User(@NotNull @Email String email, @NotNull @Size(min = 3, max = 20) String name, @NotNull String password, LocalDate registrationDate) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.registrationDate = registrationDate;
    }

}