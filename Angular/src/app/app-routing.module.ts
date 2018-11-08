import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {ClientEditorComponent} from "./client-editor/client-editor.component";
import {DateEditorComponent} from "./date/date-editor/date-editor.component";
import {AccomodationComponent} from "./accomodation/accomodation.component";
import {SurgeonEditorComponent} from "./surgeon-editor/surgeon-editor.component";

const routes: Routes = [
  {path: "", pathMatch: "full", redirectTo: "accomodation"},
  {path: "accomodation", component: AccomodationComponent},
  {path: "dates", component: DateEditorComponent},
  {path: "client", component: ClientEditorComponent},
  {path: "surgeon", component: SurgeonEditorComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
