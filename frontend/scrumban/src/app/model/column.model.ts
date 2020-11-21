import {
  Progress,
  PROGRESS_BACKLOG,
  PROGRESS_DEPLOYMENT,
  PROGRESS_DEVELOPMENT,
  PROGRESS_DONE,
  PROGRESS_QA,
  PROGRESS_TEST
} from './progress.model';
import {Task} from './task.model';

export class Column {
  progress: Progress;
  tasks: Task[];

  constructor(progress: Progress, tasks: Task[]) {
    this.progress = progress;
    this.tasks = tasks;
  }
}

export const COLUMNS: Column[] = [
  new Column(PROGRESS_BACKLOG, []),
  new Column(PROGRESS_QA, []),
  new Column(PROGRESS_DEVELOPMENT, []),
  new Column(PROGRESS_TEST, []),
  new Column(PROGRESS_DEPLOYMENT, []),
  new Column(PROGRESS_DONE, [])
];
