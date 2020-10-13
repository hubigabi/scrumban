import {User} from './user.model';
import {Task} from './task.model';

export interface Comment {
  id: number;
  commentText: string;
  localDateTime: string;
  task: Task;
  user: User;
}


