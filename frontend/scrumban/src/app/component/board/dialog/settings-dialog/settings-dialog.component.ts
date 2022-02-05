import {Component, Inject, OnInit} from '@angular/core';
import {User} from '../../../../model/user.model';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {UserService} from '../../../../service/user.service';
import {ToastrService} from 'ngx-toastr';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {PasswordChangeRequest} from '../../../../model/request/password-change-request.model';
import {MatTabChangeEvent} from '@angular/material/tabs';
import {EditProfileRequest} from '../../../../model/request/edit-profile-request.model';

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

  formProfile: FormGroup;
  editProfileNamesIdentical = false;
  editProfileInvalidPassword = false;

  serverError = false;

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

    this.formProfile = this.fb.group({
      name: [this.user.name, Validators.required],
      email: [{value: this.user.email, disabled: true}, [Validators.required, Validators.email]],
      registration: [{value: this.convertStringDate(this.user.registrationDate), disabled: true}],
      confirmPassword: ['', Validators.required]
    });

  }

  changePassword() {
    this.newPasswordsNotValid = false;
    this.newPasswordsNotIdentical = false;
    this.changePasswordInvalidPassword = false;
    this.serverError = false;

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
            this.serverError = true;
          });

      } else {
        this.newPasswordsNotIdentical = true;
      }
    } else {
      this.newPasswordsNotValid = true;
    }
  }

  editProfile() {
    this.editProfileNamesIdentical = false;
    this.editProfileInvalidPassword = false;
    this.serverError = false;

    const name = this.formProfile.get('name').value;
    const email = this.formProfile.get('email').value;
    const confirmPassword = this.formProfile.get('confirmPassword').value;

    const editProfileRequest: EditProfileRequest = {
      name,
      email,
      confirmPassword
    };

    if (name !== this.user.name) {
      this.userService.editProfile(this.user.id, editProfileRequest).subscribe(res => {
          if (res) {
            this.user.name = name;

            this.formProfile.get('name').setValue('');
            this.formProfile.get('confirmPassword').setValue('');
            this.formProfile.markAsUntouched();

            this.toastrService.success('',
              'The profile has been successfully edited.',
              {
                timeOut: 3000,
                closeButton: true,
                progressBar: true,
                positionClass: 'toast-bottom-center'
              });
          } else {
            this.editProfileInvalidPassword = true;
          }
        },
        err => {
          this.serverError = true;
        });
    } else {
      this.editProfileNamesIdentical = true;
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

    this.formProfile.setValue({
      name: this.user.name,
      email: this.user.email,
      registration: this.user.registrationDate,
      confirmPassword: ''
    });

    this.formProfile.markAsUntouched();
  }

  // From YYYY-MM-DD to DD-MM-YYYY
  convertStringDate(s: string): string {
    const stringArray = s.split('-');
    if (stringArray.length === 3) {
      return stringArray.reverse().join('-');
    } else {
      return s;
    }
  }

}

export interface DialogData {
  user: User;
}
