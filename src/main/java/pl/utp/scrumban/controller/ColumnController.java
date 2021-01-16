package pl.utp.scrumban.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.utp.scrumban.model.Column;
import pl.utp.scrumban.service.ColumnService;

import java.util.List;

@RestController
@RequestMapping("/api/column")
@CrossOrigin
public class ColumnController {

    private ColumnService columnService;

    @Autowired
    public ColumnController(ColumnService columnService) {
        this.columnService = columnService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Column>> getAllColumns() {
        List<Column> allColumns = columnService.getAllColumns();

        return new ResponseEntity<>(allColumns, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Column> getColumn(@PathVariable("id") long id) {
        Column column = columnService.getColumn(id);

        if (column != null) {
            return new ResponseEntity<>(column, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    public ResponseEntity<Column> createColumn(@RequestBody Column column) {
        column = columnService.createColumn(column);

        if (column != null) {
            return new ResponseEntity<>(column, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    public ResponseEntity<Column> updateColumn(@RequestBody Column column) {
        column = columnService.updateColumn(column);

        if (column != null) {
            return new ResponseEntity<>(column, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/allByProject/{id}")
    public ResponseEntity<List<Column>> findAllByProject_Id(@PathVariable("id") long id) {
        List<Column> columns = columnService.findAllByProject_Id(id);
        return new ResponseEntity<>(columns, HttpStatus.OK);
    }

}