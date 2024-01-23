import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { AuthService } from '../auth.service';
import { DataService } from '../services/data.service';
import { PosReceiptPrinterService } from '../services/pos-receipt-printer.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  constructor(private router : Router,
    private http  : HttpClient,
    private auth : AuthService,
    private titleService: Title,
    private spinner: NgxSpinnerService,
    private data : DataService,
    private printer : PosReceiptPrinterService) { }

  ngOnInit(): void {
  }

  public grant(privilege : string[]) : boolean{
    /**Allow user to perform an action if the user has that priviledge */
    var granted : boolean = false
    privilege.forEach(
      element => {
        if(this.auth.checkPrivilege(element)){
          granted = true
        }
      }
    )
    return granted
  }

}
