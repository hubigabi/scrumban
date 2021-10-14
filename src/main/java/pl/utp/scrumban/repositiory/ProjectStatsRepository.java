package pl.utp.scrumban.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.utp.scrumban.model.ProjectStats;

import java.util.List;

@Repository
public interface ProjectStatsRepository extends JpaRepository<ProjectStats, Long> {

    List<ProjectStats> findAllByProject_Id(Long id);

}
