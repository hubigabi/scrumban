import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserTaskDialogComponent } from './user-task-dialog.component';

describe('UserTaskDialogComponent', () => {
  let component: UserTaskDialogComponent;
  let fixture: ComponentFixture<UserTaskDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserTaskDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserTaskDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
