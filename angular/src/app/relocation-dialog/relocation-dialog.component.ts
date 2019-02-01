import {Component, Inject, Injectable, OnInit} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {NgbCalendar, NgbDatepickerConfig, NgbDatepickerI18n, NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {NgbDate} from "@ng-bootstrap/ng-bootstrap/datepicker/ngb-date";
import {VisitDate} from "../backend_types/visit-date";
import {Accomodation} from "../backend_types/accomodation";
import {ToastMessageService} from "../service/toast-message.service";
import {VisitDateService} from "../service/visit-date.service";
import {Visit} from "../backend_types/visit";
import {Client} from "../backend_types/client";
import {AccomodationService} from "../service/accomodation.service";
import {VisitService} from "../service/visit.service";
import {HttpErrorResponse} from "@angular/common/http";

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
  selector: "app-relocation-dialog",
  templateUrl: "./relocation-dialog.component.html",
  styleUrls: ["./relocation-dialog.component.css"],
  providers: [I18n, {provide: NgbDatepickerI18n, useClass: EditedDatepickerI18n}] // define custom NgbDatepickerI18n provider
})
export class RelocationDialogComponent implements OnInit {

  minDate: NgbDate = NgbDate.from(this.calendar.getToday());
  maxDate: NgbDate = new NgbDate(this.calendar.getToday().year + 5, 12, 31);
  visitDates: VisitDate[] = [];
  selected_date: VisitDate = this.data.visitDate;
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
    private dateService: VisitDateService,
    @Inject(MAT_DIALOG_DATA) public data: {
      visit: Visit,
      client: Client,
      visitDate: VisitDate
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
    this.dateService.getVisitDates().toPromise().then((visitDates: VisitDate[]) => {
      this.visitDates = visitDates;
      setTimeout(() => this.dates_loading = false,1000);
    });
  }

  onSelectDate(date: NgbDateStruct, disabled) {

    if (!disabled) {
      this.selected_accomodationID = 0;
      this.selected_date = this.visitDates.find((value: VisitDate) =>
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
        setTimeout(() => this.visits_loading = false,500);
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
      setTimeout(() => this.accomodations_loading = false, 1000 );
    });
  }

  onMove() {
      this.data.visit.accomodationID = this.selected_accomodationID;
      this.data.visit.visitDateID = this.selected_date.visitDateId;
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
