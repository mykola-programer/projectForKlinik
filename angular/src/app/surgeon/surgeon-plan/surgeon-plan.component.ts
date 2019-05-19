import {Compiler, Component, OnDestroy, OnInit} from "@angular/core";
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
import {Surgeon} from "../../types/surgeon";
import {SurgeonPlanService} from "../../service/surgeon-plan.service";
import {SurgeonPlan} from "../../types/surgeonPlan";
import {EditedDatepickerI18n, I18n} from "../../types/ua-i18n";


@Component({
  selector: "app-surgeon-plan",
  templateUrl: "./surgeon-plan.component.html",
  styleUrls: ["./surgeon-plan.component.css"],
  providers: [I18n, {provide: NgbDatepickerI18n, useClass: EditedDatepickerI18n}]
})
export class SurgeonPlanComponent implements OnInit, OnDestroy {
  minDate: NgbDate = NgbDate.from(this.calendar.getToday());
  maxDate: NgbDate = new NgbDate(this.calendar.getToday().year + 5, 12, 31);

  surgeonPlans: SurgeonPlan[] = [];
  selectedSurgeonPlansByCurrentDepartment: SurgeonPlan[] = [];
  surgeonPlansByOtherDepartments: SurgeonPlan[] = [];

  datePlansByDepartment: DatePlan[] = [];

  selectedDepartment: Department;
  selectedSurgeon: Surgeon;
  private departments: Department[] = [];
  private departmentSubscriber: Subscription;
  private surgeonSubscriber: Subscription;

  loading = false;
  saving = false;
  deleting = false;

  isLoading(value: boolean) {
    if (value) {
      this.loading = value;
    } else {
      setTimeout(() => {
        this.loading = value;
      }, 1000);
    }
  }

  isSaving(value: boolean) {
    if (value) {
      this.saving = value;
    } else {
      setTimeout(() => {
        this.saving = value;
      }, 1000);
    }
  }

  isDeleting(value: boolean) {
    if (value) {
      this.deleting = value;
    } else {
      setTimeout(() => {
        this.deleting = value;
      }, 1000);
    }
  }

  constructor(private router: Router,
              private config: NgbDatepickerConfig,
              private calendar: NgbCalendar,
              private datePlanService: DatePlanService,
              private surgeonPlanService: SurgeonPlanService,
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
      const index = this.indexOfDatePlan(date, this.datePlansByDepartment);
      return !(index !== -1 && !this.datePlansByDepartment[index].disable
        && !(this.checkForLock(date)));
    };
  }

  ngOnInit() {
    this.globalService.changeNavbar("surgeonPlan");
    this.departmentSubscriber = this.globalService.emittedDepartment.subscribe((selectedDepartment: Department) => {
      this.selectedDepartment = selectedDepartment;
      this.getDepartments();
      this.getDatePlansByDepartment(this.selectedDepartment.departmentId,
        new Date(this.minDate.year, this.minDate.month - 2, this.minDate.day));
    });
    this.surgeonSubscriber = this.globalService.emittedSurgeon.subscribe((selectedSurgeon: Surgeon) => {
      this.selectedSurgeon = selectedSurgeon;
      this.getSurgeonPlans(this.selectedSurgeon.surgeonId, new Date(this.minDate.year, this.minDate.month - 2, this.minDate.day));
    });

    if (!this.selectedDepartment || !this.selectedSurgeon) {
      if (this.globalService.getDepartment()) {
        this.selectedDepartment = this.globalService.getDepartment();
        if (this.globalService.getSurgeon()) {
          this.selectedSurgeon = this.globalService.getSurgeon();
          this.getSurgeonPlans(this.selectedSurgeon.surgeonId, new Date(this.minDate.year, this.minDate.month - 2, this.minDate.day));
        } else {
          this.toastMessageService.inform("Виберіть хірурга", "", "info", 1000);
        }
        this.getDepartments();
        this.getDatePlansByDepartment(this.selectedDepartment.departmentId,
          new Date(this.minDate.year, this.minDate.month - 2, this.minDate.day));
      } else {
        this.toastMessageService.inform("Виберіть філіал", "", "info", 1000);
      }

    }

  }

  ngOnDestroy(): void {
    this.compiler.clearCache();
    this.departmentSubscriber.unsubscribe();
    this.surgeonSubscriber.unsubscribe();
  }

  onSelect(date: NgbDateStruct, disabled: boolean) {
    if (!disabled) {
      const date_index: number = this.indexOfSurgeonPlans(date, this.selectedSurgeonPlansByCurrentDepartment);
      if (date_index !== -1) {
        if (this.selectedSurgeonPlansByCurrentDepartment[date_index].surgeonPlanId > 0) {
          this.changeLock(this.selectedSurgeonPlansByCurrentDepartment[date_index]);
        } else {
          this.selectedSurgeonPlansByCurrentDepartment.splice(date_index, 1);
        }
      } else {
        const datePlan_index = this.indexOfDatePlan(date, this.datePlansByDepartment);
        if (datePlan_index !== -1) {
          this.selectedSurgeonPlansByCurrentDepartment.push(new SurgeonPlan(this.datePlansByDepartment[datePlan_index], this.selectedSurgeon.surgeonId, true));
        }
      }
    }

    this.selectedSurgeonPlansByCurrentDepartment.sort((date1, date2) => {
      if (date1.datePlan.date[0] !== date2.datePlan.date[0]) {
        return date1.datePlan.date[0] - date2.datePlan.date[0];
      } else if (date1.datePlan.date[1] !== date2.datePlan.date[1]) {
        return date1.datePlan.date[1] - date2.datePlan.date[1];
      } else if (date1.datePlan.date[2] !== date2.datePlan.date[2]) {
        return date1.datePlan.date[2] - date2.datePlan.date[2];
      } else {
        return 0;
      }
    });
  }

  changeLock(surgeonPlan: SurgeonPlan) {
    const date = new NgbDate(surgeonPlan.datePlan.date[0], surgeonPlan.datePlan.date[1], surgeonPlan.datePlan.date[2]);
    const index = this.indexOfSurgeonPlans(date, this.surgeonPlansByOtherDepartments);

    if (index !== -1 && !this.surgeonPlansByOtherDepartments[index].disable) {
      this.toastMessageService.inform("Заблоковано! <br>" + "Ця дата заброньована в м." + this.getDepartmentByDate(date),
        "Спочатку заблокуйте це дату в м." + this.getDepartmentByDate(date), "info");
      return;
    }
    surgeonPlan.disable = !surgeonPlan.disable;
    surgeonPlan.isChanged = true;
  }

  onSave() {
    const changedSurgeonPlan: SurgeonPlan = this.selectedSurgeonPlansByCurrentDepartment.find((surgeonPlan: SurgeonPlan) => {
      return surgeonPlan.isChanged;
    });

    if (changedSurgeonPlan) {
      this.isSaving(true);

      if (changedSurgeonPlan.surgeonPlanId === 0) {
        this.surgeonPlanService.addSurgeonPlan(changedSurgeonPlan).toPromise().then((surgeonPlan: SurgeonPlan) => {
          changedSurgeonPlan.isChanged = false;
          this.isSaving(false);
          this.success_saving(surgeonPlan);
        }).catch((err: HttpErrorResponse) => {
          this.error_saving(err, changedSurgeonPlan);
        });

      } else if (changedSurgeonPlan.surgeonPlanId > 0) {
        this.surgeonPlanService.editSurgeonPlan(changedSurgeonPlan).toPromise().then((surgeonPlan: SurgeonPlan) => {
          changedSurgeonPlan.isChanged = false;
          this.success_saving(surgeonPlan);
          this.isSaving(false);
        }).catch((err: HttpErrorResponse) => {
          this.error_saving(err);
        });
      }
    } else {
      this.isSaving(false);
      this.onRefresh();
    }
  }

  private success_saving(surgeonPlan?: SurgeonPlan) {
    this.toastMessageService.inform("Збережено !", "Дата хірурга успішно збережена !", "success");
    // setTimeout(() => {
    this.onSave();
    // }, 500);
  }

  private error_saving(err: HttpErrorResponse, surgeonPlan?: SurgeonPlan) {
    this.isSaving(false);
    switch (err.status) {
      case 401 : {
        this.toastMessageService.inform("Помилка авторизації! ", "Спробуйте повторно залогінитись.", "error");
        return;
      }
      case 404 : {
        this.toastMessageService.inform("Помилка при збережені! <br>" + this.refactorDay(surgeonPlan.datePlan),
          err.error + "<br> Обновіть сторінку та спробуйте знову.", "error");
        return;
      }
      case 409 : {
        this.toastMessageService.inform("Помилка при збережені! <br>" + this.refactorDay(surgeonPlan.datePlan) + "<br> Конфлікт в базі даних !",
          err.error + "<br> Обновіть сторінку та спробуйте знову. <br> Можливо дата існує серед прихованих.", "error");
        return;
      }
      case 422 : {
        this.toastMessageService.inform("Помилка при збережені! <br>" + this.refactorDay(surgeonPlan.datePlan) + "<br> не відповідає критеріям !",
          err.error, "error");
        return;
      }
      default : {
        this.toastMessageService.inform("Помилка при збережені! <br>" + this.refactorDay(surgeonPlan.datePlan),
          err.error + "<br>" + "HTTP status: " + err.status, "error");
        return;
      }
    }
  }


  onDelete() {
    const changedSurgeonPlan: SurgeonPlan = this.selectedSurgeonPlansByCurrentDepartment.find((surgeonPlan: SurgeonPlan) => {
      return surgeonPlan.isChanged;
    });

    if (changedSurgeonPlan) {
      this.isDeleting(true);
      if (changedSurgeonPlan.surgeonPlanId > 0) {
        this.surgeonPlanService.deleteSurgeonPlan(changedSurgeonPlan.surgeonPlanId).toPromise().then(() => {
          changedSurgeonPlan.isChanged = false;
          this.success_deleting();
        }).catch((err: HttpErrorResponse) => {
          this.error_deleting(err);
        });
      } else {
        const date = new NgbDate(changedSurgeonPlan.datePlan.date[0], changedSurgeonPlan.datePlan.date[1], changedSurgeonPlan.datePlan.date[2]);
        const date_index: number = this.indexOfSurgeonPlans(date, this.selectedSurgeonPlansByCurrentDepartment);
        this.selectedSurgeonPlansByCurrentDepartment.splice(date_index, 1);
        this.success_deleting();
      }
    } else {
      this.isDeleting(false);
      this.onRefresh();
    }
  }

  private success_deleting(surgeonPlan?: SurgeonPlan) {
    this.isDeleting(false);
    this.toastMessageService.inform("Видалено !", "Запланована дата успішно видалена !", "success");
    // setTimeout(() => {
    this.onDelete();
    // }, 500);
  }

  private error_deleting(err: HttpErrorResponse) {
    this.isDeleting(false);
    switch (err.status) {
      case 401 : {
        this.toastMessageService.inform("Помилка авторизації! ", "Спробуйте повторно залогінитись.", "error");
        return;
      }
      case 409 : {
        this.toastMessageService.inform("Помилка при видалені! <br>" + "Конфлікт в базі даних !",
          err.error + "<br> Можливо на цю дату вже заплановані операції.", "error");
        return;
      }
      default : {
        this.toastMessageService.inform("Помилка при видалені! <br>",
          err.error + "<br>" + "HTTP status: " + err.status, "error");
        return;
      }
    }
  }

  onRefresh() {
    if (this.globalService.getDepartment()) {
      this.selectedDepartment = this.globalService.getDepartment();
      if (this.globalService.getSurgeon()) {
        this.selectedSurgeon = this.globalService.getSurgeon();
        this.getSurgeonPlans(this.selectedSurgeon.surgeonId, new Date(this.minDate.year, this.minDate.month - 2, this.minDate.day));
      } else {
        this.toastMessageService.inform("Виберіть хірурга", "", "info", 1000);
      }
      this.getDepartments();
      this.getDatePlansByDepartment(this.selectedDepartment.departmentId,
        new Date(this.minDate.year, this.minDate.month - 2, this.minDate.day));
    } else {
      this.toastMessageService.inform("Виберіть філіал", "", "info", 1000);
    }


    this.getDatePlansByDepartment(this.selectedDepartment.departmentId, new Date(this.minDate.year, this.minDate.month - 2, this.minDate.day));
  }

  /**
   /  Checking for locked or busy date
   */
  checkForLock(date: NgbDate): boolean {
    const date_index: number = this.indexOfSurgeonPlans(date, this.surgeonPlansByOtherDepartments);
    return (date_index !== -1 && !this.surgeonPlansByOtherDepartments[date_index].disable);
  }

  /**
   /  Checking for disable surgeon plan
   */
  checkForDisable(date: NgbDate): boolean {
    const date_index: number = this.indexOfSurgeonPlans(date, this.selectedSurgeonPlansByCurrentDepartment);
    return (date_index !== -1 && this.selectedSurgeonPlansByCurrentDepartment[date_index].disable);
  }

  /**
   /  Getting Department's name for date
   */
  getDepartmentByDate(date: NgbDate): string {
    const surgeonPlan: SurgeonPlan = (this.surgeonPlansByOtherDepartments.find((surgeon_plan: SurgeonPlan) => {
      return surgeon_plan.datePlan.date[0] == date.year && surgeon_plan.datePlan.date[1] == date.month && surgeon_plan.datePlan.date[2] == date.day;
    }));

    if (surgeonPlan && surgeonPlan.datePlan && surgeonPlan.datePlan.departmentID > 0) {
      const depart: Department = this.departments.find(value => value.departmentId === surgeonPlan.datePlan.departmentID);
      if (depart) {
        return depart.name;
      }
    }
    return null;
  }

  isSelected(date: NgbDate): boolean {
    return this.indexOfSurgeonPlans(date, this.selectedSurgeonPlansByCurrentDepartment) !== -1;
  }

  isPresented(date: NgbDate): boolean {
    const index = this.indexOfDatePlan(date, this.datePlansByDepartment);
    return index !== -1 && !this.datePlansByDepartment[index].disable;
  }

  isDisabled(date: NgbDate): boolean {
    const index = this.indexOfDatePlan(date, this.datePlansByDepartment);
    return index !== -1 && this.datePlansByDepartment[index].disable;
  }

  private indexOfDatePlan(date: NgbDateStruct, datePlans: DatePlan[]): number {
    return datePlans.findIndex((datePlan: DatePlan) => {
      return datePlan.date[0] == date.year && datePlan.date[1] == date.month && datePlan.date[2] == date.day;
    });
  }

  private indexOfSurgeonPlans(date: NgbDateStruct, surgeonPlans: SurgeonPlan[]): number {
    return surgeonPlans.findIndex((surgeonPlan: SurgeonPlan) => {
      return surgeonPlan.datePlan.date[0] == date.year && surgeonPlan.datePlan.date[1] == date.month && surgeonPlan.datePlan.date[2] == date.day;
    });
  }

  /**
   /  Get departments
   */
  private getDepartments() {
    this.isLoading(true);
    this.departmentService.getDepartments().toPromise().then((departments: Department[]) => {
      this.departments = departments;
      this.isLoading(false);
    }).catch((err: HttpErrorResponse) => {
      this.isLoading(true);
      this.toastMessageService.inform("Сервер недоступний!",
        "Спробуйте пізніше !" + "<br>" + err.error + "<br>" + err.message, "error", 10000);
      setTimeout(() => {
        this.getDepartments();
      }, 15000);
    });
  }

  // +++
  /**
   /  Get SurgeonPlans by surgeon
   */
  private getSurgeonPlans(surgeonID: number, minDate: Date) {
    this.isLoading(true);
    this.surgeonPlanService.getBySurgeonID(surgeonID, minDate).toPromise().then((surgeonPlans: SurgeonPlan[]) => {
      this.surgeonPlans = surgeonPlans;

      this.surgeonPlans.sort((date1, date2) => {
        if (date1.datePlan.date[0] !== date2.datePlan.date[0]) {
          return date1.datePlan.date[0] - date2.datePlan.date[0];
        } else if (date1.datePlan.date[1] !== date2.datePlan.date[1]) {
          return date1.datePlan.date[1] - date2.datePlan.date[1];
        } else if (date1.datePlan.date[2] !== date2.datePlan.date[2]) {
          return date1.datePlan.date[2] - date2.datePlan.date[2];
        } else {
          return 0;
        }
      });

      this.selectedSurgeonPlansByCurrentDepartment = this.surgeonPlans.filter((surgeonPlan: SurgeonPlan) => {
        return surgeonPlan.datePlan.departmentID === this.selectedDepartment.departmentId;
      });

      this.surgeonPlansByOtherDepartments = this.surgeonPlans.filter((surgeonPlan: SurgeonPlan) => {
        return surgeonPlan.datePlan.departmentID !== this.selectedDepartment.departmentId;
      });

      this.isLoading(false);
    }).catch((err: HttpErrorResponse) => {
      this.isLoading(true);
      this.toastMessageService.inform("Сервер недоступний!",
        "Спробуйте пізніше !" + "<br>" + err.error + "<br>" + err.message, "error", 10000);
      setTimeout(() => {
        this.getSurgeonPlans(surgeonID, minDate);
      }, 15000);
    });
  }

  private getDatePlansByDepartment(departmentID: number, minDate: Date): void {
    this.isLoading(true);
    this.datePlanService.getDatePlansByDepartment(departmentID, minDate).toPromise().then((datePlansByDepartment: DatePlan[]) => {
      this.datePlansByDepartment = datePlansByDepartment;

      this.getSurgeonPlans(this.selectedSurgeon.surgeonId, minDate);

      this.isLoading(false);
    }).catch((err: HttpErrorResponse) => {
      this.isLoading(true);
      this.toastMessageService.inform("Сервер недоступний!",
        "Спробуйте пізніше !" + "<br>" + err.error + "<br>" + err.message, "error", 10000);
      setTimeout(() => {
        this.getDatePlansByDepartment(departmentID, minDate);
      }, 15000);
    });
  }

  /*  private getDatePlansBySurgeon(surgeonID: number, minDate: Date): void {
      this.surgeon_dates_loading = true;
      this.selectedDates = [];
      this.datePlanService.getDatePlansBySurgeon(surgeonID, minDate).toPromise().then((datePlansBySurgeon: DatePlan[]) => {
        // this.datePlansBySurgeon = datePlansBySurgeon;

        // this.datePlansBySurgeonCurrentDepartment = datePlansBySurgeon.filter((datePlan: DatePlan) => {
        //   return datePlan.departmentID === this.selectedDepartment.departmentId;
        // });
        // this.selectedDates = this.datePlansBySurgeonCurrentDepartment;
        //
        // this.datePlansBySurgeonOtherDepartments = datePlansBySurgeon.filter((datePlan: DatePlan) => {
        //   return datePlan.departmentID !== this.selectedDepartment.departmentId;
        // });

        // console.log(this.datePlansBySurgeonCurrentDepartment);
        // console.log(this.datePlansBySurgeonOtherDepartments);

        // setTimeout(() => this.surgeon_dates_loading = false, 400);
      }).catch((err: HttpErrorResponse) => {
        this.surgeon_dates_loading = true;
        this.toastMessageService.inform("Сервер недоступний!",
          "Спробуйте пізніше !" + "<br>" + err.error + "<br>" + err.message, "error", 10000);
        setTimeout(() => {
          this.getDatePlansBySurgeon(surgeonID, minDate);
        }, 15000);
      });
    }*/

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
