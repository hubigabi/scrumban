import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewTaskDialogComponent } from './new-task-dialog.component';

describe('TaskDialogComponent', () => {
  let component: NewTaskDialogComponent;
  let fixture: ComponentFixture<NewTaskDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewTaskDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewTaskDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
