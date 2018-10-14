import {Component, OnInit} from '@angular/core';
import {DateService} from "../../service/date.service";
import {VisitService} from "../../service/visit.service";
import {NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {Visit} from "../../backend_types/visit";
import {FormControl} from "@angular/forms";
import {Client} from "../../backend_types/client";
import {map, startWith} from "rxjs/operators";
import {Observable} from "rxjs";
import {ClientService} from "../../service/client.service";
import {SurgeonService} from "../../service/surgeon.service";
import {ManagerService} from "../../service/manager.service";
import {OperationTypeService} from "../../service/operation-type.service";
import {Surgeon} from "../../backend_types/surgeon";
import {Manager} from "../../backend_types/manager";
import {OperationType} from "../../backend_types/operation-type";
import {VisitDate} from "../../backend_types/visit-date";

@Component({
  selector: 'app-no-ward',
  templateUrl: './no-ward.component.html',
  styleUrls: ['./no-ward.component.css']
})
export class NoWardComponent implements OnInit {
  visits_without_wards: Visit[] = [];
  selected_date: Date = null;
  selected_visit_date: VisitDate = null;
  clients: Client[] = [];
  myControlClients = new FormControl();
  filteredClients: Observable<Client[]>;
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
    private operationTypeService: OperationTypeService) {

  }

  ngOnInit() {
    this.dateService.selected_date.subscribe((selected_visit_date: VisitDate) => {
      this.selected_date = new Date(selected_visit_date.date[0], selected_visit_date.date[1]-1, selected_visit_date.date[2]);
      this.selected_visit_date = selected_visit_date;
      this.getVisits();
    });
    this.getSurgeons();
    this.getManagers();
    this.getOperationTypes();

    this.getClients();
    this.filteringClients();

  }

  getVisits() {
    if (this.selected_date != null) {
      this.visitService.getVisitsWithoutWard(this.selected_date)
        .toPromise().then(visits_with_wards => {
          if (visits_with_wards != null) {
            this.visits_without_wards = visits_with_wards;
          }else this.visits_without_wards = [];
      });
    }
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

  changeSurgeon(visit: Visit, surgeon_id: number) {
    visit.isChanged = true;
    this.surgeons.forEach((surgeon: Surgeon) => {
      if (surgeon.surgeonId == surgeon_id)
        visit.surgeon = surgeon;
    });
  }

  changeManager(visit: Visit, manager_id: number) {
    visit.isChanged = true;
    this.managers.forEach((manager: Manager) => {
      if (manager.managerId == manager_id)
        visit.manager = manager;
    });
  }

  changeOperationType(visit: Visit, operation_type_id: number) {
    visit.isChanged = true;
    this.operation_types.forEach((operation_type: OperationType) => {
      if (operation_type.operationTypeId == operation_type_id)
        visit.operationType = operation_type;
    });
  }

  changeEye(visit: Visit, eye: string) {
    visit.isChanged = true;
    visit.eye = eye;
  }

  changeStatus(visit: Visit, status: string) {
    console.log(status);
    visit.isChanged = true;
    visit.status = status;
  }

  changeClient(visit: Visit, client_id: number) {
    visit.isChanged = true;
    this.clients.forEach((client: Client) => {
      if (client.clientId == client_id)
        visit.client = client;
    });
  }

  refactorTime(timeForCome: number[]): Date {
    if (timeForCome != null) {
      return new Date(1970, 0, 1, timeForCome[0], timeForCome[1]);
    }
  }

  changeTimeForCome(visit: Visit, HTMLElement: HTMLInputElement) {
    visit.isChanged = true;
    const hours = Number(HTMLElement.value.substring(0, 2));
    const minutes = Number(HTMLElement.value.substring(3, 5));

    if (HTMLElement.checkValidity()) {
      visit.timeForCome = [hours, minutes];
    } else if (hours < 9) {
      HTMLElement.value = "09:00";
      visit.timeForCome = [9, 0];
    } else {
      HTMLElement.value = "17:30";
      visit.timeForCome = [17, 30];
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
      const filterValue = value.toLowerCase();
      return this.clients.filter(option => {
        return (option.surname.toLowerCase().includes(filterValue) ||
          option.firstName.toLowerCase().includes(filterValue) ||
          option.secondName.toLowerCase().includes(filterValue))
      });
    } else return this.clients;
  }

  onAdd() {
    if (this.visits_without_wards == null) this.visits_without_wards = [];
    if (this.visits_without_wards[0] == null) this.visits_without_wards[0] = new Visit();
    if (this.visits_without_wards[0].client != null) {
      let visit = new Visit();
      visit.status = "пацієнт";
      this.visits_without_wards.unshift(visit);
    }
  }

  onRefresh() {
    this.getVisits();
    this.ngOnInit();
  }

  onCancel() {
    this.onRefresh();
    // this.router.navigateByUrl("");

  }
}
