import {HttpHeaders} from "@angular/common/http";
import {EventEmitter} from "@angular/core";
import {User} from "../backend_types/user";
import {Subscription} from "rxjs";

export class UrlProperty {

  static readonly serverUrl = "https://localhost:8443/";  // URL to REST-server
  // static readonly serverUrl = "http://localhost:8080/";  // URL to REST-server

  static readonly accomodationsUrl = "accomodations/";
  static readonly clientsUrl = "clients/";
  static readonly count = "count/";
  static readonly search = "search/";
  static readonly datePlansUrl = "date_plans/";
  static readonly managersUrl = "managers/";
  static readonly departmentsUrl = "departments/";
  static readonly proceduresUrl = "operation_types/";
  static readonly surgeonPlansUrl = "surgeon_plans/";
  static readonly surgeonsUrl = "surgeons/";
  static readonly usersUrl = "users/";
  static readonly visitsUrl = "visits/";

  static httpOptions = {
    headers: new HttpHeaders({
      "Content-Type": "application/json; charset=utf-8",
      // "Authorization": "Basic " + btoa("user" + ":" + "password"),
    })
  };

  static authUser: EventEmitter<User> = new EventEmitter(null);

  static authorizationSubscriber: Subscription = UrlProperty.authUser.subscribe((user: User) => {
    if (user) {
      UrlProperty.httpOptions.headers = UrlProperty.httpOptions.headers
        .append("Authorization", "Basic " + btoa(user.username + ":" + user.password));
    } else {
      UrlProperty.httpOptions.headers = UrlProperty.httpOptions.headers.delete("Authorization");
    }
  });


}
