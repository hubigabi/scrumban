package pl.utp.scrumban.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.utp.scrumban.model.Project;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByLeaderUser_Id(Long id);

    List<Project> findAllByUsers_Id(Long id);
}