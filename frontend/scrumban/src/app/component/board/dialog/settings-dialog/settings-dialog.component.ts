import {Component, Inject, OnInit} from '@angular/core';
import {User} from '../../../../model/user.model';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {UserService} from '../../../../service/user.service';
import {ToastrService} from 'ngx-toastr';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {PasswordChangeRequest} from '../../../../model/password-change-request.model';
import {MatTabChangeEvent} from '@angular/material/tabs';

@Component({
  selector: 'app-settings-dialog',
  templateUrl: './settings-dialog.component.html',
  styleUrls: ['./settings-dialog.component.scss']
})
export class SettingsDialogComponent implements OnInit {

  user: User;

  formPassword: FormGroup;
  newPasswordsNotValid = false;
  newPasswordsNotIdentical = false;
  changePasswordInvalidPassword = false;

  constructor(public dialogRef: MatDialogRef<SettingsDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: DialogData,
              private userService: UserService, private fb: FormBuilder,
              private toastrService: ToastrService) {
  }

  ngOnInit() {
    this.user = this.data.user;

    this.formPassword = this.fb.group({
      currentPassword: ['', Validators.required],
      newPassword1: ['', Validators.required],
      newPassword2: ['', Validators.required]
    });
  }

  changePassword() {
    this.newPasswordsNotValid = false;
    this.newPasswordsNotIdentical = false;
    this.changePasswordInvalidPassword = false;

    const currentPassword = this.formPassword.get('currentPassword').value;
    const newPassword1 = this.formPassword.get('newPassword1').value;
    const newPassword2 = this.formPassword.get('newPassword2').value;

    if (this.validatePassword(newPassword1)) {
      if (newPassword1 === newPassword2) {

        const passwordChangeRequest: PasswordChangeRequest = {
          oldPassword: currentPassword,
          newPassword: newPassword1
        };

        this.userService.changePassword(this.user.id, passwordChangeRequest).subscribe(res => {
            if (res) {

              this.formPassword.setValue({
                currentPassword: '',
                newPassword1: '',
                newPassword2: ''
              });
              this.formPassword.markAsUntouched();

              this.toastrService.success('',
                'The password has been successfully changed.',
                {
                  timeOut: 3000,
                  closeButton: true,
                  progressBar: true,
                  positionClass: 'toast-bottom-center'
                });
            } else {
              this.changePasswordInvalidPassword = true;
            }
          },
          err => {
            this.changePasswordInvalidPassword = true;
          });

      } else {
        this.newPasswordsNotIdentical = true;
      }
    } else {
      this.newPasswordsNotValid = true;
    }

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

  changeTab($event: MatTabChangeEvent) {
    this.formPassword.setValue({
      currentPassword: '',
      newPassword1: '',
      newPassword2: ''
    });

    this.formPassword.markAsUntouched();
  }
}

export interface DialogData {
  user: User;
}
