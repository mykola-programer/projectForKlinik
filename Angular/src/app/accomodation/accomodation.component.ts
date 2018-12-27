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
import {HttpErrorResponse} from "@angular/common/http";
import {MassageResponse} from "../backend_types/massage-response";

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
  patients: Client[] = [];
  filteredClients: Client[];
  filteredPatients: Client[];
  surgeons: Surgeon[] = [];
  managers: Manager[] = [];
  operation_types: OperationType[] = [];
  eyes: string[] = ["OU", "OS", "OD"];
  statuses: string[] = ["пацієнт", "супров."];

  loading_visits = false;
  loading_calender = false;
  loading_displace = false;
  loading_save = false;


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
    this.loading_calender = true;
    this.dateService.selected_date.subscribe((selected_visit_date: VisitDate) => {
      this.loading_calender = false;
      this.loading_visits = true;
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
    this.loading_visits = true;
    if (this.selected_date !== null) {
      this.visitService.findVisits(this.selected_date)
        .toPromise().then(visits_of_date => {
        this.visits_of_date = visits_of_date;
        this.getVisitsWithoutWards();
        this.getVisitsWithWards();
      });
    }
  }

  private getVisitsWithWards() {
    this.accomodationService.getActiveAccomodations().toPromise().then((accomodations: Accomodation[]) => {
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

      this.patients = [];
      this.visits_with_wards.forEach((visit: Visit) => {
        if (visit.client && visit.status === "пацієнт") {
          this.patients.push(visit.client);
        }
      });
      this.loading_visits = false;
      this.loading_calender = false;

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
    this.surgeonService.getActiveSurgeons().toPromise().then(surgeons => this.surgeons = surgeons);
  }

  private getManagers() {
    this.managerService.getManagers().toPromise().then(managers => this.managers = managers);
  }

  private getOperationTypes() {
    this.operationTypeService.getActiveProcedures().toPromise().then(operation_types => this.operation_types = operation_types);
  }

  /*             Order For Come              */

  changeOrderForCome(visit: Visit, numberInOrder: number) {
    if (visit.orderForCome !== numberInOrder) {
      if (this.validOrderForCome(visit, numberInOrder)) {
        visit.orderForCome = numberInOrder;
        visit.isChanged = true;
      }
    }
  }

  validOrderForCome(visit: Visit, numberInOrder: number): boolean {
    if (visit.orderForCome != numberInOrder) {
      for (let i = 0; i < this.visits_of_date.length; i++) {
        if (this.visits_of_date[i].orderForCome == numberInOrder) {
          return false;
        }
      }
      return true;
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

  filterClients(client_value: string) {
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


  // Patients

  selectedPatient(visit: Visit, patient: Client) {
    if (visit && patient && (!visit.patient || visit.patient.clientId !== patient.clientId)) {
      visit.isChanged = true;
      visit.patient = patient;
    }
  }

  filterPatients(patient_value: string) {
    if (patient_value) {
      const filterValue: string[] = patient_value.toLowerCase().split(" ");
      this.filteredPatients = this.patients.filter(patient => {
        switch (filterValue.length) {
          case 0:
            return true;
          case 1:
            return (patient.surname.toLowerCase().indexOf(filterValue[0]) === 0);
          case 2:
            return (patient.surname.toLowerCase().indexOf(filterValue[0]) === 0
              && patient.firstName.toLowerCase().indexOf(filterValue[1]) === 0);
          default:
            return (patient.surname.toLowerCase().indexOf(filterValue[0]) === 0
              && patient.firstName.toLowerCase().indexOf(filterValue[1]) === 0
              && patient.secondName.toLowerCase().indexOf(filterValue[2]) === 0);
        }
      });
    } else {
      this.filteredPatients = this.patients;
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
      if (surgeon.surgeonId == surgeon_id) {
        place_in_ward.surgeon = surgeon;
      }
    });
  }

  changeManager(place_in_ward: Visit, manager_id: number) {
    place_in_ward.isChanged = true;
    this.managers.forEach((manager: Manager) => {
      if (manager.managerId == manager_id) {
        place_in_ward.manager = manager;
      }
    });
  }

  changeOperationType(place_in_ward: Visit, operation_type_id: number) {
    place_in_ward.isChanged = true;
    this.operation_types.forEach((operation_type: OperationType) => {
      if (operation_type.operationTypeId == operation_type_id) {
        place_in_ward.operationType = operation_type;
      }
    });
  }

  changeEye(place_in_ward: Visit, eye: string) {
    place_in_ward.isChanged = true;
    place_in_ward.eye = eye;
  }


  onAdd() {
    if (this.visits_of_date[0] == null || this.visits_of_date[0].client != null) {
      const visit = new Visit();
      visit.status = "пацієнт";
      visit.visitDate = this.selected_visit_date;
      this.visits_of_date.unshift(visit);
      this.getVisitsWithoutWards();
    }
    document.getElementById("table_no_ward").scrollIntoView();
  }

  onSave() {
    this.loading_save = true;
    const changedVisits = this.visits_of_date.filter((visit: Visit) => {
      return visit.isChanged && visit.client && visit.client.clientId > 0;
    });

    if (changedVisits.length !== 0) {
      this.visitService.putVisits(changedVisits).toPromise().then((visits: Visit[]) => {
        alert("Візити успішно збережені !");
        this.getClients();
        this.loading_save = false;
        this.onRefresh();
      }).catch((err: HttpErrorResponse) => {
        this.loading_save = false;
        alert(
          ((<MassageResponse> err.error).exceptionMassage != null ? (<MassageResponse> err.error).exceptionMassage : "") + " \n" +
          ((<MassageResponse> err.error).validationMassage != null ? (<MassageResponse> err.error).validationMassage : ""));
      });
    } else {
      this.loading_save = false;
      alert("Виберіть хоча б один запис!");
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

  onDisplace() {
    this.loading_displace = true;
    const changedVisits = this.visits_with_wards.filter((visit: Visit) => {
      return visit.isChanged && visit.client && visit.client.clientId > 0;
    });

    if (changedVisits.length !== 0) {
      this.visitService.displaceVisits(changedVisits).toPromise().then((visits: Visit[]) => {
        alert("Клієнти успішно виселені !");
        this.getClients();
        this.loading_displace = false;
        this.onRefresh();
      }).catch((err: HttpErrorResponse) => {
        this.loading_displace = false;
        alert(
          ((<MassageResponse> err.error).exceptionMassage != null ? (<MassageResponse> err.error).exceptionMassage : "") + " \n" +
          ((<MassageResponse> err.error).validationMassage != null ? (<MassageResponse> err.error).validationMassage : ""));
      });
    } else {
      this.loading_displace = false;
      alert("Виберіть хоча б один запис!");
    }
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

}
