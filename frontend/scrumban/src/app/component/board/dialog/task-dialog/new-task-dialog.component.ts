import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Project} from '../../../../model/project.model';
import {Task} from 'src/app/model/task.model';
import {ALL_PROGRESS, Progress} from '../../../../model/progress.model';
import {MatSelectChange} from '@angular/material/select';
import {ALL_PRIORITY, Priority} from '../../../../model/priority.model';

@Component({
  selector: 'app-new-task-dialog',
  templateUrl: './new-task-dialog.component.html',
  styleUrls: ['./new-task-dialog.component.scss']
})

export class NewTaskDialogComponent implements OnInit {

  task: Task;
  allProgress: Progress[];
  allPriority: Priority[];

  constructor(public dialogRef: MatDialogRef<NewTaskDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: DialogData) {
  }

  ngOnInit(): void {
    this.task = {
      id: 0,
      name: '',
      description: '',
      priority: 0,
      progress: 'BACKLOG',
      project: this.data.currentProject,
      users: []
    };

    this.allProgress = ALL_PROGRESS;
    this.allPriority = ALL_PRIORITY;
  }

  changeProgress($event: MatSelectChange) {
    this.task.progress = $event.value.name;
  }

  changePriority($event: MatSelectChange) {
    this.task.priority = $event.value.value;
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
}
