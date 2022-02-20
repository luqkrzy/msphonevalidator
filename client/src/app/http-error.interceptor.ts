import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {
  private matSnackBarConfig: MatSnackBarConfig = new MatSnackBarConfig();

  constructor(private matSnackBar: MatSnackBar) {
    this.matSnackBarConfig.duration = 6000;
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        const errorMessage = this.setError(error);
        console.log(error);
        this.matSnackBar.open(errorMessage, 'OK', this.matSnackBarConfig);
        return throwError(error.error);
      })
    );
  }

  private setError(error: HttpErrorResponse): string {
    if (error.error instanceof ErrorEvent) {
      //  client side error
      return error.error.message;
    } else {
      //  server side error
      switch (error.status) {
        case 0:
          return 'Balancer unresponsive';
        case 400:
        case 500:
        case 503:
          return error.error.message;
      }
    }
    return 'Error occurred';
  }
}
