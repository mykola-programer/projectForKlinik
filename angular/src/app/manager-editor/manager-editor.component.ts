import {Component, OnInit} from "@angular/core";
import {NavbarService} from "../service/navbar.service";
import {ManagerService} from "../service/manager.service";
import {Manager} from "../backend_types/manager";
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";
import {ToastMessageService} from "../service/toast-message.service";
import {AbstractControl} from "@angular/forms/src/model";

@Component({
  selector: "app-manager-editor",
  templateUrl: "./manager-editor.component.html",
  styleUrls: ["./manager-editor.component.css"]
})
export class ManagerEditorComponent implements OnInit {
  private managers: Manager[];
  public genders: string[] = ["Ч", "Ж"];

  searchForm: FormGroup = this.fb.group({
    searchControlForm: ["", [Validators.maxLength(50), Validators.pattern("[A-Za-zА-Яа-яЁёІіЇїЄє ]*")]],
  });
  managersForm: FormArray = this.fb.array([]);
  tableForm: FormGroup = this.fb.group({
    managersForm: this.managersForm
  });

  loading_managers = false;
  loading_save = false;
  loading_del = false;

  // "1" - ASC,
  // "-1" - DESC
  private sorting_order = 1;

  constructor(private managerService: ManagerService,
              private navbarService: NavbarService,
              private toastMessageService: ToastMessageService,
              private fb: FormBuilder) {
  }

  ngOnInit() {
    this.loading_managers = true;
    this.getManagers();
    this.navbarService.change("manager");
  }

  getManagers() {
    this.loading_managers = true;
    this.managerService.getActiveManagers().toPromise().then((managers: Manager[]) => {
      this.managers = managers;
      this.sortManagers(this.managers);
      this.updateFormGroups(this.managers);
    }).catch((err: HttpErrorResponse) => {
      this.loading_managers = true;
      this.toastMessageService.inform("Сервер недоступний!",
        "Спробуйте пізніше !" + "<br>" + err.error + "<br>" + err.message, "error", 8000);
      setTimeout(() => {
        this.getManagers();
      }, 10000);
    });
  }

  onAdd() {
    const manager = new Manager();
    manager.managerId = 0;
    manager.inactive = false;
    manager.isChanged = false;
    this.managersForm.insert(0, this.createFormGroup(manager));
    window.scroll(0, 0);
  }

  getSelectedElementCount(): number {
    return (this.managersForm.controls).filter((control: AbstractControl) => {
      return control.valid && (<Manager>control.value).isChanged;
    }).length;
  }

  onSave() {
    this.loading_save = true;
    const edited_managers: Manager[] = this.managersForm.value.filter((manager: Manager) => {
      return manager.isChanged;
    });
    if (edited_managers.length) {
      this.managerService.putManagers(edited_managers).toPromise().then(() => {
        this.toastMessageService.inform("Збережено !", "Менеджери успішно збережені !", "success");
        this.getManagers();
        this.loading_save = false;
      }).catch((err: HttpErrorResponse) => {
          this.loading_save = false;
          if (err.status === 422) {
            this.toastMessageService.inform("Помилка при збережені! <br> Менеджер не відповідає критеріям !",
              err.error, "error");
          } else if (err.status === 404) {
            this.toastMessageService.inform("Помилка при збережені!",
              err.error + "<br> Обновіть сторінку та спробуйте знову.", "error");
          } else if (err.status === 409) {
            this.toastMessageService.inform("Помилка при збережені! <br> Конфлікт в базі даних !",
              err.error + "<br> Обновіть сторінку та спробуйте знову.", "error");
          } else {
            this.toastMessageService.inform("Помилка при збережені!",
              err.error + "<br>" + "HTTP status: " + err.status, "error");
          }
          // TODO Create dialog window for activate meneger

        }
      );
    } else {
      this.loading_save = false;
      this.toastMessageService.inform("Виберіть хоча б один запис!", "", "info");
    }
  }

  onDelete() {
    this.loading_del = true;
    const manager_for_del: Manager = (<Manager[]>this.managersForm.value).find((manager: Manager) => {
      return manager.isChanged;
    });
    if (manager_for_del.managerId > 0) {
      this.managerService.deleteManager(manager_for_del.managerId).toPromise().then((success: boolean) => {
        if (success) {
          this.toastMessageService.inform("Видалено !", "Менеджер успішно видалений !", "success");
          this.loading_del = false;
          this.getManagers();
        }
      }).catch((err: HttpErrorResponse) => {
        this.loading_del = false;
        if (err.status === 409) {
          this.toastMessageService.inform("Помилка при видалені!", "Менеджер має активні візити! <br>" +
            " Спочатку видаліть візити цього менеджера !", "error");
        } else {
          this.toastMessageService.inform("Помилка при видалені!",
            err.error + "<br>" + "HTTP status: " + err.status, "error");
        }
        // TODO Create dialog window for disactivate meneger
      });
    } else {
      this.loading_del = false;
      this.onRefresh();
    }
  }

  onRefresh() {
    this.getManagers();
  }

  onCancel() {
    this.updateFormGroups(this.managers);
  }

  private updateFormGroups(managers: Manager[]) {
    this.loading_managers = true;
    this.managersForm = this.fb.array([]);
    managers.forEach((manager: Manager) => {
      this.managersForm.push(this.createFormGroup(manager));
    });
    this.tableForm.setControl("managersForm", this.managersForm);
    this.loading_managers = false;
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

  private sortManagers(managers: Manager[]) {
    managers.sort((manager1, manager2) => {
      if (manager1.surname && manager2.surname && manager1.surname.localeCompare(manager2.surname) !== 0) {
        return manager1.surname.localeCompare(manager2.surname) * this.sorting_order;
      } else if (manager1.firstName && manager2.firstName && manager1.firstName.localeCompare(manager2.firstName) !== 0) {
        return manager1.firstName.localeCompare(manager2.firstName) * this.sorting_order;
      } else if (manager1.secondName && manager2.secondName && manager1.secondName.localeCompare(manager2.secondName) !== 0) {
        return manager1.secondName.localeCompare(manager2.secondName) * this.sorting_order;
      }
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
      this.updateFormGroups(filteredManagers);
    } else {
      this.updateFormGroups(this.managers);
    }
  }


 /* test(value?: any) {
    // console.log(value);
    console.log(this.searchForm);
    // console.log(this.tableForm.statusChanges);
    // console.log((<FormArray>this.tableForm.controls.managersForm).controls);
    // (this.managersForm.value).forEach(value1 => console.log(value1));
  }*/
}
