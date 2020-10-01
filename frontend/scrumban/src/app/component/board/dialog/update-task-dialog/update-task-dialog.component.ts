import {Component, Inject, OnInit} from '@angular/core';
import {Task} from '../../../../model/task.model';
import {ALL_PROGRESS, Progress} from '../../../../model/progress.model';
import {ALL_PRIORITY, Priority} from '../../../../model/priority.model';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {MatSelectChange} from '@angular/material/select';
import {Project} from '../../../../model/project.model';

@Component({
  selector: 'app-update-task-dialog',
  templateUrl: './update-task-dialog.component.html',
  styleUrls: ['./update-task-dialog.component.scss']
})
export class UpdateTaskDialogComponent implements OnInit {

  task: Task;
  allProgress: Progress[];
  allPriority: Priority[];
  taskStartedDate: Date;

  constructor(public dialogRef: MatDialogRef<UpdateTaskDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: DialogData) {
  }

  ngOnInit(): void {
    this.task = {
      id: this.data.task.id,
      name: this.data.task.name,
      description: this.data.task.description,
      priority: this.data.task.priority,
      progress: this.data.task.progress,
      startedLocalDate: this.data.task.startedLocalDate,
      finishedLocalDate: this.data.task.finishedLocalDate,
      project: this.data.task.project,
      users: this.data.task.users
    };

    this.allProgress = ALL_PROGRESS;
    this.allPriority = ALL_PRIORITY;

    this.taskStartedDate = this.getDateFromString(this.task.startedLocalDate);
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

