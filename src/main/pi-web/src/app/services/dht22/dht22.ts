export class Dht22 {
  constructor(public id: number, public temperature: number, public humidity: number, public measuredDate: Date, public newMeasure: boolean) {
  }
}
