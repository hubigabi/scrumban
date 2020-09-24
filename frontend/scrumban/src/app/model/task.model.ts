import {Project} from './project.model';
import {User} from './user.model';

export interface Task {
  id: number;
  name: string;
  description: string;
  priority: number;
  progress: string;
  project: Project;
  users: User[];
}



