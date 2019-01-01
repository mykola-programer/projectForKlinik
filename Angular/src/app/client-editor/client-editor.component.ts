import {Component, OnInit} from "@angular/core";
import {Client} from "../backend_types/client";
import {ClientService} from "../service/client.service";
import {Router} from "@angular/router";
import {NavbarService} from "../service/navbar.service";
import {HttpErrorResponse} from "@angular/common/http";
import {ToastMessageService} from "../service/toast-message.service";

@Component({
  selector: "app-client-editor",
  templateUrl: "./client-editor.component.html",
  styleUrls: ["./client-editor.component.css"]
})
export class ClientEditorComponent implements OnInit {
  public clients: Client[];
  public filteredClients: Client[];
  public genders: string[] = ["Ч", "Ж"];
  min_date: Date = new Date(new Date(Date.now()).getFullYear() - 100, 0, 0);
  max_date: Date = new Date(new Date(Date.now()).getFullYear() - 3, 0, 0);
  loading_save = false;
  loading_del = false;
  loading_clients = false;

  // "1" - ASC,
  // "-1" - DESC
  private sorting_order = 1;

  constructor(
    private router: Router,
    private clientService: ClientService,
    private navbarService: NavbarService,
    private toastMessageService: ToastMessageService,
  ) {
  }

  ngOnInit(): void {
    this.getClients();
    this.navbarService.change("client");
  }

  getClients() {
    this.loading_clients = true;
    this.clientService.getClients().toPromise().then((clients: Client[]) => {
      this.clients = clients;
      this.sortClients(this.clients);
      this.filteredClients = this.clients;
      this.loading_clients = false;
    });
  }

  onBlur(client: Client, element: HTMLInputElement) {
    if (!element.checkValidity()) {
      client.isChanged = false;
    }
  }

  changeSurname(client: Client, surnameElement: HTMLInputElement) {
    if (surnameElement.checkValidity()) {
      client.isChanged = true;
      client.surname = surnameElement.value;
    } else {
      client.isChanged = false;
      this.toastMessageService.inform("Помилка !",
        "Введіть корректне Прізвище! <br> 1. Не пусте поле. <br> 2. Тільки букви. <br> 3. Макс. - 50",
        "info");
    }
  }

  changeFirstName(client: Client, firstNameElement: HTMLInputElement) {
    if (firstNameElement.checkValidity()) {
      client.isChanged = true;
      client.firstName = firstNameElement.value;
    } else {
      client.isChanged = false;
      this.toastMessageService.inform("Помилка !",
        "Введіть корректне Ім'я! <br> 1. Не пусте поле. <br> 2. Тільки букви. <br> 3. Макс. - 50",
        "info");
    }
  }

  changeSecondName(client: Client, secondNameElement: HTMLInputElement) {
    if (secondNameElement.checkValidity()) {
      client.isChanged = true;
      client.secondName = secondNameElement.value;
    } else {
      client.isChanged = false;
      this.toastMessageService.inform("Помилка !",
        "Введіть корректне по-Батькові! <br> 1. Не пусте поле. <br> 2. Тільки букви. <br> 3. Макс. - 50",
        "info");
    }
  }

  changeSex(client: Client, sexElement: HTMLInputElement) {
    client.isChanged = true;
    client.sex = sexElement.value;
  }

  changeBirthday(client: Client, birthdayElement: HTMLInputElement) {
    const year = Number(birthdayElement.value.substring(0, 4));
    const month = Number(birthdayElement.value.substring(5, 7));
    const day = Number(birthdayElement.value.substring(8, 10));

    if (birthdayElement.checkValidity()) {
      client.isChanged = true;
      client.birthday = [year, month, day];
    } else {
      client.isChanged = false;
    }
  }

  refactorDate(birthday: number[]): Date {
    if (birthday) {
      return new Date(birthday[0], birthday[1] - 1, birthday[2]);
    } else {
      return new Date();
    }
  }

  changeTelephone(client: Client, telephoneElement: HTMLInputElement) {
    if (telephoneElement.checkValidity()) {
      client.isChanged = true;
      client.telephone = telephoneElement.value;
    } else {
      client.isChanged = false;
      this.toastMessageService.inform("Помилка !",
        "Введіть корректний телефон! <br> Мін. - 10, Макс. - 19 <br>  Формати :<br> 099 888 88 88 <br> (099)888-88-88 <br> +38 099 888 88 88",
        "info");
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
    } else {
      this.filteredClients = this.clients;
    }
  }

  onAdd() {
    if (this.filteredClients[0].surname != null) {
      const client = new Client();
      client.sex = "Ч";
      this.filteredClients.unshift(client);
      window.scroll(0, 0);
    }
  }

  onRefresh() {
    this.loading_clients = true;
    this.getClients();
  }

  onSave() {
    this.loading_save = true;
    const edit_clients: Client[] = this.filteredClients.filter((client: Client) => {
      return client.isChanged;
    });
    if (edit_clients.length) {
      this.clientService.putClients(edit_clients).toPromise().then(() => {
        this.toastMessageService.inform("Збережено !", "Кліенти успішно збережені !", "success");
        this.getClients();
        this.loading_save = false;
      }).catch((err: HttpErrorResponse) => {
          this.loading_save = false;
          if (err.status === 422) {
          }
          this.toastMessageService.inform("Помилка при збережені!", err.error, "error");

        }
      );
    } else {
      this.loading_save = false;
      this.toastMessageService.inform("Виберіть хоча б один запис!", "", "info");
    }
  }

  onDelete() {
    this.loading_del = true;
    const clients_ids_for_delete: number[] = [];
    this.filteredClients.forEach((client: Client) => {
      if (client.isChanged && client.clientId !== 0) {
        clients_ids_for_delete.push(client.clientId);
      }
    });
    if (clients_ids_for_delete.length) {
      this.clientService.deleteClients(clients_ids_for_delete).toPromise().then((success: boolean) => {
        if (success) {
          this.toastMessageService.inform("Видалено !", "Кліенти успішно видалені !", "success");
          this.getClients();
          this.loading_del = false;
        }
      }).catch((err: HttpErrorResponse) => {
        this.loading_del = false;
        if (err.status === 400) {
        }
        this.toastMessageService.inform("Помилка при видалені!", err.error, "error");

      });
    } else {
      this.loading_del = false;
      this.toastMessageService.inform("Виберіть хоча б один запис!", "", "info");
    }
  }

  onCancel() {
    this.onRefresh();
    // this.router.navigateByUrl("");

  }

  private sortClients(clients: Client[]) {
    clients.sort((client1, client2) => {
      if (client1.surname.localeCompare(client2.surname) !== 0) {
        return client1.surname.localeCompare(client2.surname) * this.sorting_order;
      } else if (client1.firstName.localeCompare(client2.firstName) !== 0) {
        return client1.firstName.localeCompare(client2.firstName) * this.sorting_order;
      } else if (client1.secondName.localeCompare(client2.secondName) !== 0) {
        return client1.secondName.localeCompare(client2.secondName) * this.sorting_order;
      } else {
        if (client1.birthday[0] !== client2.birthday[0]) {
          return (client1.birthday[0] - client2.birthday[0]) * this.sorting_order;
        } else if (client1.birthday[1] !== client2.birthday[1]) {
          return (client1.birthday[1] - client2.birthday[1]) * this.sorting_order;
        } else {
          return (client1.birthday[2] - client2.birthday[2]) * this.sorting_order;
        }
      }
    });
  }


}
