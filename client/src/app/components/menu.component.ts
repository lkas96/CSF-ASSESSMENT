import { Component, OnInit, Output } from '@angular/core';
import { Cart, Item } from '../models';
import { RestaurantService } from '../restaurant.service';
import { Subscription } from 'rxjs/internal/Subscription';
import { Subject } from 'rxjs/internal/Subject';
import { Router } from '@angular/router';
import { CheckoutService } from '../checkout.service';

@Component({
  selector: 'app-menu',
  standalone: false,
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent implements OnInit {

  // TODO: Task 2

  //this one to hold the cart items
  menuItems: Item[] = [];
  cartArray: Cart[] = [];

  private subscription: Subscription = new Subscription();

  constructor(private rs: RestaurantService, private router: Router, private cs: CheckoutService){}

  ngOnInit(): void {
      // this.loadData().subscribe((data: Item[]) => {
      //   this.menuItems = data;
      //   console.log("Data loaded from MongoDB successfully.");
      // });
      this.loadData().subscribe(
        (data: Item[]) => {
          this.menuItems = data;
          console.log("Data loaded from MongoDB successfully.");
        },
        (error) => {
          console.error("Error loading data", error);
        }
      );
  }

  loadData() {
    return this.rs.getMenuItems();
  }

  increaseQuantity(id: string, name: string, price: number) {
    console.log("clicked added by user");
    var unitPrice = price;

    //instantiate the cart object now
    var anItem: Cart = {
      id: id,
      unitPrice: price,
      name: name,
      quantity: 1,
      price: price,
    };

    //check if item exists in cart already, add on to the quantity
    if (this.existsInCart(anItem) == true) {
      //exists already just add
      this.updateQuantityAdd(anItem, unitPrice);
      console.log('qty added 1');

    } else {
      //Does not exists in cartarray so just add the item to array.
      console.log('New Item to Add: ', anItem)
      this.cartArray.push(anItem);
      console.log('Added New Entry to Cart Array', this.cartArray)
    }

    //push to app-root/app-checkout
    this.cartUpdated();
  }

  decreaseQuantity(id: string, name: string, price: number) {
    var unitPrice = price;

    //instantiate the cart object now
    var anItem: Cart = {
      id: id,
      unitPrice: price,
      name: name,
      quantity: 1,
      price: price
    };

    //check if item exists in cart
    if (this.existsInCart(anItem)) {
      //check quantity and add
      var currentQ = this.getCurrentQuantity(anItem);

      if (currentQ == 1) {
        //means one qrt, pop from the array entirely
        //just filter and override the array to r
        this.cartArray = this.cartArray.filter((item) => item.name != name);
        console.log('Array when 0', this.cartArray)
      } else if (currentQ > 1) {
        //if current cart lesser than maxquantity available, update quantity.
        this.updateQuantityRemove(anItem, unitPrice);
      } else {
        //nothing is done, maxed quantity alr
        // console.log('lowest min quantity reached cannot remove no more. ');
      }
    }

    //push to app-root/app-checkout
    this.cartUpdated();
  }

  //send to other compoments outside
  @Output() onCartUpdate = new Subject<Cart[]>(); //i want to pass an array object out

  cartUpdated() {
    this.onCartUpdate.next(this.cartArray);
    console.log('Event triggered: Blasting CartArray Out from App-Menu');
  }

  getCartQuantity(name: string) {
    for (let i = 0; i < this.cartArray.length; i++) {
      if ((name == this.cartArray[i].name)) {
        return this.cartArray[i].quantity
      }
    }
    return 0;
  }

  existsInCart(anItem: Cart) {
    //do a for loop to check if it exists
    for (let i = 0; i < this.cartArray.length; i++) {
      if ((anItem.name == this.cartArray[i].name)) {
        // console.log('existsInCart returned true.');
        return true;
      }
    }
    // console.log('existsInCart returned false.');
    return false;
  }

  getCurrentQuantity(anItem: Cart) {
    for (let i = 0; i < this.cartArray.length; i++) {
      if ((anItem.name == this.cartArray[i].name)) {
        // console.log('current quantity is: ', this.cartArray[i].quantity);
        return this.cartArray[i].quantity;
      }
    }
    console.log(
      'Should not get here, something is wrong. Need to revise the method another time. I am too dumb for this.'
    );
    return 0;
  }

  updateQuantityAdd(anItem: Cart, unitPrice: number) {
    console.log('Increasing Quantity for: ',anItem.name);
    for (let i = 0; i < this.cartArray.length; i++) {
      if ((anItem.name == this.cartArray[i].name)) {
        console.log('trying to add',anItem.name);
        console.log('before quantity adjust is : ', this.cartArray[i].quantity)
        this.cartArray[i].quantity = this.cartArray[i].quantity + 1;
        this.cartArray[i].price = unitPrice * this.cartArray[i].quantity
        console.log('Updated Cart: ', this.cartArray)
      }
    }
  }

  updateQuantityRemove(anItem: Cart, unitPrice: number) {
    for (let i = 0; i < this.cartArray.length; i++) {
      if ((anItem.name == this.cartArray[i].name)) {
        console.log('trying to remove',anItem.name);
        console.log('before quantity adjust is : ', this.cartArray[i].quantity)
        this.cartArray[i].quantity = this.cartArray[i].quantity - 1;
        this.cartArray[i].price = unitPrice * this.cartArray[i].quantity
        console.log('Updated Cart: ', this.cartArray)
      }
    }
  }

  getGrandTotal() {
    var sum = 0;
    for(let i = 0; i < this.cartArray.length; i++){
      sum = sum + this.cartArray[i].price
    }
    return sum;
  }

  getGrandItem() {
    var sum = 0;
    for(let i = 0; i < this.cartArray.length; i++){
      sum = sum + this.cartArray[i].quantity
    }
    return sum;
  }

  toCheckout() {
    // throw new Error('Method not implemented.');
    const data = this.cartArray;
    this.cs.changeData(data);
    console.log("DATA BALSTED...")
    this.router.navigate([`/checkout`]);
  }
  

}

