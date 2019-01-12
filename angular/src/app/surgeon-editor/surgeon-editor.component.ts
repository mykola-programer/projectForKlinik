import {Component, OnInit} from "@angular/core";
import {Surgeon} from "../backend_types/surgeon";
import {SurgeonService} from "../service/surgeon.service";
import {NavbarService} from "../service/navbar.service";
import {HttpErrorResponse} from "@angular/common/http";
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ToastMessageService} from "../service/toast-message.service";
import {debounceTime} from "rxjs/operators";
import {AbstractControl} from "@angular/forms/src/model";

@Component({
  selector: "app-surgeon-editor",
  templateUrl: "./surgeon-editor.component.html",
  styleUrls: ["./surgeon-editor.component.css"]
})
export class SurgeonEditorComponent implements OnInit {
  public surgeons: Surgeon[];
  public genders: string[] = ["Ч", "Ж"];

  searchForm: FormGroup = this.fb.group({
    searchControlForm: ["", [Validators.maxLength(50), Validators.pattern("[A-Za-zА-Яа-яЁёІіЇїЄє ]*")]],
  });
  surgeonsForm: FormArray = this.fb.array([]);
  tableForm: FormGroup = this.fb.group({
    surgeonsForm: this.surgeonsForm
  });

  hidden_surgeons = false;
  surgeons_loading = false;
  save_loading = false;
  del_loading = false;

  // "true" - ASC,
  // "false" - DESC
  sorting_order = true;

  constructor(private surgeonService: SurgeonService,
              private navbarService: NavbarService,
              private toastMessageService: ToastMessageService,
              private fb: FormBuilder) {
  }

  ngOnInit() {
    this.navbarService.change("surgeon");
    this.surgeons_loading = true;
    this.getSurgeons();
    this.searchForm.get("searchControlForm").valueChanges
      .pipe(debounceTime(900))
      .subscribe(search_value => this.filterSurgeons(search_value));

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

  getSurgeons() {
    this.surgeons_loading = true;
    this.surgeonService.getSurgeons().toPromise().then((surgeons: Surgeon[]) => {
      this.surgeons = surgeons;
      this.surgeonsForm = this.updateFormGroups(this.surgeons);
      this.tableForm.setControl("surgeonsForm", this.surgeonsForm);
      this.surgeons_loading = false;
      setTimeout(() => {
        document.getElementById("search").focus();
      });
    }).catch((err: HttpErrorResponse) => {
      this.surgeons_loading = true;
      this.toastMessageService.inform("Сервер недоступний!",
        "Спробуйте пізніше !" + "<br>" + err.error + "<br>" + err.message, "error", 10000);
      setTimeout(() => {
        this.getSurgeons();
      }, 15000);
    });
  }

  onAdd() {
    const surgeon = new Surgeon();
    surgeon.surgeonId = 0;
    surgeon.inactive = false;
    surgeon.isChanged = false;
    this.searchForm.get("searchControlForm").setValue("");
    if (this.surgeonsForm.controls[0].valid && (<Surgeon>this.surgeonsForm.controls[0].value).surgeonId !== 0) {
      this.surgeonsForm.insert(0, this.createFormGroup(surgeon));
    }
    setTimeout(() => {
      document.getElementById("surname").focus();
    }, 1000);
    window.scroll(0, 0);
  }

  getSelectedElementCount(): number {
    // @ts-ignore
    return (this.tableForm.get("surgeonsForm").controls).filter((control: AbstractControl) => {
      return control.valid && (<Surgeon>control.value).isChanged;
    }).length;
  }

  onSave() {
    this.save_loading = true;
    const edited_surgeon: Surgeon = (<Surgeon[]>this.tableForm.get("surgeonsForm").value).find((surgeon: Surgeon) => {
      return surgeon.isChanged;
    });
    if (edited_surgeon && edited_surgeon.surgeonId > 0) {
      this.surgeonService.editSurgeon(edited_surgeon).toPromise().then(() => {
        this.toastMessageService.inform("Збережено !", "Хірург успішно збережений !", "success");
        this.getSurgeons();
        this.save_loading = false;
      }).catch((err: HttpErrorResponse) => {
          this.save_loading = false;
          if (err.status === 422) {
            this.toastMessageService.inform("Помилка при збережені! <br> Хірург не відповідає критеріям !",
              err.error, "error");
          } else if (err.status === 404) {
            this.toastMessageService.inform("Помилка при збережені!",
              err.error + "<br> Обновіть сторінку та спробуйте знову.", "error");
          } else if (err.status === 409) {
            this.toastMessageService.inform("Помилка при збережені! <br> Конфлікт в базі даних !",
              err.error + "<br> Обновіть сторінку та спробуйте знову. <br> Можливо ваш хірург існує серед прихованих.", "error");
          } else {
            this.toastMessageService.inform("Помилка при збережені!",
              err.error + "<br>" + "HTTP status: " + err.status, "error");
          }
        }
      );
    } else if (edited_surgeon && edited_surgeon.surgeonId === 0) {
      this.surgeonService.addSurgeon(edited_surgeon).toPromise().then(() => {
        this.toastMessageService.inform("Збережено !", "Хірург успішно збережений !", "success");
        this.getSurgeons();
        this.save_loading = false;
      }).catch((err: HttpErrorResponse) => {
          this.save_loading = false;
          if (err.status === 422) {
            this.toastMessageService.inform("Помилка при збережені! <br> Хірург не відповідає критеріям !",
              err.error, "error");
          } else if (err.status === 404) {
            this.toastMessageService.inform("Помилка при збережені!",
              err.error + "<br> Обновіть сторінку та спробуйте знову.", "error");
          } else if (err.status === 409) {
            this.toastMessageService.inform("Помилка при збережені! <br> Конфлікт в базі даних !",
              err.error + "<br> Обновіть сторінку та спробуйте знову. <br> Можливо ваш хірург існує серед прихованих.", "error");
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
    const surgeon_for_del: Surgeon = (<Surgeon[]>this.tableForm.get("surgeonsForm").value).find((surgeon: Surgeon) => {
      return surgeon.isChanged;
    });
    if (surgeon_for_del.surgeonId > 0) {
      this.surgeonService.deleteSurgeon(surgeon_for_del.surgeonId).toPromise().then((success: boolean) => {
        if (success) {
          this.toastMessageService.inform("Видалено !", "Хірург успішно видалений !", "success");
          this.del_loading = false;
          this.getSurgeons();
        }
      }).catch((err: HttpErrorResponse) => {
        this.del_loading = false;
        if (err.status === 409) {
          this.toastMessageService.inform("Помилка при видалені!", "Хірург має активні візити! <br>" +
            " Спочатку видаліть операції цього хірурга ! <br> Або приховайте його.", "error");
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
    this.hidden_surgeons = false;
    this.sorting_order = true;
    this.getSurgeons();
    this.searchForm.get("searchControlForm").setValue("");
  }

  onCancel() {
    this.searchForm.get("searchControlForm").setValue("");
    this.surgeonsForm = this.updateFormGroups(this.surgeons);
    this.tableForm.setControl("surgeonsForm", this.surgeonsForm);
  }

  private updateFormGroups(surgeons: Surgeon[]): FormArray {
    this.surgeons_loading = true;
    this.sorting_order ? surgeons.sort(this.compareSurgeons) : surgeons.sort(this.compareSurgeons).reverse();
    const surgeonsForm = this.fb.array([]);
    surgeons.forEach((surgeon: Surgeon) => {
      surgeonsForm.push(this.createFormGroup(surgeon));
    });
    this.surgeons_loading = false;
    return surgeonsForm;
  }

  private createFormGroup(surgeon: Surgeon): FormGroup {
    return this.fb.group({
      surgeonId: [surgeon.surgeonId],
      surname: [surgeon.surname, [Validators.required, Validators.maxLength(50), Validators.pattern("[A-Za-zА-Яа-яЁёІіЇїЄє ]*")]],
      firstName: [surgeon.firstName, [Validators.required, Validators.maxLength(50), Validators.pattern("[A-Za-zА-Яа-яЁёІіЇїЄє ]*")]],
      secondName: [surgeon.secondName, [Validators.required, Validators.maxLength(50), Validators.pattern("[A-Za-zА-Яа-яЁёІіЇїЄє ]*")]],
      city: [surgeon.city, [Validators.required, Validators.maxLength(50), Validators.pattern("[A-Za-zА-Яа-яЁёІіЇїЄє ]*")]],
      sex: [surgeon.sex, [Validators.required, Validators.maxLength(1), Validators.pattern("^[ЧЖ]*$")]],
      inactive: [surgeon.inactive],
      isChanged: [false],
    });
  }

  filterSurgeons(search_value: string) {
    if (search_value) {
      const filterValue: string[] = search_value.toLowerCase().split(" ");
      const filteredSurgeons = this.surgeons.filter((surgeon: Surgeon) => {
        switch (filterValue.length) {
          case 0:
            return true;
          case 1:
            if (surgeon.surname) {
              return (surgeon.surname.toLowerCase().indexOf(filterValue[0]) === 0);
            } else {
              return true;
            }
          case 2:
            if (surgeon.surname && surgeon.firstName) {
              return (surgeon.surname.toLowerCase().indexOf(filterValue[0]) === 0
                && surgeon.firstName.toLowerCase().indexOf(filterValue[1]) === 0);
            } else {
              return true;
            }
          default:
            if (surgeon.surname && surgeon.firstName && surgeon.secondName) {
              return (surgeon.surname.toLowerCase().indexOf(filterValue[0]) === 0
                && surgeon.firstName.toLowerCase().indexOf(filterValue[1]) === 0
                && surgeon.secondName.toLowerCase().indexOf(filterValue[2]) === 0);
            } else {
              return true;
            }
        }
      });
      this.tableForm.setControl("surgeonsForm", this.updateFormGroups(filteredSurgeons));
    } else {
      this.tableForm.setControl("surgeonsForm", this.surgeonsForm);
    }
  }

  determineOrder() {
    this.sorting_order = !this.sorting_order;
    this.tableForm.setControl("surgeonsForm", this.updateFormGroups(<Surgeon[]>this.tableForm.get("surgeonsForm").value));
    this.surgeonsForm = this.updateFormGroups(<Surgeon[]>this.surgeonsForm.value);

  }

  private compareSurgeons = (surgeon1, surgeon2) => {
    if (surgeon1.surgeonId === 0) {
      return !this.sorting_order;
    } else if (surgeon2.surgeonId === 0) {
      return this.sorting_order;
    } else if (surgeon1.surname && surgeon2.surname && surgeon1.surname.localeCompare(surgeon2.surname) !== 0) {
      return surgeon1.surname.localeCompare(surgeon2.surname);
    } else if (surgeon1.firstName && surgeon2.firstName && surgeon1.firstName.localeCompare(surgeon2.firstName) !== 0) {
      return surgeon1.firstName.localeCompare(surgeon2.firstName);
    } else if (surgeon1.secondName && surgeon2.secondName && surgeon1.secondName.localeCompare(surgeon2.secondName) !== 0) {
      return surgeon1.secondName.localeCompare(surgeon2.secondName);
    }
  };

}
