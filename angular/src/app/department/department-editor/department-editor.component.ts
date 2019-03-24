import {Component, OnInit} from "@angular/core";
import {AbstractControl, FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ToastMessageService} from "../../service/toast-message.service";
import {debounceTime} from "rxjs/operators";
import {HttpErrorResponse} from "@angular/common/http";
import {Department} from "../../backend_types/department";
import {DepartmentService} from "../../service/department.service";
import {GlobalService} from "../../service/global.service";

@Component({
  selector: "app-department-editor",
  templateUrl: "./department-editor.component.html",
  styleUrls: ["./department-editor.component.css"]
})
export class DepartmentEditorComponent implements OnInit {
  public departments: Department[] = [];
  public count_of_department = 0;

  searchForm: FormGroup = this.fb.group({
    searchControlForm: ["", [Validators.maxLength(50), Validators.pattern("[A-Za-zА-Яа-яЁёІіЇїЄє ]*")]],
  });

  tableForm: FormGroup = this.fb.group({
    departmentsForm: this.fb.array([])
  });

  hidden_departments = false;
  departments_loading = false;
  badConnection = false;
  save_loading = false;
  del_loading = false;

  // "true" - ASC,
  // "false" - DESC
  sorting_order = true;

  search_value = "";

  constructor(private departmentService: DepartmentService,
              private global: GlobalService,
              private toastMessageService: ToastMessageService,
              private fb: FormBuilder) {
  }

  ngOnInit() {
    this.global.changeNavbar("department");
    this.departments_loading = true;
    this.getDepartments();

    this.searchForm.get("searchControlForm").valueChanges
      .subscribe(search_value => {
        this.search_value = search_value;
      });

    this.tableForm.valueChanges.pipe(debounceTime(600)).subscribe(() => {
      this.count_of_department = (<Department[]>this.tableForm.get("departmentsForm").value)
        .filter((department: Department) => {
          return department.isChanged;
        }).length;
    });
  }

  getDepartments() {
    this.departments_loading = true;
    this.departmentService.getDepartments().toPromise().then((departments: Department[]) => {
      if (departments) {
        this.departments = departments;
      } else {
        this.departments = [];
        this.departments.push(new Department());
      }
      const departmentsForm = this.updateFormGroups(this.departments);
      this.tableForm.setControl("departmentsForm", departmentsForm);
      this.departments_loading = false;
      this.badConnection = false;
      document.getElementById("search").focus();
    }).catch((err: HttpErrorResponse) => {
      this.departments_loading = true;
      this.badConnection = true;
      this.toastMessageService.inform("Сервер недоступний!",
        "Спробуйте пізніше !" + "<br>" + err.error + "<br>" + err.message, "error", 10000);
      setTimeout(() => {
        this.getDepartments();
      }, 15000);
    });
  }

  onAdd() {
    const department = new Department();
    department.departmentId = 0;
    department.disable = false;
    department.isChanged = false;
    if ((<FormArray>this.tableForm.get("departmentsForm")).controls[0].valid) {
      (<FormArray>this.tableForm.get("departmentsForm")).insert(0, this.createFormGroup(department));
    }
    setTimeout(() => {
      document.getElementById("name").focus();
    }, 400);
    window.scroll(0, 0);
  }

  onSave() {
    this.save_loading = true;
    const control = (<FormArray>this.tableForm.get("departmentsForm")).controls.find((abstractControl: AbstractControl) => {
      return abstractControl.get("isChanged").value;
    });

    if (control) {
      if (control.valid) {
        const edited_department: Department = control.value;
        if (edited_department.departmentId > 0) {
          this.departmentService.editDepartment(edited_department).toPromise().then((department: Department) => {
            control.get("isChanged").setValue(false);
            this.success_saving(department);
          }).catch((err: HttpErrorResponse) => {
              this.error_saving(err);
            }
          );
        } else {
          this.departmentService.addDepartment(edited_department).toPromise().then((department: Department) => {
            control.get("isChanged").setValue(false);
            this.success_saving(department);
          }).catch((err: HttpErrorResponse) => {
              this.error_saving(err);
            }
          );
        }
      } else {
        this.toastMessageService.inform("Помилка !", "Ви ввели некоректні дані !"
          + "<br>"
          + control.value.name, "error");
        this.save_loading = false;
      }
    } else {
      this.save_loading = false;
      this.onRefresh();
    }
  }

  private success_saving(department?: Department) {
    this.toastMessageService.inform("Збережено !", "Філія успішно збережена !", "success");
    this.onSave();
  }

  private error_saving(err: HttpErrorResponse) {
    this.save_loading = false;
    if (err.status === 422) {
      this.toastMessageService.inform("Помилка при збережені! <br> Не відповідає критеріям !",
        err.error, "error");
    } else if (err.status === 404) {
      this.toastMessageService.inform("Помилка при збережені!",
        err.error + "<br> Обновіть сторінку та спробуйте знову.", "error");
    } else if (err.status === 409) {
      this.toastMessageService.inform("Помилка при збережені! <br> Конфлікт в базі даних !",
        err.error + "<br> Обновіть сторінку та спробуйте знову. <br> Можливо філія існує серед прихованих.", "error");
    } else {
      this.toastMessageService.inform("Помилка при збережені!",
        err.error + "<br>" + "HTTP status: " + err.status, "error");
    }
  }

  onDelete() {
    this.del_loading = true;
    const control = (<FormArray>this.tableForm.get("departmentsForm")).controls.find((abstractControl: AbstractControl) => {
      return abstractControl.get("isChanged").value && abstractControl.get("departmentId").value > 0;
    });

    if (control) {
      const department_for_del: Department = control.value;
      if (department_for_del.departmentId > 0) {
        this.departmentService.deleteDepartment(department_for_del.departmentId).toPromise().then(() => {
          control.get("isChanged").setValue(false);
          this.success_deleting();
        }).catch((err: HttpErrorResponse) => {
          this.error_deleting(err);
        });
      } else {
        control.get("isChanged").setValue(false);
        this.onDelete();
      }
    } else {
      this.del_loading = false;
      this.onRefresh();
    }
  }


  private success_deleting(department?: Department) {
    this.toastMessageService.inform("Видалено !", "Філія успішно видалена !", "success");
    this.onDelete();
  }

  private error_deleting(err: HttpErrorResponse) {
    this.del_loading = false;
    if (err.status === 409) {
      this.toastMessageService.inform("Помилка при видалені!", "Можливо філія має операційні дати! <br>" +
        " Спочатку видаліть всі дати з цією філією ! <br> Або приховайте її.", "error");
    } else {
      this.toastMessageService.inform("Помилка при видалені!",
        err.error + "<br>" + "HTTP status: " + err.status, "error");
    }
  }

  onRefresh() {
    // this.departments_loading = true;
    this.hidden_departments = false;
    this.sorting_order = true;
    this.getDepartments();
    this.searchForm.get("searchControlForm").setValue("");
  }

  onCancel() {
    this.searchForm.get("searchControlForm").setValue("");
  }

  private updateFormGroups(departments: Department[]): FormArray {
    this.departments_loading = true;
    const departmentsForm = this.fb.array([]);
    departments.forEach((department: Department) => {
      departmentsForm.push(this.createFormGroup(department));
    });
    departmentsForm.controls.sort(this.compareForms);
    this.departments_loading = false;
    return departmentsForm;
  }

  private createFormGroup(department: Department): FormGroup {
    return this.fb.group({
      departmentId: [department.departmentId],
      name: [department.name, [Validators.required, Validators.maxLength(50), Validators.pattern("[A-Za-zА-Яа-яЁёІіЇїЄє+-. ]*")]],
      disable: [department.disable],
      isChanged: [false],
    });
  }

  change_sorting() {
    this.sorting_order = !this.sorting_order;
    (<FormArray>this.tableForm.get("departmentsForm")).controls.sort(this.compareForms);
  }

  compareForms = (form1, form2) => {
    if (form1.value.departmentId === 0) {
      return -1;
    } else if (form2.value.departmentId === 0) {
      return -1;
    } else if (form1.value.name && form2.value.name) {
      return this.sorting_order ? form1.value.name.localeCompare(form2.value.name) : form1.value.name.localeCompare(form2.value.name) * -1;
    }
  };

  test() {
    console.log(this.tableForm);
  }
}
