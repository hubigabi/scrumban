import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Column} from '../model/column.model';

@Injectable({
  providedIn: 'root'
})
export class ColumnService {

  private readonly COLUMN_URL = environment.baseUrl + '/api/column';
  private readonly ALL_COLUMNS_BY_PROJECT_ID = this.COLUMN_URL + '/allByProject';

  constructor(private httpClient: HttpClient) {
  }

  public findAllColumnsByProject_Id(id: number): Observable<Column[]> {
    return this.httpClient.get<Column[]>(this.ALL_COLUMNS_BY_PROJECT_ID + '/' + id.toString());
  }

}
