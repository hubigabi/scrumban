import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../service/auth.service';
import {AuthRequest} from '../../model/auth-request.model';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {

  form: FormGroup;
  loginInvalid = false;

  constructor(private fb: FormBuilder, private authService: AuthService,
              private router: Router) {
  }

  async ngOnInit() {
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
}
