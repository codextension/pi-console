import {AfterViewInit, Component} from '@angular/core';
import {Dht22Service} from "./services/dht22/dht22.service";
import {ElectretService} from "./services/electret/electret.service";
import {DustService} from "./services/dust/dust.service";
import {Dust} from "./services/dust/dust";
import {Dht22} from "./services/dht22/dht22";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['app.component.scss']
})
export class AppComponent implements AfterViewInit {
  private dht22: Dht22 = new Dht22(0, 0, 0, new Date(), true);
  private dust: Dust = new Dust(0, 0, new Date(), true, 0, 0);

  constructor(private dht22Service: Dht22Service, private noiseService: ElectretService, private dustService: DustService) {
    dht22Service.getCurrent().subscribe(value => {
      if (value != null) {
        this.dht22 = value;
      }
    });

    dustService.getCurrent().subscribe(value => {
      if (value != null) {
        this.dust = value;
      }
    });
  }

  ngAfterViewInit(): void {
    setInterval(() => {
      this.dht22Service.getCurrent().subscribe(value => {
        if (value != null) {
          this.dht22 = value;
        }
      });
    }, 3000);


    setInterval(() => {
      this.dustService.getCurrent().subscribe(value => {
        if (value != null && value.density > 0) {
          this.dust = value;
        }
      });
    }, 1000);
  }
}
