import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../service/auth.service';
import {AuthRequest} from '../../model/auth-request.model';
import {Router} from '@angular/router';
import {SocialAuthService} from 'angularx-social-login';
import {FacebookLoginProvider, GoogleLoginProvider} from 'angularx-social-login';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {

  form: FormGroup;
  loginInvalid = false;

  constructor(private fb: FormBuilder, private authService: AuthService,
              private router: Router, private socialAuthService: SocialAuthService) {
  }

  ngOnInit() {
    this.form = this.fb.group({
      email: ['', Validators.email],
      password: ['', Validators.required]
    });
  }

  login() {
    const email = this.form.get('email').value;
    const password = this.form.get('password').value;
    const authRequest: AuthRequest = {
      email,
      password
    };

    this.authService.login(authRequest).subscribe(value => {
      if (value) {
        this.router.navigate(['/']);
      } else {
        this.loginInvalid = true;
      }
    });

  }

  signUp() {
    this.router.navigate(['signup']);
  }

  loginWithGoogle() {
    this.socialAuthService.signIn(GoogleLoginProvider.PROVIDER_ID)
      .then(user => {
        console.log(user.idToken);
        if (user.idToken) {
          this.authService.loginWithGoogle(user.idToken).subscribe(value => {
            if (value) {
              this.router.navigate(['/']);
            } else {
              console.log('Could not login with Google.');
            }
          });
        }
      })
      .catch(reason => console.log(reason));
  }

  loginWithFB() {
    this.socialAuthService.signIn(FacebookLoginProvider.PROVIDER_ID);
  }

}

