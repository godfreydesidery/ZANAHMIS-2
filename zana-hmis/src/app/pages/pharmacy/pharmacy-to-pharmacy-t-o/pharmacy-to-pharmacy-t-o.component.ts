import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IMedicine } from 'src/app/domain/medicine';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';
import { IPharmacyToPharmacyTO } from 'src/app/domain/pharmacy-to-pharmacy-t-o';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-pharmacy-to-pharmacy-t-o',
  templateUrl: './pharmacy-to-pharmacy-t-o.component.html',
  styleUrls: ['./pharmacy-to-pharmacy-t-o.component.scss'],
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
export class PharmacyToPharmacyTOComponent {

id : any

pharmacyToPharmacyTO! : IPharmacyToPharmacyTO

loadedMedicine! : IMedicine

batchId : any
batchNo : string = ''
batchQty : number = 0
batchExpiryDate!  : Date | string | undefined
batchMedicineId : any
batchMedicineCode : string = ''
batchMedicineName : string = ''
batchPharmacyToPharmacyTODetailId : any

filterRecords : string = ''


constructor(
  private auth : AuthService,
  private http :HttpClient,
  private modalService: NgbModal,
  private spinner : NgxSpinnerService,
  private router : Router,
  private msgBox : MsgBoxService) { }

async ngOnInit(): Promise<void> {
  this.id = localStorage.getItem('pharmacy-to-pharmacy-t-o-id')
  localStorage.removeItem('pharmacy-to-pharmacy-t-o-id')
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
  await this.http.get<IPharmacyToPharmacyTO>(API_URL+'/pharmacy_to_pharmacy_t_os/get?id='+id, options)
  .pipe(finalize(() => this.spinner.hide()))
  .toPromise()
  .then(
    data => {
      this.pharmacyToPharmacyTO = data!
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
  await this.http.post<IPharmacyToPharmacyTO>(API_URL+'/pharmacy_to_pharmacy_t_os/verify', order, options)
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
  await this.http.post<IPharmacyToPharmacyTO>(API_URL+'/pharmacy_to_pharmacy_t_os/approve', order, options)
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
  await this.http.post<IPharmacyToPharmacyTO>(API_URL+'/pharmacy_to_pharmacy_t_os/issue', order, options)
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

async loadMedicineNames(medicineId : any, detailId : any){

  this.batchMedicineId                   = null
  this.batchMedicineCode                 = ''
  this.batchMedicineName                 = ''

  let options = {
    headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
  }

  this.spinner.show()
  await this.http.get<IMedicine>(API_URL+'/pharmacy_to_pharmacy_t_os/load_medicine_names_by_requested_medicine?medicine_id='+medicineId, options)
  .pipe(finalize(() => this.spinner.hide()))
  .toPromise()
  .then(
    data => {
      this.loadedMedicine = data!

      this.batchMedicineId                   = data?.id
      this.batchMedicineCode                 = data!.code
      this.batchMedicineName                 = data!.name

      this.batchNo = ''
      this.batchQty = 0
      this.batchPharmacyToPharmacyTODetailId = detailId
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
    qty: this.batchQty,
    expiryDate: this.batchExpiryDate,
    pharmacyToPharmacyTODetail: {id : this.batchPharmacyToPharmacyTODetailId},
    medicine: {name : this.batchMedicineName}
  }

  this.spinner.show()
  await this.http.post<boolean>(API_URL+'/pharmacy_to_pharmacy_t_os/add_batch', batch, options)
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
  await this.http.post<boolean>(API_URL+'/pharmacy_to_pharmacy_t_os/delete_batch', batch, options)
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

async searchMedicine(){
  let options = {
    headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
  }
  var code = this.batchMedicineCode
  var barcode = ''
  var name = this.batchMedicineName
  if(code != ''){
    barcode = ''
    name = ''
  }
  if(barcode != ''){
    name = ''
  }

  this.spinner.show()
  await this.http.get<IMedicine>(API_URL+'/medicines/search?code='+code+'&name='+name, options)
  .pipe(finalize(() => this.spinner.hide()))
  .toPromise()
  .then(
    data => {
      this.batchMedicineId                   = data?.id
      this.batchMedicineCode                 = data!.code
      //this.it              = data!.barcode
      this.batchMedicineName                 = data!.name
      
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