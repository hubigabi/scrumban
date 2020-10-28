import {Component, Inject, OnInit} from '@angular/core';
import {Project} from '../../../../model/project.model';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';


@Component({
  selector: 'app-project-stats-dialog',
  templateUrl: './project-stats-dialog.component.html',
  styleUrls: ['./project-stats-dialog.component.scss']
})
export class ProjectStatsDialogComponent implements OnInit {

  project: Project;

  constructor(public dialogRef: MatDialogRef<ProjectStatsDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: DialogData) {
  }

  ngOnInit(): void {
    this.project = this.data.project;
  }

}

export interface DialogData {
  project: Project;
}
