package pl.utp.scrumban.model;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
@EqualsAndHashCode(exclude = {"projects", "tasks"})
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

    private String password;
    private LocalDate registrationDate;

    @ManyToMany(mappedBy = "users")
    private Set<Project> projects = new HashSet<>();

    @ManyToMany(mappedBy = "users")
    private Set<Task> tasks = new HashSet<>();

    public User(@NotNull @Email String email, @NotNull @Size(min = 3, max = 30) String name,
                String password, LocalDate registrationDate) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.registrationDate = registrationDate;
    }

}