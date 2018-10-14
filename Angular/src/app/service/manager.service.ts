import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/internal/Observable";
import {Manager} from "../backend_types/manager";

@Injectable({
  providedIn: 'root'
})
export class ManagerService {
  private serverUrl = 'http://localhost:8080/';  // URL to REST-server
  private managerUrl = 'managers/';

  constructor(private http: HttpClient) {
  }

  getManagers(): Observable<Manager[]> {
    return this.http.get<Manager[]>(this.serverUrl + this.managerUrl);
  }
}
