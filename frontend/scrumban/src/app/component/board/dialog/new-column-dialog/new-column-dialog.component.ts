import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Project} from '../../../../model/project.model';
import {FormControl, Validators} from '@angular/forms';
import {Column} from '../../../../model/column.model';

@Component({
  selector: 'app-new-column-dialog',
  templateUrl: './new-column-dialog.component.html',
  styleUrls: ['./new-column-dialog.component.scss']
})
export class NewColumnDialogComponent implements OnInit {

  title = 'New column';
  column: Column;

  readonly minNumberWIP = 0;
  readonly maxNumberWIP = 10;
  numberWIPFormControl: FormControl;

  readonly minColumnOrder = 1;
  maxColumnOrder = 1;
  columnOrderFormControl: FormControl;

  constructor(public dialogRef: MatDialogRef<NewColumnDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: DialogData) {
  }

  ngOnInit() {
    this.maxColumnOrder = this.data.currentColumnNumber + 1;

    if (this.data.column) {
      this.title = 'Edit column';
      this.column = {
        id: this.data.column?.id,
        name: this.data.column?.name,
        description: this.data.column?.description,
        numberOrder: this.data.column?.numberOrder + 1,
        isWIP: this.data.column?.isWIP,
        numberWIP: this.data.column?.numberWIP,
        tasks: [],
      };
    } else {
      this.column = {
        id: 0,
        name: '',
        description: '',
        numberOrder: this.maxColumnOrder,
        isWIP: false,
        numberWIP: 0,
        tasks: [],
      };
    }

    this.numberWIPFormControl = new FormControl(this.column.numberWIP,
      [Validators.min(this.minNumberWIP), Validators.max(this.maxNumberWIP)]);
    this.numberWIPFormControl.valueChanges.subscribe(value => {
      this.column.numberWIP = value;
    });

    this.columnOrderFormControl = new FormControl(this.column.numberOrder,
      [Validators.min(this.minColumnOrder), Validators.max(this.maxColumnOrder)]);
    this.columnOrderFormControl.valueChanges.subscribe(value => {
      this.column.numberOrder = value;
    });

  }

  cancel() {
    this.dialogRef.close();
  }

  submit() {
    this.column.numberOrder--;
    this.dialogRef.close(this.column);
  }

  changeIsWIP($event: any) {
    if ($event) {
      this.numberWIPFormControl.enable();
    } else {
      this.numberWIPFormControl.setValue(0);
      this.numberWIPFormControl.disable();
    }
  }
}

export interface DialogData {
  column: Column;
  currentColumnNumber: number;
  currentProject: Project;
}
