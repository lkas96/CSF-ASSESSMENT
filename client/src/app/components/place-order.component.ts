import { Component, inject, OnInit} from '@angular/core';
import { CheckoutService } from '../checkout.service';
import { Cart, Clean, User } from '../models';
import { Router } from '@angular/router';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RestaurantService } from '../restaurant.service';

@Component({
  selector: 'app-place-order',
  standalone: false,
  templateUrl: './place-order.component.html',
  styleUrls: ['./place-order.component.css'],
})
export class PlaceOrderComponent implements OnInit{
  // TODO: Task 3

  //FROM CHECKOUT BLAST VIEW 1 TO RECEIVE THE CART ARRAY ITEMS

  cartArrayFromCheckout: Cart[] = [];

  constructor(private cs: CheckoutService, private router: Router, private rs: RestaurantService) {}
  ngOnInit(): void {
    this.cs.currentData.subscribe(data => {
      this.cartArrayFromCheckout = data;

      console.log("CART RECEVIED ...")
    });

    this.form = this.createForm();
  }

  getGrandTotal() {
    // Calculate the grand total from the cart array
    let sum = 0;
    for (let i = 0; i < this.cartArrayFromCheckout.length; i++) {
      sum += this.cartArrayFromCheckout[i].price;
    }
    return sum;
  }

  startOver() {
    this.cartArrayFromCheckout = []; //clear the array and send it back
    this.cs.changeData(this.cartArrayFromCheckout);
    console.log("START OVER EMPTY ARRAY DATA BALSTED...")
    this.router.navigate([``]);
  }

  private fb = inject(FormBuilder);
  protected form!: FormGroup;
  protected user!: User;

  createForm(): FormGroup {
    return this.fb.group({
      username: this.fb.control<String>('', [Validators.required, Validators.maxLength(64)]), //mysql varchar 64
      password: this.fb.control<String>('', [Validators.required])
    })
  }

  processForm() {
    console.log("submitting form...");

    this.user = this.form.value;

    const requestBodyToSend = {
      username: this.user.username,
      password: this.user.password,
      items: this.cartArrayFromCheckout
    };

    
    //clean/format the array into the way I want like on exam paper
    //then send as body
    let CleanArray = [];
    
    for (let i = 0; i < this.cartArrayFromCheckout.length; i++) {
    let item = this.cartArrayFromCheckout[i];
    let cleanedItem = {
        id: item.id,
        price: item.unitPrice,
        quantity: item.quantity
    };

    CleanArray.push(cleanedItem);
    }


    this.rs.processForm(requestBodyToSend).subscribe(() => {
      console.log("sent over to server validation processing...");
    });
  }


}
