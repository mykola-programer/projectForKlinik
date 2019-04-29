import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/internal/Observable";
import {User} from "../backend_types/user";
import {UrlProperty} from "./url-property";

@Injectable({
  providedIn: "root"
})
export class UserService {

  constructor(private http: HttpClient) {
  }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(UrlProperty.serverUrl + UrlProperty.usersUrl);
  }

  getUser(user_id: number): Observable<User> {
    return this.http.get<User>(UrlProperty.serverUrl + UrlProperty.usersUrl + user_id);
  }

  addUser(user: User): Observable<User> {
    return this.http.post<User>(UrlProperty.serverUrl + UrlProperty.usersUrl, JSON.stringify(user), UrlProperty.httpOptions);
  }

  editUser(userID: number, currentUser: User, editedUser: User): Observable<User> {
    return this.http.put<User>(UrlProperty.serverUrl + UrlProperty.usersUrl + userID, JSON.stringify({
      currentUser: currentUser,
      editedUser: editedUser
    }), UrlProperty.httpOptions);
  }

  loginUser(user: User): Observable<boolean> {
    return this.http.post<boolean>(UrlProperty.serverUrl + UrlProperty.usersUrl + "login", JSON.stringify(user), UrlProperty.httpOptions);
  }

  deleteUser(user_id: number): Observable<boolean> {
    return this.http.delete<boolean>(UrlProperty.serverUrl + UrlProperty.usersUrl + user_id, UrlProperty.httpOptions);
  }

  testLogin(user: User): Observable<object> {
    return this.http.post(UrlProperty.serverUrl + UrlProperty.usersUrl + "login", JSON.stringify(user), UrlProperty.httpOptions);
  }
}
