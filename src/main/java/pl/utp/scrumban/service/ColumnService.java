package pl.utp.scrumban.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.utp.scrumban.model.Column;
import pl.utp.scrumban.repositiory.ColumnRepository;
import pl.utp.scrumban.repositiory.TaskRepository;

import java.util.List;

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

    public Boolean deleteById(Long id) {
        long taskNumberByColumn = taskRepository.countByColumn_Id(id);
        if (taskNumberByColumn == 0) {
            columnRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Integer getMaxColumnNumberOrder(Long projectID) {
        return columnRepository.findAllByProject_IdOrderByNumberOrderDesc(projectID)
                .stream()
                .mapToInt(Column::getNumberOrder)
                .findFirst().orElse(0);
    }
}
