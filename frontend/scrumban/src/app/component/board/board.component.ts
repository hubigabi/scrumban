import {Component, OnInit} from '@angular/core';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';
import {ProjectService} from '../../service/project.service';
import {Project} from '../../model/project.model';
import {User} from '../../model/user.model';
import {Task} from '../../model/task.model';
import {UserService} from '../../service/user.service';
import {TaskService} from '../../service/task.service';
import {Column, COLUMNS} from '../../model/column.model';
import {ALL_PROGRESS, Progress, PROGRESS_DONE} from '../../model/progress.model';
import {environment} from '../../../environments/environment';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {MatDialog} from '@angular/material/dialog';
import {NewTaskDialogComponent} from './dialog/new-task-dialog/new-task-dialog.component';
import {ALL_PRIORITY, Priority} from '../../model/priority.model';
import {UpdateTaskDialogComponent} from './dialog/update-task-dialog/update-task-dialog.component';
import {NewProjectDialogComponent} from './dialog/new-project-dialog/new-project-dialog.component';
import {UpdateProjectDialogComponent} from './dialog/update-project-dialog/update-project-dialog.component';
import {Toast, ToasterConfig, ToasterService} from 'angular2-toaster';

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.scss']
})
export class BoardComponent implements OnInit {

  private readonly SERVER_WEB_SOCKET = environment.baseUrl + '/scrumban';

  private taskStompClient;
  private readonly TASK_URL_SEND = '/app/task/';
  private readonly TASK_URL_SUBSCRIBE = '/updateTask/';

  private projectStompClient;
  private readonly PROJECT_URL_SEND = '/app/project/';
  private readonly PROJECT_URL_SUBSCRIBE = '/updateProject/';

  allUserProjects: Project[];
  project: Project;
  tasks: Task[];
  user: User;

  columns: Column[] = COLUMNS;
  allPriority: Priority[] = ALL_PRIORITY;

  private allProgress: Progress[] = ALL_PROGRESS;
  private allProgressNotActive: Progress[];
  private allProgressNotActiveStrings: string[];

  public config: ToasterConfig = new ToasterConfig(
    {
      positionClass: 'toast-bottom-center',
      animation: 'fade',
      limit: 2
    },
  );

  constructor(private userService: UserService, private projectService: ProjectService,
              private taskService: TaskService, public dialog: MatDialog,
              private toasterService: ToasterService) {
  }

  ngOnInit() {

    this.userService.getUserByID(2).subscribe(u => {
      this.user = u;

      this.projectService.getAllProjectsByUser_Id(this.user.id).subscribe(p => {
        this.allUserProjects = p;
      });

    });
    this.allProgressNotActive = this.allProgress.filter(value => value.inProgress === false);
    this.allProgressNotActiveStrings = this.allProgressNotActive.map(value => value.name);
  }

  drop(event: CdkDragDrop<Column>) {

    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data.tasks, event.previousIndex, event.currentIndex);
    } else {
      if (this.isTaskMoveToNotActive(event.container.data.progress)
        || this.isTaskMoveBetweenActive(event.previousContainer.data.progress, event.container.data.progress)
        || this.checkWIP()) {

        transferArrayItem(event.previousContainer.data.tasks,
          event.container.data.tasks,
          event.previousIndex,
          event.currentIndex);

        event.container.data.tasks[event.currentIndex].progress = event.container.data.progress.name;
        const task = this.tasks.find(value =>
          value.id === event.container.data.tasks[event.currentIndex].id);
        task.progress = event.container.data.progress.name;

        if (event.container.data.progress === PROGRESS_DONE) {
          // Change finishedLocalDate to today date
          const todayLocalDateString = new Date().toISOString().split('T')[0];

          task.finishedLocalDate = todayLocalDateString;
          event.container.data.tasks[event.currentIndex].finishedLocalDate = todayLocalDateString;
        } else if (event.previousContainer.data.progress === PROGRESS_DONE) {
          // Change finishedLocalDate to null
          task.finishedLocalDate = null;
          event.container.data.tasks[event.currentIndex].finishedLocalDate = null;
        }

        this.taskWebSocketSend(event.container.data.tasks[event.currentIndex]);
      } else {
        const toast: Toast = {
          type: 'error',
          title: 'You can\'t move task',
          body: 'Number of tasks in WIP is at maximum',
          showCloseButton: true,
          timeout: 5000
        };

        this.toasterService.pop(toast);
      }

    }
  }

  taskWebSocketConnect() {
    const socket = new SockJS(this.SERVER_WEB_SOCKET);
    this.taskStompClient = Stomp.over(socket);

    this.taskStompClient.connect({}, frame => {
      this.taskStompClient.subscribe(this.TASK_URL_SUBSCRIBE + this.project.id, message => {
        const newTask: Task = JSON.parse(message.body);
        const oldTask: Task = this.tasks.find(value => value.id === newTask.id);

        if (oldTask) {
          // Update task
          const index: number = this.columns.find(column => column.progress.name === oldTask.progress).tasks.indexOf(oldTask);
          if (index !== -1) {

            if (oldTask.progress === newTask.progress) {
              this.columns.find(column => column.progress.name === oldTask.progress).tasks.splice(index, 1, newTask);
            } else {
              this.columns.find(column => column.progress.name === oldTask.progress).tasks.splice(index, 1);
              this.columns.find(column => column.progress.name === newTask.progress).tasks.push(newTask);
            }
          }

          this.tasks[this.tasks.findIndex(value => value.id === oldTask.id)] = newTask;
        } else {
          // Insert new task
          this.columns.find(column => column.progress.name === newTask.progress).tasks.push(newTask);
          this.tasks.push(newTask);
        }

      });
    });
  }

  taskWebSocketSend(task: Task) {
    this.taskStompClient.send(this.TASK_URL_SEND + this.project.id, {}, JSON.stringify(task));
  }

  taskWebSocketDisconnect() {
    if (this.taskStompClient != null) {
      this.taskStompClient.disconnect();
    }
  }

  projectWebSocketConnect(projectID: number) {
    const socket = new SockJS(this.SERVER_WEB_SOCKET);
    this.projectStompClient = Stomp.over(socket);

    this.projectStompClient.connect({}, frame => {
      this.projectStompClient.subscribe(this.PROJECT_URL_SUBSCRIBE + projectID, message => {
        const updatedProject: Project = JSON.parse(message.body);

        const index = this.allUserProjects.map(val => val.id).indexOf(updatedProject.id);
        if (index !== -1) {
          this.allUserProjects[index] = updatedProject;
        }

        this.project = updatedProject;
      });
    });
  }

  projectWebSocketSend(project: Project) {
    if (this.projectStompClient != null) {
      this.projectStompClient.send(this.PROJECT_URL_SEND + project.id, {}, JSON.stringify(project));
    } else {
      console.log('Cant send project');
      console.log('Null: ' + this.projectStompClient);
    }
  }

  projectWebSocketDisconnect() {
    if (this.projectStompClient != null) {
      this.projectStompClient.disconnect();
    }
  }

  changeProject(project: Project) {
    this.taskWebSocketDisconnect();
    this.projectWebSocketDisconnect();

    this.projectService.getProjectByID(project.id).subscribe((p: Project) => {
      const index = this.allUserProjects.map(value1 => value1.id).indexOf(p.id);
      if (index !== -1) {
        this.allUserProjects[index] = p;
      }

      this.project = p;
      this.projectWebSocketConnect(p.id);
    });

    this.taskService.findAllTasksByProject_Id(project.id).subscribe(t => {
      this.tasks = t;

      this.columns.forEach(column => {
        column.tasks = [];

        this.tasks.forEach(value => {
          if (value.progress === column.progress.name) {
            column.tasks.push(value);
          }
        });

      });
      this.taskWebSocketConnect();
    });
  }

  assignUserToTask($event: MouseEvent, task: Task) {
    task.users.push(this.user);

    // https://stackoverflow.com/questions/39196766/angular-2-do-not-refresh-view-after-array-push-in-ngoninit-promise
    task.users = task.users.slice();

    this.taskWebSocketSend(task);
  }

  dismissUserFromTask($event: MouseEvent, task: Task) {
    const index = task.users.findIndex(value => value.id === this.user.id);
    if (index > -1) {
      task.users.splice(index, 1);
      task.users = task.users.slice();

      this.taskWebSocketSend(task);
    }
  }

  openNewTaskDialog(): void {
    const dialogRef = this.dialog.open(NewTaskDialogComponent, {
      autoFocus: true,
      data: {
        currentProject: this.project
      }
    });

    dialogRef.afterClosed().subscribe((task: Task) => {
      if (task) {
        this.taskWebSocketSend(task);
      }
    });
  }

  openUpdateTaskDialog(task: Task) {
    const dialogRef = this.dialog.open(UpdateTaskDialogComponent, {
      autoFocus: true,
      data: {
        task
      }
    });

    dialogRef.afterClosed().subscribe((t: Task) => {
      if (t) {
        this.taskWebSocketSend(t);
      }
    });
  }

  openNewProjectDialog(leaderUser: User) {
    const dialogRef = this.dialog.open(NewProjectDialogComponent, {
      autoFocus: true,
      data: {
        leaderUser
      }
    });

    dialogRef.afterClosed().subscribe((project: Project) => {
      if (project) {
        this.projectService.createProject(project).subscribe(value => {
            this.allUserProjects.push(value);
            this.changeProject(value);
          }
        );
      }
    });
  }

  openUpdateProjectDialog(project: Project) {
    const dialogRef = this.dialog.open(UpdateProjectDialogComponent, {
      autoFocus: true,
      data: {
        project
      }
    });

    dialogRef.afterClosed().subscribe((p: Project) => {
      if (p) {
        this.projectWebSocketSend(p);
      }
    });
  }

  isTaskMoveToNotActive(progress: Progress) {
    return this.allProgressNotActive.indexOf(progress) > -1;
  }

  private isTaskMoveBetweenActive(progress1: Progress, progress2: Progress) {
    return this.allProgressNotActive.indexOf(progress1) < 0
      && this.allProgressNotActive.indexOf(progress2) < 0;
  }

  // Check if WIP of project is bigger than active tasks
  checkWIP() {
    const activeTasksNumber = this.tasks
      .map(value => value.progress)
      .filter(value => this.allProgressNotActiveStrings.indexOf(value) < 0)
      .length;

    return this.project.numberWIP > activeTasksNumber;
  }

}
