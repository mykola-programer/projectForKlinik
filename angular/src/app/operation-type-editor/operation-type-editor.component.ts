import {Component, OnInit} from "@angular/core";
import {AbstractControl, FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {OperationType} from "../types/operation-type";
import {ToastMessageService} from "../service/toast-message.service";
import {OperationTypeService} from "../service/operation-type.service";
import {debounceTime} from "rxjs/operators";
import {HttpErrorResponse} from "@angular/common/http";
import {GlobalService} from "../service/global.service";

@Component({
  selector: "app-operation-type-editor",
  templateUrl: "./operation-type-editor.component.html",
  styleUrls: ["./operation-type-editor.component.css"]
})
export class OperationTypeEditorComponent implements OnInit {
  public operation_types: OperationType[] = [];
  public count_of_operation_type = 0;

  searchForm: FormGroup = this.fb.group({
    searchControlForm: ["", [Validators.maxLength(50), Validators.pattern("[A-Za-zА-Яа-яЁёІіЇїЄє ]*")]],
  });

  operationTypesForm: FormArray = this.fb.array([]);
  tableForm: FormGroup = this.fb.group({
    operationTypesForm: this.operationTypesForm
  });

  hidden_operation_types = false;
  operation_types_loading = false;
  save_loading = false;
  del_loading = false;

  // "true" - ASC,
  // "false" - DESC
  sorting_order = true;


  constructor(private operationTypeService: OperationTypeService,
              private globalService: GlobalService,
              private toastMessageService: ToastMessageService,
              private fb: FormBuilder) {
  }

  ngOnInit() {
    this.globalService.changeNavbar("operation-type");
    this.operation_types_loading = true;
    this.getOperationTypes();
    this.searchForm.get("searchControlForm").valueChanges
      .pipe(debounceTime(900))
      .subscribe(search_value => this.filterOperationTypes(search_value));

    this.tableForm.valueChanges.pipe(debounceTime(600)).subscribe(() => {
      this.count_of_operation_type = (<OperationType[]>this.tableForm.get("operationTypesForm").value)
        .filter((operationType: OperationType) => {
          return operationType.isChanged;
        }).length;
    });
  }

  getOperationTypes() {
    this.operation_types_loading = true;
    this.operationTypeService.getProcedures().toPromise().then((operationTypes: OperationType[]) => {
      if (operationTypes) {
        this.operation_types = operationTypes;
      } else {
        this.operation_types = [];
        this.operation_types.push(new OperationType());
      }
      this.operationTypesForm = this.updateFormGroups(this.operation_types);
      this.tableForm.setControl("operationTypesForm", this.operationTypesForm);
      this.operation_types_loading = false;
      setTimeout(() => {
        document.getElementById("search").focus();
      });
    }).catch((err: HttpErrorResponse) => {
      this.operation_types_loading = true;
      this.toastMessageService.inform("Сервер недоступний!",
        "Спробуйте пізніше !" + "<br>" + err.error + "<br>" + err.message, "error", 10000);
      setTimeout(() => {
        this.getOperationTypes();
      }, 15000);
    });
  }

  onAdd() {
    const operation_type = new OperationType();
    operation_type.operationTypeId = 0;
    operation_type.disable = false;
    operation_type.isChanged = false;
    this.searchForm.get("searchControlForm").setValue("");
    if (this.operationTypesForm.controls[0].valid) {
      this.operationTypesForm.insert(0, this.createFormGroup(operation_type));
    }
    setTimeout(() => {
      document.getElementById("name").focus();
    }, 1000);
    window.scroll(0, 0);
  }

  onSave() {
    this.save_loading = true;
    // @ts-ignore
    const control = (<AbstractControl>(this.tableForm.get("operationTypesForm").controls).find((abstractControl: AbstractControl) => {
      return abstractControl.get("isChanged").value;
    }));

    if (control && control.value) {
      const edited_operation_type: OperationType = control.value;
      if (edited_operation_type.operationTypeId > 0) {
        this.operationTypeService.editProcedure(edited_operation_type).toPromise().then((operationType: OperationType) => {
          control.get("isChanged").setValue(false);
          this.success_saving(operationType);
        }).catch((err: HttpErrorResponse) => {
            this.error_saving(err);
          }
        );
      } else {
        this.operationTypeService.addProcedure(edited_operation_type).toPromise().then((operationType: OperationType) => {
          control.get("isChanged").setValue(false);
          this.success_saving(operationType);
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

  private success_saving(operationType?: OperationType) {
    this.toastMessageService.inform("Збережено !", "Операція успішно збережена !", "success");
    this.onSave();
  }

  private error_saving(err: HttpErrorResponse) {
    this.save_loading = false;
    if (err.status === 422) {
      this.toastMessageService.inform("Помилка при збережені! <br> Операція не відповідає критеріям !",
        err.error, "error");
    } else if (err.status === 404) {
      this.toastMessageService.inform("Помилка при збережені!",
        err.error + "<br> Обновіть сторінку та спробуйте знову.", "error");
    } else if (err.status === 409) {
      this.toastMessageService.inform("Помилка при збережені! <br> Конфлікт в базі даних !",
        err.error + "<br> Обновіть сторінку та спробуйте знову. <br> Можливо ваша операція існує серед прихованих.", "error");
    } else {
      this.toastMessageService.inform("Помилка при збережені!",
        err.error + "<br>" + "HTTP status: " + err.status, "error");
    }
  }

  onDelete() {
    this.del_loading = true;
    // @ts-ignore
    const control = (<AbstractControl>(this.tableForm.get("operationTypesForm").controls).find((abstractControl: AbstractControl) => {
      return abstractControl.get("isChanged").value;
    }));

    if (control && control.value) {
      const operation_type_for_del: OperationType = control.value;
      if (operation_type_for_del.operationTypeId > 0) {
        this.operationTypeService.removeProcedure(operation_type_for_del.operationTypeId).toPromise().then(() => {
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


  private success_deleting(operationType?: OperationType) {
    this.toastMessageService.inform("Видалено !", "Операція успішно видалена !", "success");
    this.onDelete();
  }

  private error_deleting(err: HttpErrorResponse) {
    this.del_loading = false;
    if (err.status === 409) {
      this.toastMessageService.inform("Помилка при видалені!", "Операція існує в активних візитах! <br>" +
        " Спочатку видаліть візити з цією орерацією ! <br> Або приховайте її.", "error");
    } else {
      this.toastMessageService.inform("Помилка при видалені!",
        err.error + "<br>" + "HTTP status: " + err.status, "error");
    }
  }

  onRefresh() {
    this.operation_types_loading = true;
    this.hidden_operation_types = false;
    this.sorting_order = true;
    this.getOperationTypes();
    this.searchForm.get("searchControlForm").setValue("");
  }

  onCancel() {
    this.searchForm.get("searchControlForm").setValue("");
    this.operationTypesForm = this.updateFormGroups(this.operation_types);
    this.tableForm.setControl("operationTypesForm", this.operationTypesForm);
  }

  private updateFormGroups(operationTypes: OperationType[]): FormArray {
    this.operation_types_loading = true;
    this.sorting_order ? operationTypes.sort(this.compareOperationTypes) : operationTypes.sort(this.compareOperationTypes).reverse();
    const operationTypesForm = this.fb.array([]);
    operationTypes.forEach((operationType: OperationType) => {
      operationTypesForm.push(this.createFormGroup(operationType));
    });
    this.operation_types_loading = false;
    return operationTypesForm;
  }

  private createFormGroup(operationType: OperationType): FormGroup {
    return this.fb.group({
      operationTypeId: [operationType.operationTypeId],
      name: [operationType.name, [Validators.required, Validators.maxLength(50), Validators.pattern("[A-Za-zА-Яа-яЁёІіЇїЄє+-. ]*")]],
      disable: [operationType.disable],
      isChanged: [false],
    });
  }

  private filterOperationTypes(search_value: string) {
    if (search_value) {
      const filteredOperationTypes = this.operation_types.filter((operationType: OperationType) => {
        if (operationType.name) {
          return (operationType.name.toLowerCase().indexOf(search_value.toLowerCase()) === 0);
        } else {
          return true;
        }
      });
      this.tableForm.setControl("operationTypesForm", this.updateFormGroups(filteredOperationTypes));
    } else {
      this.tableForm.setControl("operationTypesForm", this.operationTypesForm);
    }
  }

  change_sorting() {
    this.sorting_order = !this.sorting_order;
    this.tableForm.setControl("operationTypesForm", this.updateFormGroups(<OperationType[]>this.tableForm.get("operationTypesForm").value));
    this.operationTypesForm = this.updateFormGroups(<OperationType[]>this.operationTypesForm.value);
  }

  compareOperationTypes = (operation_type1, operation_type2) => {
    if (operation_type1.operationTypeId === 0) {
      return !this.sorting_order;
    } else if (operation_type2.operationTypeId === 0) {
      return this.sorting_order;
    } else if (operation_type1.name && operation_type2.name && operation_type1.name.localeCompare(operation_type2.name) !== 0) {
      return operation_type1.name.localeCompare(operation_type2.name);
    }
  };


}
