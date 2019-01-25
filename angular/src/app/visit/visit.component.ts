import {Component, OnInit} from "@angular/core";
import {NavbarService} from "../service/navbar.service";
import {VisitService} from "../service/visit.service";
import {AccomodationService} from "../service/accomodation.service";
import {VisitDate} from "../backend_types/visit-date";
import {DateService} from "../service/date.service";
import {AbstractControl, FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Visit} from "../backend_types/visit";
import {Accomodation} from "../backend_types/accomodation";
import {HttpErrorResponse} from "@angular/common/http";
import {ToastMessageService} from "../service/toast-message.service";
import {Client} from "../backend_types/client";
import {ClientService} from "../service/client.service";
import {debounceTime} from "rxjs/operators";
import {OperationType} from "../backend_types/operation-type";
import {OperationTypeService} from "../service/operation-type.service";
import {Surgeon} from "../backend_types/surgeon";
import {Manager} from "../backend_types/manager";
import {SurgeonService} from "../service/surgeon.service";
import {ManagerService} from "../service/manager.service";

@Component({
  selector: "app-visit",
  templateUrl: "./visit.component.html",
  styleUrls: ["./visit.component.css"]
})

export class VisitComponent implements OnInit {

  visits_loading = false;
  save_loading = false;
  del_loading = false;
  displace_loading = false;
  calender_loading = false;
  hidden_visits = true;

  selected_date: Date;
  selected_visit_date: VisitDate;

  visits_by_date: Visit[];

  wards: number[];
  accomodations: Accomodation[];
  patients: Client[];
  clients: Client[];
  filteredClients: Client[];

  eyes: string[] = ["OU", "OS", "OD"];
  statuses: string[] = ["пацієнт", "супров."];
  operation_types: OperationType[];
  surgeons: Surgeon[];
  managers: Manager[];

  operationTablesForm = this.fb.group({
    tablesLabel: this.fb.control(""),
    tablesForm: this.fb.array([])
  });

  validateTime = (control: AbstractControl) => {
    const timeValue: number[] = control.value ? control.value.split(":").map(value => Number(value)) : null;
    if (timeValue && (timeValue[0] < 9 || timeValue[0] > 17)) {
      return {invalidTime: true};
    } else {
      return null;
    }
  };

  validateOrder = (control: AbstractControl) => {
// TODO Method
    return null;
  };

  constructor(private navbarService: NavbarService,
              private dateService: DateService,
              private accomodationService: AccomodationService,
              private clientService: ClientService,
              private surgeonService: SurgeonService,
              private managerService: ManagerService,
              private operationTypeService: OperationTypeService,
              private visitService: VisitService,
              private toastMessageService: ToastMessageService,
              private fb: FormBuilder
  ) {
  }

  ngOnInit() {
    this.navbarService.change("visit");
    this.calender_loading = true;

    this.dateService.selected_date.subscribe(
      (selected_visit_date: VisitDate) => {
        this.calender_loading = false;
        this.visits_loading = true;
        this.selected_visit_date = selected_visit_date;
        this.selected_date = new Date(selected_visit_date.date[0], selected_visit_date.date[1] - 1, selected_visit_date.date[2]);
        this.getAccomodations();
        this.getClients();
        this.getOperationTypes();
        this.getSurgeons();
        this.getManagers();
      }
    );
  }

  private getAccomodations() {
    this.visits_loading = true;
    this.accomodationService.getAccomodations().toPromise().then(accomodations => {
      this.accomodations = accomodations;
      this.wards = accomodations.map(value => value.ward).filter((value, index, self) => self.indexOf(value) === index);
      this.getVisits();
    });
  }

  private getOperationTypes() {
    this.operationTypeService.getActiveProcedures().toPromise().then(operation_types => this.operation_types = operation_types);
  }
  private getSurgeons() {
    this.surgeonService.getSurgeons().toPromise().then(surgeons => this.surgeons = surgeons);
  }
  private getManagers() {
    this.managerService.getManagers().toPromise().then(managers => this.managers = managers);
  }
  private getClients() {
    this.clientService.getClients().toPromise().then(clients => {
      this.clients = clients;
      this.filteredClients = clients;
    });
  }

  private getVisits() {
    this.visits_loading = true;
    if (this.selected_date !== null) {
      this.visitService.findVisits(this.selected_date)
        .toPromise().then((visits_by_date) => {
        this.visits_by_date = this.replaceVisitsByWards(visits_by_date);

        this.operationTablesForm.setControl("tablesLabel", this.fb.control("Операційний день - " + this.selected_date.toLocaleDateString()));
        this.operationTablesForm.setControl("tablesForm", this.createTablesForm(this.visits_by_date));
      }).catch((err: HttpErrorResponse) => {
        this.visits_loading = false;
        // this.calender_loading = true;
        this.toastMessageService.inform("Помилка !", err.error, "error");
      });
    }
  }

  displayFn(client?: Client): string | null {
    if (client) {
      if (typeof client === "string") {
        return client;
      } else if (typeof client === "object") {
        return (client.surname + " " + client.firstName + " " + client.secondName);
      }
    }
    return null;
  }

  getSelectedElementCount() {
    return 0;
  }

  test() {
    // console.log(this.no_wardForm);
    console.log(this.operationTablesForm);
  }

  private createTablesForm(visits: Visit[]): FormArray {
    const tablesForm = this.fb.array([]);
    this.wards.forEach(ward => {
      const filtered_visits = visits.filter(visit => (visit.accomodation && visit.accomodation.ward === ward));
      const wardTableForm = this.fb.group({
        tableLabel: this.fb.control("Палата " + ward),
        visitsForm: this.createVisitsTableForm(filtered_visits)
      });
      tablesForm.push(wardTableForm);
    });

    {
      const filtered_visits = visits.filter(visit => (visit.accomodation == null));
      filtered_visits.unshift(new Visit());
      const wardTableForm = this.fb.group({
        tableLabel: this.fb.control("Безстаціонарні "),
        visitsForm: this.createVisitsTableForm(filtered_visits)
      });
      tablesForm.push(wardTableForm);
    }
    this.visits_loading = false;
    this.calender_loading = false;
    return tablesForm;
  }

  private createVisitsTableForm(visits: Visit[]): FormArray {
    const visits_tableForm = this.fb.array([]);
    visits.forEach((visit: Visit) => {
      visits_tableForm.push(this.createVisitFormGroup(visit));
    });
    return visits_tableForm;
  }

  private createVisitFormGroup(visit: Visit): FormGroup {
    const form_group = this.fb.group({
      visitId: [visit.visitId],
      timeForCome:
        [visit.timeForCome ? new Date(1970, 0, 1, visit.timeForCome[0], visit.timeForCome[1]).toLocaleTimeString().substring(0, 5) : null,
          [this.validateTime]],
      orderForCome: [visit.orderForCome, [Validators.min(1), Validators.max(100), this.validateOrder]],
      client: [visit.client],
      client2: [visit.client ? visit.client.clientId : 0],
      sex: [visit.client ? visit.client.sex : null],
      status: [visit.status],
      patient: [visit.patient ? visit.patient.clientId : 0],
      operationType: [visit.operationType ? visit.operationType.operationTypeId : 0],
      eye: [visit.eye],
      surgeon: [visit.surgeon ? visit.surgeon.surgeonId : 0],
      manager: [visit.manager ? visit.manager.managerId : 0],
      accomodation: [visit.accomodation ? visit.accomodation.wardPlace : null],
      note: [visit.note],
      inactive: [visit.accomodation ? visit.accomodation.inactive : false],
      isChanged: [false],
    });
    return form_group;
  }

  // private getVisitsWithoutWards() {
  //   this.visits_without_wards = this.visits_by_date.filter((visit: Visit) => visit.accomodation == null);
  //   this.no_wardForm.setControl("no_ward_tableForm", this.createVisitsTableForm(this.visits_without_wards));
  //
  // }

  private replaceVisitsByWards(visits: Visit[]): Visit[] {
    const result: Visit[] = [];
    if (this.accomodations) {
      // replace Visits to Wards
      this.accomodations.forEach((accomodation: Accomodation) => {
        const found_visit = visits.find((visit: Visit) => {
          return (visit.accomodation
            && visit.accomodation.ward === accomodation.ward
            && visit.accomodation.wardPlace === accomodation.wardPlace);
        });
        if (found_visit) {
          result.push(found_visit);
        } else {
          const new_visit = new Visit();
          new_visit.visitDate = this.selected_visit_date;
          new_visit.accomodation = accomodation;
          result.push(new_visit);
        }
      });

      // add NoWard Visits
      visits.forEach((visit: Visit) => {
        if (visit.accomodation == null) {
          result.push(visit);
        }
      });
    } else {
      this.getAccomodations();
    }

    {
      this.patients = [];
      result.forEach((visit: Visit) => {
        if (visit.client && visit.status === "пацієнт") {
          this.patients.push(visit.client);
        }
      });
    }
    this.calender_loading = false;
    this.visits_loading = false;

    return result;
  }


  private filterClients(client_value) {
    console.log(client_value);
    if (typeof client_value === "string") {
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
  }

  search(formControl: FormControl) {
    formControl.valueChanges
      .pipe(debounceTime(1000))
      .subscribe(value => this.filterClients(value));
  }

}
