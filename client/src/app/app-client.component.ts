import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-root',
  templateUrl: './app-client.component.html',
  styleUrls: ['./app-client.component.css']
})
export class AppClient implements OnInit {
  base64 = '';
  isLoadingResults: boolean = false;
  hideImg: boolean = true;
  resultForm: FormControl = new FormControl('',
    [Validators.required, Validators.pattern('[0-9 ]+')]);
  digits = [
    {value: 6, viewValue: '6-Digit'},
    {value: 7, viewValue: '7-Digit'}];
  selectedValue = 7;
  imgSrc: any = '';

  onSubmit() {
    console.log("form is invalid", this.resultForm.invalid);
    console.log(this.resultForm.value);
    console.log(this.selectedValue);
    console.log(this.base64);
  }

  validateForm(): boolean {
    return !this.resultForm.valid;
  }

  ngOnInit(): void {
  }

  onPaste(event: any) {
    const items = (event.clipboardData || event.originalEvent.clipboardData).items;
    let blob = null;
    for (let i = 0; i < items.length; i++) {
      if (items[i].type.indexOf("image") === 0) {
        blob = items[i].getAsFile();
      }
    }
    if (blob !== null) {
      this.readFileAsDataURL(blob).then(result => {
        this.imgSrc = result;
      });
      this.hideImg = false;
      let buffer64 = this.imgSrc.slice(22);
      console.log('image found: ', buffer64.length !== 0);
    }
  }

  async readFileAsDataURL(file: File) {
    return await new Promise((resolve) => {
      let fileReader = new FileReader();
      fileReader.onload = (e) => resolve(fileReader.result);
      fileReader.readAsDataURL(file);
    });
  }
}
