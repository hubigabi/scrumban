import {Component, OnInit} from '@angular/core';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';
import {ProjectService} from '../../service/project.service';
import {Project} from '../../model/project.model';
import {User} from '../../model/user.model';
import {Task} from '../../model/task.model';
import {UserService} from '../../service/user.service';
import {TaskService} from '../../service/task.service';
import {Column, COLUMNS} from '../../model/column.model';
import {Progress} from '../../model/progress.model';
import {environment} from '../../../environments/environment';

import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.scss']
})
export class BoardComponent implements OnInit {

  private SERVER_WEB_SOCKET = environment.baseUrl + '/task';
  private taskStompClient;

  private project: Project;
  private tasks: Task[];
  private user: User;

  columns: Column[] = COLUMNS;

  constructor(private userService: UserService, private projectService: ProjectService,
              private taskService: TaskService) {
  }

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
      this.taskWebSocketConnect();
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
      const task = this.tasks.find(value =>
        value.id === event.container.data.tasks[event.currentIndex].id);
      task.progress = event.container.data.progress.name;

      // this.taskService.updateTask(event.container.data.tasks[event.currentIndex])
      //   .subscribe(value => {
      //       event.container.data.tasks[event.currentIndex] = value;
      //       task = value;
      //     }
      //   );

      this.taskWebSocketSend(event.container.data.tasks[event.currentIndex]);
    }
  }


  test(task: Task) {
    console.log(task.description);
    // this.moveTask(task.id, PROGRESS_DONE);
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

  taskWebSocketConnect() {
    const socket = new SockJS(this.SERVER_WEB_SOCKET);
    this.taskStompClient = Stomp.over(socket);

    this.taskStompClient.connect({}, frame => {
      this.taskStompClient.subscribe('/updatedTask/' + this.project.id, message => {
        const updatedTask: Task = JSON.parse(message.body);
        const oldTask: Task = this.tasks.find(value => value.id === updatedTask.id);

        // Changing task to updated version
        const indexToDelete: number = this.columns.find(column => column.progress.name === oldTask.progress).tasks.indexOf(oldTask);
        if (indexToDelete !== -1) {
          this.columns.find(column => column.progress.name === oldTask.progress).tasks.splice(indexToDelete, 1);
        }
        this.columns.find(column => column.progress.name === updatedTask.progress).tasks.push(updatedTask);

        this.tasks[this.tasks.findIndex(value => value.id === oldTask.id)] = updatedTask;
      });
    });

  }

  taskWebSocketSend(task: Task) {
    this.taskStompClient.send('/app/task/' + this.project.id, {}, JSON.stringify(task));
  }

  taskWebSocketDisconnect() {
    if (this.taskStompClient != null) {
      this.taskStompClient.disconnect();
    }
  }

}
