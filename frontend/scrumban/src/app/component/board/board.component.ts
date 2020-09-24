import {Component, OnInit} from '@angular/core';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';
import {Board} from 'src/app/model/board.model';
import {Column} from 'src/app/model/column.model';
import {ProjectService} from '../../service/project.service';
import {Project} from '../../model/project.model';
import {User} from '../../model/user.model';
import {Task} from '../../model/task.model';
import {UserService} from '../../service/user.service';
import {TaskService} from '../../service/task.service';
import {Columm} from '../../model/columm.model';
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

  columns: Columm[] = [
    new Columm(new Progress('BACKLOG', false), []),
    new Columm(new Progress('QA', true), []),
    new Columm(new Progress('DEVELOPMENT', true), []),
    new Columm(new Progress('TEST', true), []),
    new Columm(new Progress('DEPLOYMENT', true), []),
    new Columm(new Progress('DONE', false), []),
  ];

  board: Board = new Board('Web app', [
    new Column('Ideas', [
      'Some random idea',
      'This is another random idea',
      'build an awesome application'
    ]),
    new Column('Research', [
      'Lorem ipsum',
      'foo',
      'This was in the Research column'
    ]),
    new Column('Todo', [
      'Get to work',
      'Pick up groceries',
      'Go home',
      'Fall asleep'
    ]),
    new Column('Done', [
      'Get up',
      'Brush teeth',
      'Take a shower',
      'Check e-mail',
      'Walk dog'
    ])
  ]);

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

  drop(event: CdkDragDrop<Columm>) {

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
