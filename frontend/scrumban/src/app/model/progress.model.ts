export class Progress {
  name: string;
  inProgress: boolean;

  constructor(name: string, inProgress: boolean) {
    this.name = name;
    this.inProgress = inProgress;
  }
}

export const PROGRESS_BACKLOG = new Progress('BACKLOG', false);
export const PROGRESS_QA = new Progress('QA', true);
export const PROGRESS_DEVELOPMENT = new Progress('DEVELOPMENT', true);
export const PROGRESS_TEST = new Progress('TEST', true);
export const PROGRESS_DEPLOYMENT = new Progress('DEPLOYMENT', true);
export const PROGRESS_DONE = new Progress('DONE', false);
