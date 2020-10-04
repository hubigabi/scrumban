import {AfterViewInit, Component, Inject, OnInit, ViewChild} from '@angular/core';
import {MatSort} from '@angular/material/sort';
import {Task} from '../../../../model/task.model';
import {ALL_PROGRESS, Progress} from '../../../../model/progress.model';
import {ALL_PRIORITY, Priority} from '../../../../model/priority.model';
import {MatTableDataSource} from '@angular/material/table';
import {User} from '../../../../model/user.model';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Project} from '../../../../model/project.model';
import {FormControl, Validators} from '@angular/forms';

@Component({
  selector: 'app-update-project-dialog',
  templateUrl: './update-project-dialog.component.html',
  styleUrls: ['./update-project-dialog.component.scss']
})
export class UpdateProjectDialogComponent implements OnInit, AfterViewInit {

  @ViewChild(MatSort) sort: MatSort;

  project: Project;
  projectStartedDate: Date;

  readonly minNumberWIP = 3;
  readonly maxNumberWIP = 15;
  numberWIPFormControl: FormControl;

  startedProjectDate: Date;

  displayedColumns: string[] = ['index', 'name', 'email'];
  dataSource: MatTableDataSource<User>;

  constructor(public dialogRef: MatDialogRef<UpdateProjectDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: DialogData) {
  }

  ngOnInit() {
    this.project = {
      id: this.data.project.id,
      name: this.data.project.name,
      description: this.data.project.description,
      numberWIP: this.data.project.numberWIP,
      startedLocalDate: this.data.project.startedLocalDate,
      finishedLocalDate: this.data.project.finishedLocalDate,
      leaderUser: this.data.project.leaderUser,
      users: this.data.project.users
    };

    this.numberWIPFormControl = new FormControl(this.project.numberWIP,
      [Validators.min(this.minNumberWIP), Validators.max(this.maxNumberWIP)]);
    this.numberWIPFormControl.valueChanges.subscribe(value => {
      this.project.numberWIP = value;
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

  cancel() {
    this.dialogRef.close();
  }

  submit() {
    this.dialogRef.close(this.project);
  }

}

export interface DialogData {
  project: Project;
}
