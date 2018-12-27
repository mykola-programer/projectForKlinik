import {Component, Injectable, OnInit} from "@angular/core";
import {VisitDateService} from "../../service/visit-date.service";

import {NgbDateParserFormatter, NgbDatepickerConfig, NgbDatepickerI18n, NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {isNumber, padNumber, toInteger} from "@ng-bootstrap/ng-bootstrap/util/util";
import {Router} from "@angular/router";
import {VisitDate} from "../../backend_types/visit-date";
import {DateService} from "../../service/date.service";

const I18N_VALUES = {
  "ua": {
    weekdays: ["Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Нд"],
    months: ["Січень", "Лютий", "Березень", "Квітень", "Травень", "Червень", "Липень", "Серпень", "Вересень", "Жовтень", "Листопад", "Грудень"],
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
export class SelectedDatepickerI18n extends NgbDatepickerI18n {

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

@Injectable()
export class NgbDateCustomParserFormatter extends NgbDateParserFormatter {
  parse(value: string): NgbDateStruct {
    if (value) {
      const dateParts = value.trim().split("-");
      if (dateParts.length === 1 && isNumber(dateParts[0])) {
        return {day: toInteger(dateParts[0]), month: null, year: null};
      } else if (dateParts.length === 2 && isNumber(dateParts[0]) && isNumber(dateParts[1])) {
        return {day: toInteger(dateParts[0]), month: toInteger(dateParts[1]), year: null};
      } else if (dateParts.length === 3 && isNumber(dateParts[0]) && isNumber(dateParts[1]) && isNumber(dateParts[2])) {
        return {day: toInteger(dateParts[0]), month: toInteger(dateParts[1]), year: toInteger(dateParts[2])};
      }
    }
    return null;
  }

  format(date: NgbDateStruct): string {
    return date ?
      `${isNumber(date.day) ? padNumber(date.day) : ""}.${isNumber(date.month) ? padNumber(date.month) : ""}.${date.year}` :
      "";
  }
}

@Component({
  selector: "app-date-selector",
  templateUrl: "./date-selector.component.html",
  styleUrls: ["./date-selector.component.css"],
  providers: [I18n, {provide: NgbDatepickerI18n, useClass: SelectedDatepickerI18n}, {
    provide: NgbDateParserFormatter,
    useClass: NgbDateCustomParserFormatter
  }] // define custom NgbDatepickerI18n provider

})
export class DateSelectorComponent implements OnInit {
  private readonly year_now: number = new Date(Date.now()).getFullYear();
  private readonly month_now: number = new Date(Date.now()).getMonth() + 1;
  private readonly day_now: number = new Date(Date.now()).getDate();
  minDate: NgbDateStruct = {year: this.year_now, month: this.month_now - 2, day: this.day_now};
  maxDate: NgbDateStruct = {year: this.year_now + 5, month: 12, day: 31};

  visitDates: VisitDate[] = [];
  dates: NgbDateStruct[] = [];

  constructor(
    private visitDateService: VisitDateService,
    private config: NgbDatepickerConfig,
    private router: Router,
    private dateService: DateService) {

    // {
    //   const visit_date = new VisitDate();
    //   visit_date.visitDateId = 2;
    //   visit_date.date = [2018, 12, 10];
    //   this.dateService.change(visit_date);
    // }
  }

  ngOnInit(): void {
    this.setNgbDatepickerConfig();
    this.getDates();
  }


  onChangeDate(date: NgbDateStruct, disabled): void {
    if (this.isPresented(date) && !disabled) {
      const selected_visitDate: VisitDate = this.visitDates.find((value: VisitDate) => {
        return (value.date[0] == date.year &&
          value.date[1] == date.month &&
          value.date[2] == date.day);
      });
      this.dateService.change(selected_visitDate);
    }
  }

  isPresented(date: NgbDateStruct): boolean {
    for (let i = 0; i < this.dates.length; i++) {
      if ((this.dates[i].year == date.year) &&
        (this.dates[i].month == date.month) &&
        (this.dates[i].day == date.day)) {
        return true;
      }
    }
    return false;
  }

  addDate() {
    this.router.navigateByUrl("dates");
  }


  onClick(calendar) {
    this.setNgbDatepickerConfig();
    calendar.toggle();
  }

  private setNgbDatepickerConfig() {
    this.config.outsideDays = "hidden";
    this.config.displayMonths = 2;
    this.config.navigation = "select";
    this.config.showWeekNumbers = false;
    this.config.firstDayOfWeek = 1;

    this.config.markDisabled = (date: NgbDateStruct) => {
      for (let i = 0; i < this.dates.length; i++) {
        if ((this.dates[i].year == date.year) &&
          (this.dates[i].month == date.month) &&
          (this.dates[i].day == date.day)) {
          return false;
        }
      }
      return true;
    };
  }

  private getDates(): void {
    this.visitDateService.getVisitDates().toPromise().then((visitDates: VisitDate[]) => {
      this.visitDates = visitDates;

      this.dates.splice(0, this.dates.length);
      this.visitDates.forEach((value: VisitDate) => {
        const d: NgbDateStruct = {year: value.date[0], month: value.date[1], day: value.date[2]};
        this.dates.push(d);
      });
    });
  }

}
