import {Component, OnInit} from "@angular/core";
import {ManagerService} from "../service/manager.service";
import {Manager} from "../backend_types/manager";
import {AbstractControl, FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";
import {ToastMessageService} from "../service/toast-message.service";
import {debounceTime} from "rxjs/operators";
import {GlobalService} from "../service/global.service";

@Component({
  selector: "app-manager-editor",
  templateUrl: "./manager-editor.component.html",
  styleUrls: ["./manager-editor.component.css"]
})
export class ManagerEditorComponent implements OnInit {
  public managers: Manager[] = [];
  public count_of_managers = 0;
  public genders: string[] = ["Ч", "Ж"];

  searchForm: FormGroup = this.fb.group({
    searchControlForm: ["", [Validators.maxLength(50), Validators.pattern("[A-Za-zА-Яа-яЁёІіЇїЄє ]*")]],
  });

  managersForm: FormArray = this.fb.array([]);
  tableForm: FormGroup = this.fb.group({
    managersForm: this.managersForm
  });

  hidden_managers = false;
  managers_loading = false;
  save_loading = false;
  del_loading = false;

  // "true" - ASC,
  // "false" - DESC
  sorting_order = true;

  constructor(private managerService: ManagerService,
              private globalService: GlobalService,
              private toastMessageService: ToastMessageService,
              private fb: FormBuilder) {
  }

  ngOnInit() {
    this.globalService.changeNavbar("manager");
    this.managers_loading = true;
    this.getManagers();
    this.searchForm.get("searchControlForm").valueChanges
      .pipe(debounceTime(900))
      .subscribe(search_value => this.filterManagers(search_value));

    this.searchForm.get("searchControlForm").statusChanges
      .pipe(debounceTime(900))
      .subscribe(value => {
        if (value === "INVALID") {
          this.toastMessageService.inform("Некорректне значення для пошуку",
            "Вводьте тільки літери та пробіли." + "<br>" +
            "Не більше 50 символів", "info");
        }
      });
    this.tableForm.valueChanges.pipe(debounceTime(600)).subscribe(() => {
      this.count_of_managers = (<Manager[]>this.tableForm.get("managersForm").value).filter((manager: Manager) => {
        return manager.isChanged;
      }).length;
    });
  }

  getManagers() {
    this.managers_loading = true;
    this.managerService.getManagers().toPromise().then((managers: Manager[]) => {
      if (managers) {
        this.managers = managers;
      } else {
        this.managers = [];
        this.managers.push(new Manager());
      }
      this.managersForm = this.updateFormGroups(this.managers);
      this.tableForm.setControl("managersForm", this.managersForm);
      this.managers_loading = false;
      setTimeout(() => {
        document.getElementById("search").focus();
      });
    }).catch((err: HttpErrorResponse) => {
      this.managers_loading = true;
      this.toastMessageService.inform("Сервер недоступний!",
        "Спробуйте пізніше !" + "<br>" + err.error + "<br>" + err.message, "error", 10000);
      setTimeout(() => {
        this.getManagers();
      }, 15000);
    });
  }

  onAdd() {
    const manager = new Manager();
    manager.managerId = 0;
    manager.disable = false;
    manager.isChanged = false;
    this.searchForm.get("searchControlForm").setValue("");
    if (this.managersForm.controls[0].valid) {
      this.managersForm.insert(0, this.createFormGroup(manager));
    }
    setTimeout(() => {
      document.getElementById("surname").focus();
    }, 1000);
    window.scroll(0, 0);
  }

  onSave() {
    this.save_loading = true;
    // @ts-ignore
    const control = (<AbstractControl>(this.tableForm.get("managersForm").controls).find((abstractControl: AbstractControl) => {
      return abstractControl.get("isChanged").value;
    }));
    if (control && control.value) {
      const edited_manager: Manager = control.value;
      if (edited_manager && edited_manager.managerId > 0) {
        this.managerService.editManager(edited_manager).toPromise().then((manager: Manager) => {
          control.get("isChanged").setValue(false);
          this.success_saving(manager);
        }).catch((err: HttpErrorResponse) => {
            this.error_saving(err);
          }
        );
      } else if (edited_manager && edited_manager.managerId === 0) {
        this.managerService.addManager(edited_manager).toPromise().then((manager: Manager) => {
          control.get("isChanged").setValue(false);
          this.success_saving(manager);
        }).catch((err: HttpErrorResponse) => {
            this.error_saving(err);
          }
        );
      }
    } else {
      this.save_loading = false;
      this.onRefresh();
    }
  }

  private success_saving(manager?: Manager) {
    this.toastMessageService.inform("Збережено !", "Менеджер успішно збережений !", "success");
    this.onSave();
  }

  private error_saving(err: HttpErrorResponse) {
    this.save_loading = false;
    if (err.status === 422) {
      this.toastMessageService.inform("Помилка при збережені! <br> Менеджер не відповідає критеріям !",
        err.error, "error");
    } else if (err.status === 404) {
      this.toastMessageService.inform("Помилка при збережені!",
        err.error + "<br> Обновіть сторінку та спробуйте знову.", "error");
    } else if (err.status === 409) {
      this.toastMessageService.inform("Помилка при збережені! <br> Конфлікт в базі даних !",
        err.error + "<br> Обновіть сторінку та спробуйте знову. <br> Можливо ваш менеджер існує серед прихованих.", "error");
    } else {
      this.toastMessageService.inform("Помилка при збережені!",
        err.error + "<br>" + "HTTP status: " + err.status, "error");
    }
  }

  onDelete() {
    this.del_loading = true;
    // @ts-ignore
    const control = (<AbstractControl>(this.tableForm.get("managersForm").controls).find((abstractControl: AbstractControl) => {
      return abstractControl.get("isChanged").value;
    }));
    if (control && control.value) {
      const manager_for_del: Manager = control.value;
      if (manager_for_del.managerId > 0) {
        this.managerService.deleteManager(manager_for_del.managerId).toPromise().then(() => {
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

  private success_deleting(manager?: Manager) {
    this.toastMessageService.inform("Видалено !", "Менеджер успішно видалений !", "success");
    this.onDelete();
  }

  private error_deleting(err: HttpErrorResponse) {
    this.del_loading = false;
    if (err.status === 409) {
      this.toastMessageService.inform("Помилка при видалені!", "Менеджер має активні візити! <br>" +
        " Спочатку видаліть візити цього менеджера ! <br> Або приховайте його.", "error");
    } else {
      this.toastMessageService.inform("Помилка при видалені!",
        err.error + "<br>" + "HTTP status: " + err.status, "error");
    }
  }

  onRefresh() {
    this.managers_loading = true;
    this.hidden_managers = false;
    this.sorting_order = true;
    this.getManagers();
    this.searchForm.get("searchControlForm").setValue("");
  }

  onCancel() {
    this.searchForm.get("searchControlForm").setValue("");
    this.managersForm = this.updateFormGroups(this.managers);
    this.tableForm.setControl("managersForm", this.managersForm);
  }

  private updateFormGroups(managers: Manager[]): FormArray {
    this.managers_loading = true;
    this.sorting_order ? managers.sort(this.compareManagers) : managers.sort(this.compareManagers).reverse();
    const managersForm = this.fb.array([]);
    managers.forEach((manager: Manager) => {
      managersForm.push(this.createFormGroup(manager));
    });
    this.managers_loading = false;
    return managersForm;
  }

  private createFormGroup(manager: Manager): FormGroup {
    return this.fb.group({
      managerId: [manager.managerId],
      surname: [manager.surname, [Validators.required, Validators.maxLength(50), Validators.pattern("[A-Za-zА-Яа-яЁёІіЇїЄє ]*")]],
      firstName: [manager.firstName, [Validators.required, Validators.maxLength(50), Validators.pattern("[A-Za-zА-Яа-яЁёІіЇїЄє ]*")]],
      secondName: [manager.secondName, [Validators.required, Validators.maxLength(50), Validators.pattern("[A-Za-zА-Яа-яЁёІіЇїЄє ]*")]],
      city: [manager.city, [Validators.required, Validators.maxLength(50), Validators.pattern("[A-Za-zА-Яа-яЁёІіЇїЄє ]*")]],
      sex: [manager.sex, [Validators.required, Validators.maxLength(1), Validators.pattern("^[ЧЖ]*$")]],
      disable: [manager.disable],
      isChanged: [false],
    });
  }

  private filterManagers(search_value: string) {
    if (search_value) {
      const filterValue: string[] = search_value.toLowerCase().split(" ");
      const filteredManagers = this.managers.filter(manager => {
        switch (filterValue.length) {
          case 0:
            return true;
          case 1:
            if (manager.surname) {
              return (manager.surname.toLowerCase().indexOf(filterValue[0]) === 0);
            } else {
              return true;
            }
          case 2:
            if (manager.surname && manager.firstName) {
              return (manager.surname.toLowerCase().indexOf(filterValue[0]) === 0
                && manager.firstName.toLowerCase().indexOf(filterValue[1]) === 0);
            } else {
              return true;
            }
          default:
            if (manager.surname && manager.firstName && manager.secondName) {
              return (manager.surname.toLowerCase().indexOf(filterValue[0]) === 0
                && manager.firstName.toLowerCase().indexOf(filterValue[1]) === 0
                && manager.secondName.toLowerCase().indexOf(filterValue[2]) === 0);
            } else {
              return true;
            }
        }
      });
      this.tableForm.setControl("managersForm", this.updateFormGroups(filteredManagers));
    } else {
      this.tableForm.setControl("managersForm", this.managersForm);
    }
  }

  change_sorting() {
    this.sorting_order = !this.sorting_order;
    this.tableForm.setControl("managersForm", this.updateFormGroups(<Manager[]>this.tableForm.get("managersForm").value));
    this.managersForm = this.updateFormGroups(<Manager[]>this.managersForm.value);

  }

  private compareManagers = (manager1, manager2) => {
    if (manager1.managerId === 0) {
      return !this.sorting_order;
    } else if (manager2.managerId === 0) {
      return this.sorting_order;
    } else if (manager1.surname && manager2.surname && manager1.surname.localeCompare(manager2.surname) !== 0) {
      return manager1.surname.localeCompare(manager2.surname);
    } else if (manager1.firstName && manager2.firstName && manager1.firstName.localeCompare(manager2.firstName) !== 0) {
      return manager1.firstName.localeCompare(manager2.firstName);
    } else if (manager1.secondName && manager2.secondName && manager1.secondName.localeCompare(manager2.secondName) !== 0) {
      return manager1.secondName.localeCompare(manager2.secondName);
    }
  };
}

// test(value?: any) {
// console.log(value);
// console.log(this.sorting_order);
// console.log(this.searchForm);
// console.log(this.tableForm.get("managersForm").value);
// console.log(this.count_of_managers);
// console.log((<FormArray>this.tableForm.controls.managersForm).controls);
// (this.managersForm.value).forEach(value1 => console.log(value1));
// }
// }


