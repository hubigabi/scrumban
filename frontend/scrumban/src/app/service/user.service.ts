import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';
import {User} from '../model/user.model';
import {PasswordChangeRequest} from '../model/password-change-request.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly USER_URL = environment.baseUrl + '/api/user';
  private readonly USER_BY_EMAIL = this.USER_URL + '/email';
  private readonly CHANGE_PASSWORD = this.USER_URL + '/changePassword';

  constructor(private httpClient: HttpClient) {
  }

  public getUserByID(id: number): Observable<User> {
    return this.httpClient.get<User>(this.USER_URL + '/' + id.toString());
  }

  public getUserByEmail(email: string): Observable<User> {
    return this.httpClient.get<User>(this.USER_BY_EMAIL + '/' + email);
  }

  public changePassword(id: number, passwordChangeRequest: PasswordChangeRequest): Observable<boolean> {
    return this.httpClient.put<boolean>(this.CHANGE_PASSWORD + '/' + id.toString(), passwordChangeRequest);
  }

}
