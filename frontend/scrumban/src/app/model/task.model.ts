import {User} from './user.model';

export interface Task {
  id: number;
  name: string;
  description: string;
  priority: number;
  startedLocalDate: string;
  finishedLocalDate: string;
  columnId: number;
  columnName: string;
  projectId: number;
  projectName: string;
  users: User[];
}
