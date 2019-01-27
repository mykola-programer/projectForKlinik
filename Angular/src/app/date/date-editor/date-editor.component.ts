import {Component, Injectable, OnInit} from "@angular/core";
import {NgbCalendar, NgbDatepickerConfig, NgbDatepickerI18n, NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {VisitDateService} from "../../service/visit-date.service";
import {Router} from "@angular/router";
import {VisitDate} from "../../backend_types/visit-date";
import {NavbarService} from "../../service/navbar.service";
import {NgbDate} from "@ng-bootstrap/ng-bootstrap/datepicker/ngb-date";
import {HttpErrorResponse} from "@angular/common/http";
import {ToastMessageService} from "../../service/toast-message.service";

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
export class DateEditorComponent implements OnInit {
  minDate: NgbDate = NgbDate.from(this.calendar.getToday());
  maxDate: NgbDate = new NgbDate(this.calendar.getToday().year + 5, 12, 31);
  dates: NgbDate[] = [];
  visitDates: VisitDate[] = [];
  selectedDates: NgbDate[] = [];
  loading_save = false;
  loading_del = false;
  loading_dates = false;

  constructor(private router: Router,
              private config: NgbDatepickerConfig,
              private calendar: NgbCalendar,
              private dateService: VisitDateService,
              private serviceNavbar: NavbarService,
              private toastMessageService: ToastMessageService,
  ) {

    this.config.outsideDays = "hidden";
    this.config.displayMonths = 2;
    this.config.navigation = "select";
    this.config.showWeekNumbers = false;
    this.config.firstDayOfWeek = 1;
    this.config.markDisabled = (date: NgbDate) => this.isWeekend(date);
  }

  ngOnInit(): void {
    this.serviceNavbar.change("date");
    this.getDates();
  }

  onSelect(date: NgbDateStruct) {
    const ngbDate = NgbDate.from(date);
    if (!this.isWeekend(ngbDate) && ngbDate.after(this.minDate) && ngbDate.before(this.maxDate)) {
      const index_date: number = this.indexOf(ngbDate, this.selectedDates);
      if (index_date === -1) {
        this.selectedDates.push(ngbDate);
      } else {
        this.selectedDates.splice(index_date, 1);
      }

      this.selectedDates.sort((date1, date2) => {
        if (date1.equals(date2)) {
          return 0;
        }
        if (date1.before(date2)) {
          return -1;
        }
        if (date1.after(date2)) {
          return 1;
        }
      });
    }
  }

  onSave() {
    this.loading_save = true;
    const visitDates: VisitDate[] = [];

    this.selectedDates.forEach((value: NgbDate) => {
      if (this.indexOf(value, this.dates) === -1) {
        const visit_date: VisitDate = new VisitDate();
        visit_date.visitDateId = 0;
        visit_date.date = [value.year, value.month, value.day];
        visit_date.disable = false;
        visitDates.push(visit_date);
      }
    });

    if (visitDates.length) {
      this.dateService.addVisitDates(visitDates).toPromise().then(() => {
        this.toastMessageService.inform("Збережено !", "Дати успішно збережені !", "success");
        this.getDates();
        this.loading_save = false;
      }).catch((err: HttpErrorResponse) => {
        this.loading_save = false;
        if (err.status === 422) {
        }
        this.toastMessageService.inform("Помилка при збережені!", err.error, "error");
      });
    } else {
      this.loading_save = false;
      this.toastMessageService.inform("Виберіть хоча б один запис!", "", "info");
    }
  }

  onRefresh() {
    this.getDates();
  }

  onDelete() {
    this.loading_del = true;
    const ids: number[] = [];
    for (let i = 0; i < this.visitDates.length; i++) {
      const ngbDate: NgbDate = new NgbDate(this.visitDates[i].date[0], this.visitDates[i].date[1], this.visitDates[i].date[2]);
      if (this.indexOf(ngbDate, this.selectedDates) !== -1) {
        ids.push(this.visitDates[i].visitDateId);
      }
    }
    if (ids.length) {
      this.dateService.removeVisitDates(ids).toPromise().then((success: boolean) => {
        if (success) {
          this.toastMessageService.inform("Видалено !", "Дати успішно видалені !", "success");
          this.getDates();
          this.loading_del = false;
        }
      }).catch((err: HttpErrorResponse) => {
        this.loading_del = false;
        if (err.status === 400) {
        }
        this.toastMessageService.inform("Помилка при видалені!", err.error, "error");
      });
    } else if (this.selectedDates.length) {
      this.toastMessageService.inform("Ці дати не існували в базі даних!", "", "info");
      this.loading_del = false;
      this.getDates();
    } else {
      this.getDates();
      this.toastMessageService.inform("Виберіть хоча б один запис!", "", "info");
    }
  }

  onCancel() {
    this.selectedDates = [];
    this.getDates();
  }

  // This is selected dates
  isSelected(date: NgbDate): boolean {
    return this.indexOf(date, this.selectedDates) !== -1;
  }

  isPresented(date: NgbDate): boolean {
    return this.indexOf(date, this.dates) !== -1;
  }

  private indexOf(date: NgbDate, dates: NgbDate[]): number {
    for (let i = 0; i < dates.length; i++) {
      if (NgbDate.from(date).equals(dates[i])) {
        return i;
      }
    }
    return -1;
  }

  private isWeekend(date: NgbDate): boolean {
    const d = new Date(date.year, date.month - 1, date.day);
    return d.getDay() === 0 || d.getDay() === 6;
  }

  private getDates(): void {
    this.loading_dates = true;
    this.dateService.getVisitDates().toPromise().then((visitDates: VisitDate[]) => {
      this.visitDates = visitDates;
      this.dates = [];
      this.selectedDates = [];
      this.visitDates.forEach((value: VisitDate) => {
        this.dates.push(new NgbDate(value.date[0], value.date[1], value.date[2]));
      });
      this.loading_dates = false;
    });
  }


}
