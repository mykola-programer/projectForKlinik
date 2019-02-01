import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {AppComponent} from "./app.component";
import {AppRoutingModule} from "./app-routing.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {HttpClientInMemoryWebApiModule} from "angular-in-memory-web-api";
import {VisitDateService} from "./service/visit-date.service";
import {ClientEditorComponent} from "./client-editor/client-editor.component";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatDialogModule} from "@angular/material/dialog";
import {DateEditorComponent} from "./date/date-editor/date-editor.component";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {NavbarComponent} from "./navbar/navbar.component";
import {NavbarService} from "./service/navbar.service";
import {OperationEditorComponent} from "./operation-editor/operation-editor.component";
import {DateSelectorDialogComponent} from "./date/date-selector-dialog/date-selector-dialog.component";
import {SurgeonEditorComponent} from "./surgeon-editor/surgeon-editor.component";
import {ToastaModule} from "ngx-toasta";
import {ManagerEditorComponent} from "./manager-editor/manager-editor.component";
import {VisitComponent} from "./visit/visit.component";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {OperationTypeEditorComponent} from "./operation-type-editor/operation-type-editor.component";
import {DateSelectorComponent} from "./date/date-selector/date-selector.component";
import { RelocationDialogComponent } from './relocation-dialog/relocation-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    DateSelectorComponent,
    ClientEditorComponent,
    DateEditorComponent,
    NavbarComponent,
    // AccomodationComponent,
    OperationEditorComponent,
    DateSelectorDialogComponent,
    SurgeonEditorComponent,
    ManagerEditorComponent,
    VisitComponent,
    OperationTypeEditorComponent,
    RelocationDialogComponent,
  ],
  imports: [
    ReactiveFormsModule,
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatDialogModule,
    // MatTooltipModule,
    // MatInputModule,
    MatAutocompleteModule,
    ReactiveFormsModule,
    HttpClientModule,
    HttpClientInMemoryWebApiModule,
    MatProgressSpinnerModule,
    NgbModule.forRoot(),
    ToastaModule.forRoot()
  ],
  exports: [BrowserModule, ToastaModule],
  providers: [VisitDateService, NavbarService],
  entryComponents: [
    RelocationDialogComponent,
    DateSelectorDialogComponent,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
