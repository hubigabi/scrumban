import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable, Subject} from 'rxjs' ;
import {AuthRequest} from '../model/auth-request.model';
import {CookieService} from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly AUTH_URL = environment.baseUrl + '/api/auth';
  private readonly LOGIN_URL = this.AUTH_URL + '/login';

  constructor(private httpClient: HttpClient, private cookieService: CookieService) {
  }

  public login(authRequest: AuthRequest): Observable<boolean> {
    this.cookieService.delete('jwt-token');
    const subject = new Subject<boolean>();

    this.httpClient.post<string>(this.LOGIN_URL, authRequest, {responseType: 'text' as 'json'})
      .subscribe(res => {
          this.cookieService.set('jwt-token', res);
          subject.next(true);
        },
        err => {
          subject.next(false);
        }
      );

    return subject.asObservable();
  }
}
