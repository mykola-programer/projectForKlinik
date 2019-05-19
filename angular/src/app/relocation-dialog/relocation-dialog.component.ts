import {Component, Inject, Injectable, OnInit} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {NgbCalendar, NgbDatepickerConfig, NgbDatepickerI18n, NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {NgbDate} from "@ng-bootstrap/ng-bootstrap/datepicker/ngb-date";
import {Accomodation} from "../types/accomodation";
import {ToastMessageService} from "../service/toast-message.service";
import {Visit} from "../types/visit";
import {Client} from "../types/client";
import {AccomodationService} from "../service/accomodation.service";
import {VisitService} from "../service/visit.service";
import {HttpErrorResponse} from "@angular/common/http";
import {DatePlan} from "../types/date-plan";
import {DatePlanService} from "../service/date-plan.service";
import {EditedDatepickerI18n, I18n} from "../types/ua-i18n";

@Component({
  selector: "app-relocation-dialog",
  templateUrl: "./relocation-dialog.component.html",
  styleUrls: ["./relocation-dialog.component.css"],
  providers: [I18n, {provide: NgbDatepickerI18n, useClass: EditedDatepickerI18n}] // define custom NgbDatepickerI18n provider
})
export class RelocationDialogComponent implements OnInit {

  minDate: NgbDate = NgbDate.from(this.calendar.getToday());
  maxDate: NgbDate = new NgbDate(this.calendar.getToday().year + 5, 12, 31);
  visitDates: DatePlan[] = [];
  selected_date: DatePlan = this.data.visitDate;
  accomodations: Accomodation[] = [];
  selected_accomodationID: number = this.data.visit.accomodationID;

  private dates_loading = false;
  private visits_loading = false;
  accomodations_loading = false;

  isLoadingOFF(): boolean {
    return !(this.dates_loading
      || this.visits_loading
    );
  };

  constructor(
    public dialogRef: MatDialogRef<RelocationDialogComponent>,
    private config: NgbDatepickerConfig,
    private calendar: NgbCalendar,
    private toastMessageService: ToastMessageService,
    private accomodationService: AccomodationService,
    private visitService: VisitService,
    private dateService: DatePlanService,
    @Inject(MAT_DIALOG_DATA) public data: {
      visit: Visit,
      client: Client,
      visitDate: DatePlan
    }) {
  }

  ngOnInit() {
    this.visits_loading = true;
    this.dates_loading = true;
    this.getDates();
    this.getVisits();
    this.setNgbDatepickerConfig();
  }

  private setNgbDatepickerConfig() {
    this.config.outsideDays = "hidden";
    this.config.displayMonths = 2;
    this.config.navigation = "select";
    this.config.showWeekNumbers = false;
    this.config.firstDayOfWeek = 1;
    this.config.markDisabled = (date: NgbDateStruct) => {
      return this.visitDates.findIndex(value =>
        ((value.date[0] == date.year) &&
          (value.date[1] == date.month) &&
          (value.date[2] == date.day))) == -1;
    };
  };

  private getDates(): void {
    this.dates_loading = true;
    this.dateService.getDatePlans().toPromise().then((visitDates: DatePlan[]) => {
      this.visitDates = visitDates;
      setTimeout(() => this.dates_loading = false, 1000);
    });
  }

  onSelectDate(date: NgbDateStruct, disabled) {

    if (!disabled) {
      this.selected_accomodationID = 0;
      this.selected_date = this.visitDates.find((value: DatePlan) =>
        ((value.date[0] == date.year) &&
          (value.date[1] == date.month) &&
          (value.date[2] == date.day)));
      this.getVisits();
    }
  }

  private getVisits() {
    if (this.selected_date != null) {
      this.visitService.getVisitsByDate(new Date(this.selected_date.date[0], this.selected_date.date[1] - 1, this.selected_date.date[2]))
        .toPromise().then((visits_of_date: Visit[]) => {
        this.getAllFreeAccomodations(visits_of_date);
        setTimeout(() => this.visits_loading = false, 500);
      });
    }
  }

  private getAllFreeAccomodations(visits_of_date: Visit[]) {
    this.accomodations_loading = true;
    this.accomodationService.getAccomodations().toPromise().then((accomodations: Accomodation[]) => {
      this.accomodations = accomodations.filter(accomodation => !accomodation.disable);

      visits_of_date.forEach((visit: Visit) => {
        const index = this.accomodations.findIndex((accomodation: Accomodation) => {
          return visit.accomodationID === accomodation.accomodationId;
        });
        if (index >= 0) {
          this.accomodations[index].disable = true;
        }
      });
      setTimeout(() => this.accomodations_loading = false, 1000);
    });
  }

  onMove() {
    this.data.visit.accomodationID = this.selected_accomodationID;
    //TODO !!!!!!!!!!
    // this.data.visit.visitDateID = this.selected_date.datePlanId;
    this.visitService.editVisit(this.data.visit).toPromise().then((visit: Visit) => {
      this.dialogRef.close(visit);
    }).catch((err: HttpErrorResponse) => {
      this.toastMessageService.inform("Помилка при переміщені!", err.error, "error");
    });
  }


  onRefresh() {
    this.selected_date = this.data.visitDate;
    this.selected_accomodationID = this.data.visit.accomodationID;
    this.getDates();
    this.visits_loading = true;
    this.getVisits();
  }

  onCancel() {
    this.dialogRef.close();
  }

  refactorDate(numbers: number[]): Date {
    if (numbers) {
      return new Date(numbers[0], numbers[1] - 1, numbers[2]);
    } else {
      return new Date();
    }
  }

  isSelected(date: NgbDate): boolean {
    return this.selected_date != null && (this.selected_date.date[0] == date.year) &&
      (this.selected_date.date[1] == date.month) &&
      (this.selected_date.date[2] == date.day);
  }

  isPresented(date: NgbDate): boolean {
    return this.visitDates.findIndex(value =>
      ((value.date[0] == date.year) &&
        (value.date[1] == date.month) &&
        (value.date[2] == date.day))) !== -1;
  }

  test() {
    console.log(this.accomodations.map(value => value.ward).filter((value, index, self) => self.indexOf(value) === index));
  }
}
