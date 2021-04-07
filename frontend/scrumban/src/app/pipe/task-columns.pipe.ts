import {Pipe, PipeTransform} from '@angular/core';
import {Column} from '../model/column.model';
import {Task} from '../model/task.model';

@Pipe({
  name: 'taskColumnsFilter',
  pure: false
})
export class TaskColumnsPipe implements PipeTransform {

  transform(tasks: Task[], columnId: number): Task[] {
    if (!tasks) {
      return [];
    }

    return tasks.filter(task => task.columnId === columnId);
  }

}
