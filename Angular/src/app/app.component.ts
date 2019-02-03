import {Component} from "@angular/core";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"]
})
export class AppComponent {

  constructor() {

  }
}


/*

export class AppComponent {

  @Output() isHospitalizations: Visit[];
  @Output() noHospitalizations: Visit[];
  selected_date: NgbDateStruct = null;

  constructor(private dialog: MatDialog,
              private hospitalizationService: VisitService,
  ) {
  }

  onSelect(selected_date: NgbDateStruct) {
    if (selected_date != null) {
      this.selected_date = selected_date;
      this.getHospitalizations(selected_date);
      this.getNotHospitalizations(selected_date);
    }
  }

  onChangePatient(patient_id: number) {
    console.info("Change Client: " + patient_id);
    this.openDialogChangePatient(patient_id);
  }

  getHospitalizations(selected_date: NgbDateStruct) {
    this.isHospitalizations = null;
    if (selected_date != null) {
      this.hospitalizationService.getHospitalizations(selected_date)
        .subscribe(isHospitalizations => this.isHospitalizations = isHospitalizations);
    }
  }

  getNotHospitalizations(selected_date: NgbDateStruct) {
    this.noHospitalizations = null;
    if (selected_date != null) {
      this.hospitalizationService.getNotHospitalizations(selected_date)
        .subscribe(noHospitalizations => this.noHospitalizations = noHospitalizations);
    }
  }

  addClient() {
    this.openDialogChangePatient(0);
  }

  openDialogChangePatient(patient_id: number) {
    const dialogRef = this.dialog.open(ClientEditorComponent, {
      width: '550px',
      height: '330px',
      data: {patientID: patient_id}
    });
    dialogRef.afterClosed().subscribe((result: Client) => {
      console.log('The dialog was closed !');
      if ((result != null) && (result.clientId > 0)) {
        this.getHospitalizations(this.selected_date);
        this.getNotHospitalizations(this.selected_date);
      }
    });
  }
}
*/
