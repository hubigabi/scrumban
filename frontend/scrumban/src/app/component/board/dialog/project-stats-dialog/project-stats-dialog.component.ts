import {Component, Inject, OnInit} from '@angular/core';
import {Project} from '../../../../model/project.model';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ProjectStatsService} from '../../../../service/project-stats.service';
import {ProjectStats} from '../../../../model/project-stats.model';


@Component({
  selector: 'app-project-stats-dialog',
  templateUrl: './project-stats-dialog.component.html',
  styleUrls: ['./project-stats-dialog.component.scss']
})
export class ProjectStatsDialogComponent implements OnInit {

  project: Project;

  public chartType = 'line';

  public chartLabels: Array<any>;

  public startedTasksDataSet = {data: [], label: 'Started tasks', hidden: true};
  public activeTasksDataSet = {data: [], label: 'Active tasks'};
  public finishedTasksDataSet = {data: [], label: 'Finished tasks', hidden: true};
  public throughputDataSet = {data: [], label: 'Avg. Throughput'};
  public leadTimeDataSet = {data: [], label: 'Avg. Lead time'};
  public WIPDataSet = {data: [], label: 'Avg. WIP', hidden: true};

  public chartDatasets: Array<any> = [
    this.startedTasksDataSet,
    this.activeTasksDataSet,
    this.finishedTasksDataSet,
    this.throughputDataSet,
    this.leadTimeDataSet,
    this.WIPDataSet
  ];

  public chartOptions: any = {
    responsive: true,
    scales: {
      xAxes: [{
        ticks: {
          callback: (label, index, labels) => {
            // Change YYYY-MM-DD to DD.MM
            return label.toString().split('-').splice(1).reverse().join('.');
          },
        }
      }],
      yAxes: [{
        afterDataLimits(scale) {
          scale.max *= 1.05;
        }
      }]
    },
  };

  private fractionDigits = 2;

  constructor(public dialogRef: MatDialogRef<ProjectStatsDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: DialogData,
              private projectStatsService: ProjectStatsService) {
  }

  ngOnInit() {
    this.project = this.data.project;

    this.projectStatsService.getProjectStatsByID(this.project.id)
      .subscribe((projectStats: ProjectStats[]) => {
        this.chartLabels = projectStats.map(value => value.localDate);

        this.startedTasksDataSet.data = projectStats.map(value => value.startedTasks.toFixed(this.fractionDigits));
        this.activeTasksDataSet.data = projectStats.map(value => value.activeTasks.toFixed(this.fractionDigits));
        this.finishedTasksDataSet.data = projectStats.map(value => value.finishedTasks.toFixed(this.fractionDigits));
        this.throughputDataSet.data = projectStats.map(value => value.throughput.toFixed(this.fractionDigits));
        this.leadTimeDataSet.data = projectStats.map(value => value.leadTime.toFixed(this.fractionDigits));
        this.WIPDataSet.data = projectStats.map(value => value.wip.toFixed(this.fractionDigits));
      });
  }

}

export interface DialogData {
  project: Project;
}
