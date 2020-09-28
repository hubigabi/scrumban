import {Pipe, PipeTransform} from '@angular/core';
import {User} from '../model/user.model';

@Pipe({
  name: 'taskUsers',
})
export class TaskUsersPipe implements PipeTransform {

  counter = 0;

  transform(users: User[], args?: any): any {
    return this.getUsersByTask(users);
  }

  getUsersByTask(users: User[]) {
    console.log('--------------------------------------------');

    if (users.length === 0) {
      return '';
    }
    return users.map(value => value.name)
      .sort((a, b) => a.localeCompare(b))
      .join(', ');
  }

}
