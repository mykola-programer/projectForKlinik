import {EventEmitter, Injectable} from "@angular/core";
import {Department} from "../backend_types/department";
import {DatePlan} from "../backend_types/date-plan";

@Injectable()
export class GlobalService {
  private selectedDepartment: Department;

  emittedDepartment: EventEmitter<Department> = new EventEmitter();
  statusNavbar: EventEmitter<string> = new EventEmitter();
  selected_datePlan: EventEmitter<DatePlan> = new EventEmitter();


  constructor() {
    this.emittedDepartment.subscribe(selectedDepartment => {
      this.selectedDepartment = selectedDepartment;
      // console.log("Global emitted");
      // console.log(this.selectedDepartment);
    });
  }

  getDepartment(): Department {
    // console.log("getDepartment");
    return this.selectedDepartment;
  }

  public changeDepartment(selectedDepartment: Department) {
    // console.log("changeDepartment");
    this.emittedDepartment.emit(selectedDepartment);
  }

  public changeNavbar(status: string) {
    this.statusNavbar.emit(status);
  }

  public changeDatePlan(selected_date: DatePlan) {
    this.selected_datePlan.emit(selected_date);
  }
}
