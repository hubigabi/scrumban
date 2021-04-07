package pl.utp.scrumban.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.utp.scrumban.dto.ColumnDto;
import pl.utp.scrumban.exception.NotExistsException;
import pl.utp.scrumban.mapper.ColumnMapper;
import pl.utp.scrumban.model.Column;
import pl.utp.scrumban.model.Project;
import pl.utp.scrumban.repositiory.ColumnRepository;
import pl.utp.scrumban.repositiory.ProjectRepository;
import pl.utp.scrumban.repositiory.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ColumnService {

    private final ColumnRepository columnRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final ColumnMapper columnMapper;

    @Autowired
    public ColumnService(ColumnRepository columnRepository, TaskRepository taskRepository,
                         ProjectRepository projectRepository, ColumnMapper columnMapper) {
        this.columnRepository = columnRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.columnMapper = columnMapper;
    }

    public List<ColumnDto> getAllColumns() {
        return columnRepository.findAll(Sort.by(Sort.Order.desc("id")))
                .stream()
                .map(columnMapper::mapToColumnDto)
                .collect(Collectors.toList());
    }

    public ColumnDto getColumn(long id) {
        Column column = columnRepository.findById(id).orElse(null);
        return columnMapper.mapToColumnDto(column);
    }

    public List<Column> findAllByProject_Id(Long id) {
        return columnRepository.findAllByProject_IdOrderByNumberOrderAsc(id);
    }

    public List<ColumnDto> findAllDtoByProject_Id(Long id) {
        return columnRepository.findAllByProject_IdOrderByNumberOrderAsc(id)
                .stream()
                .map(columnMapper::mapToColumnDto)
                .collect(Collectors.toList());
    }

    public List<Column> saveAll(Iterable<Column> columns) {
        return columnRepository.saveAll(columns);
    }

    public void deleteById(Long id) {
        columnRepository.deleteById(id);
    }

    @Transactional
    public Column createColumnModifyingOthers(Column column) {
        column = columnRepository.save(column);
        columnRepository.updateOrdersAfterCreatingColumn(column.getProject().getId(), column.getNumberOrder(), column.getId());
        checkColumnsOrderInProject(column.getProject().getId());
        taskRepository.setFinishedDateToNullForAllTasksInProjectInNotLastColumn(column.getProject().getId());
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
                taskRepository.setFinishedDateToNullForAllTasksInProjectInNotLastColumn(newColumn.getProject().getId());
                taskRepository.setFinishedDateToTodaylForAllTasksInProjectInLastColumn(newColumn.getProject().getId());
            } else if (newColumnOrder < oldColumnOrder) {
                columnRepository.updateOrdersAfterUpdatingColumnIfNewOrderIsLess(newColumn.getProject().getId(),
                        newColumnOrder, oldColumnOrder, newColumn.getId());
                checkColumnsOrderInProject(newColumn.getProject().getId());
                taskRepository.setFinishedDateToNullForAllTasksInProjectInNotLastColumn(newColumn.getProject().getId());
                taskRepository.setFinishedDateToTodaylForAllTasksInProjectInLastColumn(newColumn.getProject().getId());
            }

            return newColumn;
        } else {
            return createColumnModifyingOthers(newColumn);
        }
    }

    @Transactional
    public ColumnDto saveColumn(ColumnDto columnDto, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotExistsException("Project does not exist"));
        Column column = columnMapper.mapToColumn(columnDto, project);

        if (columnRepository.existsById(column.getId())) {
            return columnMapper.mapToColumnDto(updateColumnModifyingOthers(column));
        } else {
            return columnMapper.mapToColumnDto(createColumnModifyingOthers(column));
        }
    }

    @Transactional
    public ColumnDto saveColumnNoChangeInOrder(ColumnDto columnDto, Long projectId) {
        if (columnRepository.existsById(columnDto.getId())) {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new NotExistsException("Project does not exist"));
            Column column = columnMapper.mapToColumn(columnDto, project);

            columnRepository.updateColumnNoChangeInOrder(column.getId(), column.getName(),
                    column.getDescription(), column.getIsWIP(), column.getNumberWIP());
            column = columnRepository.findById(column.getId())
                    .orElseThrow(() -> new NotExistsException("Column does not exist"));
            return columnMapper.mapToColumnDto((column));
        } else {
            throw new NotExistsException("Column does not exist");
        }
    }

    @Transactional
    public Boolean deleteColumnModifyingOthers(ColumnDto columnDto) {
        Column column = columnRepository.findById(columnDto.getId())
                .orElseThrow(() -> new NotExistsException("Column does not exist"));

        long taskNumberByColumn = taskRepository.countByColumn_Id(column.getId());
        if (taskNumberByColumn == 0) {
            columnRepository.deleteById(column.getId());
            columnRepository.updateOrdersAfterDeletingColumn(column.getProject().getId(), column.getNumberOrder());
            checkColumnsOrderInProject(column.getProject().getId());
            taskRepository.setFinishedDateToTodaylForAllTasksInProjectInLastColumn(column.getProject().getId());
            return true;
        } else {
            throw new RuntimeException("Column contains tasks! Move them to another column.");
        }

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
