import {Injectable} from "@angular/core";
import {Observable} from "rxjs/internal/Observable";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Client} from "../backend_types/client";
import {MyObjectList} from "../backend_types/my-object-list";
import {UrlProperty} from "./url-property";

@Injectable({
  providedIn: "root"
})
export class ClientService {

  constructor(private http: HttpClient) {
  }

  getClients(search?: string, limit?: number, offset?: number, sort?: boolean): Observable<Client[]> {
    let params = new HttpParams().set("search", search ? search : "").set("limit", limit ? limit.toString() : "10").set("offset", offset ? offset.toString() : "0").set("sort", sort ? "ASC" : "DESC");
    return this.http.get<Client[]>(UrlProperty.serverUrl + UrlProperty.clientsUrl, {
      headers: UrlProperty.httpOptions.headers,
      params: params
    });
  }

  getCountOfClients(search?: string): Observable<number> {
    let params = new HttpParams().set("search", search ? search : "");
    return this.http.get<number>(UrlProperty.serverUrl + UrlProperty.clientsUrl + UrlProperty.count, {
      headers: UrlProperty.httpOptions.headers,
      params: params
    });
  }

  getClient(client_id: number): Observable<Client> {
    return this.http.get<Client>(UrlProperty.serverUrl + UrlProperty.clientsUrl + client_id);
  }

  addClient(client: Client): Observable<Client> {
    return this.http.post<Client>(UrlProperty.serverUrl + UrlProperty.clientsUrl, JSON.stringify(client), UrlProperty.httpOptions);
  }

  editClient(client: Client): Observable<Client> {
    return this.http.put<Client>(UrlProperty.serverUrl + UrlProperty.clientsUrl + client.clientId, JSON.stringify(client), UrlProperty.httpOptions);
  }

  deleteClient(client_id: number): Observable<boolean> {
    return this.http.delete<boolean>(UrlProperty.serverUrl + UrlProperty.clientsUrl + client_id, UrlProperty.httpOptions);
  }


  /** @deprecated */
  putClients(clients: Client[]): Observable<Client[]> {
    const myClients: MyObjectList<Client> = new MyObjectList();
    myClients.objects = clients;
    return this.http.put<Client[]>(UrlProperty.serverUrl + UrlProperty.clientsUrl + "list/", JSON.stringify(myClients), UrlProperty.httpOptions);
  }

  /** @deprecated */
  deleteClients(client_ids: number[]): Observable<boolean> {
    return this.http.delete<boolean>(UrlProperty.serverUrl + UrlProperty.clientsUrl + "list/" + client_ids, UrlProperty.httpOptions);
  }
}


