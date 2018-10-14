import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/internal/Observable";
import {OperationType} from "../backend_types/operation-type";

@Injectable({
  providedIn: 'root'
})
export class OperationTypeService {
  private serverUrl = 'http://localhost:8080/';  // URL to REST-server
  private procedureUrl = 'operation_types/';

  constructor(private http: HttpClient) {
  }

  getProcedures(): Observable<OperationType[]> {
    return this.http.get<OperationType[]>(this.serverUrl + this.procedureUrl);
  }

  getProcedure(procedure_id: number): Observable<OperationType> {
    return this.http.get<OperationType>(this.serverUrl + this.procedureUrl + procedure_id);
  }
}
