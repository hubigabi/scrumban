import {Pipe, PipeTransform} from '@angular/core';
import {User} from '../model/user.model';

@Pipe({
  name: 'taskUsers',
})
export class TaskUsersPipe implements PipeTransform {

  transform(users: User[], loggedUser?: User, args?: any): any {
    if (loggedUser) {
      return this.isLoggedUserInTaskUsers(users, loggedUser);
    } else {
      return this.getUsersByTask(users);
    }
  }

  getUsersByTask(users: User[]) {
    if (users.length === 0) {
      return '';
    }
    return users.map(value => value.name)
      .sort((a, b) => a.localeCompare(b))
      .join(', ');
  }

  isLoggedUserInTaskUsers(users: User[], loggedUser: User) {
    return users.map(value => value.id).indexOf(loggedUser.id) > -1;
  }

}
