import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/internal/Observable";
import {Accomodation} from "../backend_types/accomodation";

@Injectable({
  providedIn: 'root'
})
export class AccomodationService {
  private serverUrl = 'http://localhost:8080/';  // URL to REST-server
  private accomodationUrl = 'accomodations/';
  private wardsUrl = 'wards/';

  constructor(private http: HttpClient) {
  }

  getAccomodations(): Observable<Accomodation[]> {
    return this.http.get<Accomodation[]>(this.serverUrl + this.accomodationUrl);
  }

  getAccomodation(accomodation_id: number): Observable<Accomodation> {
    return this.http.get<Accomodation>(this.serverUrl + this.accomodationUrl + accomodation_id);
  }

  getWards(): Observable<number[]> {
    return this.http.get<number[]>(this.serverUrl + this.accomodationUrl + this.wardsUrl);
  }

  addAccomodation(accomodation: Accomodation): Observable<Accomodation> {
    return this.http.put<Accomodation>(this.serverUrl + this.accomodationUrl, accomodation);
  }

  editAccomodation(accomodation: Accomodation): Observable<Accomodation> {
    return this.http.post<Accomodation>(this.serverUrl + this.accomodationUrl + accomodation.accomodationId.toString(), accomodation);
  }


}
