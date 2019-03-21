import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/internal/Observable";
import {UrlProperty} from "./url-property";
import {SurgeonPlan} from "../backend_types/surgeonPlan";

@Injectable({
  providedIn: "root"
})
export class SurgeonPlanService {

  constructor(private http: HttpClient) {
  }

  getSurgeonPlans(): Observable<SurgeonPlan[]> {
    return this.http.get<SurgeonPlan[]>(UrlProperty.serverUrl + UrlProperty.surgeonPlansUrl, UrlProperty.httpOptions);
  }

  addSurgeonPlan(surgeonPlan: SurgeonPlan): Observable<SurgeonPlan> {
    return this.http.post<SurgeonPlan>(UrlProperty.serverUrl + UrlProperty.surgeonPlansUrl, JSON.stringify(surgeonPlan), UrlProperty.httpOptions);
  }

  editSurgeonPlan(surgeonPlan: SurgeonPlan): Observable<SurgeonPlan> {
    return this.http.put<SurgeonPlan>(UrlProperty.serverUrl + UrlProperty.surgeonPlansUrl + surgeonPlan.surgeonPlanId, JSON.stringify(surgeonPlan),
      UrlProperty.httpOptions);
  }

  deleteSurgeonPlan(surgeonPlan_id: number): Observable<boolean> {
    return this.http.delete<boolean>(UrlProperty.serverUrl + UrlProperty.surgeonPlansUrl + surgeonPlan_id, UrlProperty.httpOptions);
  }

}
