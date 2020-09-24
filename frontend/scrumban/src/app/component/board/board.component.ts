import {Component, OnInit} from '@angular/core';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';
import {ProjectService} from '../../service/project.service';
import {Project} from '../../model/project.model';
import {User} from '../../model/user.model';
import {Task} from '../../model/task.model';
import {UserService} from '../../service/user.service';
import {TaskService} from '../../service/task.service';
import {Column} from '../../model/column.model';
import {Progress} from '../../model/progress.model';

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.scss']
})
export class BoardComponent implements OnInit {

  constructor(private userService: UserService, private projectService: ProjectService,
              private taskService: TaskService) {
  }

  project: Project;
  tasks: Task[];
  user: User;

  columns: Column[] = [
    new Column(new Progress('BACKLOG', false), []),
    new Column(new Progress('QA', true), []),
    new Column(new Progress('DEVELOPMENT', true), []),
    new Column(new Progress('TEST', true), []),
    new Column(new Progress('DEPLOYMENT', true), []),
    new Column(new Progress('DONE', false), []),
  ];

  ngOnInit() {

    this.projectService.getProjectByID(1).subscribe(p => {
      this.project = p;
    });

    this.userService.getUserByID(1).subscribe(u => {
      this.user = u;
    });

    this.taskService.findAllByProject_Id(1).subscribe(t => {
      this.tasks = t;

      this.columns.forEach(column => {
        console.log(column.progress.name);

        this.tasks.forEach(task => {
          if (task.progress === column.progress.name) {
            column.tasks.push(task);
          }
        });

      });
      console.log(this.columns);
    });
  }

  drop(event: CdkDragDrop<Column>) {

    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data.tasks, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data.tasks,
        event.container.data.tasks,
        event.previousIndex,
        event.currentIndex);

      console.log(event.container.data.tasks[event.currentIndex].progress);
      event.container.data.tasks[event.currentIndex].progress = event.container.data.progress.name;
      console.log(event.container.data.tasks[event.currentIndex].progress);

      console.log('Updated task');
      this.taskService.updateTask(event.container.data.tasks[event.currentIndex])
        .subscribe(task => {
            console.log(task);
            event.container.data.tasks[event.currentIndex] = task;
          }
        );
    }

  }


  test(task: Task) {
    console.log(5 + 5);
    console.log(task.description);
  }
}
