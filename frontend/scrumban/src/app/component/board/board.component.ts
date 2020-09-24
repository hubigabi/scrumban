import {Component, OnInit} from '@angular/core';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';
import {ProjectService} from '../../service/project.service';
import {Project} from '../../model/project.model';
import {User} from '../../model/user.model';
import {Task} from '../../model/task.model';
import {UserService} from '../../service/user.service';
import {TaskService} from '../../service/task.service';
import {Column, COLUMNS} from '../../model/column.model';
import {Progress, PROGRESS_BACKLOG, PROGRESS_DONE} from '../../model/progress.model';

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

  columns: Column[] = COLUMNS;

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

        this.tasks.forEach(task => {
          if (task.progress === column.progress.name) {
            column.tasks.push(task);
          }
        });

      });
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

      event.container.data.tasks[event.currentIndex].progress = event.container.data.progress.name;
      let task = this.tasks.find(value =>
        value.id === event.container.data.tasks[event.currentIndex].id);
      task.progress = event.container.data.progress.name;

      this.taskService.updateTask(event.container.data.tasks[event.currentIndex])
        .subscribe(value => {
            event.container.data.tasks[event.currentIndex] = value;
            task = value;
          }
        );
    }
  }


  test(task: Task) {
    console.log(task.description);
    this.moveTask(task.id, PROGRESS_DONE);
  }

  moveTask(taskID: number, progress: Progress) {
    let task = this.tasks.find(value => value.id === taskID);

    if (task.progress !== progress.name) {
      const previousContainer = this.columns.find(column => column.progress.name === task.progress).tasks;
      const currentContainer = this.columns.find(column => column.progress === progress).tasks;

      const previousIndex = previousContainer.findIndex(value => value.id === task.id);
      const currentIndex = currentContainer.length;

      transferArrayItem(previousContainer,
        currentContainer,
        previousIndex,
        currentIndex);
      task.progress = progress.name;

      this.taskService.updateTask(task)
        .subscribe(value => {
            currentContainer[currentIndex] = value;
            task = value;
          }
        );
    }
  }
}
