import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Rx';
import {HttpClient} from "@angular/common/http";
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import {Noise} from "./noise";

@Injectable()
export class ElectretService {

  private baseUrl: string; // range?from=15.09.2012-10:12&to=15.09.2017-10:00

  constructor(private http: HttpClient) {
    // this.baseUrl = this.window.location.protocol + "//" + this.window.location.hostname + ":8080/pi/noise/";
    this.baseUrl = "http://192.168.0.31:8080/pi/noise/";
  }

  public getCurrent(): Observable<Noise> {
    let measurments = this.http.get(this.baseUrl + 'current', {
      withCredentials: true
    }).map(this.mapNoise)
      .catch((err: any) => {
        console.error("Something is wrong..");
        return Observable.of(undefined);
      });
    return measurments;
  }

  private mapNoise(rawValue: number): Noise {
    try {
      return new Noise(rawValue, (rawValue * 5) / 1024.0, new Date());
    } catch (ex) {
      return null;
    }
  }
}
