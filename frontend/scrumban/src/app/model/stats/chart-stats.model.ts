export interface ChartStats {
  localDate: string;

  startedTasks: number;
  activeTasks: number;
  finishedTasks: number;
  throughput: number;
  leadTime: number;
  wip: number;
}
