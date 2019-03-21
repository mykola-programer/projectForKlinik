import {Compiler, Component, Injectable, OnDestroy, OnInit} from "@angular/core";
import {NgbCalendar, NgbDatepickerConfig, NgbDatepickerI18n, NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {Router} from "@angular/router";
import {NavbarService} from "../../service/navbar.service";
import {NgbDate} from "@ng-bootstrap/ng-bootstrap/datepicker/ngb-date";
import {HttpErrorResponse} from "@angular/common/http";
import {ToastMessageService} from "../../service/toast-message.service";
import {DatePlan} from "../../backend_types/date-plan";
import {DatePlanService} from "../../service/date-plan.service";

const I18N_VALUES = {
  "ua": {
    weekdays: ["Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Нд"],
    months: ["Січень", "Лютий", "Березень", "Квітень", "Травень",
      "Червень", "Липень", "Серпень", "Вересень", "Жовтень", "Листопад", "Грудень"],
  }
  // other languages you would support
};

// Define a service holding the language. You probably already have one if your app is i18ned. Or you could also
// use the Angular LOCALE_ID value
@Injectable()
export class I18n {
  language = "ua";
}

// Define custom service providing the months and weekdays translations
@Injectable()
export class EditedDatepickerI18n extends NgbDatepickerI18n {

  constructor(private _i18n: I18n) {
    super();
  }

  getWeekdayShortName(weekday: number): string {
    return I18N_VALUES[this._i18n.language].weekdays[weekday - 1];
  }

  getMonthShortName(month: number): string {
    return I18N_VALUES[this._i18n.language].months[month - 1];
  }

  getMonthFullName(month: number): string {
    return this.getMonthShortName(month);
  }

  getDayAriaLabel(date: NgbDateStruct): string {
    return `${date.day}-${date.month}-${date.year}`;
  }
}

@Component({
  selector: "app-date-editor",
  templateUrl: "./date-editor.component.html",
  styleUrls: ["./date-editor.component.css"],
  providers: [I18n, {provide: NgbDatepickerI18n, useClass: EditedDatepickerI18n}] // define custom NgbDatepickerI18n provider

})
export class DateEditorComponent implements OnInit, OnDestroy {
  minDate: NgbDate = NgbDate.from(this.calendar.getToday());
  maxDate: NgbDate = new NgbDate(this.calendar.getToday().year + 5, 12, 31);
  visitDates: DatePlan[] = [];
  selectedDates: DatePlan[] = [];
  loading_save = false;
  del_loading = false;
  lock_loading = false;
  unlock_loading = false;
  dates_loading = false;

  isLoadingOFF(): boolean {
    return !(this.loading_save
      || this.del_loading
      || this.lock_loading
      || this.unlock_loading);
  };

  constructor(private router: Router,
              private config: NgbDatepickerConfig,
              private calendar: NgbCalendar,
              private dateService: DatePlanService,
              private serviceNavbar: NavbarService,
              private compiler: Compiler,
              private toastMessageService: ToastMessageService,
  ) {

    this.config.outsideDays = "hidden";
    this.config.displayMonths = 2;
    this.config.navigation = "select";
    this.config.showWeekNumbers = false;
    this.config.firstDayOfWeek = 1;
    this.config.markDisabled = (date: NgbDate) => {
      const d = new Date(date.year, date.month - 1, date.day);
      return d.getDay() === 0 || d.getDay() === 6;
    };
  }

  ngOnInit(): void {
    this.serviceNavbar.change("date");
    this.getDates();
  }

  ngOnDestroy(): void {
    this.compiler.clearCache();
  }

  onSelect(date: NgbDateStruct, disabled: boolean) {
    if (!disabled) {
      const date_index: number = this.indexOf(date, this.selectedDates);
      if (date_index !== -1) {
        this.selectedDates.splice(date_index, 1);
      } else {
        const visitDate_index = this.indexOf(date, this.visitDates);
        if (visitDate_index !== -1) {
          this.selectedDates.push(this.visitDates[visitDate_index]);
        } else {
          const visitDate: DatePlan = new DatePlan();
          visitDate.date = [date.year, date.month, date.day];
          this.selectedDates.push(visitDate);
        }
        this.selectedDates.sort((date1, date2) => {
          if (date1.date[0] !== date2.date[0]) {
            return date1.date[0] - date2.date[0];
          } else if (date1.date[1] !== date2.date[1]) {
            return date1.date[1] - date2.date[1];
          } else if (date1.date[2] !== date2.date[2]) {
            return date1.date[2] - date2.date[2];
          } else return 0;
        });
      }
    }
  }

  onSave() {
    if (this.selectedDates.length > 0) {
      this.loading_save = true;
      if (this.selectedDates[0].datePlanId === 0) {
        this.dateService.addDatePlan(this.selectedDates[0]).toPromise().then((visitDate: DatePlan) => {
          this.loading_save = false;
          this.selectedDates.splice(0, 1);
          this.success_saving(visitDate);
        }).catch((err: HttpErrorResponse) => {
          this.error_saving(err, this.selectedDates[0]);
        });
      } else if (this.selectedDates[0].datePlanId > 0) {
        this.dateService.editDatePlan(this.selectedDates[0]).toPromise().then((visitDate: DatePlan) => {
          this.loading_save = false;
          this.selectedDates.splice(0, 1);
          this.success_saving(visitDate);
        }).catch((err: HttpErrorResponse) => {
          this.error_saving(err);
        });
      }
    } else {
      this.lock_loading = false;
      this.unlock_loading = false;
      this.loading_save = false;
      this.onRefresh();
    }
  }

  private success_saving(visitDate?: DatePlan) {
    this.toastMessageService.inform("Збережено !", "Операційна дата успішно збережена !", "success");
    this.onSave();
  }

  private error_saving(err: HttpErrorResponse, visitDate?: DatePlan) {
    this.loading_save = false;
    if (err.status === 422) {
      this.toastMessageService.inform("Помилка при збережені! <br>" + this.refactorDay(visitDate) + "<br> не відповідає критеріям !",
        err.error, "error");
    } else if (err.status === 404) {
      this.toastMessageService.inform("Помилка при збережені! <br>" + this.refactorDay(visitDate),
        err.error + "<br> Обновіть сторінку та спробуйте знову.", "error");
    } else if (err.status === 409) {
      this.toastMessageService.inform("Помилка при збережені! <br>" + this.refactorDay(visitDate) + "<br> Конфлікт в базі даних !",
        err.error + "<br> Обновіть сторінку та спробуйте знову. <br> Можливо дата існує серед прихованих.", "error");
    } else {
      this.toastMessageService.inform("Помилка при збережені! <br>" + this.refactorDay(visitDate),
        err.error + "<br>" + "HTTP status: " + err.status, "error");
    }
  }

  onRefresh() {
    this.getDates();
  }

  onLock() {
    this.lock_loading = true;
    this.selectedDates.forEach(value => value.disable = true);
    this.onSave();
  }

  onUnlock() {
    this.unlock_loading = true;
    this.selectedDates.forEach(value => value.disable = false);
    this.onSave();
  }

  onDelete() {
    if (this.selectedDates.length > 0) {
      this.del_loading = true;
      if (this.selectedDates[0].datePlanId > 0) {
        this.dateService.removeDatePlan(this.selectedDates[0].datePlanId).toPromise().then(() => {
          this.selectedDates.splice(0, 1);
          this.success_deleting();
        }).catch((err: HttpErrorResponse) => {
          this.error_deleting(err, this.selectedDates[0]);
        });
      } else {
        this.selectedDates.splice(0, 1);
        this.onDelete();
      }
    } else {
      this.del_loading = false;
      this.onRefresh();
    }

  }

  private success_deleting(visitDate?: DatePlan) {
    this.toastMessageService.inform("Видалено !", "Дати успішно видалені !", "success");
    this.onDelete();
  }

  private error_deleting(err: HttpErrorResponse, visitDate?: DatePlan) {
    this.del_loading = false;
    if (err.status === 409) {
      this.toastMessageService.inform("Помилка при видалені! <br>" + this.refactorDay(visitDate), "Цього числа існують активні візити! <br>" +
        " Спочатку видаліть візити !", "error");
      this.toastMessageService.inform("Рекомендація.", "Можна заблокувати через кнопку 'Lock'", "info", 10000);
    } else {
      this.toastMessageService.inform("Помилка при видалені! <br>" + this.refactorDay(visitDate),
        err.error + "<br>" + "HTTP status: " + err.status, "error");
    }
  }

  isSelected(date: NgbDate): boolean {
    return this.indexOf(date, this.selectedDates) !== -1;
  }

  isPresented(date: NgbDate): boolean {
    const index = this.indexOf(date, this.visitDates);
    return index !== -1 && !this.visitDates[index].disable;
  }

  isDisabled(date: NgbDate): boolean {
    const index = this.indexOf(date, this.visitDates);
    return index !== -1 && this.visitDates[index].disable;
  }

  private indexOf(date: NgbDateStruct, visitDates: DatePlan[]): number {
    return visitDates.findIndex((visitDate: DatePlan) => {
      return visitDate.date[0] == date.year && visitDate.date[1] == date.month && visitDate.date[2] == date.day;
    });
  }

  private getDates(): void {
    this.dates_loading = true;
    this.selectedDates = [];
    this.dateService.getDatePlans().toPromise().then((visitDates: DatePlan[]) => {
      this.visitDates = visitDates;
      setTimeout(() => this.dates_loading = false, 400);
    }).catch((err: HttpErrorResponse) => {
      this.dates_loading = true;
      this.toastMessageService.inform("Сервер недоступний!",
        "Спробуйте пізніше !" + "<br>" + err.error + "<br>" + err.message, "error", 10000);
      setTimeout(() => {
        this.getDates();
      }, 15000);
    });
  }

  refactorDay(visitDate: DatePlan): string {
    return (visitDate.date[2] < 10 ? "0" + visitDate.date[2] : visitDate.date[2])
      + (visitDate.date[1] < 10 ? ".0" + visitDate.date[1] : "." + visitDate.date[1])
      + (visitDate.date[0] < 10 ? ".0" + visitDate.date[0] : "." + visitDate.date[0]);
  }

}
