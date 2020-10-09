package pl.utp.scrumban.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.utp.scrumban.model.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByUsers_Id(Long id);

    List<Task> findAllByProject_Id(Long id);

    List<Task> findAllByProject_IdAndUsers_Id(Long project_ID, Long user_ID);

    void deleteById(Long Id);
}