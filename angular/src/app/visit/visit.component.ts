import {Compiler, Component, OnDestroy, OnInit} from "@angular/core";
import {VisitService} from "../service/visit.service";
import {AccomodationService} from "../service/accomodation.service";
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
import {MatDialog} from "@angular/material";
import {RelocationDialogComponent} from "../relocation-dialog/relocation-dialog.component";
import {Subscription} from "rxjs";
import {DatePlan} from "../backend_types/date-plan";
import {GlobalService} from "../service/global.service";
import {Department} from "../backend_types/department";

@Component({
  selector: "app-visit",
  templateUrl: "./visit.component.html",
  styleUrls: ["./visit.component.css"]
})

export class VisitComponent implements OnInit, OnDestroy {

  save_loading = false;
  del_loading = false;
  displace_loading = false;
  hidden_visits = false;

  selected_visit_date: DatePlan;

  visits_by_date: Visit[];
  number_of_changed_values = 0;

  wards: number[];
  accomodations: Accomodation[];
  patients: Client[] = [];
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

  calender_loading = false;
  private clients_loading = false;
  private accomodation_loading = false;
  private operationType_loading = false;
  private surgeons_loading = false;
  private managers_loading = false;
  private visits_loading = false;
  private form_building = false;

  isLoadingOFF(): boolean {
    return !(this.clients_loading
      || this.accomodation_loading
      || this.operationType_loading
      || this.managers_loading
      || this.visits_loading
      || this.form_building
      || this.surgeons_loading);
  };

  validateTime = (control: AbstractControl) => {
    const timeValue: number[] = control.value ? (<string>control.value).split(":").map(value => Number(value)) : null;
    if (timeValue && (timeValue[0] < 9 || timeValue[0] > 17)) {
      return {invalidTime: true};
    } else {
      return null;
    }
  };

  validateQueue = (abstractControl: AbstractControl) => {
    if (abstractControl.value != 0) {
      let count = 0;

      ((<FormArray>this.pageForm.get("tablesForm")).getRawValue()
        .forEach(tablesForm => tablesForm.visitsForm
          .forEach(visitsForm => {
            if (visitsForm.orderForCome == abstractControl.value) {
              count++;
            }
          })));
      if (count > 1) {
        return {invalidQueue: true};
      } else {
        return null;
      }
    } else {
      return {nullQueue: true};
    }
  };

  validateClient = (abstractControl: AbstractControl) => {
    if (abstractControl.value != 0) {
      let count = 0;

      ((<FormArray>this.pageForm.get("tablesForm")).getRawValue()
        .forEach(tablesForm => tablesForm.visitsForm
          .forEach(visitsForm => {
            if (visitsForm.client == abstractControl.value) {
              count++;
            }
          })));
      if (count > 1) {
        return {invalidClient: true};
      } else {
        return null;
      }
    } else {
      return {nullClient: true};
    }
  };

  validatePatient = (abstractControl: AbstractControl) => {
    if (abstractControl.value != 0) {
      let count = 0;

      ((<FormArray>this.pageForm.get("tablesForm")).getRawValue()
        .forEach(tablesForm => tablesForm.visitsForm
          .forEach(visitsForm => {
            if (visitsForm.patient == abstractControl.value) {
              count++;
            }
          })));
      if (count > 1) {
        return {invalidPatient: true};
      } else {
        return null;
      }
    } else {
      return null;
    }
  };

  private selected_date_Subscriber: Subscription;
  private page_form_Subscriber: Subscription;

  constructor(private globalService: GlobalService,
              private accomodationService: AccomodationService,
              private clientService: ClientService,
              private surgeonService: SurgeonService,
              private managerService: ManagerService,
              private operationTypeService: OperationTypeService,
              private visitService: VisitService,
              private toastMessageService: ToastMessageService,
              private dialog: MatDialog,
              private compiler: Compiler,
              private fb: FormBuilder
  ) {
    this.globalService.emittedDepartment.subscribe((selectedDepartment: Department) => {
    });
  }

  ngOnInit() {
    this.globalService.changeNavbar("visit");
    this.calender_loading = true;
    // console.log("Init Visit Editor !!!");

    this.selected_date_Subscriber = this.globalService.selected_datePlan.subscribe(
      (selected_visit_date: DatePlan) => {
        this.calender_loading = false;
        this.selected_visit_date = selected_visit_date;
        this.getAccomodations();
        this.getClients();
        this.getOperationTypes();
        this.getSurgeons();
        this.getManagers();
      }
    );

    this.page_form_Subscriber = this.pageForm.valueChanges
      .pipe(debounceTime(400))
      .subscribe((value) => {
        this.number_of_changed_values = 0;
        this.patients = [];
        const patient_ids = new Set();
        value.tablesForm.forEach(tableForm => tableForm.visitsForm.forEach((form_value: FormValue) => {
          if (form_value.isChanged) {
            this.number_of_changed_values++;
          }
          if (form_value.client > 0 && form_value.status == "пацієнт") {
            patient_ids.add(Number(form_value.client));
          }
          if (form_value.patient > 0) {
            patient_ids.add(Number(form_value.patient));
          }
        }));

        patient_ids.forEach(value => {
          this.patients.push(this.clients.find(client => client.clientId == value));
        });
        this.patients.sort(this.compareClients);
      });
  }

  ngOnDestroy(): void {
    this.selected_date_Subscriber.unsubscribe();
    this.page_form_Subscriber.unsubscribe();
    this.compiler.clearCache();
  }

  private getAccomodations() {
    this.accomodation_loading = true;
    this.accomodationService.getAccomodations().toPromise().then((accomodations: Accomodation[]) => {
      this.accomodations = accomodations;
      this.wards = accomodations.map(value => value.ward).filter((value, index, self) => self.indexOf(value) === index);
      this.getVisits();
      this.accomodation_loading = false;
    }).catch((err: HttpErrorResponse) => {
      this.accomodation_loading = true;
      this.toastMessageService.inform("Сервер недоступний!",
        "Спробуйте пізніше !" + "<br>" + err.error + "<br>" + err.message, "error", 10000);
      setTimeout(() => {
        this.getAccomodations();
      }, 15000);
    });
  }

  private getOperationTypes() {
    this.operationType_loading = true;
    this.operationTypeService.getProcedures().toPromise().then(operation_types => {
      this.operation_types = operation_types;
      this.operationType_loading = false;
    }).catch((err: HttpErrorResponse) => {
      this.operationType_loading = true;
      this.toastMessageService.inform("Сервер недоступний!",
        "Спробуйте пізніше !" + "<br>" + err.error + "<br>" + err.message, "error", 10000);
      setTimeout(() => {
        this.getOperationTypes();
      }, 15000);
    });
  }

  private getSurgeons() {
    this.surgeons_loading = true;
    this.surgeonService.getSurgeons().toPromise().then(surgeons => {
      this.surgeons = surgeons;
      this.surgeons_loading = false;
    }).catch((err: HttpErrorResponse) => {
      this.surgeons_loading = true;
      this.toastMessageService.inform("Сервер недоступний!",
        "Спробуйте пізніше !" + "<br>" + err.error + "<br>" + err.message, "error", 10000);
      setTimeout(() => {
        this.getSurgeons();
      }, 15000);
    });
  }

  private getManagers() {
    this.managers_loading = true;
    this.managerService.getManagers().toPromise().then(managers => {
      this.managers = managers;
      this.managers_loading = false;
    }).catch((err: HttpErrorResponse) => {
      this.managers_loading = true;
      this.toastMessageService.inform("Сервер недоступний!",
        "Спробуйте пізніше !" + "<br>" + err.error + "<br>" + err.message, "error", 10000);
      setTimeout(() => {
        this.getManagers();
      }, 15000);
    });
  }

  private getClients() {
    this.clients_loading = true;
    this.clientService.getClients().toPromise().then(clients => {
      this.clients = clients;
      this.filteredClients = this.clients;
      this.clients_loading = false;
    }).catch((err: HttpErrorResponse) => {
      this.clients_loading = true;
      this.toastMessageService.inform("Сервер недоступний!",
        "Спробуйте пізніше !" + "<br>" + err.error + "<br>" + err.message, "error", 10000);
      setTimeout(() => {
        this.getClients();
      }, 15000);
    });
  }

  private getVisits() {
    this.visits_loading = true;
    if (this.selected_visit_date && this.selected_visit_date.date) {
      const selected_date: Date = new Date(this.selected_visit_date.date[0], this.selected_visit_date.date[1] - 1, this.selected_visit_date.date[2]);
      this.visitService.getVisitsByDate(selected_date)
        .toPromise().then((visits_by_date) => {
        // this.visits_by_date = this.replaceVisitsByWards(visits_by_date);
        this.visits_by_date = visits_by_date;
        this.visitsFormArray = this.assortVisitsToAccomodation(this.visits_by_date);
        this.pageForm.setControl("tablesLabel", this.fb.control("Операційний день - " + selected_date.toLocaleDateString()));
        this.splitVisitFormsToWards(this.visitsFormArray);
        this.visits_loading = false;

      }).catch((err: HttpErrorResponse) => {
        this.visits_loading = false;
        this.calender_loading = true;
        this.toastMessageService.inform("Сервер недоступний!",
          "Спробуйте пізніше !" + "<br>" + err.error + "<br>" + err.message, "error", 10000);
        setTimeout(() => {
          this.getAccomodations();
        }, 15000);
      });
    }
  }

  assortVisitsToAccomodation(visits: Visit[]): FormArray {
    this.form_building = true;
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
    this.form_building = false;
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
        [visit.timeForCome ? (new Date(1970, 0, 1, visit.timeForCome[0], visit.timeForCome[1])).toLocaleTimeString().substring(0, 5) : null,
          [this.validateTime]],
      orderForCome: [visit.orderForCome, [Validators.min(0), Validators.max(100), this.validateQueue]],
      client: [visit.clientID, [this.validateClient]],
      sex: [visit.clientID > 0 ? this.clients.find(value => value.clientId === visit.clientID).sex : ""],
      status: new FormControl({
        value: visit.status,
        disabled: visit.accomodationID > 0 ? (this.accomodations.find(value => value.accomodationId === visit.accomodationID)).disable : false
      }),
      patient: [visit.patientID, [this.validatePatient]],
      operationType: [visit.operationTypeID],
      eye: [visit.eye],
      surgeonPlan: [visit.surgeonPlanId],
      manager: [visit.managerID],
      note: [visit.note],
      isChanged: [false],
    });
  }

  splitVisitFormsToWards(visitsFormArray: FormArray) {
    this.form_building = true;
    const tablesForm = this.fb.array([]);

    this.wards.forEach(ward => {
      tablesForm.push(this.fb.group({
        tableLabel: this.fb.control("Палата " + ward),
        visitsForm: this.fb.array(visitsFormArray.controls.filter(value => value.get("accomodation_ward").value == ward))
      }));
    });

    tablesForm.push(this.fb.group({
      tableLabel: this.fb.control("Безстаціонарні"),
      visitsForm: this.fb.array(visitsFormArray.controls.filter(value => value.get("accomodation_ward").value == 0))
    }));
    this.pageForm.setControl("tablesForm", tablesForm);
    this.form_building = false;
  }

  displayFn = (clientID?: number): string => {
    if (clientID > 0) {
      const client: Client = this.clients.find(value => value.clientId == clientID);
      if (client) {
        return (client.surname + " " + client.firstName + " " + client.secondName);
      } else return "";
    } else return "";
  };

  changeState(visitForm: AbstractControl) {
    if (!visitForm.value.disable) {
      if (visitForm.value.isChanged != visitForm.valid) {
        visitForm.get("isChanged").patchValue(visitForm.valid);
      }
    } else {
      visitForm.get("isChanged").patchValue(false);
    }
  }

  changeClients(visitForm: AbstractControl) {
    this.changeState(visitForm);
    const client: Client = this.clients.find(client => client.clientId == visitForm.get("client").value);
    visitForm.get("sex").patchValue(client ? client.sex : "");
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

  private compareClients = (client1, client2) => {
    if (client1.clientId === 0) {
      return false;
    } else if (client2.clientId === 0) {
      return true;
    } else if (client1.surname && client2.surname && client1.surname.localeCompare(client2.surname) !== 0) {
      return client1.surname.localeCompare(client2.surname);
    } else if (client1.firstName && client2.firstName && client1.firstName.localeCompare(client2.firstName) !== 0) {
      return client1.firstName.localeCompare(client2.firstName);
    } else if (client1.secondName && client2.secondName && client1.secondName.localeCompare(client2.secondName) !== 0) {
      return client1.secondName.localeCompare(client2.secondName);
    }
  };

  onSave(isDisplace?: boolean) {
    this.save_loading = true;
    const control: AbstractControl = this.visitsFormArray.controls.find((abstractControl: AbstractControl) => {
      return abstractControl.get("isChanged").value;
    });
    if (control && control.value) {
      const edited_visit: Visit = this.convertToVisit(control.value);
      if (isDisplace) {
        edited_visit.accomodationID = 0;
      }
      if (edited_visit && edited_visit.visitId > 0) {
        this.visitService.editVisit(edited_visit).toPromise().then((visit: Visit) => {
          control.get("isChanged").setValue(false);
          this.toastMessageService.inform("Збережено !", "Візит успішно збережений !", "success");
          this.onSave(isDisplace);
        }).catch((err: HttpErrorResponse) => {
            this.error_saving(err);
          }
        );
      } else if (edited_visit && edited_visit.visitId === 0) {
        this.visitService.addVisit(edited_visit).toPromise().then((visit: Visit) => {
          control.get("isChanged").setValue(false);
          this.toastMessageService.inform("Збережено !", "Візит успішно збережений !", "success");
          this.onSave(isDisplace);
        }).catch((err: HttpErrorResponse) => {
            this.error_saving(err);
          }
        );
      }
    } else {
      this.save_loading = false;
      this.onRefresh();
    }
  }

  private error_saving(err: HttpErrorResponse) {
    this.save_loading = false;
    if (err.status === 422) {
      this.toastMessageService.inform("Помилка при збережені! <br> Параметри візиту не відповідають критеріям !",
        err.error, "error");
    } else if (err.status === 404) {
      this.toastMessageService.inform("Помилка при збережені!",
        err.error + "<br> Обновіть сторінку та спробуйте знову.", "error");
    } else if (err.status === 409) {
      this.toastMessageService.inform("Помилка при збережені! <br> Конфлікт в базі даних !",
        err.error + "<br> Обновіть сторінку та спробуйте знову.", "error");
    } else {
      this.toastMessageService.inform("Помилка при збережені!",
        err.error + "<br>" + "HTTP status: " + err.status, "error");
    }
  }

  onDelete() {
    this.del_loading = true;
    const control: AbstractControl = this.visitsFormArray.controls.find((abstractControl: AbstractControl) => {
      return abstractControl.get("isChanged").value;
    });
    if (control && control.value) {
      const visit_for_del: Visit = this.convertToVisit(control.value);
      if (visit_for_del.visitId > 0) {
        this.visitService.removeVisit(visit_for_del.visitId).toPromise().then(() => {
          control.get("isChanged").setValue(false);
          this.success_deleting();
        }).catch((err: HttpErrorResponse) => {
          this.error_deleting(err);
        });
      } else {
        control.get("isChanged").setValue(false);
        this.onDelete();
      }
    } else {
      this.del_loading = false;
      this.onRefresh();
    }
  }

  private success_deleting(visit?: Visit) {
    this.toastMessageService.inform("Видалено !", "Візит успішно видалений !", "success");
    this.onDelete();
  }

  private error_deleting(err: HttpErrorResponse) {
    this.del_loading = false;
    if (err.status === 409) {
      this.toastMessageService.inform("Помилка при видалені!", "Конфлікт в базі даних!", "error");
    } else {
      this.toastMessageService.inform("Помилка при видалені!",
        err.error + "<br>" + "HTTP status: " + err.status, "error");
    }
  }

  onDisplace() {
    this.onSave(true);
  }

  moveToAnotherDatePlace() {
    let control: AbstractControl = this.visitsFormArray.controls.find((abstractControl: AbstractControl) => {
      return abstractControl.get("isChanged").value;
    });
    if (control && control.value) {
      const visit: Visit = this.convertToVisit(control.value);
      const dialogRef = this.dialog.open(RelocationDialogComponent, {
        width: "700px",
        height: "410px",
        data: {
          visit: visit,
          client: this.clients.find(client => client.clientId == visit.clientID),
          visitDate: this.selected_visit_date
        }
      });

      dialogRef.afterClosed().subscribe((changed_visit: Visit) => {
          if (changed_visit) {
            this.toastMessageService.inform("Перенесено !", "Візит успішно перенесений!", "success");
            control.get("isChanged").setValue(false);
            this.moveToAnotherDatePlace();
          } else {
            this.toastMessageService.inform("Відміна !", "", "warning", 1000);
          }
        },
        () => {
          this.toastMessageService.inform("Помилка !", "Відбулась критична помилка !", "error");
        },
      );
    } else {
      this.onRefresh();
    }
  }

  onRefresh() {
    this.getAccomodations();
    this.getClients();
    this.getOperationTypes();
    this.getSurgeons();
    this.getManagers();
  }

  // test() {
  //   // console.log(this.no_wardForm);
  //   // console.log(this.pageForm.get("tablesForm").value);
  //   // console.log(this.clients);
  //   // this.getAllValuesFromForm();
  // console.log(this.pageForm);
  //   // console.log((<FormArray>this.pageForm.get("tablesForm")).getRawValue());
  // }

  private convertToVisit(formValue: FormValue): Visit {
    const visit: Visit = new Visit();
    visit.visitId = formValue.visitId;
    //TODO !!!!!!!
    // visit.visitDateID = this.selected_visit_date.datePlanId;
    visit.clientID = formValue.client;
    visit.status = formValue.status;
    visit.accomodationID = formValue.accomodation;
    // @ts-ignore
    visit.timeForCome = formValue.timeForCome ? <number[]>(<string>formValue.timeForCome).split(":").map(value => Number(value)) : [];
    visit.orderForCome = formValue.orderForCome;
    visit.managerID = formValue.manager;
    visit.patientID = formValue.patient;
    visit.note = formValue.note;

    visit.status == "пацієнт" ? visit.operationTypeID = formValue.operationType : visit.operationTypeID = 0;
    visit.status == "пацієнт" ? visit.eye = formValue.eye : visit.eye = null;
    // TODO !!!!!!
    //  visit.status == "пацієнт" ? visit.surgeonID = formValue.surgeonPlan : visit.surgeonID = 0;

    visit.isChanged = false;
    return visit;
  }
}

interface FormValue {
  accomodation: number;
  accomodation_ward: number;
  accomodation_place: number;
  disable: boolean;
  visitId: number;
  timeForCome: Date;
  orderForCome: number;
  client: number;
  sex: string;
  status: string;
  patient: number;
  operationType: number;
  eye: string;
  surgeon: number;
  manager: number;
  note: string;
  isChanged: boolean;
}

