import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';
import {User} from '../model/user.model';
import {Task} from '../model/task.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly USER_URL = environment.baseUrl + '/api/user';
  private readonly USER_BY_EMAIL = this.USER_URL + '/email';

  constructor(private httpClient: HttpClient) {
  }

  public getUserByID(id: number): Observable<User> {
    return this.httpClient.get<User>(this.USER_URL + '/' + id.toString());
  }

  public getUserByEmail(email: string): Observable<User> {
    return this.httpClient.get<User>(this.USER_BY_EMAIL + '/' + email);
  }

}
