import {EventEmitter, Injectable} from "@angular/core";
import {TokenStorage} from "./token-storage";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {User} from "../../backend_types/user";
import {Observable} from "rxjs/internal/Observable";
import {UrlProperty} from "../url-property";

@Injectable({
  providedIn: "root"
})
export class AuthService {

  // isLogging: EventEmitter<boolean> = new EventEmitter(false);

  constructor(private jwt: TokenStorage,
              private http: HttpClient) {
  }

  loginUser(user: User): Observable<User> {
    const headers = new HttpHeaders({
      "Content-type": "application/json; charset=utf-8",
      "Authorization": "Basic " + btoa(user.username + ":" + user.password)
    });
    return this.http.post<User>(UrlProperty.serverUrl + UrlProperty.usersUrl + "login", JSON.stringify(user), {headers: headers});
  }
}


/*

retrieveToken(username: string, password: string) {
  const headers = new HttpHeaders({
    "Content-type": "application/json; charset=utf-8",
    "Authorization": "Basic " + btoa(username + ":" + password)
  });
  this.http.post("https://localhost:8443/users/login/", {}, {headers: headers})
    .subscribe(
      data => console.log(data),
      err => {
        console.log("Error : ");
        console.log(err);
      }
    );
}*/
