package pl.utp.scrumban.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.utp.scrumban.model.ProjectStats;
import pl.utp.scrumban.service.ProjectStatsService;

import java.util.List;

@RestController
@RequestMapping("/api/stats/project")
@CrossOrigin
public class ProjectStatsController {

    private ProjectStatsService projectStatsService;

    @Autowired
    public ProjectStatsController(ProjectStatsService projectStatsService) {
        this.projectStatsService = projectStatsService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ProjectStats>> getProjectStats(@PathVariable("id") long id) {
        List<ProjectStats> projectStats = projectStatsService.getProjectStats(id);

        return new ResponseEntity<>(projectStats, HttpStatus.OK);
    }

}