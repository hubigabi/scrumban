package pl.utp.scrumban.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.utp.scrumban.model.Column;
import pl.utp.scrumban.repositiory.ColumnRepository;

import java.util.List;

@Service
public class ColumnService {

    private ColumnRepository columnRepository;

    @Autowired
    public ColumnService(ColumnRepository columnRepository) {
        this.columnRepository = columnRepository;
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
        return columnRepository.findAllByProject_Id(id);
    }

    public List<Column> saveAll(Iterable<Column> columns) {
        return columnRepository.saveAll(columns);
    }

    public void deleteById(Long id) {
        columnRepository.deleteById(id);
    }

    public Integer getMaxColumnNumberOrder(Long projectID) {
        return columnRepository.findAllByProject_Id(projectID)
                .stream()
                .mapToInt(Column::getNumberOrder)
                .max().orElse(0);
    }
}
