import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/internal/Observable";
import {OperationType} from "../backend_types/operation-type";

@Injectable({
  providedIn: "root"
})
export class OperationTypeService {
  private serverUrl = "http://localhost:8080/";  // URL to REST-server
  private proceduresUrl = "operation_types/";
  private activeUrl = "active/";

  private readonly httpOptions = {
    headers: new HttpHeaders({
      "Content-Type": "application/json"
    })
  };

  constructor(private http: HttpClient) {
  }

  getProcedures(): Observable<OperationType[]> {
    return this.http.get<OperationType[]>(this.serverUrl + this.proceduresUrl);
  }

  getProcedure(procedure_id: number): Observable<OperationType> {
    return this.http.get<OperationType>(this.serverUrl + this.proceduresUrl + procedure_id);
  }

  addProcedure(procedure: OperationType): Observable<OperationType> {
    return this.http.post<OperationType>(this.serverUrl + this.proceduresUrl, JSON.stringify(procedure), this.httpOptions);
  }

  editProcedure(procedure: OperationType): Observable<OperationType> {
    return this.http.put<OperationType>(this.serverUrl + this.proceduresUrl + procedure.operationTypeId,
      JSON.stringify(procedure), this.httpOptions);
  }

  removeProcedure(procedure_id: number): Observable<boolean> {
    return this.http.delete<boolean>(this.serverUrl + this.proceduresUrl + procedure_id, this.httpOptions);
  }

}
