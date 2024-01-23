import { Component } from '@angular/core';

@Component({
  selector: 'app-medicine-prices',
  templateUrl: './medicine-price.component.html',
  styleUrls: ['./medicine-price.component.scss']
})
export class MedicinePricesComponent {
  filterRecords : string = ''
}
