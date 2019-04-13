import {HttpHeaders} from "@angular/common/http";

export class UrlProperty {

  static readonly serverUrl = "https://localhost:8334/";  // URL to REST-server

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

  static readonly httpOptions = {
    headers: new HttpHeaders({
      "Content-Type": "application/json; charset=utf-8",
      "Cache-Control": "no-cache, no-store, must-revalidate, post-check=0, pre-check=0",
      "Pragma": "no-cache",
      "Expires": "0"
    })
  };

}
