import {EventEmitter, Injectable} from "@angular/core";
import {Department} from "../backend_types/department";

@Injectable()
export class GlobalService {
  emittedDepartment: EventEmitter<Department> = new EventEmitter();
  private selectedDepartment: Department;

  constructor() {
    this.emittedDepartment.subscribe(selectedDepartment => {
      this.selectedDepartment = selectedDepartment;
      // console.log("Global emitted");
      // console.log(this.selectedDepartment);
    });
  }

  public change(selectedDepartment: Department) {
    // console.log("changeDepartment");
    this.emittedDepartment.emit(selectedDepartment);
  }

  getDepartment(): Department {
    // console.log("getDepartment");
    return this.selectedDepartment;
  }
}
