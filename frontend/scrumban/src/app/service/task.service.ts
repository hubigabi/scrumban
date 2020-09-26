import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';
import {Task} from '../model/task.model';
import {Project} from '../model/project.model';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private readonly TASK_URL = environment.baseUrl + '/api/task';
  private readonly ALL_TASKS_BY_PROJECT_ID = this.TASK_URL + '/allByProject';

  constructor(private httpClient: HttpClient) { }

  public findAllTasksByProject_Id(id: number): Observable<Task[]> {
    return this.httpClient.get<Task[]>(this.ALL_TASKS_BY_PROJECT_ID + '/' + id.toString());
  }

  public updateTask(task: Task): Observable<Task> {
    return this.httpClient.put<Task>(this.TASK_URL, task);
  }
}
