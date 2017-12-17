import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Dht22Service} from './dht22/dht22.service';
import {HttpClientJsonpModule, HttpClientModule} from "@angular/common/http";
import {ElectretService} from './electret/electret.service';
import {DustService} from './dust/dust.service';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    HttpClientJsonpModule
  ],
  declarations: [],
  providers: [Dht22Service, ElectretService, DustService]
})
export class ServicesModule {
}
