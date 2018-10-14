import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/internal/Observable";
import {Surgeon} from "../backend_types/surgeon";

@Injectable({
  providedIn: 'root'
})
export class SurgeonService {
  private serverUrl = 'http://localhost:8080/';  // URL to REST-server
  private surgeonUrl = 'surgeons/';

  constructor(private http: HttpClient) {
  }

  getSurgeons(): Observable<Surgeon[]> {
    return this.http.get<Surgeon[]>(this.serverUrl + this.surgeonUrl);
  }
}
