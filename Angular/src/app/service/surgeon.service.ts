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

  getUnlockSurgeons(): Observable<Surgeon[]> {
    return this.http.get<Surgeon[]>(this.serverUrl + this.surgeonUrl + "unlock/");
  }

  addSurgeon(surgeon: Surgeon): Observable<Surgeon> {
    return this.http.post<Surgeon>(this.serverUrl + this.surgeonUrl, JSON.stringify(surgeon), this.httpOptions);
  }

  editSurgeon(surgeon: Surgeon): Observable<Surgeon> {
    return this.http.put<Surgeon>(this.serverUrl + this.surgeonUrl + surgeon.surgeonId.toString(), JSON.stringify(surgeon),
      this.httpOptions);
  }

  lockSurgeon(surgeon: Surgeon): Observable<Surgeon> {
    surgeon.lock = true;
    return this.http.put<Surgeon>(this.serverUrl + this.surgeonUrl + surgeon.surgeonId.toString(), JSON.stringify(surgeon),
      this.httpOptions);
  }
}
