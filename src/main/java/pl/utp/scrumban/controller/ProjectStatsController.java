package pl.utp.scrumban.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.utp.scrumban.dto.stats.ProjectCumulativeStats;
import pl.utp.scrumban.dto.stats.ProjectChartStats;
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

    @GetMapping("chart/{id}")
    public ResponseEntity<List<ProjectChartStats>> getProjectChartStats(@PathVariable("id") long id) {
        List<ProjectChartStats> projectChartStats = projectStatsService.getProjectChartStats(id);
        return new ResponseEntity<>(projectChartStats, HttpStatus.OK);
    }

    @GetMapping("/cumulative/{id}")
    public ResponseEntity<ProjectCumulativeStats> getProjectCumulativeStats(@PathVariable long id) {
        ProjectCumulativeStats projectCumulativeStats = projectStatsService.getProjectCumulativeStats(id);
        return new ResponseEntity<>(projectCumulativeStats, HttpStatus.OK);
    }

}