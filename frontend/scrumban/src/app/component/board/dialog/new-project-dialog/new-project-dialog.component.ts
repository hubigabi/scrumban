import {Component, Inject, OnInit} from '@angular/core';
import {User} from '../../../../model/user.model';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Project} from '../../../../model/project.model';
import {FormControl, Validators} from '@angular/forms';

@Component({
  selector: 'app-new-project-dialog',
  templateUrl: './new-project-dialog.component.html',
  styleUrls: ['./new-project-dialog.component.scss']
})
export class NewProjectDialogComponent implements OnInit {

  project: Project;
  todayDate: Date = new Date();

  constructor(public dialogRef: MatDialogRef<NewProjectDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: DialogData) {
  }

  ngOnInit() {
    this.project = {
      id: 0,
      name: '',
      description: '',
      startedLocalDate: '',
      finishedLocalDate: '',
      leaderUserId: this.data.leaderUser.id,
      users: [this.data.leaderUser],
    };

    this.project.startedLocalDate = this.todayDate.toISOString().split('T')[0];
  }

  changeStartedLocalDate($event) {
    // https://stackoverflow.com/questions/10830357/javascript-toisostring-ignores-timezone-offset
    this.project.startedLocalDate = new Date($event.getTime() - ($event.getTimezoneOffset() * 60000))
      .toISOString().split('T')[0];
  }

  cancel() {
    this.dialogRef.close();
  }

  submit() {
    this.dialogRef.close(this.project);
  }

}

export interface DialogData {
  leaderUser: User;
}
