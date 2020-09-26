import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';
import {User} from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly PROJECT_URL = environment.baseUrl + '/api/user';

  constructor(private httpClient: HttpClient) { }

  public getUserByID(id: number): Observable<User> {
    return this.httpClient.get<User>(this.PROJECT_URL + '/' + id.toString());
  }
}
