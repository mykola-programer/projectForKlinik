import {Component, Inject} from '@angular/core';
// import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {Client} from "../backend_types/client";
import {Operation} from "../backend_types/operation";
import {Visit} from "../backend_types/visit";
import {AccomodationService} from "../service/accomodation.service";
import {Observable} from "rxjs";
import {FormControl} from "@angular/forms";
import {SurgeonService} from "../service/surgeon.service";
import {ManagerService} from "../service/manager.service";
import {OperationTypeService} from "../service/operation-type.service";
import {OperationType} from "../backend_types/operation-type";
import {ClientService} from "../service/client.service";

@Component({
  selector: 'app-operation-editor',
  templateUrl: './operation-editor.component.html',
  styleUrls: ['./operation-editor.component.css']
})
export class OperationEditorComponent {
  public client: Client = new Client();
  public operationOfPatient: Operation = new Operation();
  public visit: Visit = new Visit();
  public procedures: OperationType[] = [];

  // myControlSurgeons = new FormControl();
  // myControlManagers = new FormControl();
  surgeons: string[] = [];
  managers: string[] = [];
  readonly eyes: string[] = ["OD", "OS", "OU"];
  filteredSurgeons: Observable<string[]>;
  filteredManagers: Observable<string[]>;

  constructor(
    private patientService: ClientService,
    private surgeonService: SurgeonService,
    private managerService: ManagerService,
    private operationService: AccomodationService,
    private procedureService: OperationTypeService,
    // public dialogRef: MatDialogRef<OperationEditorComponent>,
    // @Inject(MAT_DIALOG_DATA) public data: any
  )
  {
  }

  /*
    ngOnInit() {
      this.visit = this.data.visit;
      this.getPatient(this.visit.client.clientId);
      if (this.visit.operationType != null) this.getOperationOfPatient(this.visit.operationType.operationTypeId);
      this.getProcedures();
      this.surgeonService.getSurgeons().toPromise().then((surgeons: string[]) => this.surgeons = surgeons);
      this.managerService.getManagers().toPromise().then((managers: string[]) => this.managers = managers);


      this.filteredSurgeons = this.myControlSurgeons.valueChanges.pipe(
        startWith(''),
        map(value => this._filterSurgeons(value))
      );

      this.filteredManagers = this.myControlManagers.valueChanges.pipe(
        startWith(''),
        map(value => this._filterManagers(value))
      );
    }

    getPatient(patient_id: number) {
      // console.info("Start getClient " + patient_id);
      if (patient_id != null && patient_id > 0) {
        this.patientService.getClient(patient_id).toPromise().then((patient: Client) => this.client = patient);
      } else this.client = new Client();
    }

    getOperationOfPatient(operation_id: number) {
      if (operation_id != null && operation_id > 0) {
        console.log(operation_id);
        this.operationService.getOperation(operation_id).toPromise().then((operationOfPatient: Operation) => this.operationOfPatient = operationOfPatient);
      } else this.operationOfPatient = new Operation();
    }

    getProcedures() {
      this.procedureService.getProcedures().toPromise().then((procedures: OperationType[]) => this.procedures = procedures);
    }

    private _filterSurgeons(value: string): string[] {
      const filterValue = value.toLowerCase();

      return this.surgeons.filter(option => option.toLowerCase().indexOf(filterValue) === 0);
    }

    private _filterManagers(value: string): string[] {
      const filterValue = value.toLowerCase();

      return this.managers.filter(option => option.toLowerCase().indexOf(filterValue) === 0);
    }

    onCancel(){

    }
    onSave(){

    }*/
}
