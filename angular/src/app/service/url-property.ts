import {HttpHeaders, HttpParams} from "@angular/common/http";

export class UrlProperty {

  static readonly serverUrl = "http://localhost:8080/";  // URL to REST-server

  static readonly proceduresUrl = "operation_types/";
  static readonly visitDatesUrl = "visit_dates/";
  static readonly visitUrl = "visits/";
  static readonly surgeonUrl = "surgeons/";
  static readonly managersUrl = "managers/";
  static readonly clientsUrl = "clients/";
  static readonly accomodationUrl = "accomodations/";
  static readonly usersUrl = "users/";

  static readonly httpOptions = {
    headers: new HttpHeaders({
      "Content-Type": "application/json; charset=utf-8",
      "Cache-Control": "no-cache, no-store, must-revalidate, post-check=0, pre-check=0",
      "Pragma": "no-cache",
      "Expires": "0"
    })
  };


  /** @deprecated */
  static readonly isWardsUrl = "all_wards/";

  /** @deprecated */
  static readonly noWardUrl = "no_wards/";

}
