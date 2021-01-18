import {User} from './user.model';

export interface Project {
  id: number;
  name: string;
  description: string;
  startedLocalDate: string;
  finishedLocalDate: string;
  leaderUser: User;
  users: User[];
}
