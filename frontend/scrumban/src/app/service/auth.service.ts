import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable, Subject} from 'rxjs' ;
import {AuthRequest} from '../model/auth-request.model';
import {CookieService} from 'ngx-cookie-service';
import {User} from '../model/user.model';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly AUTH_URL = environment.baseUrl + '/api/auth';
  private readonly LOG_OUT_URL = environment.baseUrl + '/logout';
  private readonly LOGIN_URL = this.AUTH_URL + '/login';
  private readonly SIGN_UP_URL = this.AUTH_URL + '/signup';
  private readonly EMAIL_USED_URL = this.AUTH_URL + '/isEmailUsed';

  private readonly COOKIE_TOKEN_NAME = 'jwt-token';

  constructor(private httpClient: HttpClient, private cookieService: CookieService,
              private router: Router) {
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

  public isEmailUsed(email: string): Observable<boolean> {
    return this.httpClient.get<boolean>(this.EMAIL_USED_URL + '/' + email);
  }

  public signUp(user: User): Observable<User> {
    return this.httpClient.post<User>(this.SIGN_UP_URL, user);
  }

  public logOut() {
    this.cookieService.delete(this.COOKIE_TOKEN_NAME);
    this.httpClient.post<void>(this.LOG_OUT_URL, null).subscribe();
    this.router.navigate(['/login']);
  }

}
