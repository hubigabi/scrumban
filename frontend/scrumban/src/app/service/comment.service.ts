import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Comment} from '../model/comment.model';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private readonly COMMENT_URL = environment.baseUrl + '/api/comment';
  private readonly ALL_COMMENTS_BY_TASK_ID = this.COMMENT_URL + '/allByTask';

  constructor(private httpClient: HttpClient) {
  }

  public getCommentByID(id: number): Observable<Comment> {
    return this.httpClient.get<Comment>(this.COMMENT_URL + '/' + id.toString());
  }

  public createComment(comment: Comment): Observable<Comment> {
    return this.httpClient.post<Comment>(this.COMMENT_URL, comment);
  }

  public updateComment(comment: Comment): Observable<Comment> {
    return this.httpClient.put<Comment>(this.COMMENT_URL, comment);
  }

  public getAllCommentsByTask_Id(taskID: number): Observable<Comment[]> {
    return this.httpClient.get<Comment[]>(this.ALL_COMMENTS_BY_TASK_ID + '/' + taskID.toString());
  }
}
