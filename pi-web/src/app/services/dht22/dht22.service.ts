import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Rx';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Dht22} from './dht22';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import {format} from 'date-fns';
import {environment} from "../../../environments/environment";

@Injectable()
export class Dht22Service {
  private baseUrl: string; // range?from=15.09.2012-10:12&to=15.09.2017-10:00

  constructor(private http: HttpClient) {
    this.baseUrl = "http://" + environment.serverIp + ":8080/pi/temperature/";
  }

  public getCurrent(): Observable<Dht22> {
    let measurments = this.http.get(this.baseUrl + 'current', {
      withCredentials: true
    }).map(this.mapDht22)
      .catch((err: any) => {
        console.error("Something is wrong..");
        return Observable.of(undefined);
      });
    return measurments;
  }

  public getRange(from: Date, to: Date): Observable<Dht22[]> {
    let query: HttpParams = new HttpParams();

    query.append('from', format(from, 'DD.MM.YYYY-HH:mm'));
    query.append('to', format(to, 'DD.MM.YYYY-HH:mm'));

    let measurments = this.http.get(this.baseUrl + 'range',
      {
        withCredentials: true,
        params: query
      }).map(this.mapDht22List)
      .catch((err: any) => {
        console.error("Something is wrong..");
        return Observable.of(undefined);
      });
    return measurments;
  }

  private mapDht22(jsonDht22: Dht22): Dht22 {
    try {
      return jsonDht22;
    } catch (ex) {
      return null;
    }
  }

  private mapDht22List(jsonDht22: Dht22[]): Dht22[] {
    try {
      let output = [];
      for (let val of jsonDht22) {
        if (val.temperature != 99) {
          output.push(val);
        }
      }
      return output;
    } catch (ex) {
      return null;
    }
  }
}
