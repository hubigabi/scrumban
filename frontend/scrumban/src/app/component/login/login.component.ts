import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../service/auth.service';
import {AuthRequest} from '../../model/auth-request.model';
import {Router} from '@angular/router';
import {SocialAuthService} from 'angularx-social-login';
import {FacebookLoginProvider, GoogleLoginProvider} from 'angularx-social-login';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {

  form: FormGroup;
  loginInvalid = false;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router,
              private socialAuthService: SocialAuthService, private toastrService: ToastrService) {
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
        if (user.idToken) {
          this.authService.loginWithGoogle(user.idToken).subscribe(value => {
            if (value) {
              this.router.navigate(['/']);
            } else {
              this.displayErrorNotification('Could not login with Google.');
            }
          });
        }
      })
      .catch(reason => {
        this.displayErrorNotification('Could not login with Google.');
      });
  }

  loginWithFB() {
    this.socialAuthService.signIn(FacebookLoginProvider.PROVIDER_ID)
      .then(user => {
        if (user.authToken) {
          this.authService.loginWithFacebook(user.authToken).subscribe(value => {
            if (value) {
              this.router.navigate(['/']);
            } else {
              this.displayErrorNotification('Could not login with Facebook.');
            }
          });
        }
      })
      .catch(reason => this.displayErrorNotification('Could not login with Facebook.'));
  }

  displayErrorNotification(title: string) {
    this.toastrService.error('',
      title,
      {
        timeOut: 3000,
        closeButton: true,
        progressBar: true,
        positionClass: 'toast-bottom-center'
      });
  }

}

