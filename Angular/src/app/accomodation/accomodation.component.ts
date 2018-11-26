import {Component, OnInit} from "@angular/core";
import {NavbarService} from "../service/navbar.service";
import {Visit} from "../backend_types/visit";
import {DateService} from "../service/date.service";
import {VisitService} from "../service/visit.service";
import {AccomodationService} from "../service/accomodation.service";
import {Surgeon} from "../backend_types/surgeon";
import {SurgeonService} from "../service/surgeon.service";
import {Manager} from "../backend_types/manager";
import {ManagerService} from "../service/manager.service";
import {OperationType} from "../backend_types/operation-type";
import {OperationTypeService} from "../service/operation-type.service";
import {ClientService} from "../service/client.service";
import {Client} from "../backend_types/client";
import {VisitDate} from "../backend_types/visit-date";
import {MatDialog} from "@angular/material";
import {Accomodation} from "../backend_types/accomodation";
import {DateSelectorDialogComponent} from "../date/date-selector-dialog/date-selector-dialog.component";

@Component({
  selector: "app-accomodation",
  templateUrl: "./accomodation.component.html",
  styleUrls: ["./accomodation.component.css"]
})
export class AccomodationComponent implements OnInit {
  visits_of_date: Visit[] = [];
  visits_without_wards: Visit[] = [];
  visits_with_wards: Visit[] = [];
  wards: number[] = [];

  selected_date: Date = null;
  selected_visit_date: VisitDate = null;
  clients: Client[] = [];
  filteredClients: Client[];
  surgeons: Surgeon[] = [];
  managers: Manager[] = [];
  operation_types: OperationType[] = [];
  eyes: string[] = ["OU", "OS", "OD"];
  statuses: string[] = ["пацієнт", "супров."];

  constructor(
    private serviceNavbar: NavbarService,
    private dateService: DateService,
    private visitService: VisitService,
    private clientService: ClientService,
    private surgeonService: SurgeonService,
    private managerService: ManagerService,
    private operationTypeService: OperationTypeService,
    private accomodationService: AccomodationService,
    private dialog: MatDialog
  ) {
    this.serviceNavbar.change("accomodation");
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

  private getVisits() {
    if (this.selected_date !== null) {
      this.visitService.getVisits(this.selected_date)
        .toPromise().then(visits_of_date => {
        this.visits_of_date = visits_of_date;
        this.getVisitsWithWards();
        this.getVisitsWithoutWards();
      });
    }
  }

  private getVisitsWithWards() {
    this.accomodationService.getAccomodations().toPromise().then((accomodations: Accomodation[]) => {
      this.visits_with_wards = [];
      accomodations.forEach((accomodation: Accomodation) => {
        let isAdd = false;
        this.visits_of_date.forEach((visit: Visit) => {
          if (visit.accomodation
            && visit.accomodation.ward === accomodation.ward
            && visit.accomodation.wardPlace === accomodation.wardPlace) {

            this.visits_with_wards.push(visit);
            isAdd = true;
          }
        });
        if (!isAdd) {
          const visit = new Visit();
          visit.visitDate = this.selected_visit_date;
          visit.accomodation = accomodation;
          this.visits_with_wards.push(visit);
        }
      });
    });
  }

  private getVisitsWithoutWards() {
    this.visits_without_wards = this.visits_of_date.filter((visit: Visit) => visit.accomodation == null);
  }

  private getWards() {
    this.accomodationService.getWards().toPromise().then(wards => this.wards = wards);
  }

  private getClients() {
    this.clientService.getClients().toPromise().then(clients => {
      this.clients = clients;
    });
  }

  private getSurgeons() {
    this.surgeonService.getUnlockSurgeons().toPromise().then(surgeons => this.surgeons = surgeons);
  }

  private getManagers() {
    this.managerService.getManagers().toPromise().then(managers => this.managers = managers);
  }

  private getOperationTypes() {
    this.operationTypeService.getProcedures().toPromise().then(operation_types => this.operation_types = operation_types);
  }

  /*             Order For Come              */

  changeOrderForCome(visit: Visit, numberInOrder: number) {
    if (visit.orderForCome != numberInOrder) {
      if (this.validOrderForCome(visit, numberInOrder)) {
        visit.orderForCome = numberInOrder;
        visit.isChanged = true;
      }
    }
  }

  validOrderForCome(visit: Visit, numberInOrder: number): boolean {
    if (visit.orderForCome != numberInOrder) {
      let isFree = true;
      this.visits_of_date.forEach(value => {
        if (value.orderForCome == numberInOrder) {
          isFree = false;
        }
      });
      return isFree;
    } else {
      return true;
    }
  }


  /*             Time For Come              */

  refactorTime(timeForCome: number[]): Date {
    if (timeForCome) {
      return new Date(1970, 0, 1, timeForCome[0], timeForCome[1]);
    }
  }

  refactorDate(numbers: number[]): Date {
    if (numbers) {
      return new Date(numbers[0], numbers[1] - 1, numbers[2]);
    } else {
      return new Date();
    }
  }

  changeTimeForCome(visit: Visit, time_element: HTMLInputElement) {
    visit.isChanged = true;
    const hours = Number(time_element.value.substring(0, 2));
    const minutes = Number(time_element.value.substring(3, 5));
    if (time_element.checkValidity()) {
      visit.timeForCome = [hours, minutes];
    } else if (hours < 9) {
      time_element.value = "09:00";
      visit.timeForCome = [9, 0];
    } else {
      time_element.value = "17:30";
      visit.timeForCome = [17, 30];
    }
  }


  // Clients

  selectedClient(visit: Visit, client: Client) {
    if (visit && client && (!visit.client || visit.client.clientId !== client.clientId)) {
      visit.isChanged = true;
      visit.client = client;
    }
  }

  filteringClients(client_value: string) {
    if (client_value) {
      const filterValue: string[] = client_value.toLowerCase().split(" ");
      this.filteredClients = this.clients.filter(client => {
        switch (filterValue.length) {
          case 0:
            return true;
          case 1:
            return (client.surname.toLowerCase().indexOf(filterValue[0]) === 0);
          case 2:
            return (client.surname.toLowerCase().indexOf(filterValue[0]) === 0
              && client.firstName.toLowerCase().indexOf(filterValue[1]) === 0);
          default:
            return (client.surname.toLowerCase().indexOf(filterValue[0]) === 0
              && client.firstName.toLowerCase().indexOf(filterValue[1]) === 0
              && client.secondName.toLowerCase().indexOf(filterValue[2]) === 0);
        }
      });
    } else {
      this.filteredClients = this.clients;
    }
  }

  displayFn(client?: Client): string | null {
    return client ? (client.surname + " " + client.firstName + " " + client.secondName) : null;
  }

  validClient(client: Client, client_value: string): boolean {
    if (client) {
      const separateValue: string[] = client_value.toLowerCase().split(" ");
      switch (separateValue.length) {
        case 0:
          return false;
        case 1:
          return (client.surname.toLowerCase() === separateValue[0]);
        case 2:
          return (client.surname.toLowerCase() === separateValue[0] &&
            client.firstName.toLowerCase() === separateValue[1]);
        default:
          return (client.surname.toLowerCase() === separateValue[0] &&
            client.firstName.toLowerCase() === separateValue[1] &&
            client.secondName.toLowerCase() === separateValue[2]);
      }
    } else {
      return true;
    }
  }

  /*          Status             */

  changeStatus(place_in_ward: Visit, status: string) {
    place_in_ward.isChanged = true;
    place_in_ward.status = status;
  }


  changeSurgeon(place_in_ward: Visit, surgeon_id: number) {
    place_in_ward.isChanged = true;
    this.surgeons.forEach((surgeon: Surgeon) => {
      if (surgeon.surgeonId === surgeon_id) {
        place_in_ward.surgeon = surgeon;
      }
    });
  }

  changeManager(place_in_ward: Visit, manager_id: number) {
    place_in_ward.isChanged = true;
    this.managers.forEach((manager: Manager) => {
      if (manager.managerId === manager_id) {
        place_in_ward.manager = manager;
      }
    });
  }

  changeOperationType(place_in_ward: Visit, operation_type_id: number) {
    place_in_ward.isChanged = true;
    this.operation_types.forEach((operation_type: OperationType) => {
      if (operation_type.operationTypeId === operation_type_id) {
        place_in_ward.operationType = operation_type;
      }
    });
  }

  changeEye(place_in_ward: Visit, eye: string) {
    place_in_ward.isChanged = true;
    place_in_ward.eye = eye;
  }


  onAdd() {
    if (this.visits_of_date == null) {
      this.visits_of_date = [];
    }
    if (this.visits_of_date[0] == null) {
      this.visits_of_date[0] = new Visit();
    }
    if (this.visits_of_date[0].client != null) {
      const visit = new Visit();
      visit.status = "пацієнт";
      this.visits_of_date.unshift(visit);
      this.getVisitsWithoutWards();
    }
    document.getElementById("table_no_ward").scrollIntoView();
  }

  onSave() {
    for (let i = 0; i < this.visits_with_wards.length; i++) {
      if (this.visits_with_wards[i].isChanged === true) {
        if (this.visits_with_wards[i].visitId > 0) {
          this.visitService.editVisit(this.visits_with_wards[i]).toPromise().then((visit: Visit) => {
            this.visits_with_wards.splice(i, 1, visit);
          });
        } else if (this.visits_with_wards[i].client !== null && this.visits_with_wards[i].status !== null) {
          this.visitService.addVisit(this.visits_with_wards[i]).toPromise().then((visit: Visit) => {
            this.visits_with_wards.splice(i, 1, visit);
          });
        } else {
          this.visits_with_wards[i].isChanged = true;
        }
      }
    }
  }

  onRefresh() {
    this.getVisits();
    this.ngOnInit();
  }

  onDelete() {
    this.visits_with_wards.forEach((value: Visit, i: number) => {
      if (value.isChanged === true) {
        if (value.visitId > 0) {
          this.visitService.removeVisit(this.visits_with_wards[i].visitId).toPromise().then(() => {
            const visit: Visit = new Visit();
            visit.accomodation = this.visits_with_wards[i].accomodation;
            visit.visitDate = this.visits_with_wards[i].visitDate;
            this.visits_with_wards.splice(i, 1, visit);
          });
        } else {
          const visit: Visit = new Visit();
          visit.accomodation = this.visits_with_wards[i].accomodation;
          visit.visitDate = this.visits_with_wards[i].visitDate;
          this.visits_with_wards.splice(i, 1, visit);
        }
      }
    });
  }

  onUnplace() {
    this.visits_with_wards.forEach((value: Visit, i: number) => {
      if (value.isChanged === true) {
        if (value.visitId > 0 && value.client !== null) {
          this.visitService.doUnplaced(this.visits_with_wards[i]).toPromise().then(() => {
            const visit: Visit = new Visit();
            visit.accomodation = this.visits_with_wards[i].accomodation;
            visit.visitDate = this.visits_with_wards[i].visitDate;
            this.visits_with_wards.splice(i, 1, visit);
          });

        } else if (value.visitId === 0 && value.client !== null) {
          this.visitService.addVisit(this.visits_with_wards[i]).toPromise().then((visit: Visit) => {
            this.visitService.doUnplaced(visit).toPromise().then(() => {
              const visit: Visit = new Visit();
              visit.accomodation = this.visits_with_wards[i].accomodation;
              visit.visitDate = this.visits_with_wards[i].visitDate;
              this.visits_with_wards.splice(i, 1, visit);
            });
          });

/*        } else if (value.visitId > 0 && value.status !== "пацієнт") {
          this.visitService.removeVisit(this.visits_with_wards[i].visitId).toPromise().then(() => {
            const visit: Visit = new Visit();
            visit.accomodation = this.visits_with_wards[i].accomodation;
            visit.visitDate = this.visits_with_wards[i].visitDate;
            this.visits_with_wards.splice(i, 1, visit);
          });*/

        } else {
          const visit: Visit = new Visit();
          visit.accomodation = this.visits_with_wards[i].accomodation;
          visit.visitDate = this.visits_with_wards[i].visitDate;
          this.visits_with_wards.splice(i, 1, visit);

        }
      }
    });
  }

  moveToAnotherDatePlace() {

    this.visits_of_date.forEach((visit: Visit) => {
      if (visit.isChanged === true && visit.visitId > 0) {
        this.openDialogSelectVisitDate(visit);
      }
    });

    // for (let i = 0; i < 5; i++) {

    // this.openDialogSelectVisitDate();---------------------------------------------------
    // }
  }

  onCancel() {
    this.onRefresh();
  }

  openDialogSelectVisitDate(visit: Visit) {

    const dialogRef = this.dialog.open(DateSelectorDialogComponent, {
      width: "700px",
      height: "410px",
      data: {visit: visit}
    });
    dialogRef.afterClosed().subscribe((data: { visit: Visit }) => {
      this.onRefresh();
    });
  }

  /*  openDialogSelectVisitDate() {

      const dialogRef = this.dialog.open(DateSelectorDialogComponent, {
        width: "700px",
        height: "410px",
        data: {visit_date: this.selected_visit_date, accomodation: Accomodation}
      });
      dialogRef.afterClosed().subscribe((data: { visit_date: VisitDate, accomodation: Accomodation }) => {
        if (data !== null && data.visit_date !== null) {

          this.visits_of_date.forEach((value, index) => {
            if (value.isChanged === true) {

              const visit: Visit = new Visit();
              visit.accomodation = value.accomodation;
              visit.visitDate = this.selected_visit_date;

              value.visitDate = data.visit_date;
              if (data.accomodation !== null) {
                value.accomodation = data.accomodation;
              } else {
                value.accomodation = null;
              }

              if (value.visitId > 0) {
                this.visitService.editVisit(value).toPromise().then(() => {
                  this.visits_of_date.splice(index, 1, visit);
                });

              } else if (value.client !== null && value.status !== null) {
                this.visitService.addVisit(value).toPromise().then(() => {
                  this.visits_of_date.splice(index, 1, visit);
                });
              } else {
                value.isChanged = false;
              }
            }
          });
        }
      });
    }*/

}
