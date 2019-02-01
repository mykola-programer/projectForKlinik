import {Injectable} from "@angular/core";
import {Observable} from "rxjs/internal/Observable";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {VisitDate} from "../backend_types/visit-date";
import {MyObjectList} from "../backend_types/my-object-list";

@Injectable({
  providedIn: "root"
})
export class VisitDateService {
  private serverUrl = "http://localhost:8080/";  // URL to REST-server
  private visitDatesUrl = "visit_dates/";  // URL to web api

  private readonly httpOptions = {
    headers: new HttpHeaders({
      "Content-Type": "application/json"
    })
  };

  constructor(private http: HttpClient) {
  }

  getVisitDate(visitDateId: number): Observable<VisitDate> {
    return this.http.get<VisitDate>(this.serverUrl + this.visitDatesUrl + visitDateId);
  }

  getVisitDates(): Observable<VisitDate[]> {
    return this.http.get<VisitDate[]>(this.serverUrl + this.visitDatesUrl);
  }

  addVisitDates(visitDates: VisitDate[]): Observable<VisitDate[]> {
    const myVisitDates: MyObjectList<VisitDate> = new MyObjectList();
    myVisitDates.objects = visitDates;
    return this.http.put<VisitDate[]>(this.serverUrl + this.visitDatesUrl + "list/", JSON.stringify(myVisitDates), this.httpOptions);
  }

  removeVisitDate(visitDateId: number): Observable<boolean> {
    return this.http.delete<boolean>(this.serverUrl + this.visitDatesUrl + visitDateId + "/", this.httpOptions);
  }

  removeVisitDates(ids: number[]): Observable<boolean> {
    return this.http.delete<boolean>(this.serverUrl + this.visitDatesUrl + "list/" + ids.toString(), this.httpOptions);
  }
}
