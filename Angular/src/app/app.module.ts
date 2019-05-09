import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {AppComponent} from "./app.component";
import {AppRoutingModule} from "./app-routing.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {HttpClientInMemoryWebApiModule} from "angular-in-memory-web-api";
import {ClientEditorComponent} from "./client-editor/client-editor.component";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatDialogModule} from "@angular/material/dialog";
import {DateEditorComponent} from "./date/date-editor/date-editor.component";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {NavbarComponent} from "./navbar/navbar.component";
import {SurgeonEditorComponent} from "./surgeon/surgeon-editor/surgeon-editor.component";
import {ToastaModule} from "ngx-toasta";
import {ManagerEditorComponent} from "./manager-editor/manager-editor.component";
import {VisitComponent} from "./visit/visit.component";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {OperationTypeEditorComponent} from "./operation-type-editor/operation-type-editor.component";
import {DateSelectorComponent} from "./date/date-selector/date-selector.component";
import {RelocationDialogComponent} from "./relocation-dialog/relocation-dialog.component";
import {LoginComponent} from "./login/login.component";
import {MatPaginatorModule} from "@angular/material/paginator";
import {DatePlanService} from "./service/date-plan.service";
import {DepartmentSelectorComponent} from "./department/department-selector/department-selector.component";
import {DepartmentService} from "./service/department.service";
import {GlobalService} from "./service/global.service";
import {AccomodationService} from "./service/accomodation.service";
import {AuthService} from "./service/auth/auth.service";
import {ClientService} from "./service/client.service";
import {ManagerService} from "./service/manager.service";
import {OperationTypeService} from "./service/operation-type.service";
import {SurgeonService} from "./service/surgeon.service";
import {SurgeonPlanService} from "./service/surgeon-plan.service";
import {ToastMessageService} from "./service/toast-message.service";
import {UserService} from "./service/auth/user.service";
import {VisitService} from "./service/visit.service";
import {DepartmentEditorComponent} from "./department/department-editor/department-editor.component";
import {FilterDepartmentPipe} from "./department/department-editor/filter-department.pipe";
import { SurgeonPlanComponent } from "./surgeon/surgeon-plan/surgeon-plan.component";
import { SurgeonSelectorComponent } from "./surgeon/surgeon-selector/surgeon-selector.component";

@NgModule({
  declarations: [
    AppComponent,
    DateSelectorComponent,
    ClientEditorComponent,
    DateEditorComponent,
    NavbarComponent,
    SurgeonEditorComponent,
    ManagerEditorComponent,
    VisitComponent,
    OperationTypeEditorComponent,
    RelocationDialogComponent,
    LoginComponent,
    DepartmentSelectorComponent,
    DepartmentEditorComponent,
    FilterDepartmentPipe,
    SurgeonPlanComponent,
    SurgeonSelectorComponent,
  ],
  imports: [
    ReactiveFormsModule,
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatDialogModule,
    MatAutocompleteModule,
    ReactiveFormsModule,
    HttpClientModule,
    HttpClientInMemoryWebApiModule,
    MatProgressSpinnerModule,
    MatPaginatorModule,
    NgbModule.forRoot(),
    ToastaModule.forRoot()
  ],
  exports: [BrowserModule, ToastaModule],
  providers: [AccomodationService,
    AuthService,
    ClientService,
    DatePlanService,
    DepartmentService,
    GlobalService,
    ManagerService,
    OperationTypeService,
    SurgeonService,
    SurgeonPlanService,
    ToastMessageService,
    UserService,
    VisitService],
  entryComponents: [
    RelocationDialogComponent,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
