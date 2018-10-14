import {Component, OnInit} from '@angular/core';
import {Visit} from "../../backend_types/visit";
import {DateService} from "../../service/date.service";
import {VisitService} from "../../service/visit.service";
import {AccomodationService} from "../../service/accomodation.service";
import {Accomodation} from "../../backend_types/accomodation";
import {Surgeon} from "../../backend_types/surgeon";
import {SurgeonService} from "../../service/surgeon.service";
import {Manager} from "../../backend_types/manager";
import {ManagerService} from "../../service/manager.service";
import {OperationType} from "../../backend_types/operation-type";
import {OperationTypeService} from "../../service/operation-type.service";
import {ClientService} from "../../service/client.service";
import {Client} from "../../backend_types/client";
import {VisitDate} from "../../backend_types/visit-date";
import {MatDialog} from "@angular/material";
import {DateSelectorDialogComponent} from "../../date/date-selector-dialog/date-selector-dialog.component";

@Component({
  selector: 'app-ward',
  templateUrl: './ward.component.html',
  styleUrls: ['./ward.component.css']
})
export class WardComponent implements OnInit {
  visits_of_date: Visit[] = [];
  placesOfWards: Visit[] = [];
  selected_date: Date = null;
  selected_visit_date: VisitDate = null;
  wards: number[] = [];
  clients: Client[] = [];
  // myControlClients = new FormControl();
  filteredClients: Client[];
  // @ViewChild('client_value', { read: MatAutocompleteTrigger }) autoComplete: MatAutocompleteTrigger;
  surgeons: Surgeon[] = [];
  managers: Manager[] = [];
  operation_types: OperationType[] = [];
  eyes: string[] = ["OU", "OS", "OD"];
  statuses: string[] = ["пацієнт", "супров."];

  constructor(
    private dateService: DateService,
    private visitService: VisitService,
    private clientService: ClientService,
    private surgeonService: SurgeonService,
    private managerService: ManagerService,
    private operationTypeService: OperationTypeService,
    private accomodationService: AccomodationService,
    private dialog: MatDialog) {

  }

  ngOnInit(): void {
    this.dateService.selected_date.subscribe((selected_visit_date: VisitDate) => {
      this.selected_date = new Date(selected_visit_date.date[0], selected_visit_date.date[1] - 1, selected_visit_date.date[2]);
      this.selected_visit_date = selected_visit_date;
      this.getVisits();
    });
    this.getWards();
    this.getSurgeons();
    this.getManagers();
    this.getOperationTypes();
    this.getClients();

  }


  getAllPlacesOfWards() {
    this.accomodationService.getAccomodations().toPromise().then((placesOfWards: Accomodation[]) => {
      this.placesOfWards = [];
      placesOfWards.forEach((accomodation: Accomodation) => {
        let isAdd: boolean = false;
        this.visits_of_date.forEach((visit: Visit) => {
          if (visit.accomodation.ward == accomodation.ward && visit.accomodation.wardPlace == accomodation.wardPlace) {
            this.placesOfWards.push(visit);
            isAdd = true;
          }
        });
        if (isAdd == false) {
          const visit = new Visit();
          visit.visitDate = this.selected_visit_date;
          visit.accomodation = accomodation;
          this.placesOfWards.push(visit);
        }
      });

    });
  }

  getVisits() {
    if (this.selected_date != null) {
      this.visitService.getVisitsWithWard(this.selected_date)
        .toPromise().then(visits_of_date => {
        this.visits_of_date = visits_of_date;
        this.getAllPlacesOfWards();

      });
    }
  }

  getWards() {
    this.accomodationService.getWards().toPromise().then(wards => this.wards = wards);
  }

  getClients() {
    this.clientService.getClients().toPromise().then(clients => {
      this.clients = clients;
    });
  }

  getSurgeons() {
    this.surgeonService.getSurgeons().toPromise().then(surgeons => this.surgeons = surgeons);
  }

  getManagers() {
    this.managerService.getManagers().toPromise().then(managers => this.managers = managers);
  }

  getOperationTypes() {
    this.operationTypeService.getProcedures().toPromise().then(operation_types => this.operation_types = operation_types);
  }

  changeSurgeon(place_in_ward: Visit, surgeon_id: number) {
    place_in_ward.isChanged = true;
    this.surgeons.forEach((surgeon: Surgeon) => {
      if (surgeon.surgeonId == surgeon_id)
        place_in_ward.surgeon = surgeon;
    });
  }

  changeManager(place_in_ward: Visit, manager_id: number) {
    place_in_ward.isChanged = true;
    this.managers.forEach((manager: Manager) => {
      if (manager.managerId == manager_id)
        place_in_ward.manager = manager;
    });
  }

  changeOperationType(place_in_ward: Visit, operation_type_id: number) {
    place_in_ward.isChanged = true;
    this.operation_types.forEach((operation_type: OperationType) => {
      if (operation_type.operationTypeId == operation_type_id)
        place_in_ward.operationType = operation_type;
    });
  }

  changeEye(place_in_ward: Visit, eye: string) {
    place_in_ward.isChanged = true;
    place_in_ward.eye = eye;
  }

  changeStatus(place_in_ward: Visit, status: string) {
    place_in_ward.isChanged = true;
    place_in_ward.status = status;
  }


  // Clients

  selectedClient(place_in_ward: Visit, client: Client) {
    if (place_in_ward && client && (place_in_ward.client == null || place_in_ward.client.clientId != client.clientId)) {
      console.log("selectedClient : ");
      console.log(place_in_ward);
      console.log(client);
      place_in_ward.isChanged = true;
      place_in_ward.client = client;
    }
  }

  filteringClients(client_value: string) {
    if (client_value) {
      let filterValue: string[] = client_value.toLowerCase().split(" ");

      console.log("filtering... : ");
      console.log(client_value);

      this.filteredClients = this.clients.filter(client => {
        switch (filterValue.length) {
          case 0:
            return true;
          case 1:
            return (client.surname.toLowerCase().indexOf(filterValue[0]) === 0);
          case 2:
            return (client.surname.toLowerCase().indexOf(filterValue[0]) === 0 && client.firstName.toLowerCase().indexOf(filterValue[1]) === 0);
          default:
            return (client.surname.toLowerCase().indexOf(filterValue[0]) === 0 && client.firstName.toLowerCase().indexOf(filterValue[1]) === 0 && client.secondName.toLowerCase().indexOf(filterValue[2]) === 0);
        }
      });
    }else this.filteredClients = this.clients;
  }

  displayFn(client?: Client): string | null {
    return client ? (client.surname + ' ' + client.firstName + ' ' + client.secondName) : null;
  }


  refactorTime(timeForCome: number[]): Date {
    if (timeForCome != null) {
      return new Date(1970, 0, 1, timeForCome[0], timeForCome[1]);
    }
  }

  changeTimeForCome(place_in_ward: Visit, HTMLElement: HTMLInputElement) {
    place_in_ward.isChanged = true;
    const hours = Number(HTMLElement.value.substring(0, 2));
    const minutes = Number(HTMLElement.value.substring(3, 5));

    if (HTMLElement.checkValidity()) {
      place_in_ward.timeForCome = [hours, minutes];
    } else if (hours < 9) {
      HTMLElement.value = "09:00";
      place_in_ward.timeForCome = [9, 0];
    } else {
      HTMLElement.value = "17:30";
      place_in_ward.timeForCome = [17, 30];
    }
  }

  onSave() {
    for (let i = 0; i < this.placesOfWards.length; i++) {
      if (this.placesOfWards[i].isChanged == true) {
        if (this.placesOfWards[i].visitId > 0) {
          this.visitService.editVisit(this.placesOfWards[i]).toPromise().then((visit: Visit) => {
            this.placesOfWards.splice(i, 1, visit);
          });
        } else if (this.placesOfWards[i].client != null && this.placesOfWards[i].status != null) {
          this.visitService.addVisit(this.placesOfWards[i]).toPromise().then((visit: Visit) => {
            this.placesOfWards.splice(i, 1, visit);
          });
        } else this.placesOfWards[i].isChanged = true;
      }
    }
  }

  onRefresh() {
    this.getVisits();
    this.ngOnInit();
  }

  onDelete() {
    this.placesOfWards.forEach((value: Visit, i: number) => {
      if (value.isChanged == true) {
        if (value.visitId > 0) {
          this.visitService.removeVisit(this.placesOfWards[i].visitId).toPromise().then(() => {
            let visit: Visit = new Visit();
            visit.accomodation = this.placesOfWards[i].accomodation;
            visit.visitDate = this.placesOfWards[i].visitDate;
            this.placesOfWards.splice(i, 1, visit);
          })
        } else {
          let visit: Visit = new Visit();
          visit.accomodation = this.placesOfWards[i].accomodation;
          visit.visitDate = this.placesOfWards[i].visitDate;
          this.placesOfWards.splice(i, 1, visit);
        }
      }
    })
  }

  onUnplaced() {
    this.placesOfWards.forEach((value: Visit, i: number) => {
      if (value.isChanged == true) {
        if (value.visitId > 0 && value.client != null && value.status == "пацієнт") {
          this.visitService.doUnplaced(this.placesOfWards[i]).toPromise().then(() => {
            let visit: Visit = new Visit();
            visit.accomodation = this.placesOfWards[i].accomodation;
            visit.visitDate = this.placesOfWards[i].visitDate;
            this.placesOfWards.splice(i, 1, visit);
          })

        } else if (value.visitId == 0 && value.client != null && value.status == "пацієнт") {
          this.visitService.addVisit(this.placesOfWards[i]).toPromise().then((visit: Visit) => {
            this.visitService.doUnplaced(visit).toPromise().then(() => {
              let visit: Visit = new Visit();
              visit.accomodation = this.placesOfWards[i].accomodation;
              visit.visitDate = this.placesOfWards[i].visitDate;
              this.placesOfWards.splice(i, 1, visit);
            })
          })

        } else if (value.visitId > 0 && value.status != "пацієнт") {
          this.visitService.removeVisit(this.placesOfWards[i].visitId).toPromise().then(() => {
            let visit: Visit = new Visit();
            visit.accomodation = this.placesOfWards[i].accomodation;
            visit.visitDate = this.placesOfWards[i].visitDate;
            this.placesOfWards.splice(i, 1, visit);
          })

        } else {
          let visit: Visit = new Visit();
          visit.accomodation = this.placesOfWards[i].accomodation;
          visit.visitDate = this.placesOfWards[i].visitDate;
          this.placesOfWards.splice(i, 1, visit);

        }
      }
    })
  }

  moveToAnotherDatePlace() {
    for (let i = 0; i < 5; i++) {

      this.openDialogSelectVisitDate();
    }
  }

  onCancel() {
    this.onRefresh();
  }


  openDialogSelectVisitDate() {

    const dialogRef = this.dialog.open(DateSelectorDialogComponent, {
      width: '700px',
      height: '410px',
      data: {visit_date: this.selected_visit_date, accomodation: Accomodation}
    });
    dialogRef.afterClosed().subscribe((data: { visit_date: VisitDate, accomodation: Accomodation }) => {
      if (data != null && data.visit_date != null) {

        this.placesOfWards.forEach((value, index) => {
          if (value.isChanged == true) {

            let visit: Visit = new Visit();
            visit.accomodation = value.accomodation;
            visit.visitDate = this.selected_visit_date;

            value.visitDate = data.visit_date;
            if (data.accomodation != null) {
              value.accomodation = data.accomodation;
            } else value.accomodation = null;

            if (value.visitId > 0) {
              this.visitService.editVisit(value).toPromise().then(() => {
                this.placesOfWards.splice(index, 1, visit);
              });

            } else if (value.client != null && value.status != null) {
              this.visitService.addVisit(value).toPromise().then(() => {
                this.placesOfWards.splice(index, 1, visit);
              });
            } else value.isChanged = false;
          }
        });
      }
    })
  }

}

/*

  onSave() {

    let new_placesOfWards: Visit[] = this.placesOfWards.filter((visit: Visit) => {
      return visit.isChanged && visit.visitId == 0;
    });
    if (new_placesOfWards.length > 0) {
      this.visitService.addVisits(new_placesOfWards).toPromise().then((visits: Visit[]) => {

        visits.forEach((visit: Visit) => {
          for (let i = 0; i < this.placesOfWards.length; i++) {
            if (this.placesOfWards[i].accomodation.ward == visit.accomodation.ward && this.placesOfWards[i].accomodation.wardPlace == visit.accomodation.wardPlace) {
              this.placesOfWards.splice(i, 1, visit);
            }
          }

        });
      });
    }

    let edit_placesOfWards: Visit[] = this.placesOfWards.filter((visit: Visit) => {
      return visit.isChanged && visit.visitId !== 0;
    });

    for (let i = 0; i < edit_placesOfWards.length; i++) {
      this.visitService.editVisit(edit_placesOfWards[i]).toPromise().then((changed_visit: Visit) => {
        edit_placesOfWards.splice(i, 1, visit);

        changed_visit.isChanged = false;
      });
    }
  }




  onChangeOperationOfPatient(visit: Visit) {
    console.log("onChangeOperationOfPatient " + " " + visit);
    this.openDialogChangeOperationOfPatient(visit);
  }

  openDialogChangeOperationOfPatient(visit: Visit) {
    const dialogRef = this.dialog.open(OperationEditorComponent, {
      width: '550px',
      height: '330px',
      data: {visit: visit}
    });
    dialogRef.afterClosed().subscribe(() => {
      console.log('The dialog was closed !');
    });
  }
}


  filteringClients() {
    this.filteredClients = this.myControlClients.valueChanges.pipe(
      startWith(''),
      map(value => {
        return this._filterClients(value);
      })
    );
  }

  private _filterClients(value: string): Client[] {
    if (value != null) {
      let filterValue: string[] = value.toLowerCase().split(" ");

      return this.clients.filter(option => {
        switch (filterValue.length) {
          case 0:
            return true;
          case 1:
            return (option.surname.toLowerCase().indexOf(filterValue[0]) === 0);
          case 2:
            return (option.surname.toLowerCase().indexOf(filterValue[0]) === 0 && option.firstName.toLowerCase().indexOf(filterValue[1]) === 0);
          default:
            return (option.surname.toLowerCase().indexOf(filterValue[0]) === 0 && option.firstName.toLowerCase().indexOf(filterValue[1]) === 0 && option.secondName.toLowerCase().indexOf(filterValue[2]) === 0);
        }
      });
    } else return this.clients;
  }

  onSelect(countryId) {
    this.selectedClient = null;
    for (var i = 0; i < this.clients.length; i++) {
      if (this.clients[i].clientId == countryId) {
        this.selectedClient = this.clients[i];
      }
    }
  }

*/
