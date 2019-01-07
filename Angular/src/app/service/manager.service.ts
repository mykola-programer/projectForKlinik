import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/internal/Observable";
import {Manager} from "../backend_types/manager";
import {Client} from "../backend_types/client";
import {MyObjectList} from "../backend_types/my-object-list";

@Injectable({
  providedIn: "root"
})
export class ManagerService {
  private serverUrl = "http://localhost:8080/";  // URL to REST-server
  private managersUrl = "managers/";
  private activeUrl = "active/";
  private readonly httpOptions = {
    headers: new HttpHeaders({
      "Content-Type": "application/json"
    })
  };

  constructor(private http: HttpClient) {
  }

  getActiveManagers(): Observable<Manager[]> {
    return this.http.get<Manager[]>(this.serverUrl + this.managersUrl + this.activeUrl);
  }

  getManager(manager_id: number): Observable<Manager> {
    return this.http.get<Manager>(this.serverUrl + this.managersUrl + manager_id);
  }

  addManager(manager: Manager): Observable<Manager> {
    return this.http.post<Manager>(this.serverUrl + this.managersUrl, JSON.stringify(manager), this.httpOptions);
  }

  putManagers(managers: Manager[]): Observable<Manager[]> {
    const myManagers: MyObjectList<Manager> = new MyObjectList();
    myManagers.objects = managers;
    return this.http.put<Manager[]>(this.serverUrl + this.managersUrl + "list/", JSON.stringify(myManagers), this.httpOptions);
  }

  editManager(manager: Manager): Observable<Manager> {
    return this.http.put<Manager>(this.serverUrl + this.managersUrl + manager.managerId.toString(), JSON.stringify(manager),
      this.httpOptions);
  }

  deleteManager(manager_id: number): Observable<boolean> {
    return this.http.delete<boolean>(this.serverUrl + this.managersUrl + manager_id.toString(), this.httpOptions);
  }
}
