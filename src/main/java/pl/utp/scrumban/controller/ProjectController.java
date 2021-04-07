package pl.utp.scrumban.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.utp.scrumban.dto.ProjectDto;
import pl.utp.scrumban.model.Project;
import pl.utp.scrumban.service.ProjectService;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        List<ProjectDto> allProjects = projectService.getAllProjectsDto();

        return new ResponseEntity<>(allProjects, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProject(@PathVariable("id") long id) {
        ProjectDto projectDto = projectService.getProjectDto(id);

        if (projectDto != null) {
            return new ResponseEntity<>(projectDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    public ResponseEntity<ProjectDto> createDefaultProject(@RequestBody ProjectDto projectDto) {
        projectDto = projectService.createDefaultProject(projectDto);

        if (projectDto != null) {
            return new ResponseEntity<>(projectDto, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping()
    public ResponseEntity<ProjectDto> updateProject(@RequestBody ProjectDto projectDto) {
        projectDto = projectService.updateProject(projectDto);

        if (projectDto != null) {
            return new ResponseEntity<>(projectDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/allByLeaderUser/{id}")
    public ResponseEntity<List<ProjectDto>> findAllProjectsByLeaderUser_Id(@PathVariable("id") long id) {
        List<ProjectDto> projects = projectService.findAllByLeaderUser_Id(id);

        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/allByUser/{id}")
    public ResponseEntity<List<ProjectDto>> findAllProjectsByUser_Id(@PathVariable("id") long id) {
        List<ProjectDto> projects = projectService.findAllByUser_Id(id);

        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

}