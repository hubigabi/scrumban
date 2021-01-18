import {Project} from './project.model';
import {User} from './user.model';
import {Column} from './column.model';

export interface Task {
  id: number;
  name: string;
  description: string;
  priority: number;
  startedLocalDate: string;
  finishedLocalDate: string;
  column: Column;
  project: Project;
  users: User[];
}
