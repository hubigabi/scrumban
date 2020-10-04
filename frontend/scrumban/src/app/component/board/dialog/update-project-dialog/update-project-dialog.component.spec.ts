import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateProjectDialogComponent } from './update-project-dialog.component';

describe('UpdateProjectDialogComponent', () => {
  let component: UpdateProjectDialogComponent;
  let fixture: ComponentFixture<UpdateProjectDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UpdateProjectDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateProjectDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
