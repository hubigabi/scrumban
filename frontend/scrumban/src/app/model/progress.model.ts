export class Progress {
  name: string;
  value: number;
  inProgress: boolean;

  constructor(name: string, value: number, inProgress: boolean) {
    this.name = name;
    this.value = value;
    this.inProgress = inProgress;
  }
}

export const PROGRESS_BACKLOG = new Progress('BACKLOG', 0, false);
export const PROGRESS_QA = new Progress('QA', 1, true);
export const PROGRESS_DEVELOPMENT = new Progress('DEVELOPMENT', 2, true);
export const PROGRESS_TEST = new Progress('TEST', 3, true);
export const PROGRESS_DEPLOYMENT = new Progress('DEPLOYMENT', 4, true);
export const PROGRESS_DONE = new Progress('DONE', 5, false);

export const ALL_PROGRESS: Progress[] = [
  PROGRESS_BACKLOG, PROGRESS_QA, PROGRESS_DEVELOPMENT, PROGRESS_TEST, PROGRESS_DEPLOYMENT, PROGRESS_DONE
];
