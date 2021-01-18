import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Project} from '../../../../model/project.model';
import {Task} from 'src/app/model/task.model';
import {ALL_PRIORITY, Priority} from '../../../../model/priority.model';
import {Column} from '../../../../model/column.model';

@Component({
  selector: 'app-new-task-dialog',
  templateUrl: './new-task-dialog.component.html',
  styleUrls: ['./new-task-dialog.component.scss']
})

export class NewTaskDialogComponent implements OnInit {

  task: Task;
  allPriority: Priority[];

  todayDate: Date = new Date();

  constructor(public dialogRef: MatDialogRef<NewTaskDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: DialogData) {
  }

  ngOnInit() {
    this.task = {
      id: 0,
      name: '',
      description: '',
      priority: 1,
      startedLocalDate: '',
      finishedLocalDate: '',
      column: this.data.firstColumn,
      project: this.data.currentProject,
      users: []
    };
    this.task.startedLocalDate = this.todayDate.toISOString().split('T')[0];

    this.allPriority = ALL_PRIORITY;
  }

  cancel() {
    this.dialogRef.close();
  }

  submit() {
    this.dialogRef.close(this.task);
  }

}

export interface DialogData {
  currentProject: Project;
  firstColumn: Column;
}
