package pl.utp.scrumban.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.utp.scrumban.model.ProjectStats;;

@Repository
public interface ProjectStatsRepository extends JpaRepository<ProjectStats, Long> {

}
