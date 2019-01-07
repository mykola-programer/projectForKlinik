import {Injectable} from "@angular/core";
import {Observable} from "rxjs/internal/Observable";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Client} from "../backend_types/client";
import {MyObjectList} from "../backend_types/my-object-list";

@Injectable({
  providedIn: "root"
})
export class ClientService {
  private serverUrl = "http://localhost:8080/";  // URL to REST-server
  private clientsUrl = "clients/";
  private readonly httpOptions = {
    headers: new HttpHeaders({
      "Content-Type": "application/json"
    })
  };

  constructor(private http: HttpClient) {
  }

  getClients(): Observable<Client[]> {
    return this.http.get<Client[]>(this.serverUrl + this.clientsUrl);
  }

  putClients(clients: Client[]): Observable<Client[]> {
    const myClients: MyObjectList<Client> = new MyObjectList();
    myClients.objects = clients;
    return this.http.put<Client[]>(this.serverUrl + this.clientsUrl + "list/", JSON.stringify(myClients), this.httpOptions);
  }

  deleteClients(client_ids: number[]): Observable<boolean> {
    return this.http.delete<boolean>(this.serverUrl + this.clientsUrl + "list/" + client_ids.toString(), this.httpOptions);
  }
}



  /*
  getClient(client_id: number): Observable<Client> {
    return this.http.get<Client>(this.serverUrl + this.clientsUrl + client_id);
  }

  addClient(client: Client): Observable<Client> {
    return this.http.post<Client>(this.serverUrl + this.clientsUrl, JSON.stringify(client), this.httpOptions);
  }

  editClient(client: Client): Observable<Client> {
    return this.http.put<Client>(this.serverUrl + this.clientsUrl + client.clientId.toString(), JSON.stringify(client), this.httpOptions);
  }

  deleteClient(client_id: number): Observable<boolean> {
    return this.http.delete<boolean>(this.serverUrl + this.clientsUrl + client_id.toString(), this.httpOptions);
  }
*/


