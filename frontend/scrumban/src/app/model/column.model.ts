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

export const DEFAULT_COLUMNS: Column[] = [
  {id: 0, name: 'Backlog', description: 'Column for Backlog', numberOrder: 0, isWIP: false, numberWIP: 0, tasks: []},
  {id: 0, name: 'QA', description: 'Column for QA', numberOrder: 1, isWIP: true, numberWIP: 0, tasks: []},
  {id: 0, name: 'Development', description: 'Column for Development', numberOrder: 2, isWIP: true, numberWIP: 0, tasks: []},
  {id: 0, name: 'Test', description: 'Column for Test', numberOrder: 3, isWIP: true, numberWIP: 0, tasks: []},
  {id: 0, name: 'Deployment', description: 'Column for Deployment', numberOrder: 4, isWIP: true, numberWIP: 0, tasks: []},
  {id: 0, name: 'Done', description: 'Column for Done', numberOrder: 5, isWIP: false, numberWIP: 0, tasks: []}
];
