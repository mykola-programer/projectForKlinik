import {VisitDate} from "./visit-date";
import {Client} from "./client";
import {OperationType} from "./operation-type";
import {Surgeon} from "./surgeon";
import {Manager} from "./manager";
import {Accomodation} from "./accomodation";

export class Visit {
  public visitId = 0;
  public visitDate: VisitDate;
  public timeForCome: number[];
  public orderForCome: number;
  public client: Client;
  public status: string;
  public patient: Client;
  public operationType: OperationType;
  public eye: string;
  public surgeon: Surgeon;
  public manager: Manager;
  public accomodation: Accomodation;
  public inactive = false;
  public note: string;
  public isChanged = false;

}

