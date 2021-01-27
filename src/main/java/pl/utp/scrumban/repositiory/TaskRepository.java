package pl.utp.scrumban.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.utp.scrumban.model.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByUsers_Id(Long id);

    List<Task> findAllByProject_Id(Long id);

    long countByColumn_Id(Long id);

    List<Task> findAllByProject_IdAndUsers_Id(Long project_ID, Long user_ID);

    void deleteById(Long Id);

    @Modifying(clearAutomatically = true)
    @Query(nativeQuery = true, value = "UPDATE tasks t " +
            "SET finished_local_date=NULL " +
            "FROM columns c " +
            "WHERE t.column_id=c.id " +
            "AND c.project_id= :project_id " +
            "AND t.finished_local_date IS NOT NULL " +
            "AND c.number_order < (SELECT MAX(number_order) FROM columns WHERE project_id = :project_id);")
    int setFinishedDateToNullForAllTasksInProjectInNotLastColumn(@Param("project_id") Long project_id);

    @Modifying(clearAutomatically = true)
    @Query(nativeQuery = true, value = "UPDATE tasks t " +
            "SET finished_local_date=CURRENT_DATE " +
            "FROM columns c " +
            "WHERE t.column_id=c.id " +
            "AND c.project_id= :project_id " +
            "AND t.finished_local_date IS NULL " +
            "AND c.number_order = (SELECT MAX(number_order) FROM columns WHERE project_id = :project_id);")
    int setFinishedDateToTodaylForAllTasksInProjectInLastColumn(@Param("project_id") Long project_id);
}