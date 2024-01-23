import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IClinic } from 'src/app/domain/clinic';
import { IClinician } from 'src/app/domain/clinician';
import { IItem } from 'src/app/domain/item';
import { IStoreItem } from 'src/app/domain/store-item';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-item-inquiry',
  templateUrl: './item-inquiry.component.html',
  styleUrls: ['./item-inquiry.component.scss'],
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    AgePipe,
    RouterLink
  ],
})
export class ItemInquiryComponent {
  
  id                  : any
	code                : string = ''
  barcode             : string = ''
	name         : string = ''
	shortName    : string = ''
	commonName          : string = ''
	vat                 : number = 0
	uom                 : string = ''
	packSize            : number = 0
	stock               : number = 0
	minimumInventory    : number = 0
	maximumInventory    : number = 0
	defaultReorderQty   : number = 0
	defaultReorderLevel : number = 0
	active              : boolean = false
	ingredients         : string = ''


  item! : IItem

  names : string[] = []

  filterRecords : string = ''

  selectedStoreId : any
  selectedStoreCode : string = ''
  selectedStoreName : string = ''


  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
  ) {}

  ngOnInit(): void {
    this.loadItemNames()
    if(localStorage.getItem('selected-store-id') != null){
      this.selectedStoreId = localStorage.getItem('selected-store-id')
    }
    if(localStorage.getItem('selected-store-code') != null){
      this.selectedStoreCode = localStorage.getItem('selected-store-code')!.toString()
    }
    if(localStorage.getItem('selected-store-name') != null){
      this.selectedStoreName = localStorage.getItem('selected-store-name')!.toString()
    }  
  }

  clear(){
    this.id                 = null
    this.code               = ''
    this.barcode            = ''
    this.name        = ''
    this.shortName   = ''
    this.commonName         = ''
    this.vat                = 0
    this.uom                = ''
    this.packSize           = 0
    this.stock              = 0
    this.minimumInventory   = 0
    this.maximumInventory   = 0
    this.defaultReorderQty  = 0
    this.defaultReorderLevel = 0
    this.active             = false
    this.ingredients        = ''
  }

  async loadItemNames(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<string[]>(API_URL+'/items/get_names', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.names = []
        data?.forEach(element => {
          this.names.push(element)
        })
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, 'Could not load item names')
      }
    )
  }

  async searchItem(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var code = this.code
    var barcode = this.barcode
    var name = this.name
    if(code != ''){
      barcode = ''
      name = ''
    }
    if(barcode != ''){
      name = ''
    }

    this.spinner.show()
    await this.http.get<IItem>(API_URL+'/items/search?code='+code+'&barcode='+barcode+'&name='+name, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.id                   = data?.id
        this.code                 = data!.code
        this.barcode              = data!.barcode
        this.name                 = data!.name
        this.shortName            = data!.shortName
        this.commonName           = data!.commonName
        this.vat                  = data!.vat
        this.uom                  = data!.uom
        this.packSize             = data!.packSize
        this.stock                = data!.stock
        this.minimumInventory     = data!.minimumInventory
        this.maximumInventory     = data!.maximumInventory
        this.defaultReorderQty    = data!.defaultReorderQty
        this.defaultReorderLevel  = data!.defaultReorderLevel
        this.active               = data!.active
        this.ingredients          = data!.ingredients
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async loadItemByStore(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var code = this.code
    var barcode = this.barcode
    var name = this.name
    if(code != ''){
      barcode = ''
      name = ''
    }
    if(barcode != ''){
      name = ''
    }

    var store = {id : this.selectedStoreId, code : this.selectedStoreCode}

    this.spinner.show()
    await this.http.post<IStoreItem>(API_URL+'/items/load_item_by_store?code='+code+'&barcode='+barcode+'&name='+name, store, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.id                   = data?.item?.id
        this.code                 = data!.item?.code
        this.barcode              = data!.item?.barcode
        this.name                 = data!.item?.name
        this.shortName            = data!.item?.shortName
        this.commonName           = data!.item?.commonName
        this.vat                  = data!.item?.vat
        this.uom                  = data!.item?.uom
        this.packSize             = data!.item?.packSize
        this.stock                = data!.stock
        this.minimumInventory     = data!.minimumInventory
        this.maximumInventory     = data!.maximumInventory
        this.defaultReorderQty    = data!.defaultReorderQty
        this.defaultReorderLevel  = data!.defaultReorderLevel
        this.active               = data!.active
        this.ingredients          = data!.item?.ingredients
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )
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
