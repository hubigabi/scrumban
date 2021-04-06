import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Task} from 'src/app/model/task.model';
import {CommentService} from '../../../../service/comment.service';
import {User} from '../../../../model/user.model';
import {Comment} from '../../../../model/comment.model';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {environment} from '../../../../../environments/environment';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-comment-dialog',
  templateUrl: './comment-dialog.component.html',
  styleUrls: ['./comment-dialog.component.scss']
})
export class CommentDialogComponent implements OnInit {

  task: Task;
  user: User;

  allComments: Comment[];

  commentText: string;

  private readonly SERVER_WEB_SOCKET = environment.baseUrl + '/scrumban';

  private commentStompClient;
  private readonly COMMENT_URL_SAVE = '/app/saveComment/';
  private readonly COMMENT_URL_SUBSCRIBE_SAVE = '/comment/';

  constructor(public dialogRef: MatDialogRef<CommentDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: DialogData,
              private commentService: CommentService, private toastrService: ToastrService) {
  }

  ngOnInit() {
    this.task = this.data.task;
    this.user = this.data.user;

    this.commentService.getAllCommentsByTask_Id(this.task.id).subscribe(value => {

        this.allComments = value.map(comment => {
          comment.commentText = this.linkify(comment.commentText);
          return comment;
        });

        this.commentWebSocketConnect(this.task.id);
      }
    );
  }

  closeDialog() {
    this.commentWebSocketDisconnect();
    this.dialogRef.close(false);
  }

  postComment() {
    const comment: Comment = {
      id: 0,
      commentText: this.commentText,
      localDateTime: '',
      taskId: this.task.id,
      userId: this.user.id,
      userName: this.user.name
    };

    this.commentWebSocketSave(comment);
  }

  clear() {
    this.commentText = '';
  }

  commentWebSocketConnect(taskID: number) {
    const socket = new SockJS(this.SERVER_WEB_SOCKET);
    this.commentStompClient = Stomp.over(socket);
    this.commentStompClient.debug = () => {
    };

    this.commentStompClient.connect({}, frame => {
      this.commentStompClient.subscribe(this.COMMENT_URL_SUBSCRIBE_SAVE + taskID, message => {
        const comment: Comment = JSON.parse(message.body);
        comment.commentText = this.linkify(comment.commentText);

        this.allComments.unshift(comment);

        if (comment.commentText === this.linkify(this.commentText)
          && comment.userId === this.user.id) {
          this.commentText = '';
        }

        this.toastrService.success('',
          'A new comment was added',
          {
            timeOut: 3000,
            closeButton: true,
            progressBar: true,
            positionClass: 'toast-bottom-center'
          });
      });
    });
  }

  commentWebSocketSave(comment: Comment) {
    if (this.commentStompClient != null) {
      this.commentStompClient.send(this.COMMENT_URL_SAVE + this.task.id, {}, JSON.stringify(comment));
    } else {
      console.log('Cant send comment');
      console.log('Null: ' + this.commentStompClient);
    }
  }

  commentWebSocketDisconnect() {
    if (this.commentStompClient != null) {
      this.commentStompClient.disconnect();
    }
  }

  linkify(text) {
    if (text) {
      const urlRegex = /(https?:\/\/[^\s]+)/g;
      return text.replace(urlRegex, url => '<a href="' + url + '">' + url + '</a>');
    } else {
      return text;
    }
  }

}

export interface DialogData {
  task: Task;
  user: User;
}
