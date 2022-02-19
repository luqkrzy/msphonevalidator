import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { Response } from './Response';

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  private readonly API_URL = 'http://localhost:9000';

  constructor(private http: HttpClient) {
  }

  ping(): Observable<Response> {
    return this.http.get<Response>(`${this.API_URL}/ping`).pipe(
      catchError((err) => {
        console.log(err);
        return throwError(err);
      })
    );
  }
}
