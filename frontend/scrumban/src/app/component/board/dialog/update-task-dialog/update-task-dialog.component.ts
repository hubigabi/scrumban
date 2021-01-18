import {AfterViewInit, Component, Inject, OnInit, ViewChild} from '@angular/core';
import {Task} from '../../../../model/task.model';
import {ALL_PRIORITY, Priority} from '../../../../model/priority.model';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {User} from '../../../../model/user.model';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';

@Component({
  selector: 'app-update-task-dialog',
  templateUrl: './update-task-dialog.component.html',
  styleUrls: ['./update-task-dialog.component.scss']
})
export class UpdateTaskDialogComponent implements OnInit, AfterViewInit {

  @ViewChild(MatSort) sort: MatSort;

  task: Task;
  allPriority: Priority[];
  taskStartedDate: Date;
  taskFinishedDate: Date;

  displayedColumns: string[] = ['index', 'name', 'email'];
  dataSource: MatTableDataSource<User>;

  constructor(public dialogRef: MatDialogRef<UpdateTaskDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: DialogData) {
  }

  ngOnInit() {
    this.task = {
      id: this.data.task.id,
      name: this.data.task.name,
      description: this.data.task.description,
      priority: this.data.task.priority,
      startedLocalDate: this.data.task.startedLocalDate,
      finishedLocalDate: this.data.task.finishedLocalDate,
      column: this.data.task.column,
      project: this.data.task.project,
      users: this.data.task.users
    };

    this.allPriority = ALL_PRIORITY;

    this.taskStartedDate = this.getDateFromString(this.task.startedLocalDate);
    if (this.task.finishedLocalDate) {
      this.taskFinishedDate = this.getDateFromString(this.task.finishedLocalDate);
    }

    this.dataSource = new MatTableDataSource(this.task.users);
  }

  ngAfterViewInit() {
    this.dataSource.sort = this.sort;
  }

  cancel() {
    this.dialogRef.close();
  }

  submit() {
    this.dialogRef.close(this.task);
  }

  // String format: YYYY-MM-DD
  getDateFromString(dateString: string) {
    const from = dateString.split('-').map(value => parseInt(value, 10));
    return new Date(from[0], from[1] - 1, from[2]);
  }

}

export interface DialogData {
  task: Task;
}
