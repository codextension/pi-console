import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HashLocationStrategy, LocationStrategy} from "@angular/common";

const routes: Routes = [];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [{provide: LocationStrategy, useClass: HashLocationStrategy},
    {provide: Window, useValue: window}],
})
export class AppRoutingModule {
}
