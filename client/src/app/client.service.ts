import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Response } from './response';
import { ApiRequest } from './apiRequest';

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  private readonly API_URL = 'http://localhost:9000';

  constructor(private http: HttpClient) {
  }

  getOcr(request: ApiRequest): Observable<Response> {
    return this.http.post<Response>(`${this.API_URL}/ocr`, request);
  }
}
