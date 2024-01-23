import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IPrescription } from 'src/app/domain/prescription';
import { IMedicine } from 'src/app/domain/medicine';
import { IPatient } from 'src/app/domain/patient';
import { IPatientBill } from 'src/app/domain/patient-bill';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';
import { IPharmacyToStoreRO } from 'src/app/domain/pharmacy-to-store-r-o';
import { IPharmacyToStoreRODetail } from 'src/app/domain/pharmacy-to-store-r-o-detail';
import { IStoreToPharmacyTO } from 'src/app/domain/store-to-pharmacy-t-o';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';

const API_URL = environment.apiUrl;


@Component({
  selector: 'app-pharmacy-to-store-r-o-list',
  templateUrl: './pharmacy-to-store-r-o-list.component.html',
  styleUrls: ['./pharmacy-to-store-r-o-list.component.scss'],
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
export class PharmacyToStoreROListComponent {

  id : any = null

  pharmacyToStoreROs : IPharmacyToStoreRO[] = []

  pharmacyToStoreRO! : IPharmacyToStoreRO

  filterRecords : string = ''

  selectedStoreId : any = null
  selectedStoreCode : string = ''
  selectedStoreName : string = ''

  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private router : Router,
    private msgBox : MsgBoxService) { }

  async ngOnInit(): Promise<void> {
    if(localStorage.getItem('selected-store-id') != null){
      this.selectedStoreId = localStorage.getItem('selected-store-id')
    }
    if(localStorage.getItem('selected-store-code') != null){
      this.selectedStoreCode = localStorage.getItem('selected-store-code')!.toString()
    }
    if(localStorage.getItem('selected-store-name') != null){
      this.selectedStoreName = localStorage.getItem('selected-store-name')!.toString()
    } 
    this.loadOrdersByStore() 
  }


  async loadOrdersByStore(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    this.spinner.show()
    await this.http.get<IPharmacyToStoreRO[]>(API_URL+'/pharmacy_to_store_r_os/load_pharmacy_orders_by_store?store_id='+this.selectedStoreId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.pharmacyToStoreROs = data!
        console.log(data)
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async get(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    this.id = null
    
    this.spinner.show()
    await this.http.get<IPharmacyToStoreRO>(API_URL+'/pharmacy_to_store_r_os/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.id = id

        /*this.id         = data?.id
        this.no         = data!.no
        this.orderDate  = data!.orderDate
        this.validUntil = data!.validUntil
        this.status     = data!.status
        this.created    = data!.created
        this.verified   = data!.verified
        this.approved   = data!.approved*/

        this.pharmacyToStoreRO = data!

        console.log(data)
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async createTransferOrder(id : any){
    if(!window.confirm('Greate / Go to Transfer Order. Confirm?')){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var pharmacyToStoreRO = {
      id : id,
      store : {id : this.selectedStoreId, code : this.selectedStoreCode}
    }

    this.spinner.show()
    await this.http.post<IStoreToPharmacyTO>(API_URL+'/store_to_pharmacy_t_os/create', pharmacyToStoreRO, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        localStorage.setItem('store-to-pharmacy-t-o-id', data?.id)
        this.router.navigate(['store-to-pharmacy-t-o'])

        /*this.id         = data?.id
        this.no         = data!.no
        this.orderDate  = data!.orderDate
        this.validUntil = data!.validUntil
        this.status     = data!.status
        this.created    = data!.created
        this.verified   = data!.verified
        this.approved   = data!.approved*/

        console.log(data)
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )

  }

  async returnOrder(){
    if(!window.confirm('Confirm return order for correction. Confirm?')){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var order = {
      id  : this.pharmacyToStoreRO?.id,
      no : this.pharmacyToStoreRO?.no,
    }
    this.spinner.show()
    await this.http.post<IPharmacyToStoreRO>(API_URL+'/pharmacy_to_store_r_os/return', order, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      () => {
        this.msgBox.showSuccessMessage('Order returned successifuly')
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async rejectOrder(){
    if(!window.confirm('Confirm reject order. Confirm?')){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var order = {
      id  : this.pharmacyToStoreRO?.id,
      no : this.pharmacyToStoreRO?.no,
    }
    this.spinner.show()
    await this.http.post<IPharmacyToStoreRO>(API_URL+'/pharmacy_to_store_r_os/reject', order, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      () => {
        this.msgBox.showSuccessMessage('Order rejected successifuly')
      }
    )
    .catch(
      error => {
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
