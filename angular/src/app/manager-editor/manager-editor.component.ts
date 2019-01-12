import {Component, OnInit} from "@angular/core";
import {NavbarService} from "../service/navbar.service";
import {ManagerService} from "../service/manager.service";
import {Manager} from "../backend_types/manager";
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";
import {ToastMessageService} from "../service/toast-message.service";
import {AbstractControl} from "@angular/forms/src/model";
import {debounceTime} from "rxjs/operators";

@Component({
  selector: "app-manager-editor",
  templateUrl: "./manager-editor.component.html",
  styleUrls: ["./manager-editor.component.css"]
})
export class ManagerEditorComponent implements OnInit {
  public managers: Manager[];
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
              private navbarService: NavbarService,
              private toastMessageService: ToastMessageService,
              private fb: FormBuilder) {
  }

  ngOnInit() {
    this.navbarService.change("manager");
    this.managers_loading = true;
    this.getManagers();
    this.searchForm.get("searchControlForm").valueChanges
      .pipe(debounceTime(900))
      .subscribe(search_value => this.filterManagers(search_value));

    this.searchForm.get("searchControlForm").statusChanges
      .pipe(debounceTime(900))
      .subscribe(value => {
        if (value === "INVALID") {
          this.toastMessageService.inform("Некорректне значення пошуку",
            "Вводьте тільки літери та пробіли." + "<br>" +
            "Не більше 50 символів", "info");
        }
      });

  }

  getManagers() {
    this.managers_loading = true;
    this.managerService.getManagers().toPromise().then((managers: Manager[]) => {
      this.managers = managers;
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
    manager.inactive = false;
    manager.isChanged = false;
    this.searchForm.get("searchControlForm").setValue("");
    if (this.managersForm.controls[0].valid && (<Manager>this.managersForm.controls[0].value).managerId !== 0) {
      this.managersForm.insert(0, this.createFormGroup(manager));
    }
    setTimeout(() => {
      document.getElementById("surname").focus();
    }, 1000);
    window.scroll(0, 0);
  }

  getSelectedElementCount(): number {
    // @ts-ignore
    return (this.tableForm.get("managersForm").controls).filter((control: AbstractControl) => {
      return control.valid && (<Manager>control.value).isChanged;
    }).length;
  }

  onSave() {
    this.save_loading = true;
    const edited_manager: Manager = (<Manager[]>this.tableForm.get("managersForm").value).find((manager: Manager) => {
      return manager.isChanged;
    });
    if (edited_manager && edited_manager.managerId > 0) {
      this.managerService.editManager(edited_manager).toPromise().then(() => {
        this.toastMessageService.inform("Збережено !", "Менеджер успішно збережений !", "success");
        this.getManagers();
        this.save_loading = false;
      }).catch((err: HttpErrorResponse) => {
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
      );
    } else if (edited_manager && edited_manager.managerId === 0) {
      this.managerService.addManager(edited_manager).toPromise().then(() => {
        this.toastMessageService.inform("Збережено !", "Менеджер успішно збережений !", "success");
        this.getManagers();
        this.save_loading = false;
      }).catch((err: HttpErrorResponse) => {
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
      );
    } else {
      this.save_loading = false;
      this.toastMessageService.inform("Виберіть хоча б один запис!", "", "info");
    }
  }

  onDelete() {
    this.del_loading = true;
    const manager_for_del: Manager = (<Manager[]>this.tableForm.get("managersForm").value).find((manager: Manager) => {
      return manager.isChanged;
    });
    if (manager_for_del.managerId > 0) {
      this.managerService.deleteManager(manager_for_del.managerId).toPromise().then((success: boolean) => {
        if (success) {
          this.toastMessageService.inform("Видалено !", "Менеджер успішно видалений !", "success");
          this.del_loading = false;
          this.getManagers();
        }
      }).catch((err: HttpErrorResponse) => {
        this.del_loading = false;
        if (err.status === 409) {
          this.toastMessageService.inform("Помилка при видалені!", "Менеджер має активні візити! <br>" +
            " Спочатку видаліть візити цього менеджера ! <br> Або приховайте його.", "error");
        } else {
          this.toastMessageService.inform("Помилка при видалені!",
            err.error + "<br>" + "HTTP status: " + err.status, "error");
        }
      });
    } else {
      this.del_loading = false;
      this.onRefresh();
    }
  }

  onRefresh() {
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
      inactive: [manager.inactive],
      isChanged: [false],
    });
  }

  filterManagers(search_value: string) {
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

  determineOrder() {
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
  }

  // test(value?: any) {
    // console.log(value);
    // console.log(this.sorting_order);
    // console.log(this.searchForm);
    // console.log(this.tableForm.statusChanges);
    // console.log((<FormArray>this.tableForm.controls.managersForm).controls);
    // (this.managersForm.value).forEach(value1 => console.log(value1));
  // }
}


