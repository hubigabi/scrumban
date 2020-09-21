package pl.utp.scrumban.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.utp.scrumban.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

}