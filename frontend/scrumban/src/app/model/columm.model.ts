import {Progress} from './progress.model';
import { Task } from './task.model';

export class Columm {
  progress: Progress;
  tasks: Task[];

  constructor(progress: Progress, tasks: Task[]) {
    this.progress = progress;
    this.tasks = tasks;
  }
}


