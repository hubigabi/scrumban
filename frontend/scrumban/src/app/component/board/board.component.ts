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
import {ToastrService} from 'ngx-toastr';
import * as $ from 'jquery';
import {ConfirmDialogComponent} from './dialog/confirm-dialog/confirm-dialog.component';
import {CommentDialogComponent} from './dialog/comment-dialog/comment-dialog.component';
import {ProjectStatsDialogComponent} from "./dialog/project-stats-dialog/project-stats-dialog.component";

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.scss']
})
export class BoardComponent implements OnInit {

  private readonly SERVER_WEB_SOCKET = environment.baseUrl + '/scrumban';

  private taskStompClient;
  private readonly TASK_URL_SAVE = '/app/saveTask/';
  private readonly TASK_URL_SUBSCRIBE_SAVE = '/task/';
  private readonly TASK_URL_DELETE = '/app/deleteTask/';
  private readonly TASK_URL_SUBSCRIBE_DELETE = '/deletedTask/';

  private projectStompClient;
  private readonly PROJECT_URL_SAVE = '/app/saveProject/';
  private readonly PROJECT_URL_SUBSCRIBE = '/project/';

  allUserProjects: Project[];
  project: Project;
  tasks: Task[];
  user: User;

  columns: Column[] = COLUMNS;
  allPriority: Priority[] = ALL_PRIORITY;

  private allProgress: Progress[] = ALL_PROGRESS;
  private allProgressNotActive: Progress[];
  private allProgressNotActiveStrings: string[];

  constructor(private userService: UserService, private projectService: ProjectService,
              private taskService: TaskService, public dialog: MatDialog,
              private toastrService: ToastrService) {
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

        this.taskWebSocketSave(event.container.data.tasks[event.currentIndex]);
      } else {
        this.toastrService.error('Number of tasks in WIP is at maximum',
          'You can\'t move this task',
          {
            timeOut: 3000,
            closeButton: true,
            progressBar: true,
            positionClass: 'toast-bottom-center'
          });
      }

    }
  }

  taskWebSocketConnect() {
    const socket = new SockJS(this.SERVER_WEB_SOCKET);
    this.taskStompClient = Stomp.over(socket);

    this.taskStompClient.connect({}, frame => {

      this.taskStompClient.subscribe(this.TASK_URL_SUBSCRIBE_SAVE + this.project.id, message => {
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

          this.toastrService.success('',
            'The task: "' + newTask.name + '" has been added',
            {
              timeOut: 3000,
              closeButton: true,
              progressBar: true,
              positionClass: 'toast-bottom-center'
            });
        }
      });

      this.taskStompClient.subscribe(this.TASK_URL_SUBSCRIBE_DELETE + this.project.id, message => {
        const task: Task = JSON.parse(message.body);

        if (task) {
          const tasksInColumn: Task[] = this.columns.find(column => column.progress.name === task.progress).tasks;
          const indexInColumn = tasksInColumn.findIndex(value => value.id === task.id);
          tasksInColumn.splice(indexInColumn, 1);

          const index = this.tasks.findIndex(value => value.id === task.id);
          if (index > -1) {
            this.tasks.splice(index, 1);
          }

          this.toastrService.success('',
            'The task: "' + task.name + '" has been deleted',
            {
              timeOut: 3000,
              closeButton: true,
              progressBar: true,
              positionClass: 'toast-bottom-center'
            });
        }
      });
    });
  }

  taskWebSocketSave(task: Task) {
    this.taskStompClient.send(this.TASK_URL_SAVE + this.project.id, {}, JSON.stringify(task));
  }

  taskWebSocketDelete(task: Task) {
    this.taskStompClient.send(this.TASK_URL_DELETE + this.project.id, {}, JSON.stringify(task));
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

        this.toastrService.success('',
          'The project: "' + updatedProject.name + '" has been edited',
          {
            timeOut: 3000,
            closeButton: true,
            progressBar: true,
            positionClass: 'toast-bottom-center'
          });
      });
    });
  }

  projectWebSocketSave(project: Project) {
    if (this.projectStompClient != null) {
      this.projectStompClient.send(this.PROJECT_URL_SAVE + project.id, {}, JSON.stringify(project));
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

    this.taskWebSocketSave(task);
  }

  dismissUserFromTask($event: MouseEvent, task: Task) {
    const index = task.users.findIndex(value => value.id === this.user.id);
    if (index > -1) {
      task.users.splice(index, 1);
      task.users = task.users.slice();

      this.taskWebSocketSave(task);
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
        this.taskWebSocketSave(task);
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
        this.taskWebSocketSave(t);
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
        this.projectWebSocketSave(p);
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

  deleteTask(task: Task) {

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      autoFocus: true,
      data: {
        title: 'Are you sure you want delete task: \n' + task.name
      }
    });

    dialogRef.afterClosed().subscribe((toDelete: boolean) => {
      if (toDelete) {
        this.taskWebSocketDelete(task);
      }
    });

  }

  openCommentTaskDialog(task: Task, user: User) {
    const dialogRef = this.dialog.open(CommentDialogComponent, {
      autoFocus: true,
      width: '600px',
      minWidth: '50%',
      maxHeight: '95%',
      disableClose: true,
      data: {
        task,
        user
      }
    });

  }

  openProjectStatsDialog(project: Project) {
    const dialogRef = this.dialog.open(ProjectStatsDialogComponent, {
      autoFocus: true,
      disableClose: false,
      maxWidth: '90%',
      maxHeight: '90%',
      data: {
        project
      }
    });
  }

}
