import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Rx';
import {HttpClient, HttpParams} from "@angular/common/http";
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import {format} from 'date-fns';
import {Dust} from "./dust";

@Injectable()
export class DustService {
  private baseUrl: string; // range?from=15.09.2012-10:12&to=15.09.2017-10:00

  constructor(private http: HttpClient) {
    //this.baseUrl = this.window.location.protocol + "//" + this.window.location.hostname + ":8080/pi/dust/";
    this.baseUrl = "http://192.168.0.31:8080/pi/dust/";
  }

  public getCurrent(): Observable<Dust> {
    let measurments = this.http.get(this.baseUrl + 'current', {
      withCredentials: true
    }).map(this.mapDust)
      .catch((err: any) => {
        console.error("Something is wrong..");
        return Observable.of(undefined);
      });
    return measurments;
  }

  public getRange(from: Date, to: Date): Observable<Dust[]> {
    let query: HttpParams = new HttpParams();

    query.append('from', format(from, 'DD.MM.YYYY-HH:mm'));
    query.append('to', format(to, 'DD.MM.YYYY-HH:mm'));

    let measurments = this.http.get(this.baseUrl + 'range', {
      withCredentials: true,
      params: query
    }).map(this.mapDustList)
      .catch((err: any) => {
        console.error("Something is wrong..");
        return Observable.of(undefined);
      });
    return measurments;
  }

  private mapDust(jsonDust: Dust): Dust {
    try {
      return jsonDust;
    } catch (ex) {
      return null;
    }
  }

  private mapDustList(jsonDust: Dust[]): Dust[] {
    try {
      let output = [];
      for (let val of jsonDust) {
        if (val.density != 99) {
          output.push(val);
        }
      }
      return output;
    } catch (ex) {
      return null;
    }
  }
}
