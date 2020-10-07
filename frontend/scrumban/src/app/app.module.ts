import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BoardComponent} from './component/board/board.component';

import {DragDropModule} from '@angular/cdk/drag-drop';
import {HttpClientModule} from '@angular/common/http';

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
import {SimpleNotificationsModule} from 'angular2-notifications';
import {ToasterModule} from 'angular2-toaster';

@NgModule({
  declarations: [
    AppComponent,
    BoardComponent,
    TaskUsersPipe,
    NewTaskDialogComponent,
    UpdateTaskDialogComponent,
    NewProjectDialogComponent,
    UpdateProjectDialogComponent
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
    SimpleNotificationsModule.forRoot(),
    ToasterModule.forRoot()
  ],
  providers: [{provide: MAT_DATE_LOCALE, useValue: 'en-GB'}],
  bootstrap: [AppComponent]
})
export class AppModule {
}

