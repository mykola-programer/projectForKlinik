import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/internal/Observable";
import {Manager} from "../backend_types/manager";
import {Client} from "../backend_types/client";
import {UrlProperty} from "./url-property";

@Injectable({
  providedIn: "root"
})
export class ManagerService {

  constructor(private http: HttpClient) {
  }

  getManagers(): Observable<Manager[]> {
    return this.http.get<Manager[]>(UrlProperty.serverUrl + UrlProperty.managersUrl);
  }

  getManager(managerId: number): Observable<Client> {
    return this.http.get<Client>(UrlProperty.serverUrl + UrlProperty.managersUrl + managerId);
  }

  addManager(manager: Manager): Observable<Manager> {
    return this.http.post<Manager>(UrlProperty.serverUrl + UrlProperty.managersUrl, JSON.stringify(manager), UrlProperty.httpOptions);
  }

  editManager(manager: Manager): Observable<Manager> {
    return this.http.put<Manager>(UrlProperty.serverUrl + UrlProperty.managersUrl + manager.managerId, JSON.stringify(manager),
      UrlProperty.httpOptions);
  }

  deleteManager(manager_id: number): Observable<boolean> {
    return this.http.delete<boolean>(UrlProperty.serverUrl + UrlProperty.managersUrl + manager_id, UrlProperty.httpOptions);
  }
}



