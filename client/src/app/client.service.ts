import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Response } from './response';
import { ApiRequest } from './apiRequest';
import { API_URL } from './constants';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  constructor(private http: HttpClient) {
  }

  getOcr(request: ApiRequest): Observable<Response> {
    return this.http.post<Response>(`${API_URL}/ocr`, request);
  }
}
