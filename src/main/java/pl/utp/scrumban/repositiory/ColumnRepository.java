package pl.utp.scrumban.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.utp.scrumban.model.Column;

import java.util.List;

@Repository
public interface ColumnRepository extends JpaRepository<Column, Long> {

    List<Column> findAllByProject_Id(Long id);

    void deleteById(Long Id);

}