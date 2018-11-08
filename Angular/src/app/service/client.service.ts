import {Injectable} from "@angular/core";
import {Observable} from "rxjs/internal/Observable";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Client} from "../backend_types/client";

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

  getUnlockClients(): Observable<Client[]> {
    return this.http.get<Client[]>(this.serverUrl + this.clientsUrl + "unlock/");
  }

  getClient(client_id: number): Observable<Client> {
    return this.http.get<Client>(this.serverUrl + this.clientsUrl + client_id);
  }

  addClient(client: Client): Observable<Client> {
    return this.http.post<Client>(this.serverUrl + this.clientsUrl, JSON.stringify(client), this.httpOptions);
  }

  // addClients(clients: Client[]): Observable<Client[]> {
  //   return this.http.post<Client[]>(this.serverUrl + this.clientsUrl, JSON.stringify(clients), this.httpOptions);
  // }

  editClient(client: Client): Observable<Client> {
    return this.http.put<Client>(this.serverUrl + this.clientsUrl + client.clientId.toString(), JSON.stringify(client), this.httpOptions);
  }

  lockClient(client: Client): Observable<Client> {
    client.lock = true;
    return this.http.put<Client>(this.serverUrl + this.clientsUrl + client.clientId.toString(), JSON.stringify(client), this.httpOptions);
  }
  removeClient(client_id: number): any {
    return this.http.delete(this.serverUrl + this.clientsUrl + client_id.toString(), this.httpOptions);
  }
}
