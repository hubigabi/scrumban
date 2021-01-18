import {Task} from './task.model';

export interface Column {
  id: number;
  name: string;
  description: string;
  isWIP: boolean;
  numberWIP: number;
  numberOrder: number;

  tasks: Task[];
}


