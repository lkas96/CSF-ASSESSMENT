import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MenuComponent } from './components/menu.component';
import { ConfirmationComponent } from './components/confirmation.component';
import { PlaceOrderComponent } from './components/place-order.component';


const routes: Routes = [
  { path: '', component: MenuComponent }, //this is the default one on first view - localhost:4200/  -> loads this default page/landing view page
  { path: 'checkout', component: PlaceOrderComponent }, //when hit place order, view 2
  { path: '**', redirectTo: '' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
