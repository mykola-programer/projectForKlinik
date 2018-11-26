import {Component, Inject, Injectable, OnInit} from "@angular/core";
import {NgbDatepickerConfig, NgbDatepickerI18n, NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {VisitDateService} from "../../service/visit-date.service";
import {Router} from "@angular/router";
import {VisitDate} from "../../backend_types/visit-date";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {Accomodation} from "../../backend_types/accomodation";
import {AccomodationService} from "../../service/accomodation.service";
import {Visit} from "../../backend_types/visit";
import {VisitService} from "../../service/visit.service";
import {HttpErrorResponse} from "@angular/common/http";

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
  selector: "app-date-selector-dialog",
  templateUrl: "./date-selector-dialog.component.html",
  styleUrls: ["./date-selector-dialog.component.css"],
  providers: [I18n, {provide: NgbDatepickerI18n, useClass: EditedDatepickerI18n}] // define custom NgbDatepickerI18n provider

})
export class DateSelectorDialogComponent implements OnInit {
  model;
  private readonly year_now: number = new Date(Date.now()).getFullYear();
  private readonly month_now: number = new Date(Date.now()).getMonth() + 1;
  private readonly day_now: number = new Date(Date.now()).getDate();
  minDate: NgbDateStruct = {year: this.year_now, month: this.month_now, day: this.day_now};
  maxDate: NgbDateStruct = {year: this.year_now + 1, month: 12, day: 31};
  visitDates: VisitDate[] = [];
  selected_date: VisitDate = null;
  accomodations: Accomodation[] = [];
  selected_accomodation: Accomodation = null;

  constructor(
    public dialogRef: MatDialogRef<DateSelectorDialogComponent>,
    private router: Router,
    private config: NgbDatepickerConfig,
    private dateService: VisitDateService,
    private accomodationService: AccomodationService,
    private visitService: VisitService,
    @Inject(MAT_DIALOG_DATA) public data: { visit: Visit }) {
    this.selected_date = this.data.visit.visitDate;
    this.selected_accomodation = this.data.visit.accomodation;
  }

  ngOnInit(): void {
    this.setNgbDatepickerConfig();
    this.getDates();
    this.getVisits();
  }


  onSelectDate(date: NgbDateStruct, disabled) {

    if (!disabled) {
      this.selected_date = this.visitDates.find((value: VisitDate) =>
        ((value.date[0] == date.year) &&
          (value.date[1] == date.month) &&
          (value.date[2] == date.day)));
      this.getVisits();
    }
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
  }

  refactorDate(numbers: number[]): Date {
    if (numbers) {
      return new Date(numbers[0], numbers[1] - 1, numbers[2]);
    } else {
      return new Date();
    }
  }

  // This is selected dates
  isSelected(date: NgbDateStruct): boolean {
    return this.selected_date != null && (this.selected_date.date[0] == date.year) &&
      (this.selected_date.date[1] == date.month) &&
      (this.selected_date.date[2] == date.day);
  }

  isPresented(date: NgbDateStruct): boolean {
    return this.visitDates.findIndex(value =>
      ((value.date[0] == date.year) &&
        (value.date[1] == date.month) &&
        (value.date[2] == date.day))) != -1;
  }

  onSelect() {
    this.data.visit.visitDate = this.selected_date;
    this.data.visit.accomodation = this.selected_accomodation;
    this.visitService.editVisit(this.data.visit).toPromise().then((visit: Visit) => {
      if (visit
        && this.data.visit.visitId === visit.visitId
        && this.data.visit.client.clientId === visit.client.clientId) {
        this.dialogRef.close(visit);
      } else {
        alert("Помилка запису в базу даних... Спробуйте ще !");
        this.onRefresh();
      }

    }).catch((err: HttpErrorResponse) => {

      const div = document.createElement("div");
      div.innerHTML = err.error.text;
      const text = div.textContent || div.innerText || "";

      alert(text);
    });
  }

  onRefresh() {
    this.selected_date = this.data.visit.visitDate;
    this.selected_accomodation = this.data.visit.accomodation;
    this.ngOnInit();
  }

  onCancel() {
    this.dialogRef.close();
  }

  changeAccomodation(accomodation_id: number) {
    if (accomodation_id > 0) {
      this.selected_accomodation = this.accomodations.find((accomodation: Accomodation) => {
        return accomodation.accomodationId == accomodation_id;
      });
    } else {
      this.selected_accomodation = null;
    }
  }

  private getDates(): void {
    this.dateService.getVisitDates().toPromise().then(visitDates => this.visitDates = visitDates);
  }

  private getVisits() {
    if (this.selected_date != null) {
      this.visitService.getVisitsWithWard(new Date(this.selected_date.date[0], this.selected_date.date[1] - 1, this.selected_date.date[2]))
        .toPromise().then((visits_of_date: Visit[]) => {
        this.getAllFreeAccomodations(visits_of_date);
      });
    }
  }

  private getAllFreeAccomodations(visits_of_date: Visit[]) {
    this.accomodationService.getAccomodations().toPromise().then((accomodations: Accomodation[]) => {
      this.accomodations = accomodations;

      visits_of_date.forEach((visit: Visit) => {
        const index = this.accomodations.findIndex((accomodation: Accomodation) => {
          return visit != null && visit.accomodation != null && accomodation != null &&
            visit.accomodation.ward == accomodation.ward && visit.accomodation.wardPlace == accomodation.wardPlace;
        });
        this.accomodations.splice(index, 1);
      });


    });
  }

}
