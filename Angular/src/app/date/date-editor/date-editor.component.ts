import {Compiler, Component, Injectable, OnDestroy, OnInit} from "@angular/core";
import {NgbCalendar, NgbDatepickerConfig, NgbDatepickerI18n, NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {Router} from "@angular/router";
import {NgbDate} from "@ng-bootstrap/ng-bootstrap/datepicker/ngb-date";
import {HttpErrorResponse} from "@angular/common/http";
import {ToastMessageService} from "../../service/toast-message.service";
import {DatePlan} from "../../types/date-plan";
import {DatePlanService} from "../../service/date-plan.service";
import {DepartmentService} from "../../service/department.service";
import {Department} from "../../types/department";
import {GlobalService} from "../../service/global.service";
import {Subscription} from "rxjs";
import {EditedDatepickerI18n, I18n} from "../../types/ua-i18n";

@Component({
  selector: "app-date-editor",
  templateUrl: "./date-editor.component.html",
  styleUrls: ["./date-editor.component.css"],
  providers: [I18n, {provide: NgbDatepickerI18n, useClass: EditedDatepickerI18n}] // define custom NgbDatepickerI18n provider

})
export class DateEditorComponent implements OnInit, OnDestroy {
  minDate: NgbDate = NgbDate.from(this.calendar.getToday());
  maxDate: NgbDate = new NgbDate(this.calendar.getToday().year + 5, 12, 31);
  datePlans: DatePlan[] = [];
  selectedDates: DatePlan[] = [];

  selectedDepartment: Department = this.globalService.getDepartment();
  private departmentSubscriber: Subscription;

  loading_save = false;
  del_loading = false;
  dates_loading = false;

  isLoadingOFF(): boolean {
    return !(this.loading_save
      || this.del_loading);
  };

  constructor(private router: Router,
              private config: NgbDatepickerConfig,
              private calendar: NgbCalendar,
              private datePlanService: DatePlanService,
              private globalService: GlobalService,
              private compiler: Compiler,
              private departmentService: DepartmentService,
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
    this.globalService.changeNavbar("date");
    if (this.selectedDepartment) {
      this.getDatePlans(this.selectedDepartment.departmentId, new Date(this.minDate.year, this.minDate.month - 2, this.minDate.day));
    }
    this.departmentSubscriber = this.globalService.emittedDepartment.subscribe((selectedDepartment: Department) => {
      this.selectedDepartment = selectedDepartment;
      this.getDatePlans(this.selectedDepartment.departmentId, new Date(this.minDate.year, this.minDate.month - 2, this.minDate.day));
    });
  }

  ngOnDestroy(): void {
    this.compiler.clearCache();
    this.departmentSubscriber.unsubscribe();
  }

  onSelect(date: NgbDateStruct, disabled: boolean) {
    if (!disabled) {
      const date_index: number = this.indexOf(date, this.selectedDates);
      if (date_index !== -1) {
        this.selectedDates.splice(date_index, 1);
      } else {
        const datePlan_index = this.indexOf(date, this.datePlans);
        if (datePlan_index !== -1) {
          this.selectedDates.push(this.datePlans[datePlan_index]);
        } else {
          const datePlan: DatePlan = new DatePlan();
          datePlan.date = [date.year, date.month, date.day];
          datePlan.departmentID = this.selectedDepartment.departmentId;
          datePlan.disable = false;
          this.selectedDates.push(datePlan);
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
        this.datePlanService.addDatePlan(this.selectedDates[0]).toPromise().then((datePlan: DatePlan) => {
          this.loading_save = false;
          this.selectedDates.splice(0, 1);
          this.success_saving(datePlan);
        }).catch((err: HttpErrorResponse) => {
          this.error_saving(err, this.selectedDates[0]);
        });
      } else if (this.selectedDates[0].datePlanId > 0) {
        this.datePlanService.editDatePlan(this.selectedDates[0]).toPromise().then((datePlan: DatePlan) => {
          this.loading_save = false;
          this.selectedDates.splice(0, 1);
          this.success_saving(datePlan);
        }).catch((err: HttpErrorResponse) => {
          this.error_saving(err);
        });
      }
    } else {
      this.loading_save = false;
      this.onRefresh();
    }
  }

  private success_saving(datePlan?: DatePlan) {
    this.toastMessageService.inform("Збережено !", "Операційна дата успішно збережена !", "success");
    this.onSave();
  }

  private error_saving(err: HttpErrorResponse, datePlan?: DatePlan) {
    this.loading_save = false;
    if (err.status === 422) {
      this.toastMessageService.inform("Помилка при збережені! <br>" + this.refactorDay(datePlan) + "<br> не відповідає критеріям !",
        err.error, "error");
    } else if (err.status === 404) {
      this.toastMessageService.inform("Помилка при збережені! <br>" + this.refactorDay(datePlan),
        err.error + "<br> Обновіть сторінку та спробуйте знову.", "error");
    } else if (err.status === 409) {
      this.toastMessageService.inform("Помилка при збережені! <br>" + this.refactorDay(datePlan) + "<br> Конфлікт в базі даних !",
        err.error + "<br> Обновіть сторінку та спробуйте знову. <br> Можливо дата існує серед прихованих.", "error");
    } else {
      this.toastMessageService.inform("Помилка при збережені! <br>" + this.refactorDay(datePlan),
        err.error + "<br>" + "HTTP status: " + err.status, "error");
    }
  }

  onRefresh() {
    this.getDatePlans(this.selectedDepartment.departmentId, new Date(this.minDate.year, this.minDate.month - 2, this.minDate.day));
  }

  onDelete() {
    if (this.selectedDates.length > 0) {
      this.del_loading = true;
      if (this.selectedDates[0].datePlanId > 0) {
        this.datePlanService.removeDatePlan(this.selectedDates[0].datePlanId).toPromise().then(() => {
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

  private success_deleting(datePlan?: DatePlan) {
    this.toastMessageService.inform("Видалено !", "Дати успішно видалені !", "success");
    this.onDelete();
  }

  private error_deleting(err: HttpErrorResponse, datePlan?: DatePlan) {
    this.del_loading = false;
    if (err.status === 409) {
      this.toastMessageService.inform("Помилка при видалені! <br>" + this.refactorDay(datePlan), "Це число заброньоване за хірургом!", "error");
      this.toastMessageService.inform("Рекомендація.", "Можна заблокувати через кнопку 'Lock'", "info", 10000);
    } else {
      this.toastMessageService.inform("Помилка при видалені! <br>" + this.refactorDay(datePlan),
        err.error + "<br>" + "HTTP status: " + err.status, "error");
    }
  }

  isSelected(date: NgbDate): boolean {
    return this.indexOf(date, this.selectedDates) !== -1;
  }

  isPresented(date: NgbDate): boolean {
    const index = this.indexOf(date, this.datePlans);
    return index !== -1 && !this.datePlans[index].disable;
  }

  isDisabled(date: NgbDate): boolean {
    const index = this.indexOf(date, this.datePlans);
    return index !== -1 && this.datePlans[index].disable;
  }

  private indexOf(date: NgbDateStruct, datePlans: DatePlan[]): number {
    return datePlans.findIndex((datePlan: DatePlan) => {
      return datePlan.date[0] == date.year && datePlan.date[1] == date.month && datePlan.date[2] == date.day;
    });
  }

  private getDatePlans(departmentID: number, minDate: Date): void {
    this.dates_loading = true;
    this.selectedDates = [];
    this.datePlanService.getDatePlansByDepartment(departmentID, minDate).toPromise().then((datePlans: DatePlan[]) => {
      this.datePlans = datePlans;
      setTimeout(() => this.dates_loading = false, 400);
    }).catch((err: HttpErrorResponse) => {
      this.dates_loading = true;
      this.toastMessageService.inform("Сервер недоступний!",
        "Спробуйте пізніше !" + "<br>" + err.error + "<br>" + err.message, "error", 10000);
      setTimeout(() => {
        this.getDatePlans(departmentID, minDate);
      }, 15000);
    });
  }

  refactorDay(datePlan: DatePlan): string {
    return (datePlan.date[2] < 10 ? "0" + datePlan.date[2] : datePlan.date[2])
      + (datePlan.date[1] < 10 ? ".0" + datePlan.date[1] : "." + datePlan.date[1])
      + (datePlan.date[0] < 10 ? ".0" + datePlan.date[0] : "." + datePlan.date[0]);
  }


  // test(){
  //   console.log(this.selectedDepartment);
  //   console.log(this.departmentService);
  //   console.log(this.departmentService.selectedDepartment);
  //   this.departmentService.selectedDepartment.toPromise().then(value => console.log(value));
  // }
}
