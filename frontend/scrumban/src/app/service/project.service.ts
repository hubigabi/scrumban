import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';
import {Project} from '../model/project.model';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  private readonly PROJECT_URL = environment.baseUrl + '/api/project';
  private readonly ALL_PROJECTS_BY_USER_ID = this.PROJECT_URL + '/allByUser';

  constructor(private httpClient: HttpClient) {
  }

  public getProjectByID(id: number): Observable<Project> {
    return this.httpClient.get<Project>(this.PROJECT_URL + '/' + id.toString());
  }

  public getAllProjectsByUser_Id(userID: number): Observable<Project[]> {
    return this.httpClient.get<Project[]>(this.ALL_PROJECTS_BY_USER_ID + '/' + userID.toString());
  }

  public createProject(project: Project): Observable<Project> {
    return this.httpClient.post<Project>(this.PROJECT_URL, project);
  }

  public updateProject(project: Project): Observable<Project> {
    return this.httpClient.put<Project>(this.PROJECT_URL, project);
  }

}
