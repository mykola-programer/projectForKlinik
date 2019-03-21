import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/internal/Observable";
import {Surgeon} from "../backend_types/surgeon";
import {UrlProperty} from "./url-property";

@Injectable({
  providedIn: "root"
})
export class SurgeonService {

  constructor(private http: HttpClient) {
  }

  getSurgeons(): Observable<Surgeon[]> {
    return this.http.get<Surgeon[]>(UrlProperty.serverUrl + UrlProperty.surgeonsUrl, UrlProperty.httpOptions);
  }

  addSurgeon(surgeon: Surgeon): Observable<Surgeon> {
    return this.http.post<Surgeon>(UrlProperty.serverUrl + UrlProperty.surgeonsUrl, JSON.stringify(surgeon), UrlProperty.httpOptions);
  }

  editSurgeon(surgeon: Surgeon): Observable<Surgeon> {
    return this.http.put<Surgeon>(UrlProperty.serverUrl + UrlProperty.surgeonsUrl + surgeon.surgeonId, JSON.stringify(surgeon),
      UrlProperty.httpOptions);
  }

  deleteSurgeon(surgeon_id: number): Observable<boolean> {
    return this.http.delete<boolean>(UrlProperty.serverUrl + UrlProperty.surgeonsUrl + surgeon_id, UrlProperty.httpOptions);
  }

}


// getSurgeon(surgeonId: number): Observable<Surgeon[]> {
//   return this.http.get<Surgeon[]>(UrlProperty.serverUrl + UrlProperty.surgeonsUrl + surgeonId.toString());
// }
//
// activateSurgeon(surgeon_id: number): Observable<boolean> {
//   return this.http.delete<boolean>(UrlProperty.serverUrl + UrlProperty.surgeonsUrl + surgeon_id.toString() + UrlProperty.activateUrl, UrlProperty.httpOptions);
// }
//
// deactivateSurgeon(surgeon_id: number): Observable<boolean> {
//   return this.http.delete<boolean>(UrlProperty.serverUrl + UrlProperty.surgeonsUrl + surgeon_id.toString() + UrlProperty.deactivateUrl, UrlProperty.httpOptions);
// }
