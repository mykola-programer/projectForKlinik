import {EventEmitter, Injectable} from "@angular/core";
import {Department} from "../types/department";
import {DatePlan} from "../types/date-plan";
import {Surgeon} from "../types/surgeon";

@Injectable()
export class GlobalService {
  private selectedDepartment: Department;
  private selectedSurgeon: Surgeon;

  emittedDepartment: EventEmitter<Department> = new EventEmitter();
  emittedSurgeon: EventEmitter<Surgeon> = new EventEmitter();
  statusNavbar: EventEmitter<string> = new EventEmitter();
  selected_datePlan: EventEmitter<DatePlan> = new EventEmitter();


  constructor() {
    this.emittedDepartment.subscribe(selectedDepartment => {
      this.selectedDepartment = selectedDepartment;
    });
    this.emittedSurgeon.subscribe(selectedSurgeon => {
      this.selectedSurgeon = selectedSurgeon;
    });
  }

  getDepartment(): Department {
    return this.selectedDepartment;
  }
  getSurgeon(): Surgeon {
    return this.selectedSurgeon;
  }

  public changeDepartment(selectedDepartment: Department) {
    this.emittedDepartment.emit(selectedDepartment);
  }
  public changeSurgeon(selectedSurgeon: Surgeon) {
    this.emittedSurgeon.emit(selectedSurgeon);
  }

  public changeNavbar(status: string) {
    this.statusNavbar.emit(status);
  }

  public changeDatePlan(selected_date: DatePlan) {
    this.selected_datePlan.emit(selected_date);
  }
}
