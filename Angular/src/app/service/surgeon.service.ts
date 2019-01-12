import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/internal/Observable";
import {Surgeon} from "../backend_types/surgeon";

@Injectable({
  providedIn: "root"
})
export class SurgeonService {
  private serverUrl = "http://localhost:8080/";  // URL to REST-server
  private surgeonUrl = "surgeons/";
  private activeUrl = "active/";
  private readonly httpOptions = {
    headers: new HttpHeaders({
      "Content-Type": "application/json"
    })
  };

  constructor(private http: HttpClient) {
  }

  getSurgeons(): Observable<Surgeon[]> {
    return this.http.get<Surgeon[]>(this.serverUrl + this.surgeonUrl);
  }

  /** @deprecated */
  getActiveSurgeons(): Observable<Surgeon[]> {
    return this.http.get<Surgeon[]>(this.serverUrl + this.surgeonUrl);
  }

  addSurgeon(surgeon: Surgeon): Observable<Surgeon> {
    return this.http.post<Surgeon>(this.serverUrl + this.surgeonUrl, JSON.stringify(surgeon), this.httpOptions);
  }

  editSurgeon(surgeon: Surgeon): Observable<Surgeon> {
    return this.http.put<Surgeon>(this.serverUrl + this.surgeonUrl + surgeon.surgeonId.toString(), JSON.stringify(surgeon),
      this.httpOptions);
  }

  deleteSurgeon(surgeon_id: number): Observable<boolean> {
    return this.http.delete<boolean>(this.serverUrl + this.surgeonUrl + surgeon_id.toString(), this.httpOptions);
  }

}


// getSurgeon(surgeonId: number): Observable<Surgeon[]> {
//   return this.http.get<Surgeon[]>(this.serverUrl + this.surgeonUrl + surgeonId.toString());
// }
//
// activateSurgeon(surgeon_id: number): Observable<boolean> {
//   return this.http.delete<boolean>(this.serverUrl + this.surgeonUrl + surgeon_id.toString() + this.activateUrl, this.httpOptions);
// }
//
// deactivateSurgeon(surgeon_id: number): Observable<boolean> {
//   return this.http.delete<boolean>(this.serverUrl + this.surgeonUrl + surgeon_id.toString() + this.deactivateUrl, this.httpOptions);
// }
