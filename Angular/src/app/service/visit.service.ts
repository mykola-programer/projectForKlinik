import {Injectable} from "@angular/core";
import {Observable} from "rxjs/internal/Observable";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Visit} from "../backend_types/visit";
import {UrlProperty} from "./url-property";

@Injectable({
  providedIn: "root"
})
export class VisitService {

  constructor(private http: HttpClient) {
  }

  getVisitsByDate(select_date?: Date): Observable<Visit[]> {
    let params = new HttpParams().set("date", select_date.toLocaleDateString());
    return this.http.get<Visit[]>(UrlProperty.serverUrl + UrlProperty.visitsUrl, {
      headers: UrlProperty.httpOptions.headers,
      params: params
    });
  }

  addVisit(visit: Visit): Observable<Visit> {
    return this.http.post<Visit>(UrlProperty.serverUrl + UrlProperty.visitsUrl, JSON.stringify(visit), UrlProperty.httpOptions);
  }

  editVisit(visit: Visit): Observable<Visit> {
    return this.http.put<Visit>(UrlProperty.serverUrl + UrlProperty.visitsUrl + visit.visitId, JSON.stringify(visit), UrlProperty.httpOptions);
  }

  removeVisit(visit_id: number): any {
    return this.http.delete(UrlProperty.serverUrl + UrlProperty.visitsUrl + visit_id, UrlProperty.httpOptions);
  }

}
