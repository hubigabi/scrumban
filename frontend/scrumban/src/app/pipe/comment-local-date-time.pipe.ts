import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'commentLocalDateTime'
})
export class CommentLocalDateTimePipe implements PipeTransform {

  transform(localDateTimeString: string): string {

    const date = new Date(localDateTimeString);

    const timeFormatted = ('0' + date.getHours()).slice(-2) + ':'
      + ('0' + date.getMinutes()).slice(-2);

    const dd = String(date.getDate()).padStart(2, '0');
    const mm = String(date.getMonth() + 1).padStart(2, '0');
    const yyyy = date.getFullYear();

    const dateFormatted = dd + '/' + mm + '/' + yyyy;

    return timeFormatted + ' ' + dateFormatted;
  }

}
