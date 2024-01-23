import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PosReceiptPrinterService } from '../services/pos-receipt-printer.service';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.scss']
})
export class MainPageComponent implements OnInit {

  isLoggedIn : boolean = false;

  userName : string = ''


  constructor(private router : Router,
    printer : PosReceiptPrinterService) { }

  ngOnInit(): void {
    var currentUser = null
    if(localStorage.getItem('user-name') != null){
      this.userName = localStorage.getItem('user-name')!
    }else{
      this.userName = ''
    }
    if(localStorage.getItem('current-user') != null){
      currentUser = localStorage.getItem('current-user')
    }
    if(currentUser != null){
      this.isLoggedIn = true
    }else{
      this.isLoggedIn = false
    }
  }

  public async logout() : Promise<any>{
    localStorage.removeItem('current-user')
    alert('You have logged out!')
    await this.router.navigate([''])
    window.location.reload()
  }

}
