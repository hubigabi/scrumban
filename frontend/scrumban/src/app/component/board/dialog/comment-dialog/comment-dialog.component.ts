import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Task} from 'src/app/model/task.model';
import {CommentService} from '../../../../service/comment.service';
import {User} from '../../../../model/user.model';
import {Comment} from '../../../../model/comment.model';

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

  constructor(public dialogRef: MatDialogRef<CommentDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: DialogData,
              private commentService: CommentService) {
  }

  ngOnInit() {
    this.task = this.data.task;
    this.user = this.data.user;

    this.commentService.getAllCommentsByTask_Id(this.task.id).subscribe(value => {
        this.allComments = value;
      }
    );
  }

  closeDialog() {
    this.dialogRef.close(false);
  }

  postComment() {

  }

  clear() {

  }
}

export interface DialogData {
  task: Task;
  user: User;
}
