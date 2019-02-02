import {Injectable} from "@angular/core";
import {Observable} from "rxjs/internal/Observable";
import {HttpClient} from "@angular/common/http";
import {VisitDate} from "../backend_types/visit-date";
import {MyObjectList} from "../backend_types/my-object-list";
import {UrlProperty} from "./url-property";

@Injectable({
  providedIn: "root"
})
export class VisitDateService {

  constructor(private http: HttpClient) {
  }

  getVisitDate(visitDateId: number): Observable<VisitDate> {
    return this.http.get<VisitDate>(UrlProperty.serverUrl + UrlProperty.visitDatesUrl + visitDateId);
  }

  getVisitDates(): Observable<VisitDate[]> {
    return this.http.get<VisitDate[]>(UrlProperty.serverUrl + UrlProperty.visitDatesUrl);
  }

  addVisitDate(visitDate: VisitDate): Observable<VisitDate> {
    return this.http.post<VisitDate>(UrlProperty.serverUrl + UrlProperty.visitDatesUrl, JSON.stringify(visitDate), UrlProperty.httpOptions);
  }

  editVisitDate(visitDate: VisitDate): Observable<VisitDate> {
    return this.http.put<VisitDate>(UrlProperty.serverUrl + UrlProperty.visitDatesUrl + visitDate.visitDateId, JSON.stringify(visitDate), UrlProperty.httpOptions);
  }

  removeVisitDate(visitDateId: number): Observable<boolean> {
    return this.http.delete<boolean>(UrlProperty.serverUrl + UrlProperty.visitDatesUrl + visitDateId + "/", UrlProperty.httpOptions);
  }

  /** @deprecated */
  addVisitDates(visitDates: VisitDate[]): Observable<VisitDate[]> {
    const myVisitDates: MyObjectList<VisitDate> = new MyObjectList();
    myVisitDates.objects = visitDates;
    return this.http.put<VisitDate[]>(UrlProperty.serverUrl + UrlProperty.visitDatesUrl + "list/", JSON.stringify(myVisitDates), UrlProperty.httpOptions);
  }

  /** @deprecated */
  removeVisitDates(ids: number[]): Observable<boolean> {
    return this.http.delete<boolean>(UrlProperty.serverUrl + UrlProperty.visitDatesUrl + "list/" + ids.toString(), UrlProperty.httpOptions);
  }
}
