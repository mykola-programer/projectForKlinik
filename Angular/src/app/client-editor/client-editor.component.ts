import {Component, OnInit, ViewChild} from "@angular/core";
import {Client} from "../backend_types/client";
import {ClientService} from "../service/client.service";
import {NavbarService} from "../service/navbar.service";
import {HttpErrorResponse} from "@angular/common/http";
import {ToastMessageService} from "../service/toast-message.service";
import {AbstractControl, FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {debounceTime} from "rxjs/operators";
import {MatPaginator} from "@angular/material";
import {PageEvent} from "@angular/material/paginator/typings/paginator";

@Component({
  selector: "app-client-editor",
  templateUrl: "./client-editor.component.html",
  styleUrls: ["./client-editor.component.css"]
})
export class ClientEditorComponent implements OnInit {
  public clients: Client[] = [];
  public countOfChangedClients = 0;
  public genders: string[] = ["Ч", "Ж"];
  public countOfClients: number = 100;

  min_date: Date = new Date(new Date(Date.now()).getFullYear() - 100, 0, 1);
  max_date: Date = new Date(new Date(Date.now()).getFullYear() - 3, 0, 1);


  searchForm: FormGroup = this.fb.group({
    searchControlForm: ["", [Validators.maxLength(50), Validators.pattern("[A-Za-zА-Яа-яЁёІіЇїЄє ]*")]],
  });

  tableForm: FormGroup = this.fb.group({
    clientsForm: FormArray
  });


  save_loading = false;
  del_loading = false;
  clients_loading = false;
  badConnection = false;

  // "true" - ASC,
  // "false" - DESC
  sorting_order = true;

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(
    private clientService: ClientService,
    private navbarService: NavbarService,
    private toastMessageService: ToastMessageService,
    private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.navbarService.change("client");
    this.clients_loading = true;
    this.getClients("", 50, 0, true);

    this.searchForm.get("searchControlForm").valueChanges
      .pipe(debounceTime(900))
      .subscribe(search_value => {
        if (this.paginator.pageIndex === 0) {
          this.getClients(search_value, this.paginator.pageSize, 0, this.sorting_order);
        } else {
          this.paginator.firstPage();
        }
      });

    this.searchForm.get("searchControlForm").statusChanges
      .pipe(debounceTime(900))
      .subscribe(value => {
        if (value === "INVALID") {
          this.toastMessageService.inform("Некорректне значення для пошуку",
            "Вводьте тільки літери та пробіли." + "<br>" +
            "Не більше 50 символів", "info");
        }
      });

    this.tableForm.valueChanges.pipe(debounceTime(500)).subscribe((value) => {
      this.countOfChangedClients = value.clientsForm.filter((client: Client) => {
        return client.isChanged;
      }).length;
    });

    this.paginator.page.subscribe((pageEvent: PageEvent) => {
      this.getClients(this.searchForm.getRawValue().searchControlForm, pageEvent.pageSize, pageEvent.pageIndex * pageEvent.pageSize, this.sorting_order);
    });
  }

  getClients(search: string, limit: number, offset: number, sort: boolean) {
    this.clients_loading = true;
    this.clientService.getCountOfClients(search).subscribe(value => this.countOfClients = value);
    this.clientService.getClients(search, limit, offset, sort).toPromise().then((clients: Client[]) => {
      if (clients) {
        this.clients = clients;
      } else {
        this.clients = [];
        this.clients.push(new Client());
      }
      this.tableForm.setControl("clientsForm", this.updateFormGroups(this.clients));
      this.clients_loading = false;
      this.badConnection = false;
      document.getElementById("search").focus();
    }).catch((err: HttpErrorResponse) => {
      if (err.status === 422) {
        this.toastMessageService.inform("Некорректні параметри!",
          err.error + "<br>" + err.message, "error", 10000);
      } else {
        this.clients_loading = true;
        this.badConnection = true;
        this.toastMessageService.inform("Сервер недоступний!",
          "Спробуйте пізніше !" + "<br>" + err.error + "<br>" + err.message, "error", 10000);
        setTimeout(() => {
          this.getClients(search, limit, offset, sort);
        }, 15000);
      }
    });
  }

  onAdd() {
    const client = new Client();
    client.clientId = 0;
    client.isChanged = false;
    // this.searchForm.get("searchControlForm").setValue("");
    if ((<FormArray>this.tableForm.get("clientsForm")).controls[0].valid) {
      (<FormArray>this.tableForm.get("clientsForm")).insert(0, this.createFormGroup(client));
    }
    setTimeout(() => {
      document.getElementById("surname").focus();
    }, 1000);
    window.scroll(0, 0);
  }

  onSave() {
    this.save_loading = true;
    const control = (<FormArray>this.tableForm.get("clientsForm")).controls.find((abstractControl: AbstractControl) => {
      return abstractControl.get("isChanged").value;
    });
    if (control && control.value) {
      const edited_client: Client = this.convertToClient(control.value);
      if (edited_client && edited_client.clientId > 0) {
        this.clientService.editClient(edited_client).toPromise().then((client: Client) => {
          control.get("isChanged").setValue(false);
          this.success_saving(client);
        }).catch((err: HttpErrorResponse) => {
            this.error_saving(err);
          }
        );
      } else if (edited_client && edited_client.clientId === 0) {
        this.clientService.addClient(edited_client).toPromise().then((client: Client) => {
          control.get("isChanged").setValue(false);
          this.success_saving(client);
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

  private success_saving(client?: Client) {
    this.toastMessageService.inform("Збережено !", "Клієнт успішно збережений !", "success");
    this.onSave();
  }

  private error_saving(err: HttpErrorResponse, client?: Client) {
    this.save_loading = false;
    if (err.status === 422) {
      this.toastMessageService.inform("Помилка при збережені! <br> " +
        +client.surname + " " + client.firstName + " " + client.secondName +
        "<br> не відповідає критеріям !",
        err.error, "error");
    } else if (err.status === 404) {
      this.toastMessageService.inform("Помилка при збережені! <br>"
        + client.surname + " " + client.firstName + " " + client.secondName,
        err.error + "<br> Обновіть сторінку та спробуйте знову.", "error");
    } else if (err.status === 409) {
      this.toastMessageService.inform("Помилка при збережені! <br>" +
        +client.surname + " " + client.firstName + " " + client.secondName +
        " <br> Конфлікт в базі даних !",
        err.error + "<br> Обновіть сторінку та спробуйте знову.", "error");
    } else {
      this.toastMessageService.inform("Помилка при збережені! <br>"
        + client.surname + " " + client.firstName + " " + client.secondName,
        err.error + "<br>" + "HTTP status: " + err.status, "error");
    }
  }

  onDelete() {
    this.del_loading = true;
    const control = ((<FormArray>this.tableForm.get("clientsForm")).controls).find((abstractControl: AbstractControl) => {
      return abstractControl.get("isChanged").value;
    });
    if (control && control.value) {
      const client_for_del: Client = control.value;
      if (client_for_del.clientId > 0) {
        this.clientService.deleteClient(client_for_del.clientId).toPromise().then(() => {
          control.get("isChanged").setValue(false);
          this.success_deleting();
        }).catch((err: HttpErrorResponse) => {
          this.error_deleting(err, client_for_del);
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

  private success_deleting(client?: Client) {
    this.toastMessageService.inform("Видалено !", "Клієнта успішно видалений !", "success");
    this.onDelete();
  }

  private error_deleting(err: HttpErrorResponse, client?: Client) {
    this.del_loading = false;
    if (err.status === 409) {
      this.toastMessageService.inform("Помилка при видалені! <br>"
        + client.surname + " " + client.firstName + " " + client.secondName,
        "Кліент має активні візити! <br>" +
        " Спочатку видаліть візити цього клієнта !", "error");
    } else {
      this.toastMessageService.inform("Помилка при видалені!",
        err.error + "<br>" + "HTTP status: " + err.status, "error");
    }
  }

  onRefresh() {
    this.clients_loading = true;
    this.sorting_order = true;
    this.searchForm.get("searchControlForm").setValue("");
  }

  onCancel() {
    this.searchForm.get("searchControlForm").setValue("");
    this.sorting_order = true;
  }

  private convertToClient(value: any): Client {
    const client: Client = new Client();
    client.clientId = value.clientId;
    client.surname = value.surname;
    client.firstName = value.firstName;
    client.secondName = value.secondName;
    client.sex = value.sex;
    client.birthday = (<string>value.birthday).split("-").map(value1 => Number(value1));
    client.telephone = value.telephone;
    return client;
  }

  private updateFormGroups(clients: Client[]): FormArray {
    this.clients_loading = true;
    // this.sorting_order ? clients.sort(this.compareClients) : clients.sort(this.compareClients).reverse();
    const clientsForm = this.fb.array([]);
    clients.forEach((client: Client) => {
      clientsForm.push(this.createFormGroup(client));
    });
    this.clients_loading = false;
    return clientsForm;
  }

  private createFormGroup(client: Client): FormGroup {
    return this.fb.group({
      clientId: [client.clientId],
      surname: [client.surname, [Validators.required, Validators.maxLength(50), Validators.pattern("[A-Za-zА-Яа-яЁёІіЇїЄє `]*")]],
      firstName: [client.firstName, [Validators.required, Validators.maxLength(50), Validators.pattern("[A-Za-zА-Яа-яЁёІіЇїЄє `]*")]],
      secondName: [client.secondName, [Validators.required, Validators.maxLength(50), Validators.pattern("[A-Za-zА-Яа-яЁёІіЇїЄє `]*")]],
      sex: [client.sex, [Validators.required, Validators.maxLength(1), Validators.pattern("^[ЧЖ]*$")]],
      birthday: [client.birthday ? (new Date(client.birthday[0], client.birthday[1] - 1, client.birthday[2]).toISOString().substring(0, 10)) : null,
        [Validators.required]],
      telephone: [client.telephone, [Validators.pattern("[ ()0123456789+-]*")]],
      isChanged: [false],
    });
  }

  change_sorting() {
    this.sorting_order = !this.sorting_order;
    this.getClients(this.searchForm.getRawValue().searchControlForm, this.paginator.pageSize, this.paginator.pageIndex * this.paginator.pageSize, this.sorting_order);
    // this.tableForm.setControl("clientsForm", this.updateFormGroups(<Client[]>this.tableForm.get("clientsForm").value));
    // this.clientsForm = this.updateFormGroups(<Client[]>this.clientsForm.value);
  }

  /*
    private compareClients = (client1, client2) => {
      if (client1.clientId === 0) {
        return !this.sorting_order;
      } else if (client2.clientId === 0) {
        return this.sorting_order;
      } else if (client1.surname && client2.surname && client1.surname.localeCompare(client2.surname) !== 0) {
        return client1.surname.localeCompare(client2.surname);
      } else if (client1.firstName && client2.firstName && client1.firstName.localeCompare(client2.firstName) !== 0) {
        return client1.firstName.localeCompare(client2.firstName);
      } else if (client1.secondName && client2.secondName && client1.secondName.localeCompare(client2.secondName) !== 0) {
        return client1.secondName.localeCompare(client2.secondName);
      }
    };*/

  // test(value?: any) {
  //   console.log(this.tableForm.getRawValue());
  // }

  /*
    private filterClients(client_value: string) {
      if (client_value) {
        const filterValue: string[] = client_value.toLowerCase().split(" ");
        const filteredClients = this.clients.filter(client => {
          switch (filterValue.length) {
            case 0:
              return true;
            case 1:
              if (client.surname) {
                return (client.surname.toLowerCase().indexOf(filterValue[0]) === 0);
              } else {
                return true;
              }
            case 2:
              if (client.surname && client.firstName) {
                return (client.surname.toLowerCase().indexOf(filterValue[0]) === 0
                  && client.firstName.toLowerCase().indexOf(filterValue[1]) === 0);
              } else {
                return true;
              }
            default:
              if (client.surname && client.firstName && client.secondName) {
                return (client.surname.toLowerCase().indexOf(filterValue[0]) === 0
                  && client.firstName.toLowerCase().indexOf(filterValue[1]) === 0
                  && client.secondName.toLowerCase().indexOf(filterValue[2]) === 0);
              } else {
                return true;
              }
          }
        });
        this.tableForm.setControl("clientsForm", this.updateFormGroups(filteredClients));
      } else {
        this.tableForm.setControl("clientsForm", this.clientsForm);
      }
    }*/

}
