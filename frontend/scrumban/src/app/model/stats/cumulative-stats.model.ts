export interface CumulativeStats {
  days: Date[];
  finishedTasksColumns: FinishedTasksColumn[];
}

export interface FinishedTasksColumn {
  days: Date[];
  columnId: number;
  columnName: string;
  columnOrder: number;
  finishedTaskStatus: number[];
}
