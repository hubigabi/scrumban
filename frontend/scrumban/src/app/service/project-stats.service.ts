import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CumulativeStats} from '../model/stats/cumulative-stats.model';
import {ChartStats} from '../model/stats/chart-stats.model';

@Injectable({
  providedIn: 'root'
})
export class ProjectStatsService {

  private readonly PROJECT_STATS_URL = environment.baseUrl + '/api/stats/project';

  constructor(private httpClient: HttpClient) {
  }

  public getProjectChartStatsByID(id: number): Observable<ChartStats[]> {
    return this.httpClient.get<ChartStats[]>(this.PROJECT_STATS_URL + '/chart/' + id.toString());
  }

  public getProjectCumulativeStatsByID(id: number): Observable<CumulativeStats> {
    return this.httpClient.get<CumulativeStats>(this.PROJECT_STATS_URL + '/cumulative/' + id.toString());
  }

}
