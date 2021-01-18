package pl.utp.scrumban.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.utp.scrumban.model.Project;
import pl.utp.scrumban.service.ProjectService;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    private ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> allProjects = projectService.getAllProjects();

        return new ResponseEntity<>(allProjects, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable("id") long id) {
        Project project = projectService.getProject(id);

        if (project != null) {
            return new ResponseEntity<>(project, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    public ResponseEntity<Project> createDefaultProject(@RequestBody Project project) {
        project = projectService.createDefaultProject(project);

        if (project != null) {
            return new ResponseEntity<>(project, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping()
    public ResponseEntity<Project> updateProject(@RequestBody Project project) {
        project = projectService.updateProject(project);

        if (project != null) {
            return new ResponseEntity<>(project, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/allByLeaderUser/{id}")
    public ResponseEntity<List<Project>> findAllProjectsByLeaderUser_Id(@PathVariable("id") long id) {
        List<Project> projects = projectService.findAllByLeaderUser_Id(id);

        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/allByUser/{id}")
    public ResponseEntity<List<Project>> findAllProjectsByUser_Id(@PathVariable("id") long id) {
        List<Project> projects = projectService.findAllByUser_Id(id);

        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

}