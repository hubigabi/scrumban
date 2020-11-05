import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {

  form: FormGroup;
  loginInvalid = false;

  constructor(
    private fb: FormBuilder,
    // private authService: AuthService
  ) {
  }

  async ngOnInit() {
    this.form = this.fb.group({
      email: ['', Validators.email],
      password: ['', Validators.required]
    });

    // if (await this.authService.checkAuthenticated()) {
    //   await this.router.navigate([this.returnUrl]);
    // }
  }

  async login() {
    try {
      const email = this.form.get('email').value;
      const password = this.form.get('password').value;
      // await this.authService.login(username, password);
    } catch (err) {
      this.loginInvalid = true;
    }
  }
}
