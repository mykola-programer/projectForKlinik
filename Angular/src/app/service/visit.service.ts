import {Injectable} from "@angular/core";
import {Observable} from "rxjs/internal/Observable";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Visit} from "../backend_types/visit";
import {MyObjectList} from "../backend_types/my-object-list";

@Injectable({
  providedIn: "root"
})
export class VisitService {
  private serverUrl = "http://localhost:8080/";  // URL to REST-server
  private visitUrl = "visits/";
  private isWardsUrl = "all_wards/";
  private noWardUrl = "no_wards/";
  private readonly httpOptions = {
    headers: new HttpHeaders({
      "Content-Type": "application/json"
    })
  };

  constructor(private http: HttpClient) {
  }

  findVisits(select_date: Date): Observable<Visit[]> {
    return this.http.get<Visit[]>(this.serverUrl + this.visitUrl + "all/" + select_date.toLocaleDateString() + "/");
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

  editVisit(visit: Visit): Observable<Visit> {
    return this.http.put<Visit>(this.serverUrl + this.visitUrl + visit.visitId.toString(), JSON.stringify(visit), this.httpOptions);
  }

  putVisits(visits: Visit[]): Observable<Visit[]> {
    const myVisits: MyObjectList<Visit> = new MyObjectList();
    myVisits.objects = visits;
    return this.http.put<Visit[]>(this.serverUrl + this.visitUrl + "list/", JSON.stringify(myVisits), this.httpOptions);
  }

  displaceVisits(visits: Visit[]): Observable<Visit[]> {
    const myVisits: MyObjectList<Visit> = new MyObjectList();
    myVisits.objects = visits;
    return this.http.put<Visit[]>(this.serverUrl + this.visitUrl + "list/displace", JSON.stringify(myVisits), this.httpOptions);
  }

  removeVisit(visit_id: number): any {
    return this.http.delete(this.serverUrl + this.visitUrl + visit_id.toString(), this.httpOptions);
  }

}
