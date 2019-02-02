import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/internal/Observable";
import {OperationType} from "../backend_types/operation-type";
import {UrlProperty} from "./url-property";

@Injectable({
  providedIn: "root"
})
export class OperationTypeService {

  constructor(private http: HttpClient) {
  }

  getProcedures(): Observable<OperationType[]> {
    return this.http.get<OperationType[]>(UrlProperty.serverUrl + UrlProperty.proceduresUrl);
  }

  getProcedure(procedure_id: number): Observable<OperationType> {
    return this.http.get<OperationType>(UrlProperty.serverUrl + UrlProperty.proceduresUrl + procedure_id);
  }

  addProcedure(procedure: OperationType): Observable<OperationType> {
    return this.http.post<OperationType>(UrlProperty.serverUrl + UrlProperty.proceduresUrl, JSON.stringify(procedure), UrlProperty.httpOptions);
  }

  editProcedure(procedure: OperationType): Observable<OperationType> {
    return this.http.put<OperationType>(UrlProperty.serverUrl + UrlProperty.proceduresUrl + procedure.operationTypeId,
      JSON.stringify(procedure), UrlProperty.httpOptions);
  }

  removeProcedure(procedure_id: number): Observable<boolean> {
    return this.http.delete<boolean>(UrlProperty.serverUrl + UrlProperty.proceduresUrl + procedure_id, UrlProperty.httpOptions);
  }

}
