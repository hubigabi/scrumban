import {Component, Input, OnInit} from '@angular/core';
import {Project} from '../../../../../model/project.model';
import {ProjectStatsService} from '../../../../../service/project-stats.service';
import {CumulativeStats} from '../../../../../model/stats/cumulative-stats.model';
import palette from 'google-palette';

@Component({
  selector: 'app-stats-cumulative',
  templateUrl: './stats-cumulative.component.html',
  styleUrls: ['../project-stats-dialog.component.scss']
})
export class StatsCumulativeComponent implements OnInit {

  @Input() project: Project;

  private projectCumulativeStats: CumulativeStats;
  public chartType = 'line';
  public chartLabels: Array<any>;
  public chartDatasets: Array<any> = [];
  public chartColors: Array<any> = [];

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
    }
  };

  constructor(private projectStatsService: ProjectStatsService) {
  }

  ngOnInit() {
    this.projectStatsService.getProjectCumulativeStatsByID(this.project.id)
      .subscribe((projectCumulativeStats: CumulativeStats) => {
        this.projectCumulativeStats = projectCumulativeStats;
        const colors = palette('mpn65', projectCumulativeStats.finishedTasksColumns.length);

        this.chartLabels = projectCumulativeStats.days;
        projectCumulativeStats.finishedTasksColumns
          .sort((a, b) => b.columnOrder - a.columnOrder)
          .forEach((finishedTasksColumn, index) => {
            const chartData = {
              data: finishedTasksColumn.finishedTaskStatus,
              label: finishedTasksColumn.columnName
            };
            this.chartDatasets.push(chartData);

            const chartColor = {
              backgroundColor: this.addAlphaToColor('#' + colors[index], 0.8),
              borderColor: '#' + colors[index],
              borderWidth: 2,
            };
            this.chartColors.push(chartColor);
          });
      });
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
    link.setAttribute('download', this.project.name + '-cumulative-statistics.png');
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }

  exportCSV() {
    const cumulativeStatsData = this.projectCumulativeStats.finishedTasksColumns
      .sort((a, b) => a.columnOrder - b.columnOrder)
      .map(value => value.finishedTaskStatus);

    const csvContent = 'data:text/csv;charset=utf-8,'
      + 'Date,'
      + this.projectCumulativeStats.finishedTasksColumns
        .sort((a, b) => a.columnOrder - b.columnOrder)
        .map(value => value.columnName)
        .join(',') + '\n'
      + this.projectCumulativeStats.days.map((day, index) =>
        day + ',' + cumulativeStatsData.map(value => value[index]).join(',')
      ).join('\n');

    const encodedUri = encodeURI(csvContent);
    const link = document.createElement('a');
    link.setAttribute('href', encodedUri);
    link.setAttribute('download', this.project.name + '-cumulative-statistics.csv');
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }

  addAlphaToColor(color, opacity) {
    const opacityValue = Math.round(Math.min(Math.max(opacity || 1, 0), 1) * 255);
    return color + opacityValue.toString(16).toUpperCase();
  }

}
