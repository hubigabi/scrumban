import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../service/auth.service';
import {Router} from '@angular/router';
import {AuthRequest} from '../../model/request/auth-request.model';
import {User} from '../../model/user.model';
import {SignUpRequest} from '../../model/request/sign-up-request.model';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit {

  form: FormGroup;
  passwordInvalid = false;
  emailAlreadyUsed = false;
  signUpError = false;

  constructor(private fb: FormBuilder, private authService: AuthService,
              private router: Router) {
  }

  ngOnInit() {
    this.form = this.fb.group({
      email: ['', Validators.email],
      name: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  signUp() {
    this.passwordInvalid = false;
    this.emailAlreadyUsed = false;
    this.signUpError = false;

    const password = this.form.get('password').value;
    const email = this.form.get('email').value;
    const name = this.form.get('name').value;

    if (this.validatePassword(password)) {
      this.authService.isEmailFree(email)
        .subscribe(res1 => {
            if (res1) {

              const signUpRequest: SignUpRequest = {
                email,
                name,
                password,
              };

              this.authService.signUp(signUpRequest)
                .subscribe(value => {
                    if (value) {
                      this.login(signUpRequest);
                    } else {
                      this.signUpError = true;
                    }
                  },
                  err => {
                    this.signUpError = true;
                  });

            } else {
              this.emailAlreadyUsed = true;
            }
          },
          err => {
            this.emailAlreadyUsed = true;
          }
        );
    } else {
      this.passwordInvalid = true;
    }
  }

  login(signUpRequest: SignUpRequest) {
    const authRequest: AuthRequest = {
      email: signUpRequest.email,
      password: signUpRequest.password
    };

    this.authService.login(authRequest).subscribe(value => {
      if (value) {
        document.location.href = '/';
      }
    });
  }

  navigateToLogin() {
    this.router.navigate(['login']);
  }


  validatePassword(password: string): boolean {
    const regExp = new RegExp('^'
      + '(?=.*\\d)' // should contain at least one digit
      + '(?=.*[a-z])' // should contain at least one lower case
      + '(?=.*[A-Z])' // should contain at least one upper case
      + '.{6,20}' // should contain between 6 and 20 characters
      + '$');

    return regExp.test(password);
  }
}
