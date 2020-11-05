import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    console.log('---------------------------------------');

    const token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huU21pdGhAZ21haWwuY29tIiwiZXhwIjoxNjA0NTc3MzE1LCJpYXQiOjE2MDQ1NDEzMTV9.mH6Z969dyPzDo9xLXS6dLf8_PKqaN8HtNjSgcPkuHEM';

    const req1 = req.clone({
      headers: req.headers.set('Authorization', `Bearer ` + token),
    });

    return next.handle(req1);
  }

}
