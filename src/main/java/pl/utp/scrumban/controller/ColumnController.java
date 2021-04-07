package pl.utp.scrumban.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.utp.scrumban.dto.ColumnDto;
import pl.utp.scrumban.model.Column;
import pl.utp.scrumban.service.ColumnService;

import java.util.List;

@RestController
@RequestMapping("/api/column")
@CrossOrigin
public class ColumnController {

    private final ColumnService columnService;

    @Autowired
    public ColumnController(ColumnService columnService) {
        this.columnService = columnService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ColumnDto>> getAllColumns() {
        List<ColumnDto> allColumns = columnService.getAllColumns();

        return new ResponseEntity<>(allColumns, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColumnDto> getColumn(@PathVariable("id") long id) {
        ColumnDto columnDto = columnService.getColumn(id);

        if (columnDto != null) {
            return new ResponseEntity<>(columnDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/allByProject/{id}")
    public ResponseEntity<List<ColumnDto>> findAllByProject_Id(@PathVariable("id") long id) {
        List<ColumnDto> columns = columnService.findAllDtoByProject_Id(id);
        return new ResponseEntity<>(columns, HttpStatus.OK);
    }

}