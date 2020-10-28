import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ProjectStats} from '../model/project-stats.model';

@Injectable({
  providedIn: 'root'
})
export class ProjectStatsService {

  private readonly PROJECT_STATS_URL = environment.baseUrl + '/api/stats/project';

  constructor(private httpClient: HttpClient) {
  }

  public getProjectStatsByID(id: number): Observable<ProjectStats[]> {
    return this.httpClient.get<ProjectStats[]>(this.PROJECT_STATS_URL + '/' + id.toString());
  }
}
