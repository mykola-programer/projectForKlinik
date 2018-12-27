import {EventEmitter, Injectable} from "@angular/core";
import {VisitDate} from "../backend_types/visit-date";

@Injectable({
  providedIn: "root"
})
export class DateService {
  selected_date: EventEmitter<VisitDate> = new EventEmitter();

  constructor() {
  }

  public change(selected_date: VisitDate) {
    this.selected_date.emit(selected_date);
  }
}
