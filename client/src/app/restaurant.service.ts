import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cart, Item, User } from './models';

@Injectable({
  providedIn: 'root',
})
export class RestaurantService {
  constructor(private httpClient: HttpClient) {}

  private baseURL = 'http://localhost:3000/api';

  // TODO: Task 2.2
  // You change the method's signature but not the name
  getMenuItems(): Observable<Item[]> {
    console.log('Calling API...');
    console.log(this.httpClient.get<Item>(`${this.baseURL}/menu`));

    return this.httpClient.get<Item[]>(`${this.baseURL}/menu`);
  }

  // TODO: Task 3.2
  processForm(requestBodyToSend: any): Observable<any> {
    console.log(requestBodyToSend);

    return this.httpClient.post<any>(`${this.baseURL}/food_order`, requestBodyToSend);
  }

  
}



