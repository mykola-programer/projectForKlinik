import {Component} from '@angular/core';
import {NavbarService} from "../service/navbar.service";

@Component({
  selector: 'app-accomodation',
  templateUrl: './accomodation.component.html',
  styleUrls: ['./accomodation.component.css']
})
export class AccomodationComponent {

  constructor(private serviceNavbar: NavbarService) {
    this.serviceNavbar.change('accomodation');

  }

}
