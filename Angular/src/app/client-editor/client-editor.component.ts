import {Component, OnInit} from "@angular/core";
import {Client} from "../backend_types/client";
import {ClientService} from "../service/client.service";
import {Router} from "@angular/router";
import {NavbarService} from "../service/navbar.service";
import {HttpErrorResponse} from "@angular/common/http";
import {MassageResponse} from "../backend_types/massage-response";

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

  constructor(private router: Router, private clientService: ClientService, private serviceNavbar: NavbarService) {
  }

  ngOnInit(): void {
    this.getClients();
    this.serviceNavbar.change("client");
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
      alert("Введіть корректне Прізвище! \n\n 1. Не пусте поле. \n 2. Тільки букви. \n 3. Мін. - 3 , Макс. - 50");
    }
  }

  changeFirstName(client: Client, firstNameElement: HTMLInputElement) {
    if (firstNameElement.checkValidity()) {
      client.isChanged = true;
      client.firstName = firstNameElement.value;
    } else {
      client.isChanged = false;
      alert("Введіть корректне Ім'я! \n\n 1. Не пусте поле. \n 2. Тільки букви. \n 3. Мін. - 3 , Макс. - 50");
    }
  }

  changeSecondName(client: Client, secondNameElement: HTMLInputElement) {
    if (secondNameElement.checkValidity()) {
      client.isChanged = true;
      client.secondName = secondNameElement.value;
    } else {
      client.isChanged = false;
      alert("Введіть корректне по-Батькові! \n\n 1. Не пусте поле. \n 2. Тільки букви. \n 3. Мін. - 3 , Макс. - 50");
    }
  }

  changeSex(client: Client, sexElement: HTMLInputElement) {
    if (sexElement.checkValidity() && (sexElement.value === "Ч" || sexElement.value === "Ж")) {
      client.isChanged = true;
      client.sex = sexElement.value;
    } else {
      // alert("Виберіть корректну стать!");
    }
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
      alert("Введіть корректний телефон! \n\n Мін. - 10, Макс. - 19 \n\n Формати :\n 099 888 88 88 \n (099)888-88-88 \n +38 099 888 88 88");
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
    this.getClients();
  }

  onSave() {
    this.loading_save = true;
    const edit_clients: Client[] = this.filteredClients.filter((client: Client) => {
      return client.isChanged;
    });
    if (edit_clients.length) {
      this.clientService.putClients(edit_clients).toPromise().then(() => {
        alert("Кліенти успішно збережені !");
        this.getClients();
        this.loading_save = false;
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
          alert("Кліенти успішно видалені!");
          this.getClients();
          this.loading_del = false;
        }
      }).catch((err: HttpErrorResponse) => {
        this.loading_del = false;
        alert(
          ((<MassageResponse> err.error).exceptionMassage != null ? (<MassageResponse> err.error).exceptionMassage : "") + " \n" +
          ((<MassageResponse> err.error).validationMassage != null ? (<MassageResponse> err.error).validationMassage : ""));
      });
    } else {
      this.loading_del = false;
      alert("Виберіть хоча б один запис!");
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


/*
  private copyClient(original: Client, result: Client) {
    result.clientId = original.clientId;
    result.surname = original.surname;
    result.firstName = original.firstName;
    result.secondName = original.secondName;
    result.birthday = original.birthday;
    result.sex = original.sex;
    result.telephone = original.telephone;
    result.isChanged = original.isChanged;
  }

onDelete() {
  const clients_for_lock: Client[] = this.filteredClients.filter((client: Client) => {
    return client.isChanged && client.clientId !== 0;
  });
  clients_for_lock.forEach((client: Client) => {
    this.clientService.deleteClient(client.clientId).toPromise().then((success: boolean) => {
      if (success) {
        alert("Клієнт : " + client.surname + " " + client.firstName + " "
          + client.secondName + " успішно виделений!");
        this.filteredClients.splice(this.filteredClients.indexOf(client, 0), 1);
      } else {
        alert(client.surname + " " + client.firstName + " "
          + client.secondName + "\n\n" + "Неможливо видалити! \n У клієнта є активні візити.");
      }
    }).catch((err: HttpErrorResponse) => {

      const div = document.createElement("div");
      div.innerHTML = err.error.text;
      const text = div.textContent || div.innerText || "";

      alert(text);
    });
  });
}*/

// onSave() {
//   let isSuccess = true;
//   const new_clients: Client[] = this.filteredClients.filter((client: Client) => {
//     return client.isChanged && client.clientId === 0;
//   });
//   if (new_clients.length > 0) {
//     new_clients.forEach((client: Client) => {
//       this.clientService.addClient(client).toPromise().then((returned_client: Client) => {
//         this.copyClient(returned_client, client);
//         client.isChanged = false;
//         alert("Клієнт : " + returned_client.surname + " " + returned_client.firstName + " "
//           + returned_client.secondName + " успішно збережений!");
//
//       }).catch((err: HttpErrorResponse) => {
//
//         console.log(err);
//         alert(err);
//
//         const div = document.createElement("div");
//         div.innerHTML = err.error.text;
//         const text = div.textContent || div.innerText || "";
//
//         alert(text);
//         isSuccess = false;
//       });
//     });
//   }
//
//   const edit_clients: Client[] = this.filteredClients.filter((client: Client) => {
//     return client.isChanged && client.clientId !== 0;
//   });
//   edit_clients.forEach((client: Client) => {
//     this.clientService.editClient(client).toPromise().then((returned_client: Client) => {
//       this.copyClient(returned_client, client);
//       client.isChanged = false;
//       alert("Клієнт : " + returned_client.surname + " " + returned_client.firstName + " "
//         + returned_client.secondName + " успішно оновлений!");
//     }).catch((err: HttpErrorResponse) => {
//
//       const div = document.createElement("div");
//       div.innerHTML = err.error.text;
//       const text = div.textContent || div.innerText || "";
//
//       alert(text);
//       isSuccess = false;
//     });
//   });
//   if (isSuccess) {
//     this.sortClients(this.filteredClients);
//     // alert("All record was save !");
//   }
// }
