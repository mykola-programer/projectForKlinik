import {Compiler, Component, Injectable, OnDestroy, OnInit, ViewChild} from "@angular/core";

import {
  NgbCalendar,
  NgbDateParserFormatter,
  NgbDatepicker,
  NgbDatepickerConfig,
  NgbDatepickerI18n,
  NgbDateStruct, NgbInputDatepicker
} from "@ng-bootstrap/ng-bootstrap";
import {isNumber, padNumber, toInteger} from "@ng-bootstrap/ng-bootstrap/util/util";
import {Router} from "@angular/router";
import {NgbDate} from "@ng-bootstrap/ng-bootstrap/datepicker/ngb-date";
import {HttpErrorResponse} from "@angular/common/http";
import {ToastMessageService} from "../../service/toast-message.service";
import {DatePlan} from "../../types/date-plan";
import {DatePlanService} from "../../service/date-plan.service";
import {GlobalService} from "../../service/global.service";
import {Department} from "../../types/department";
import {Subscription} from "rxjs";
import {NgbDatepickerNavigateEvent} from "@ng-bootstrap/ng-bootstrap/datepicker/datepicker";
import {DatepickerViewModel} from "@ng-bootstrap/ng-bootstrap/datepicker/datepicker-view-model";

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
export class DateSelectorComponent implements OnInit, OnDestroy {
  @ViewChild('calendar') calendar_view;

  minDate: NgbDate = this.calendar.getPrev(this.calendar.getToday(), "y", 2);
  maxDate: NgbDate = this.calendar.getNext(this.calendar.getToday(), "y", 5);

  datePlans: DatePlan[] = [];
  selectedDepartment: Department = this.globalService.getDepartment();
  private departmentSubscriber: Subscription;
  private showedMonth: NgbDate = this.calendar.getToday();

  constructor(
    private datePlanService: DatePlanService,
    private config: NgbDatepickerConfig,
    private calendar: NgbCalendar,
    private router: Router,
    private compiler: Compiler,
    private toastMessageService: ToastMessageService,
    private globalService: GlobalService) {

    this.config.outsideDays = "hidden";
    this.config.displayMonths = 2;
    this.config.navigation = "select";
    this.config.showWeekNumbers = false;
    this.config.firstDayOfWeek = 1;

  }

  ngOnInit(): void {
    this.config.markDisabled = (date: NgbDate) => {
      const index = this.indexOf(date, this.datePlans);
      return !(index !== -1 && !this.datePlans[index].disable);
    };
    // TODO to delete
    this.datePlanService.getDatePlan(154).toPromise().then(value => this.globalService.changeDatePlan(value));
    //

    if (this.selectedDepartment) {
      this.getDatePlans(this.selectedDepartment.departmentId, this.convertDate(this.calendar.getPrev(this.showedMonth, "m", 2)));
    }
    this.departmentSubscriber = this.globalService.emittedDepartment.subscribe((selectedDepartment: Department) => {
      this.selectedDepartment = selectedDepartment;
      this.getDatePlans(this.selectedDepartment.departmentId, this.convertDate(this.calendar.getPrev(this.showedMonth, "m", 2)));
    });
  }

  ngOnDestroy(): void {
    this.compiler.clearCache();
    this.departmentSubscriber.unsubscribe();
  }

  onSelect(date: NgbDate): void {
    const selected_datePlan: DatePlan = this.datePlans.find((datePlan: DatePlan) => {
      return (datePlan.date[0] == date.year &&
        datePlan.date[1] == date.month &&
        datePlan.date[2] == date.day);
    });
    this.globalService.changeDatePlan(selected_datePlan);
  }

  isLocked(date: NgbDate): boolean {
    const index = this.indexOf(date, this.datePlans);
    return index !== -1 && this.datePlans[index].disable;
  }

  addDate() {
    this.router.navigateByUrl("dates");
  }

  private getDatePlans(departmentID: number, minDate: Date): void {
    this.calendar_view.close();
    this.datePlanService.getDatePlansByDepartment(departmentID, minDate).toPromise().then((datePlans: DatePlan[]) => {
      this.datePlans = datePlans;
    }).catch((err: HttpErrorResponse) => {
      this.toastMessageService.inform("Сервер недоступний!",
        "Спробуйте пізніше !" + "<br>" + err.error + "<br>" + err.message, "error", 10000);
      setTimeout(() => {
        this.getDatePlans(departmentID, minDate);
      }, 15000);
    });
  }

  private indexOf(date: NgbDateStruct, datePlans: DatePlan[]): number {
    return datePlans.findIndex((datePlan: DatePlan) => {
      return datePlan.date[0] == date.year && datePlan.date[1] == date.month && datePlan.date[2] == date.day;
    });
  }

  setCurrentMonth(navigateEvent: NgbDatepickerNavigateEvent) {
    this.showedMonth = NgbDate.from({year: navigateEvent.next.year, month: navigateEvent.next.month, day: 1});
  }

  private convertDate(date: NgbDateStruct): Date {
    return new Date(date.year, date.month - 1, date.day);
  }
}
