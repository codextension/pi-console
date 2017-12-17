import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {FlotComponent} from './flot/flot.component';
import {FormsModule} from "@angular/forms";
import {HttpClientJsonpModule, HttpClientModule} from "@angular/common/http";
import {BrowserModule} from "@angular/platform-browser";

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    BrowserModule, HttpClientModule, HttpClientJsonpModule,
  ],
  declarations: [FlotComponent],
  exports: [FlotComponent]
})
export class ComponentsModule {
}
