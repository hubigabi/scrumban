import {Injectable} from '@angular/core';
import {JwtData} from '../model/jwt-data.model';

@Injectable({
  providedIn: 'root'
})
export class JwtService {

  constructor() {
  }

  public getJwtData(token: string): JwtData {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64)
      .split('').map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)).join(''));
    return JSON.parse(jsonPayload);
  }

  public isTokenExpired(jwtData: JwtData): boolean {
    return new Date() > new Date(jwtData.exp * 1000);
  }
}
