import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CookieService} from 'ngx-cookie-service';
import {JwtService} from '../service/jwt.service';
import {Router} from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  private readonly COOKIE_TOKEN_NAME = 'jwt-token';

  constructor(private cookieService: CookieService, private jwtService: JwtService,
              private router: Router) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    const token = this.cookieService.get(this.COOKIE_TOKEN_NAME);

    if (token !== '') {
      try {
        const jwtData = this.jwtService.getJwtData(token);

        if (this.jwtService.isTokenExpired(jwtData)) {
          this.cookieService.delete(this.COOKIE_TOKEN_NAME);
          this.router.navigate(['/login']);
        } else {
          req = req.clone({
            headers: req.headers.set('Authorization', `Bearer ` + token),
          });
        }
      } catch (error) {
        this.cookieService.delete(this.COOKIE_TOKEN_NAME);
        this.router.navigate(['/login']);
        console.error(error);
      }
    }

    return next.handle(req);
  }

}
