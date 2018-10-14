import {Component} from '@angular/core';
import {Client} from '../backend_types/client';
import {ClientService} from "../service/client.service";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material";
import {NavbarService} from "../service/navbar.service";

@Component({
  selector: 'app-client-editor',
  templateUrl: './client-editor.component.html',
  styleUrls: ['./client-editor.component.css']
})
export class ClientEditorComponent {
  public clients: Client[] = [];

  constructor(private router: Router, private clientService: ClientService, private serviceNavbar: NavbarService) {
    this.getClients();
    this.serviceNavbar.change('client');

  }

  getClients() {
    this.clientService.getClients().toPromise().then((clients: Client[]) => this.clients = clients);
  }

  onAdd() {
    this.clients.unshift(new Client());
  }

  onRefresh() {
    this.getClients();
  }

  onSave() {
    let new_clients: Client[] = this.clients.filter((value: Client) => {
      return value.isChanged && value.clientId == 0;
    });
    if (new_clients.length > 0) {
      this.clientService.addClients(new_clients).toPromise().then(() => {
        new_clients.forEach(value => value.isChanged = false)
      });
    }

    let edit_clients: Client[] = this.clients.filter((value: Client) => {
      return value.isChanged && value.clientId !== 0;
    });
    edit_clients.forEach((value: Client) => {
      this.clientService.editClient(value).toPromise().then(() => {
        value.isChanged = false;
      });
    });
  }

  onDelete() {
    let remove_clients: Client[] = this.clients.filter((value: Client) => {
      return value.isChanged && value.clientId !== 0;
    });
    remove_clients.forEach((value: Client) => {
      this.clientService.removeClient(value.clientId).toPromise().then(() => {
        this.clients.splice(this.clients.indexOf(value, 0), 1);
      });
    });
  }

  onCancel() {
    this.onRefresh();
    // this.router.navigateByUrl("");

  }

}
