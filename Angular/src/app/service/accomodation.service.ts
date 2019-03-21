import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
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
    return this.http.get<Accomodation[]>(UrlProperty.serverUrl + UrlProperty.accomodationsUrl, UrlProperty.httpOptions);
  }

  getAccomodationByID(accomodation_id: number): Observable<Accomodation> {
    return this.http.get<Accomodation>(UrlProperty.serverUrl + UrlProperty.accomodationsUrl + accomodation_id, UrlProperty.httpOptions);
  }

  addAccomodation(accomodation: Accomodation): Observable<Accomodation> {
    return this.http.post<Accomodation>(UrlProperty.serverUrl + UrlProperty.accomodationsUrl, JSON.stringify(accomodation), UrlProperty.httpOptions);
  }

  editAccomodation(accomodation: Accomodation): Observable<Accomodation> {
    return this.http.put<Accomodation>(UrlProperty.serverUrl + UrlProperty.accomodationsUrl + accomodation.accomodationId, JSON.stringify(accomodation), UrlProperty.httpOptions);
  }

  deleteAccomodation(accomodation_id: number): Observable<boolean> {
    return this.http.delete<boolean>(UrlProperty.serverUrl + UrlProperty.accomodationsUrl + accomodation_id, UrlProperty.httpOptions);
  }

}
