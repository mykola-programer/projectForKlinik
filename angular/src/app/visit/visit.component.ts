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

  selected_visit_date: VisitDate;

  visits_by_date: Visit[];

  wards: number[];
  accomodations: Accomodation[];
  patients: Client[];
  clients: Client[] = [];
  filteredClients: Client[];
  eyes: string[] = ["OU", "OS", "OD"];
  statuses: string[] = ["пацієнт", "супров."];
  operation_types: OperationType[];
  surgeons: Surgeon[];
  managers: Manager[];

  visitsFormArray: FormArray = this.fb.array([]);

  pageForm = this.fb.group({
    tablesLabel: this.fb.control(""),
    tablesForm: this.fb.array([])
  });

  validateTime = (control: AbstractControl) => {
    const timeValue: number[] = control.value ? (<string>control.value).split(":").map(value => Number(value)) : null;
    if (timeValue && (timeValue[0] < 9 || timeValue[0] > 17)) {
      return {invalidTime: true};
    } else {
      return null;
    }
  };

  validateQueue = (abstractControl: AbstractControl) => {
    if (abstractControl.value > 0) {
      let count = 0;

      // @ts-ignore
      (this.pageForm.get("tablesForm").controls
        .forEach(control => control.get("visitsForm").controls
          .forEach(value => {
            if (value.get("orderForCome").value === abstractControl.value) {
              count++;
            }
          })));
      if (count > 1) {
        return {invalidQueue: true};
      } else {
        return null;
      }
    } else {
      return null;
    }
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
    this.accomodationService.getAccomodations().toPromise().then((accomodations: Accomodation[]) => {
      this.accomodations = accomodations;
      this.wards = accomodations.map(value => value.ward).filter((value, index, self) => self.indexOf(value) === index);
      this.getVisits();
    });
  }

  private getOperationTypes() {
    this.operationTypeService.getProcedures().toPromise().then(operation_types => this.operation_types = operation_types);
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
      this.filteredClients = this.clients;
    });
  }

  private getVisits() {
    this.visits_loading = true;
    if (this.selected_visit_date !== null) {
      const selected_date: Date = new Date(this.selected_visit_date.date[0], this.selected_visit_date.date[1] - 1, this.selected_visit_date.date[2]);
      this.visitService.getVisitsByDate(selected_date)
        .toPromise().then((visits_by_date) => {
        // this.visits_by_date = this.replaceVisitsByWards(visits_by_date);
        this.visits_by_date = visits_by_date;
        this.visitsFormArray = this.assortVisitsToAccomodation(this.visits_by_date);
        this.pageForm.setControl("tablesLabel", this.fb.control("Операційний день - " + selected_date.toLocaleDateString()));
        this.splitVisitFormsToWards(this.visitsFormArray);
        this.visits_loading = false;
        this.getPatients();

      }).catch((err: HttpErrorResponse) => {
        this.visits_loading = false;
        this.calender_loading = true;
        this.toastMessageService.inform("Помилка !", err.error, "error");
      });
    }
  }

  assortVisitsToAccomodation(visits: Visit[]): FormArray {
    const visitsFormArray = this.fb.array([]);
    let visit = new Visit();
    visit.status = "пацієнт";
    visitsFormArray.push(this.createVisitFormGroup(visit));

    this.accomodations.forEach(accomodation => {
      let visit: Visit = visits.find(visit => visit.accomodationID == accomodation.accomodationId);
      if (visit) {
        visitsFormArray.push(this.createVisitFormGroup(visit));
      } else {
        visit = new Visit();
        visit.accomodationID = accomodation.accomodationId;
        visit.status = "пацієнт";
        visitsFormArray.push(this.createVisitFormGroup(visit));
      }
    });

    visits.filter(visit => visit.accomodationID === 0)
      .forEach(visit => visitsFormArray.push(this.createVisitFormGroup(visit)));
    return visitsFormArray;
  }

  private createVisitFormGroup(visit: Visit): FormGroup {
    return this.fb.group({
      accomodation: [visit.accomodationID],
      accomodation_ward: [visit.accomodationID > 0 ? this.accomodations.find(value => value.accomodationId === visit.accomodationID).ward : 0],
      accomodation_place: [visit.accomodationID > 0 ? this.accomodations.find(value => value.accomodationId === visit.accomodationID).wardPlace : 0],
      disable: [visit.accomodationID > 0 ? (this.accomodations.find(value => value.accomodationId === visit.accomodationID)).disable : false],
      visitId: [visit.visitId],
      timeForCome:
        [visit.timeForCome ? new Date(1970, 0, 1, visit.timeForCome[0], visit.timeForCome[1]).toLocaleTimeString().substring(0, 5) : null,
          [this.validateTime]],
      orderForCome: [visit.orderForCome, [Validators.min(0), Validators.max(100), this.validateQueue]],
      client: [visit.clientID],
      sex: [visit.clientID > 0 ? this.clients.find(value => value.clientId === visit.clientID).sex : ""],
      status: [visit.status],
      patient: [visit.patientID],
      operationType: [visit.operationTypeID],
      eye: [visit.eye],
      surgeon: [visit.surgeonID],
      manager: [visit.managerID],
      note: [visit.note],
      isChanged: [false],
    });
  }

  splitVisitFormsToWards(visitsFormArray: FormArray) {
    this.pageForm.setControl("tablesForm", this.fb.array([]));

    this.wards.forEach(ward => {
      (<FormArray>this.pageForm.get("tablesForm"))
        .push(this.fb.group({
          tableLabel: this.fb.control("Палата " + ward),
          visitsForm: this.fb.array(visitsFormArray.controls.filter(value => value.get("accomodation_ward").value == ward))
        }));
    });

    (<FormArray>this.pageForm.get("tablesForm"))
      .push(this.fb.group({
        tableLabel: this.fb.control("Безстаціонарні"),
        visitsForm: this.fb.array(visitsFormArray.controls.filter(value => value.get("accomodation_ward").value == 0))
      }));
  }

  displayFn = (clientID?: number): string => {
    // console.log(clientID);
    // console.log(this.clients);
    // return "Hello";
    if (clientID > 0) {
      const client: Client = this.clients.find(value => value.clientId == clientID);
      if (client) {
        return (client.surname + " " + client.firstName + " " + client.secondName);
      } else return "";
    } else return "";
  };

  getSelectedElementCount() {
    // TODO
    return 0;
  }

  test() {
    // console.log(this.no_wardForm);
    console.log(this.pageForm.get("tablesForm").value);
    // console.log(this.clients);
  }

  // private createTablesForm(visits: Visit[]): FormArray {
  //   const tablesForm = this.fb.array([]);
  //   this.wards.forEach(ward => {
  //     const filtered_visits = visits.filter(visit => {
  //       return visit.accomodation && visit.accomodation.ward === ward;
  //     });
  //     const wardTableForm = this.fb.group({
  //       tableLabel: this.fb.control("Палата " + ward),
  //       visitsForm: this.createVisitsTableForm(filtered_visits)
  //     });
  //     tablesForm.push(wardTableForm);
  //   });
  //
  //   {
  //     const filtered_visits = visits.filter(visit => (visit.accomodation == null));
  //     filtered_visits.unshift(new Visit());
  //     const wardTableForm = this.fb.group({
  //       tableLabel: this.fb.control("Безстаціонарні "),
  //       visitsForm: this.createVisitsTableForm(filtered_visits)
  //     });
  //     tablesForm.push(wardTableForm);
  //   }
  //   this.visits_loading = false;
  //   this.calender_loading = false;
  //   return tablesForm;
  // }

  // private createVisitsTableForm(visits: Visit[]): FormArray {
  //   const visits_tableForm = this.fb.array([]);
  //   visits.forEach((visit: Visit) => {
  //     visits_tableForm.push(this.createVisitFormGroup(visit));
  //   });
  //   return visits_tableForm;
  // }

  // private createVisitFormGroup(visit: Visit): FormGroup {
  //   const form_group = this.fb.group({
  //     visitId: [visit.visitId],
  //     timeForCome:
//       [visit.timeForCome ? new Date(1970, 0, 1, visit.timeForCome[0], visit.timeForCome[1]).toLocaleTimeString().substring(0, 5) : null,
  //         [this.validateTime]],
  //     orderForCome: [visit.orderForCome, [Validators.min(1), Validators.max(100), this.validateQueue]],
  //     client: [visit.client],
  //     client2: [visit.client ? visit.client.clientId : 0],
  //     sex: [visit.client ? visit.client.sex : null],
  //     status: [visit.status],
  //     patient: [visit.patient ? visit.patient.clientId : 0],
  //     operationType: [visit.operationType ? visit.operationType.operationTypeId : 0],
  //     eye: [visit.eye],
  //     surgeon: [visit.surgeon ? visit.surgeon.surgeonId : 0],
  //     manager: [visit.manager ? visit.manager.managerId : 0],
  //     accomodation: [visit.accomodation ? visit.accomodation.wardPlace : null],
  //     note: [visit.note],
  //     disable: [visit.accomodation ? visit.accomodation.disable : false],
  //     isChanged: [false],
  //   });
  //   return form_group;
  // }

  // private getVisitsWithoutWards() {
  //   this.visits_without_wards = this.visits_by_date.filter((visit: Visit) => visit.accomodation == null);
  //   this.no_wardForm.setControl("no_ward_tableForm", this.createVisitsTableForm(this.visits_without_wards));
  //
  // }

  // private replaceVisitsByWards(visits: Visit[]): Visit[] {
  //   const result: Visit[] = [];
  //   if (this.accomodations) {
  //     // replace Visits to Wards
  //     this.accomodations.forEach((accomodation: Accomodation) => {
  //       const found_visit = visits.find((visit: Visit) => {
  //         return (visit.accomodation
  //           && visit.accomodation.ward === accomodation.ward
  //           && visit.accomodation.wardPlace === accomodation.wardPlace);
  //       });
  //       if (found_visit) {
  //         result.push(found_visit);
  //       } else {
  //         const new_visit = new Visit();
  //         new_visit.visitDate = this.selected_visit_date;
  //         new_visit.accomodation = accomodation;
  //         result.push(new_visit);
  //       }
  //     });
  //
  //     // add NoWard Visits
  //     visits.forEach((visit: Visit) => {
  //       if (visit.accomodation == null) {
  //         result.push(visit);
  //       }
  //     });
  //   } else {
  //     this.getAccomodations();
  //   }
  //
  //   {
  //     this.patients = [];
  //     result.forEach((visit: Visit) => {
  //       if (visit.client && visit.status === "пацієнт") {
  //         this.patients.push(visit.client);
  //       }
  //     });
  //   }
  //   this.calender_loading = false;
  //   this.visits_loading = false;
  //
  //   return result;
  // }


  changeState(visitForm: AbstractControl) {
    visitForm.get("isChanged").patchValue(visitForm.valid);
  }

  changeClients(visitForm: AbstractControl) {
    visitForm.get("isChanged").patchValue(visitForm.valid);
    const client: Client = this.clients.find(client => client.clientId == visitForm.get("client").value);
    visitForm.get("sex").patchValue(client ? client.sex : "");
    this.getPatients();
  }

  private getPatients() {
    this.patients = [];
    const patient_ids = new Set();
    (<FormArray>this.pageForm.get("tablesForm")).controls
      .forEach(control => (<FormArray>control.get("visitsForm")).controls
        .forEach(value => {
          if (value.get("client").value > 0 && value.get("status").value == "пацієнт") {
            patient_ids.add(Number(value.get("client").value));
          }
        }));
    (<FormArray>this.pageForm.get("tablesForm")).controls
      .forEach(control => (<FormArray>control.get("visitsForm")).controls
        .forEach(value => {
          if (value.get("patient").value > 0) {
            patient_ids.add(Number(value.get("patient").value));
          }
        }));
    patient_ids.forEach(value => {
      this.patients.push(this.clients.find(client => client.clientId == value));
    })

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
