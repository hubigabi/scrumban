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

  private projectStats: ProjectStats[];
  public project: Project;

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
        ticks: {
          beginAtZero: true
        },
        afterDataLimits(scale) {
          scale.max *= 1.05;
        }
      }]
    },
  };

  public chartColors: Array<any> = [
    {
      backgroundColor: 'rgba(102, 205, 170, 0.3)',
      borderColor: 'rgba(102, 205, 170, 1)',
      borderWidth: 2,
    },
    {
      backgroundColor: 'rgba(255, 140, 0, 0.3)',
      borderColor: 'rgba(255, 140, 0, 1)',
      borderWidth: 2,
    },
    {
      backgroundColor: 'rgba(0, 255, 0, 0.3)',
      borderColor: 'rgba(0, 255, 0, 1)',
      borderWidth: 2,
    },
    {
      backgroundColor: 'rgba(0, 0, 255, 0.3)',
      borderColor: 'rgba(0, 0, 255, 1)',
      borderWidth: 2,
    },
    {
      backgroundColor: 'rgba(30, 144, 255, 0.3)',
      borderColor: 'rgba(30, 144, 255, 1)',
      borderWidth: 2,
    },
    {
      backgroundColor: 'rgba(255, 20, 147, 0.3)',
      borderColor: 'rgb(255,20,141)',
      borderWidth: 2,
    }
  ];

  private fractionDigits = 2;

  constructor(public dialogRef: MatDialogRef<ProjectStatsDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: DialogData,
              private projectStatsService: ProjectStatsService) {
  }

  ngOnInit() {
    this.project = this.data.project;

    this.projectStatsService.getProjectStatsByID(this.project.id)
      .subscribe((projectStats: ProjectStats[]) => {
        this.projectStats = projectStats;

        this.chartLabels = projectStats.map(value => value.localDate);

        this.startedTasksDataSet.data = projectStats.map(value => value.startedTasks.toFixed(this.fractionDigits));
        this.activeTasksDataSet.data = projectStats.map(value => value.activeTasks.toFixed(this.fractionDigits));
        this.finishedTasksDataSet.data = projectStats.map(value => value.finishedTasks.toFixed(this.fractionDigits));
        this.throughputDataSet.data = projectStats.map(value => value.throughput.toFixed(this.fractionDigits));
        this.leadTimeDataSet.data = projectStats.map(value => value.leadTime.toFixed(this.fractionDigits));
        this.WIPDataSet.data = projectStats.map(value => value.wip.toFixed(this.fractionDigits));
      });
  }

  closeDialog() {
    this.dialogRef.close();
  }

  exportPNG() {
    const canvas = document.getElementById('canvas-chart') as HTMLCanvasElement;
    const ctx = canvas.getContext('2d');
    ctx.globalCompositeOperation = 'destination-over';
    ctx.fillStyle = 'white';
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    const img = canvas.toDataURL('image/png');

    const link = document.createElement('a');
    link.setAttribute('href', img);
    link.setAttribute('download', this.project.name + '-statistics.png');
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }

  exportCSV() {
    const csvContent = 'data:text/csv;charset=utf-8,'
      + 'Date' + ',' + this.startedTasksDataSet.label + ','
      + this.activeTasksDataSet.label + ',' + this.finishedTasksDataSet.label + ','
      + this.throughputDataSet.label + ',' + this.leadTimeDataSet.label + ','
      + this.WIPDataSet.label + '\n'
      + this.projectStats.map(e => e.localDate + ','
        + e.startedTasks.toFixed(this.fractionDigits) + ','
        + +e.activeTasks.toFixed(this.fractionDigits) + ','
        + e.finishedTasks.toFixed(this.fractionDigits) + ','
        + e.throughput.toFixed(this.fractionDigits) + ','
        + e.leadTime.toFixed(this.fractionDigits) + ','
        + e.wip.toFixed(this.fractionDigits))
        .join('\n');

    const encodedUri = encodeURI(csvContent);

    const link = document.createElement('a');
    link.setAttribute('href', encodedUri);
    link.setAttribute('download', this.project.name + '-statistics.csv');
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }
}

export interface DialogData {
  project: Project;
}
