package pl.utp.scrumban.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.utp.scrumban.dto.ProjectStatsDto;
import pl.utp.scrumban.service.ProjectStatsService;

import java.util.List;

@RestController
@RequestMapping("/api/stats/project")
@CrossOrigin
public class ProjectStatsController {

    private final ProjectStatsService projectStatsService;

    @Autowired
    public ProjectStatsController(ProjectStatsService projectStatsService) {
        this.projectStatsService = projectStatsService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ProjectStatsDto>> getProjectStats(@PathVariable("id") long id) {
        List<ProjectStatsDto> projectStatsDtoList = projectStatsService.getProjectStatsDtoList(id);
        return new ResponseEntity<>(projectStatsDtoList, HttpStatus.OK);
    }

}