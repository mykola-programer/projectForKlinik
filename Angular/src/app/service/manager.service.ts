import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/internal/Observable";
import {Manager} from "../backend_types/manager";

@Injectable({
  providedIn: "root"
})
export class ManagerService {
  private serverUrl = "http://localhost:8080/";  // URL to REST-server
  private managersUrl = "managers/";
  private readonly httpOptions = {
    headers: new HttpHeaders({
      "Content-Type": "application/json"
    })
  };

  constructor(private http: HttpClient) {
  }

  getManagers(): Observable<Manager[]> {
    return this.http.get<Manager[]>(this.serverUrl + this.managersUrl);
  }

  getManager(manager_id: number): Observable<Manager> {
    return this.http.get<Manager>(this.serverUrl + this.managersUrl + manager_id);
  }

  addManager(manager: Manager): Observable<Manager> {
    return this.http.post<Manager>(this.serverUrl + this.managersUrl, JSON.stringify(manager), this.httpOptions);
  }

  editManager(manager: Manager): Observable<Manager> {
    return this.http.put<Manager>(this.serverUrl + this.managersUrl + manager.managerId.toString(), JSON.stringify(manager),
      this.httpOptions);
  }

  removeManager(manager_id: number): Observable<boolean> {
    return this.http.delete<boolean>(this.serverUrl + this.managersUrl + manager_id.toString(), this.httpOptions);
  }
}
