import {EventEmitter, Injectable} from "@angular/core";
import {Observable} from "rxjs/internal/Observable";
import {Department} from "../backend_types/department";
import {UrlProperty} from "./url-property";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: "root"
})
export class DepartmentService {
  constructor(private http: HttpClient) {
  }

  getDepartments(): Observable<Department[]> {
    return this.http.get<Department[]>(UrlProperty.serverUrl + UrlProperty.departmentsUrl, UrlProperty.httpOptions);
  }

  addDepartment(department: Department): Observable<Department> {
    return this.http.post<Department>(UrlProperty.serverUrl + UrlProperty.departmentsUrl, JSON.stringify(department), UrlProperty.httpOptions);
  }

  editDepartment(department: Department): Observable<Department> {
    return this.http.put<Department>(UrlProperty.serverUrl + UrlProperty.departmentsUrl + department.departmentId, JSON.stringify(department),
      UrlProperty.httpOptions);
  }

  deleteDepartment(department_id: number): Observable<boolean> {
    return this.http.delete<boolean>(UrlProperty.serverUrl + UrlProperty.departmentsUrl + department_id, UrlProperty.httpOptions);
  }
}
