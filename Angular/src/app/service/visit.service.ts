import {Injectable} from "@angular/core";
import {Observable} from "rxjs/internal/Observable";
import {HttpClient} from "@angular/common/http";
import {Visit} from "../backend_types/visit";
import {MyObjectList} from "../backend_types/my-object-list";
import {UrlProperty} from "./url-property";

@Injectable({
  providedIn: "root"
})
export class VisitService {

  constructor(private http: HttpClient) {
  }

  getVisitsByDate(select_date: Date): Observable<Visit[]> {
    return this.http.get<Visit[]>(UrlProperty.serverUrl + UrlProperty.visitUrl + "all/" + select_date.toLocaleDateString() + "/");
  }

  addVisit(visit: Visit): Observable<Visit> {
    return this.http.post<Visit>(UrlProperty.serverUrl + UrlProperty.visitUrl, JSON.stringify(visit), UrlProperty.httpOptions);
  }

  editVisit(visit: Visit): Observable<Visit> {
    return this.http.put<Visit>(UrlProperty.serverUrl + UrlProperty.visitUrl + visit.visitId, JSON.stringify(visit), UrlProperty.httpOptions);
  }

  removeVisit(visit_id: number): any {
    return this.http.delete(UrlProperty.serverUrl + UrlProperty.visitUrl + visit_id, UrlProperty.httpOptions);
  }


  /** @deprecated */
  getVisitsWithWard(select_date: Date): Observable<Visit[]> {
    return this.http.get<Visit[]>(UrlProperty.serverUrl + UrlProperty.visitUrl + UrlProperty.isWardsUrl + select_date.toLocaleDateString() + "/");
  }

  /** @deprecated */
  getVisitsWithoutWard(select_date: Date): Observable<Visit[]> {
    return this.http.get<Visit[]>(UrlProperty.serverUrl + UrlProperty.visitUrl + UrlProperty.noWardUrl + select_date.toLocaleDateString() + "/");
  }

  /** @deprecated */
  putVisits(visits: Visit[]): Observable<Visit[]> {
    const myVisits: MyObjectList<Visit> = new MyObjectList();
    myVisits.objects = visits;
    return this.http.put<Visit[]>(UrlProperty.serverUrl + UrlProperty.visitUrl + "list/", JSON.stringify(myVisits), UrlProperty.httpOptions);
  }

  /** @deprecated */
  displaceVisits(visits: Visit[]): Observable<Visit[]> {
    const myVisits: MyObjectList<Visit> = new MyObjectList();
    myVisits.objects = visits;
    return this.http.put<Visit[]>(UrlProperty.serverUrl + UrlProperty.visitUrl + "list/displace", JSON.stringify(myVisits), UrlProperty.httpOptions);
  }

}
