import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { User } from './models';

@Injectable({
  providedIn: 'root'
})
export class CheckoutService {


  private dataSource = new BehaviorSubject<any>(null);
  currentData = this.dataSource.asObservable();

  constructor() { }

  changeData(data: any) {
    this.dataSource.next(data);
  }

}