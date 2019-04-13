import {Component, OnDestroy, OnInit} from "@angular/core";
import {Department} from "../../backend_types/department";
import {DepartmentService} from "../../service/department.service";
import {HttpErrorResponse} from "@angular/common/http";
import {ToastMessageService} from "../../service/toast-message.service";
import {Router} from "@angular/router";
import {Subscription} from "rxjs";
import {GlobalService} from "../../service/global.service";

@Component({
  selector: "app-department-selector",
  templateUrl: "./department-selector.component.html",
  styleUrls: ["./department-selector.component.css"]
})
export class DepartmentSelectorComponent implements OnInit, OnDestroy {
  private defaultDepartment = "Чернівці";
  departments: Department[] = [];
  selectedDepartment: Department = this.globalService.getDepartment();
  departments_loading = false;

  constructor(private departmentService: DepartmentService,
              private globalService: GlobalService,
              private router: Router,
              private toastMessageService: ToastMessageService) {
  }

  private departmentSubscriber: Subscription;

  ngOnInit() {
    this.getDepartments();
    this.departmentSubscriber = this.globalService.emittedDepartment.subscribe(selectedDepartment => {
      this.selectedDepartment = selectedDepartment;
    });
  }

  ngOnDestroy(): void {
    this.departmentSubscriber.unsubscribe();
  }

  getDepartments() {
    this.departments_loading = true;
    this.departmentService.getDepartments().toPromise().then((departments: Department[]) => {
      this.departments = departments;
      if (!this.globalService.getDepartment()) {
        this.change(departments.find(value => value.name === this.defaultDepartment));
      } else {
        this.selectedDepartment = this.globalService.getDepartment();
      }
      this.departments_loading = false;
    }).catch((err: HttpErrorResponse) => {
      this.departments_loading = true;
      this.toastMessageService.inform("Сервер недоступний!",
        "Спробуйте пізніше !" + "<br>" + err.error + "<br>" + err.message, "error", 10000);
      setTimeout(() => {
        this.getDepartments();
      }, 15000);
    });
  }

  change(department: Department) {
    this.globalService.changeDepartment(department);
  }

  addDepartment() {
    this.router.navigateByUrl("department");
    // console.log(this.selectedDepartment);
  }
}
