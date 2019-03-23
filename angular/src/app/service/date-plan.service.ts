import {Injectable} from "@angular/core";
import {Observable} from "rxjs/internal/Observable";
import {HttpClient, HttpParams} from "@angular/common/http";
import {UrlProperty} from "./url-property";
import {DatePlan} from "../backend_types/date-plan";

@Injectable({
  providedIn: "root"
})
export class DatePlanService {

  constructor(private http: HttpClient) {
  }

  getDatePlan(datePlanId: number): Observable<DatePlan> {
    return this.http.get<DatePlan>(UrlProperty.serverUrl + UrlProperty.datePlansUrl + datePlanId);
  }

/** @deprecated */
  getDatePlans(): Observable<DatePlan[]> {
    return this.http.get<DatePlan[]>(UrlProperty.serverUrl + UrlProperty.datePlansUrl);
  }
  getDatePlansByDepartment(departmentID: number, minDate: Date): Observable<DatePlan[]> {
    let params = new HttpParams()
      .set("departmentID", departmentID.toString())
      .set("minDate", minDate.toLocaleDateString());
    return this.http.get<DatePlan[]>(UrlProperty.serverUrl + UrlProperty.datePlansUrl, {
      headers: UrlProperty.httpOptions.headers,
      params: params
    });
  }

  addDatePlan(datePlan: DatePlan): Observable<DatePlan> {
    return this.http.post<DatePlan>(UrlProperty.serverUrl + UrlProperty.datePlansUrl, JSON.stringify(datePlan), UrlProperty.httpOptions);
  }

  editDatePlan(datePlan: DatePlan): Observable<DatePlan> {
    return this.http.put<DatePlan>(UrlProperty.serverUrl + UrlProperty.datePlansUrl + datePlan.datePlanId, JSON.stringify(datePlan), UrlProperty.httpOptions);
  }

  removeDatePlan(datePlanId: number): Observable<boolean> {
    return this.http.delete<boolean>(UrlProperty.serverUrl + UrlProperty.datePlansUrl + datePlanId + "/", UrlProperty.httpOptions);
  }

/*  /!** @deprecated *!/
  addVisitDates(datePlans: DatePlan[]): Observable<DatePlan[]> {
    const myVisitDates: MyObjectList<DatePlan> = new MyObjectList();
    myVisitDates.objects = datePlans;
    return this.http.put<DatePlan[]>(UrlProperty.serverUrl + UrlProperty.datePlansUrl + "list/", JSON.stringify(myVisitDates), UrlProperty.httpOptions);
  }

  /!** @deprecated *!/
  removeVisitDates(ids: number[]): Observable<boolean> {
    return this.http.delete<boolean>(UrlProperty.serverUrl + UrlProperty.datePlansUrl + "list/" + ids.toString(), UrlProperty.httpOptions);
  }*/
}
