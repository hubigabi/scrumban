package pl.utp.scrumban.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.utp.scrumban.model.Column;

import java.util.List;

@Repository
public interface ColumnRepository extends JpaRepository<Column, Long> {

    boolean existsById(Long id);

    List<Column> findAllByProject_IdOrderByNumberOrderAsc(Long id);

    List<Column> findAllByProject_IdOrderByNumberOrderDesc(Long id);

    void deleteById(Long Id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE columns c SET c.numberOrder = c.numberOrder + 1 WHERE c.numberOrder >= :order AND c.project.id = :projectId AND c.id <> :columnId")
    int updateOrdersAfterCreatingColumn(@Param("projectId") long projectId, @Param("order") int order, @Param("columnId") long columnId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE columns c SET c.numberOrder = c.numberOrder - 1 WHERE c.numberOrder <= :newColumnOrder AND c.numberOrder > :oldColumnOrder AND c.project.id = :projectId AND c.id <> :columnId")
    int updateOrdersAfterUpdatingColumnIfNewOrderIsGreater(@Param("projectId") long projectId, @Param("newColumnOrder") int newColumnOrder,
                                                           @Param("oldColumnOrder") int oldColumnOrder, @Param("columnId") long columnId);
    @Modifying(clearAutomatically = true)
    @Query("UPDATE columns c SET c.numberOrder = c.numberOrder + 1 WHERE c.numberOrder >= :newColumnOrder AND c.numberOrder < :oldColumnOrder AND c.project.id = :projectId AND c.id <> :columnId")
    int updateOrdersAfterUpdatingColumnIfNewOrderIsLess(@Param("projectId") long projectId, @Param("newColumnOrder") int newColumnOrder,
                                                           @Param("oldColumnOrder") int oldColumnOrder, @Param("columnId") long columnId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE columns c SET c.numberOrder = c.numberOrder - 1 WHERE c.numberOrder > :order AND c.project.id = :projectId")
    int updateOrdersAfterDeletingColumn(@Param("projectId") long projectId, @Param("order") int order);

}