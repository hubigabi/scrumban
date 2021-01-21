package pl.utp.scrumban.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.utp.scrumban.model.Column;
import pl.utp.scrumban.repositiory.ColumnRepository;
import pl.utp.scrumban.repositiory.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ColumnService {

    private ColumnRepository columnRepository;
    private TaskRepository taskRepository;

    @Autowired
    public ColumnService(ColumnRepository columnRepository, TaskRepository taskRepository) {
        this.columnRepository = columnRepository;
        this.taskRepository = taskRepository;
    }

    public List<Column> getAllColumns() {
        return columnRepository.findAll(Sort.by(Sort.Order.desc("id")));
    }

    public Column getColumn(long id) {
        return columnRepository.findById(id).orElse(null);
    }

    public Column createColumn(Column column) {
        return columnRepository.save(column);
    }

    public Column updateColumn(Column column) {
        return columnRepository.save(column);
    }

    public List<Column> findAllByProject_Id(Long id) {
        return columnRepository.findAllByProject_IdOrderByNumberOrderAsc(id);
    }

    public List<Column> saveAll(Iterable<Column> columns) {
        return columnRepository.saveAll(columns);
    }

    public void deleteById(Long id) {
        columnRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return columnRepository.existsById(id);
    }

    @Transactional
    public Column createColumnModifyingOthers(Column column) {
        column = columnRepository.save(column);
        columnRepository.updateOrdersAfterCreatingColumn(column.getProject().getId(), column.getNumberOrder(), column.getId());
        checkColumnsOrderInProject(column.getProject().getId());
        return column;
    }

    @Transactional
    public Column updateColumnModifyingOthers(Column newColumn) {
        Optional<Column> oldColumn = columnRepository.findById(newColumn.getId());

        if (oldColumn.isPresent()) {
            int oldColumnOrder = oldColumn.get().getNumberOrder();

            newColumn = columnRepository.save(newColumn);
            int newColumnOrder = newColumn.getNumberOrder();
            if (newColumnOrder > oldColumnOrder) {
                columnRepository.updateOrdersAfterUpdatingColumnIfNewOrderIsGreater(newColumn.getProject().getId(),
                        newColumnOrder, oldColumnOrder, newColumn.getId());
                checkColumnsOrderInProject(newColumn.getProject().getId());
            } else if (newColumnOrder < oldColumnOrder) {
                columnRepository.updateOrdersAfterUpdatingColumnIfNewOrderIsLess(newColumn.getProject().getId(),
                        newColumnOrder, oldColumnOrder, newColumn.getId());
                checkColumnsOrderInProject(newColumn.getProject().getId());
            }

            return newColumn;
        } else {
            return createColumnModifyingOthers(newColumn);
        }
    }

    @Transactional
    public Boolean deleteColumnModifyingOthers(Column column) {
        if (columnRepository.existsById(column.getId())) {

            long taskNumberByColumn = taskRepository.countByColumn_Id(column.getId());
            if (taskNumberByColumn == 0) {
                columnRepository.deleteById(column.getId());
                columnRepository.updateOrdersAfterDeletingColumn(column.getProject().getId(), column.getNumberOrder());
                checkColumnsOrderInProject(column.getProject().getId());
                return true;
            }
        }
        return false;
    }

    @Transactional
    public Column saveColumnNoChangeInOrder(Column column) {
        columnRepository.updateColumnNoChangeInOrder(column.getId(), column.getName(),
                column.getDescription(), column.getIsWIP(), column.getNumberWIP());
        return columnRepository.findById(column.getId()).orElse(null);
    }

    public Integer getMaxColumnNumberOrder(Long projectID) {
        return columnRepository.findAllByProject_IdOrderByNumberOrderDesc(projectID)
                .stream()
                .mapToInt(Column::getNumberOrder)
                .findFirst().orElse(0);
    }

    public void checkColumnsOrderInProject(Long projectID) {
        List<Column> columns = columnRepository.findAllByProject_IdOrderByNumberOrderAsc(projectID);
        List<Column> columnsToUpdate = new ArrayList<>();

        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            if (column.getNumberOrder() != i) {
                column.setNumberOrder(i);
                columnsToUpdate.add(column);
            }
        }

        columnsToUpdate.forEach(column ->
                columnRepository.updateColumnOnlyOrder(column.getId(), column.getNumberOrder())
        );
    }

}
