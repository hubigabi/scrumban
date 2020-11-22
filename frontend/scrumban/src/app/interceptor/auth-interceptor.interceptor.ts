import {Injectable} from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {CookieService} from 'ngx-cookie-service';
import {JwtService} from '../service/jwt.service';
import {AuthService} from '../service/auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  private readonly COOKIE_TOKEN_NAME = 'jwt-token';

  constructor(private cookieService: CookieService, private jwtService: JwtService,
              private authService: AuthService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    const token = this.cookieService.get(this.COOKIE_TOKEN_NAME);

    if (token !== '') {
      try {
        const jwtData = this.jwtService.getJwtData(token);

        if (this.jwtService.isTokenExpired(jwtData)) {
          this.authService.logOut();
        } else {
          req = req.clone({
            headers: req.headers.set('Authorization', 'Bearer ' + token),
          });
        }
      } catch (error) {
        this.authService.logOut();
        console.error(error);
      }
    }

    return next.handle(req)
      .pipe(catchError(response => {
          if (response.status === 403) {
            this.authService.logOut();
          }
          return throwError(response);
        })
      );
  }

}
