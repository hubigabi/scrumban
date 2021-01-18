import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {User} from '../../../../model/user.model';
import {Task} from '../../../../model/task.model';
import {TaskService} from '../../../../service/task.service';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import {ALL_PRIORITY, Priority} from '../../../../model/priority.model';

@Component({
  selector: 'app-user-task-dialog',
  templateUrl: './user-task-dialog.component.html',
  styleUrls: ['./user-task-dialog.component.scss'],
})
export class UserTaskDialogComponent implements OnInit {

  @ViewChild(MatSort) sort: MatSort;

  user: User;
  tasks: Task[];
  taskRows: TaskRow[];
  allPriority: Priority[] = ALL_PRIORITY;

  displayedColumns: string[] = ['index', 'name', 'priority', 'startedLocalDate',
    'finishedLocalDate', 'users', 'columnName', 'project'];
  dataSource: MatTableDataSource<TaskRow>;

  constructor(public dialogRef: MatDialogRef<UserTaskDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: DialogData,
              private taskService: TaskService) {
  }

  ngOnInit() {
    this.user = this.data.user;

    this.taskService.findAllTasksByUser_Id(this.user.id)
      .subscribe(tasks => {
        this.tasks = tasks;

        this.taskRows = tasks.map(task => {
          const taskRow = {} as TaskRow;

          taskRow.name = task.name || '-';
          taskRow.description = task.description || '-';
          taskRow.priority = this.getPriorityString(task.priority) || '-';
          taskRow.columnName = task.column.name || '-';
          taskRow.startedLocalDate = task.startedLocalDate || '-';
          taskRow.finishedLocalDate = task.finishedLocalDate || '-';
          taskRow.project = task.project.name || '-';
          taskRow.users = this.getUsersString(task.users) || '-';

          return taskRow;
        });

        this.dataSource = new MatTableDataSource(this.taskRows);
        this.dataSource.sort = this.sort;
      });
  }

  getPriorityString(n: number): string {
    return this.allPriority.find(value => value.value === n).name;
  }

  getUsersString(users: User[]): string {
    return users.filter(value => value.id !== this.user.id)
      .map(value => value.name)
      .join(', ');
  }

}

export interface DialogData {
  user: User;
}

export interface TaskRow {
  name: string;
  description: string;
  priority: string;
  startedLocalDate: string;
  finishedLocalDate: string;
  columnName: string;
  project: string;
  users: string;
}
