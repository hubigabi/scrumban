import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BoardComponent} from './component/board/board.component';

import {DragDropModule} from '@angular/cdk/drag-drop';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';

import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import {TaskUsersPipe} from './pipe/task-users.pipe';
import {MatIconModule} from '@angular/material/icon';
import {NewTaskDialogComponent} from './component/board/dialog/new-task-dialog/new-task-dialog.component';
import {MatDialogModule} from '@angular/material/dialog';
import {FormsModule} from '@angular/forms';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MAT_DATE_LOCALE, MatNativeDateModule} from '@angular/material/core';
import {UpdateTaskDialogComponent} from './component/board/dialog/update-task-dialog/update-task-dialog.component';
import {MatTableModule} from '@angular/material/table';
import {MatSortModule} from '@angular/material/sort';
import {NewProjectDialogComponent} from './component/board/dialog/new-project-dialog/new-project-dialog.component';
import {ReactiveFormsModule} from '@angular/forms';
import {UpdateProjectDialogComponent} from './component/board/dialog/update-project-dialog/update-project-dialog.component';
import {MatListModule} from '@angular/material/list';
import {ToastrModule} from 'ngx-toastr';
import {CommonModule} from '@angular/common';
import {ConfirmDialogComponent} from './component/board/dialog/confirm-dialog/confirm-dialog.component';
import {CommentDialogComponent} from './component/board/dialog/comment-dialog/comment-dialog.component';
import {CommentLocalDateTimePipe} from './pipe/comment-local-date-time.pipe';
import {ProjectStatsDialogComponent} from './component/board/dialog/project-stats-dialog/project-stats-dialog.component';
import {MDBBootstrapModule} from 'angular-bootstrap-md';
import {ChartsModule, WavesModule} from 'angular-bootstrap-md';
import {AuthInterceptor} from './interceptor/auth-interceptor.interceptor';
import {LoginComponent} from './component/login/login.component';
import {SignupComponent} from './component/signup/signup.component';
import {MatMenuModule} from '@angular/material/menu';
import {UserTaskDialogComponent} from './component/board/dialog/user-task-dialog/user-task-dialog.component';
import {SettingsDialogComponent} from './component/board/dialog/settings-dialog/settings-dialog.component';
import {MatTabsModule} from '@angular/material/tabs';
import {SocialLoginModule, SocialAuthServiceConfig} from 'angularx-social-login';
import {
  GoogleLoginProvider,
  FacebookLoginProvider
} from 'angularx-social-login';

@NgModule({
  declarations: [
    AppComponent,
    BoardComponent,
    TaskUsersPipe,
    NewTaskDialogComponent,
    UpdateTaskDialogComponent,
    NewProjectDialogComponent,
    UpdateProjectDialogComponent,
    ConfirmDialogComponent,
    CommentDialogComponent,
    CommentLocalDateTimePipe,
    ProjectStatsDialogComponent,
    LoginComponent,
    SignupComponent,
    UserTaskDialogComponent,
    SettingsDialogComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    DragDropModule,
    MatFormFieldModule,
    MatInputModule,
    BrowserAnimationsModule,
    MatSelectModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    FormsModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatTableModule,
    MatSortModule,
    ReactiveFormsModule,
    MatListModule,
    ToastrModule.forRoot(),
    CommonModule,
    MDBBootstrapModule.forRoot(),
    ChartsModule,
    WavesModule,
    MatMenuModule,
    MatTabsModule,
    SocialLoginModule
  ],
  providers: [
    {provide: MAT_DATE_LOCALE, useValue: 'en-GB'},
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
    {
      provide: 'SocialAuthServiceConfig',
      useValue: {
        autoLogin: false,
        providers: [
          {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider(
              '214019591884-7l85mlkhn2o4ggar56rao6eessoodts7.apps.googleusercontent.com'
            )
          },
          {
            id: FacebookLoginProvider.PROVIDER_ID,
            provider: new FacebookLoginProvider('768194447098563')
          }
        ]
      } as SocialAuthServiceConfig,
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}

