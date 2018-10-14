import {Component, EventEmitter, Injectable, Input, OnInit, Output} from '@angular/core';
import {NgbDatepickerConfig, NgbDatepickerI18n, NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {VisitDateService} from "../../service/visit-date.service";
import {Router} from "@angular/router";
import {VisitDate} from "../../backend_types/visit-date";
import {MatDialog} from "@angular/material";
import {NavbarService} from "../../service/navbar.service";

const I18N_VALUES = {
  'ua': {
    weekdays: ['Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб', 'Нд'],
    months: ['Січень', 'Лютий', 'Березень', 'Квітень', 'Травень', 'Червень', 'Липень', 'Серпень', 'Вересень', 'Жовтень', 'Листопад', 'Грудень'],
  }
  // other languages you would support
};

// Define a service holding the language. You probably already have one if your app is i18ned. Or you could also
// use the Angular LOCALE_ID value
@Injectable()
export class I18n {
  language = 'ua';
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
  selector: 'app-date-editor',
  templateUrl: './date-editor.component.html',
  styleUrls: ['./date-editor.component.css'],
  providers: [I18n, {provide: NgbDatepickerI18n, useClass: EditedDatepickerI18n}] // define custom NgbDatepickerI18n provider

})
export class DateEditorComponent implements OnInit{
  model;
  private readonly year_now: number = new Date(Date.now()).getFullYear();
  private readonly month_now: number = new Date(Date.now()).getMonth() + 1;
  private readonly day_now: number = new Date(Date.now()).getDate();
  minDate: NgbDateStruct = {year: this.year_now, month: this.month_now, day: this.day_now};
  maxDate: NgbDateStruct = {year: this.year_now + 5, month: 12, day: 31};
  dates: NgbDateStruct[] = [];
  visitDates: VisitDate[] = [];
  selectedDates: NgbDateStruct[] = [];

  constructor(private router: Router, private config: NgbDatepickerConfig, private dateService: VisitDateService, private serviceNavbar: NavbarService) {
    this.setNgbDatepickerConfig();
  }

  ngOnInit(): void {
    this.getDates();
    this.serviceNavbar.change('date');
  }


  onSelect(date: NgbDateStruct, disabled) {
    // console.log(this.selectedDates.indexOf(date, 0));
    if (!disabled) {
      if (this.selectedDates.indexOf(date, 0) == -1) {
        this.selectedDates.push(date);
      } else {
        this.selectedDates.splice(this.selectedDates.indexOf(date, 0), 1);
      }

      this.selectedDates.sort((date1, date2) => {
        if (date1.year != date2.year) {
          return date1.year - date2.year;
        } else if (date1.month != date2.month) {
          return date1.month - date2.month;
        } else {
          return date1.day - date2.day;
        }
      });
    }
  }


  onSave() {
    let visitDates: VisitDate[] = [];

    this.selectedDates.forEach((value: NgbDateStruct) => {
      let visit_date: VisitDate = new VisitDate();
      visit_date.visitDateId = 0;
      visit_date.date = [value.year, value.month, value.day];
      visit_date.lock = false;
      visitDates.push(visit_date);
    });

    this.dateService.addVisitDates(visitDates).toPromise().then(() => {
      this.onClear();
      this.getDates();
    });
  }

  onClear() {
    this.selectedDates.splice(0, this.selectedDates.length);
  }

  onDelete() {
    let visitDates: VisitDate[] = this.visitDates
      .filter((value: VisitDate) => {
        return this.isInArray({year: value.date[0], month: value.date[1], day: value.date[2]}, this.selectedDates);
      });

    visitDates.forEach((value: VisitDate) => {
      this.dateService.removeVisitDate(value.visitDateId).toPromise().then(() => {
        this.onClear();
        this.getDates();
      });
    });
  }

  onCancel() {
    this.onClear();
    // this.router.navigateByUrl("");
  }

  private setNgbDatepickerConfig() {
    this.config.outsideDays = 'hidden';
    this.config.displayMonths = 2;
    this.config.navigation = "select";
    this.config.showWeekNumbers = false;
    this.config.firstDayOfWeek = 1;

    this.config.markDisabled = (date: NgbDateStruct) => {
      const d = new Date(date.year, date.month - 1, date.day);
      return d.getDay() === 0 || d.getDay() === 6;
    };
  }

  // This is selected dates
  isSelected(date: NgbDateStruct): boolean {
    return this.selectedDates.indexOf(date) != -1;
  }

  isPresented(date: NgbDateStruct): boolean {
    return this.isInArray(date, this.dates);
  }

  private isInArray(date: NgbDateStruct, dates: NgbDateStruct[]): boolean {
    let isEqual: boolean = false;
    dates.forEach((value: NgbDateStruct) => {
      if ((value.year == date.year) &&
        (value.month == date.month) &&
        (value.day == date.day)) {
        isEqual = true;
      }
    });
    return isEqual;
  }

  private getDates(): void {
    this.dateService.getVisitDates().toPromise().then((visitDates: VisitDate[]) => {
      this.visitDates = visitDates;
      this.dates.splice(0, this.dates.length);
      this.visitDates.forEach((value: VisitDate) => {
        let d: NgbDateStruct = {year: value.date[0], month: value.date[1], day: value.date[2]};
        this.dates.push(d);
      });
    });
  }

}
