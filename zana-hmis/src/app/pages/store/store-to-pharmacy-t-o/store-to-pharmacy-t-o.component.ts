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
import { IStoreToPharmacyBatch } from 'src/app/domain/store-to-pharmacy-batch';
import { IItem } from 'src/app/domain/item';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';

const API_URL = environment.apiUrl;


@Component({
  selector: 'app-store-to-pharmacy-t-o',
  templateUrl: './store-to-pharmacy-t-o.component.html',
  styleUrls: ['./store-to-pharmacy-t-o.component.scss'],
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
export class StoreToPharmacyTOComponent {

  //localStorage.setItem('store-to-pharmacy-t-o-id', data?.id)

  id : any

  storeToPharmacyTO! : IStoreToPharmacyTO

  loadedItemNames : string[] = []

  batchId : any
  batchNo : string = ''
  batchStoreSKUQty : number = 0
  batchExpiryDate!  : Date | string | undefined
  batchStoreItemId : any
  batchStoreItemCode : string = ''
  batchStoreItemName : string = ''
  batchStoreToPharmacyTODetailId : any

  filterRecords : string = ''


  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private router : Router,
    private msgBox : MsgBoxService) { }

  async ngOnInit(): Promise<void> {
    this.id = localStorage.getItem('store-to-pharmacy-t-o-id')
    localStorage.removeItem('store-to-pharmacy-t-o-id')
    this.loadCurrentTransferOrder(this.id)
  }

  async loadCurrentTransferOrder(id : any){
    if(id === null){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    this.spinner.show()
    await this.http.get<IStoreToPharmacyTO>(API_URL+'/store_to_pharmacy_t_os/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.storeToPharmacyTO = data!
        console.log(data)
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async verifyOrder(){
    if(!window.confirm('Confirm verify order. Confirm?')){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var order = {
      id : this.id
    }
    
    this.spinner.show()
    await this.http.post<IStoreToPharmacyTO>(API_URL+'/store_to_pharmacy_t_os/verify', order, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.loadCurrentTransferOrder(this.id)
        this.msgBox.showSuccessMessage('Order verified successifully')
        console.log(data)
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async approveOrder(){
    if(!window.confirm('Confirm approve order. Confirm?')){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var order = {
      id : this.id
    }
    
    this.spinner.show()
    await this.http.post<IStoreToPharmacyTO>(API_URL+'/store_to_pharmacy_t_os/approve', order, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.loadCurrentTransferOrder(this.id)
        this.msgBox.showSuccessMessage('Order approved successifully')
        console.log(data)
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async issueGoods(){
    if(!window.confirm('Confirm issue goods. Confirm?')){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var order = {
      id : this.id
    }
    
    this.spinner.show()
    await this.http.post<IStoreToPharmacyTO>(API_URL+'/store_to_pharmacy_t_os/issue', order, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.loadCurrentTransferOrder(this.id)
        this.msgBox.showSuccessMessage('Goods issued successifully')
        console.log(data)
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async loadItemNames(medicineId : any, detailId : any){
    this.loadedItemNames = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    this.spinner.show()
    await this.http.get<string[]>(API_URL+'/store_to_pharmacy_t_os/load_item_names_by_medicine?medicine_id='+medicineId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.loadedItemNames = data!
        this.batchNo = ''
        this.batchStoreItemName = ''
        this.batchStoreItemId = null
        this.batchStoreItemCode =  ''
        this.batchStoreSKUQty = 0
        this.batchStoreToPharmacyTODetailId = detailId
        this.batchExpiryDate = undefined

        console.log(data)
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async addBatch(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var batch = {
      id: this.batchId,
      no: this.batchNo,
      storeSKUQty: this.batchStoreSKUQty,
      expiryDate: this.batchExpiryDate,
      storeToPharmacyTODetail: {id : this.batchStoreToPharmacyTODetailId},
      item: {name : this.batchStoreItemName}
    }

    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/store_to_pharmacy_t_os/add_batch', batch, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.loadCurrentTransferOrder(this.id)
        console.log(data)
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async deleteBatch(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var batch = {
      id: id
    }
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/store_to_pharmacy_t_os/delete_batch', batch, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.loadCurrentTransferOrder(this.id)
        console.log(data)
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async searchItem(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var code = this.batchStoreItemCode
    var barcode = ''
    var name = this.batchStoreItemName
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
        this.batchStoreItemId                   = data?.id
        this.batchStoreItemCode                 = data!.code
        //this.it              = data!.barcode
        this.batchStoreItemName                 = data!.name
        
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
