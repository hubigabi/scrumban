import {AfterViewInit, Component, Inject, OnInit, ViewChild} from '@angular/core';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {User} from '../../../../model/user.model';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material/dialog';
import {Project} from '../../../../model/project.model';
import {FormControl, Validators} from '@angular/forms';
import {UserService} from '../../../../service/user.service';
import {ConfirmDialogComponent} from '../confirm-dialog/confirm-dialog.component';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {environment} from '../../../../../environments/environment';

@Component({
  selector: 'app-update-project-dialog',
  templateUrl: './update-project-dialog.component.html',
  styleUrls: ['./update-project-dialog.component.scss']
})
export class UpdateProjectDialogComponent implements OnInit, AfterViewInit {

  private readonly SERVER_WEB_SOCKET = environment.baseUrl + '/scrumban';
  private readonly PROJECT_URL_DELETE = '/app/deleteProject/';

  @ViewChild(MatSort) sort: MatSort;

  project: Project;
  projectStartedDate: Date;
  startedProjectDate: Date;

  displayedColumns: string[] = ['index', 'name', 'email'];
  dataSource: MatTableDataSource<User>;

  emailFormControl: FormControl;
  hintForEmailFieldBoolean = false;
  hintForEmailFieldValue = '';

  addedUsersToProject: User[] = [];

  constructor(public dialogRef: MatDialogRef<UpdateProjectDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: DialogData, private userService: UserService,
              public dialog: MatDialog) {
  }

  ngOnInit() {
    this.project = {
      id: this.data.project.id,
      name: this.data.project.name,
      description: this.data.project.description,
      startedLocalDate: this.data.project.startedLocalDate,
      finishedLocalDate: this.data.project.finishedLocalDate,
      leaderUserId: this.data.project.leaderUserId,
      users: this.data.project.users
    };

    this.emailFormControl = new FormControl('', [
      Validators.required,
      Validators.email,
    ]);
    this.emailFormControl.valueChanges.subscribe(value => {
      this.hintForEmailFieldBoolean = false;
    });

    this.projectStartedDate = this.getDateFromString(this.project.startedLocalDate);
    this.dataSource = new MatTableDataSource(this.project.users);
  }

  ngAfterViewInit() {
    this.dataSource.sort = this.sort;
  }

  changeStartedLocalDate($event) {
    // https://stackoverflow.com/questions/10830357/javascript-toisostring-ignores-timezone-offset
    this.project.startedLocalDate = new Date($event.getTime() - ($event.getTimezoneOffset() * 60000))
      .toISOString().split('T')[0];
    console.log(this.project.startedLocalDate);
  }

  // String format: YYYY-MM-DD
  getDateFromString(dateString: string) {
    const from = dateString.split('-').map(value => parseInt(value, 10));
    return new Date(from[0], from[1] - 1, from[2]);
  }

  addUserToProject(userEmail: string) {

    this.userService.getUserByEmail(userEmail).subscribe((user: User) => {

      if (user) {
        if (this.project.users.map(value => value.id).indexOf(user.id) > -1) {
          this.hintForEmailFieldBoolean = true;
          this.hintForEmailFieldValue = 'The user with this email already is in the project';
        } else if (this.addedUsersToProject.map(value => value.id).indexOf(user.id) > -1) {
          this.hintForEmailFieldBoolean = true;
          this.hintForEmailFieldValue = 'The user with this email was added by you earlier';
        } else {
          this.addedUsersToProject.push(user);
          this.emailFormControl.setValue('');
        }
      } else {
        this.hintForEmailFieldBoolean = true;
        this.hintForEmailFieldValue = 'The user with this email does not exist';
      }
    }, error => {
      this.hintForEmailFieldBoolean = true;
      this.hintForEmailFieldValue = 'The user with this email does not exist';
    });
  }

  deleteUserFromProject(user: User) {
    const index: number = this.addedUsersToProject.map(value => value.id).indexOf(user.id);
    if (index !== -1) {
      this.addedUsersToProject.splice(index, 1);
    }
    this.hintForEmailFieldBoolean = false;
    this.hintForEmailFieldValue = '';
  }

  cancel() {
    this.dialogRef.close();
  }

  submit() {
    this.project.users = this.project.users.concat(this.addedUsersToProject);
    this.dialogRef.close(this.project);
  }

  deleteProject() {

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      autoFocus: true,
      data: {
        title: 'Are you sure you want delete project: \n' + this.project.name
      }
    });

    dialogRef.afterClosed().subscribe((toDelete: boolean) => {
      if (toDelete) {
        const socket = new SockJS(this.SERVER_WEB_SOCKET);
        const projectStompClient = Stomp.over(socket);
        projectStompClient.debug = () => {
        };

        projectStompClient.connect({}, frame => {
          projectStompClient.send(this.PROJECT_URL_DELETE + this.project.id, {}, null);
          projectStompClient.disconnect();
          this.dialogRef.close();
        });
      }

    });

  }
}

export interface DialogData {
  project: Project;
}
