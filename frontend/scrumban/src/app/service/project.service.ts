import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';
import {Project} from '../model/project.model';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  private PROJECT_URL = environment.baseUrl + '/project';

  constructor(private httpClient: HttpClient) { }

  public getProjectByID(id: number): Observable<Project> {
    return this.httpClient.get<Project>(this.PROJECT_URL + '/' + id.toString());
  }

}
