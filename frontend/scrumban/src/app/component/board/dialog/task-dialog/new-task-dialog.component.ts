import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Project} from '../../../../model/project.model';
import {Task} from 'src/app/model/task.model';

@Component({
  selector: 'app-new-task-dialog',
  templateUrl: './new-task-dialog.component.html',
  styleUrls: ['./new-task-dialog.component.scss']
})

export class NewTaskDialogComponent implements OnInit {

  task: Task;

  constructor(public dialogRef: MatDialogRef<NewTaskDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: DialogData) {
  }

  ngOnInit(): void {
    this.task = {
      id: 0,
      name: '',
      description: '',
      priority: 0,
      progress: '',
      project: this.data.currentProject,
      users: []
    };

  }


  cancel() {
    this.dialogRef.close();
  }

  submit() {
    console.log(this.task.name);
    console.log(this.task.project.name);
    this.dialogRef.close();
  }
}

export interface DialogData {
  currentProject: Project;
}
