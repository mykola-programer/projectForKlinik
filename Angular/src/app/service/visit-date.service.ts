import {Injectable} from '@angular/core';
import {Observable} from "rxjs/internal/Observable";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {VisitDate} from "../backend_types/visit-date";

@Injectable({
  providedIn: 'root'
})
export class VisitDateService {
  private serverUrl = 'http://localhost:8080/';  // URL to REST-server
  private visitDatesUrl = 'visit_dates/';  // URL to web api

  private readonly httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  constructor(private http: HttpClient) {
  }

  getVisitDates(): Observable<VisitDate[]> {
    return this.http.get<VisitDate[]>(this.serverUrl + this.visitDatesUrl);
  }

  addVisitDates(dates: VisitDate[]): Observable<Object> {
    return this.http.post<VisitDate[]>(this.serverUrl + this.visitDatesUrl, JSON.stringify(dates), this.httpOptions);
  }

  removeVisitDate(visitDateId: number): any {
      return this.http.delete(this.serverUrl + this.visitDatesUrl + visitDateId + "/", this.httpOptions);
  }
}
