import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/internal/Observable";
import {Accomodation} from "../backend_types/accomodation";
import {UrlProperty} from "./url-property";

@Injectable({
  providedIn: "root"
})
export class AccomodationService {

  constructor(private http: HttpClient) {
  }

  getAccomodations(): Observable<Accomodation[]> {
    return this.http.get<Accomodation[]>(UrlProperty.serverUrl + UrlProperty.accomodationUrl);
  }

  getAccomodationByID(accomodation_id: number): Observable<Accomodation> {
    return this.http.get<Accomodation>(UrlProperty.serverUrl + UrlProperty.accomodationUrl + accomodation_id);
  }

  addAccomodation(accomodation: Accomodation): Observable<Accomodation> {
    return this.http.post<Accomodation>(UrlProperty.serverUrl + UrlProperty.accomodationUrl, JSON.stringify(accomodation), UrlProperty.httpOptions);
  }

  editAccomodation(accomodation: Accomodation): Observable<Accomodation> {
    return this.http.put<Accomodation>(UrlProperty.serverUrl + UrlProperty.accomodationUrl + accomodation.accomodationId, JSON.stringify(accomodation), UrlProperty.httpOptions);
  }

  deleteAccomodation(accomodation_id: number): Observable<boolean> {
    return this.http.delete<boolean>(UrlProperty.serverUrl + UrlProperty.accomodationUrl + accomodation_id, UrlProperty.httpOptions);
  }

}
