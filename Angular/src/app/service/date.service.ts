import {EventEmitter, Injectable} from "@angular/core";
import {DatePlan} from "../backend_types/date-plan";

@Injectable({
  providedIn: "root"
})
export class DateService {
  selected_date: EventEmitter<DatePlan> = new EventEmitter();

  constructor() {
  }

  public change(selected_date: DatePlan) {
    this.selected_date.emit(selected_date);
  }
}
