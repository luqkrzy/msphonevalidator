import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  base64 = '';
  isLoadingResults: boolean = false;
  resultForm: FormControl = new FormControl('',
    [Validators.required, Validators.pattern('[0-9 ]+')]);
  digits = [
    {value: 6, viewValue: '6-Digit'},
    {value: 7, viewValue: '7-Digit'}];
  selectedValue = 7;

  onSubmit() {
    console.log("form is invalid", this.resultForm.invalid);
    console.log(this.resultForm.value);
    console.log(this.selectedValue);
  }

  validateForm(): boolean {
    return !this.resultForm.valid;
  }

  ngOnInit(): void {
  }
}
