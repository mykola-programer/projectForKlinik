import {Injectable} from "@angular/core";
import {Observable} from "rxjs/internal/Observable";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Visit} from "../backend_types/visit";

@Injectable({
  providedIn: "root"
})
export class VisitService {
  private serverUrl = "http://localhost:8080/";  // URL to REST-server
  private visitUrl = "visits/";
  private isWardsUrl = "all_wards/";
  private noWardUrl = "no_ward/";
  private readonly httpOptions = {
    headers: new HttpHeaders({
      "Content-Type": "application/json"
    })
  };

  constructor(private http: HttpClient) {
  }

  getVisits(select_date: Date): Observable<Visit[]> {
    return this.http.get<Visit[]>(this.serverUrl + this.visitUrl + select_date.toLocaleDateString() + "/");
  }

  getVisitsWithWard(select_date: Date): Observable<Visit[]> {
    return this.http.get<Visit[]>(this.serverUrl + this.visitUrl + this.isWardsUrl + select_date.toLocaleDateString() + "/");
  }

  getVisitsWithoutWard(select_date: Date): Observable<Visit[]> {
    return this.http.get<Visit[]>(this.serverUrl + this.visitUrl + this.noWardUrl + select_date.toLocaleDateString() + "/");
  }

  addVisit(visit: Visit): Observable<Visit> {
    return this.http.post<Visit>(this.serverUrl + this.visitUrl, JSON.stringify(visit), this.httpOptions);
  }

  /*  addVisits(visits: Visit[]): Observable<Visit[]> {
      return this.http.post<Visit[]>(this.serverUrl + this.visitUrl, JSON.stringify(visits), this.httpOptions);
    }*/

  doUnplaced(visit: Visit): Observable<Visit> {
    const visit_without_ward: Visit = new Visit();
    visit_without_ward.visitId = visit.visitId;
    visit_without_ward.visitDate = visit.visitDate;
    visit_without_ward.accomodation = null;
    visit_without_ward.status = visit.status;
    visit_without_ward.client = visit.client;
    visit_without_ward.timeForCome = visit.timeForCome;
    visit_without_ward.note = visit.note;
    visit_without_ward.operationType = visit.operationType;
    visit_without_ward.eye = visit.eye;
    visit_without_ward.surgeon = visit.surgeon;
    visit_without_ward.manager = visit.manager;
    visit_without_ward.orderForCome = visit.orderForCome;
    visit_without_ward.relative = visit.relative;
    return this.editVisit(visit_without_ward);
  }

  editVisit(visit: Visit): Observable<Visit> {
    return this.http.put<Visit>(this.serverUrl + this.visitUrl + visit.visitId.toString(), JSON.stringify(visit), this.httpOptions);
  }

  removeVisit(visit_id: number): any {
    return this.http.delete(this.serverUrl + this.visitUrl + visit_id.toString(), this.httpOptions);
  }

}
