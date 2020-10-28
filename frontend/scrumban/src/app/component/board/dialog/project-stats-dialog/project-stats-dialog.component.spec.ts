import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectStatsDialogComponent } from './project-stats-dialog.component';

describe('ProjectStatsDialogComponent', () => {
  let component: ProjectStatsDialogComponent;
  let fixture: ComponentFixture<ProjectStatsDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectStatsDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectStatsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
