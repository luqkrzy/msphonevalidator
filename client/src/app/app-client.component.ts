import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { ClientService } from './client.service';
import { ApiRequest } from './apiRequest';
import { MS_VALIDATION_UTL, PHONE_SCRIPT } from './constants';

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
  code: string = '';
  fullScriptString: string = '';

  constructor(private service: ClientService) {
  }

  validateForm(): boolean {
    return !this.resultForm.valid ||
      !this.isCodeValid() && this.resultForm.valid;
  }

  ngOnInit(): void {
    console.log("initialized");
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
      this.isLoadingResults = true;
      let buffer64 = this.imgSrc.slice(22);
      console.log('image found: ', buffer64.length !== 0);
      if (buffer64.length !== 0) {
        this.service.getOcr(new ApiRequest(buffer64)).subscribe(resp => {
          console.log(resp);
          this.code = resp.ocr;
          this.isLoadingResults = false;
          this.setFullScriptString();
        });
      }
    }
  }

  onSubmit() {
    window.open(MS_VALIDATION_UTL);
  }

  changeDigit() {
    this.setFullScriptString();
  }

  private async readFileAsDataURL(file: File) {
    return await new Promise((resolve) => {
      let fileReader = new FileReader();
      fileReader.onload = () => resolve(fileReader.result);
      fileReader.readAsDataURL(file);
    });
  }

  private setFullScriptString(): void {
    this.isCodeValid();
    if (this.code) {
      let codeString = `const code = '${this.code}'\n`;
      let scriptString: string = PHONE_SCRIPT(this.selectedValue.toString());
      this.fullScriptString = codeString.concat(scriptString);
      console.log(this.fullScriptString);
    }
  }

  private isCodeValid(): boolean {
    let conditions: boolean[] = [];
    let fields: string[] = this.code.split(' ');
    fields.forEach(field => {
      conditions.push(field.length === this.selectedValue);
    });
    let b: boolean = true;
    for (let i = 0; i < conditions.length; i++) {
      b = conditions[i];
      if (!b) {
        return b;
      }
    }
    return b;
  }
}
