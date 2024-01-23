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
import { IPharmacyToPharmacyRO } from 'src/app/domain/pharmacy-to-pharmacy-r-o';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { IPharmacy } from 'src/app/domain/pharmacy';
import { IPharmacyToPharmacyRN } from 'src/app/domain/pharmacy-to-pharmacy-r-n';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-pharmacy-to-pharmacy-r-o',
  templateUrl: './pharmacy-to-pharmacy-r-o.component.html',
  styleUrls: ['./pharmacy-to-pharmacy-r-o.component.scss'],
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
export class PharmacyToPharmacyROComponent {
  
  pharmacyId : any = localStorage.getItem('selected-pharmacy-id')
  pharmacyCode = localStorage.getItem('selected-pharmacy-code')
  pharmacyName = localStorage.getItem('selected-pharmacy-name')

  id : any
  no : string = ''
  orderDate! : Date
  validUntil! : Date | string
  status : string = ''
  statusDescription : string = ''
  created : string = ''
  verified : string = ''
  approved : string = ''

  

  pharmacyToPharmacyRO! : IPharmacyToPharmacyRO

  noLocked = false

  pharmacyToPharmacyROs : IPharmacyToPharmacyRO[] = []

  detailId          : any
  detailCode        : string = ''
  detailName        : string = ''
  detailOrderedQty  : number = 0
  detailReceivedQty : number = 0

  medicineNames : string[] = []

  //pharmacyToPharmacyDetails : IPharmacyToPharmacyRODetail[] = []

  filterRecords : string = ''


  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private router : Router,
    private msgBox : MsgBoxService) { }

  async ngOnInit(): Promise<void> {

    this.loadOrdersByRequestingPharmacy()
    this.loadMedicineNames()
  }

  async requestNo(){
    this.clear()
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<any>(API_URL+'/pharmacy_to_pharmacy_r_os/request_no', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.no = data!['no']
        this.noLocked = true
      }
    )
    .catch(
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.pharmacyToPharmacyRO.pharmacyToPharmacyRODetails = []
  }

  async saveOrder(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var order = {
      id  : this.id,
      no : this.no,
      requestingPharmacy : {id : this.pharmacyId, code : this.pharmacyCode},
      deliveringPharmacy : {id : this.deliveringPharmacyId, code : this.deliveringPharmacyCode},
      validUntil : this.validUntil
    }
    this.spinner.show()
    await this.http.post<IPharmacyToPharmacyRO>(API_URL+'/pharmacy_to_pharmacy_r_os/save', order, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.id         = data?.id
        this.no         = data!.no
        this.orderDate  = data!.orderDate
        this.validUntil = data!.validUntil
        this.status     = data!.status
        this.statusDescription = data!.statusDescription
        this.created    = data!.created
        this.verified   = data!.verified
        this.approved   = data!.approved

        this.pharmacyToPharmacyRO = data!
        this.deliveringPharmacyName = data!.deliveringPharmacy?.name

        
        this.msgBox.showSuccessMessage('Success')
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  

  async saveDetail(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var detail = {
      id                : this.detailId,
      pharmacyToPharmacyRO : {id : this.id},
      medicine          : {name : this.detailName},
      orderedQty        : this.detailOrderedQty,
      receivedQty       : this.detailReceivedQty
    }
    this.spinner.show()
    await this.http.post(API_URL+'/pharmacy_to_pharmacy_r_os/save_detail', detail, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.search(this.id)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async deleteDetail(detailId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var detail = {
      id : detailId,
      pharmacyToPharmacyRO : {id : this.id}
    }
    
    this.spinner.show()
    await this.http.post(API_URL+'/pharmacy_to_pharmacy_r_os/delete_detail', detail, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.search(this.id)
      }
    )
    .catch(
      error => {
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
      id  : this.id,
      no : this.no,
      requestingPharmacy : {id : this.pharmacyId }
    }
    this.spinner.show()
    await this.http.post<IPharmacyToPharmacyRO>(API_URL+'/pharmacy_to_pharmacy_r_os/verify', order, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.id         = data?.id
        this.no         = data!.no
        this.orderDate  = data!.orderDate
        this.validUntil = data!.validUntil
        this.status     = data!.status
        this.statusDescription = data!.statusDescription
        this.created    = data!.created
        this.verified   = data!.verified
        this.approved   = data!.approved

        this.pharmacyToPharmacyRO = data!
        this.deliveringPharmacyName = data!.deliveringPharmacy?.name

        this.msgBox.showSuccessMessage('Order verified successifuly')
      }
    )
    .catch(
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
      id  : this.id,
      no : this.no,
      requestingPharmacy : {id : this.pharmacyId }
    }
    this.spinner.show()
    await this.http.post<IPharmacyToPharmacyRO>(API_URL+'/pharmacy_to_pharmacy_r_os/approve', order, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.id         = data?.id
        this.no         = data!.no
        this.orderDate  = data!.orderDate
        this.validUntil = data!.validUntil
        this.status     = data!.status
        this.statusDescription = data!.statusDescription
        this.created    = data!.created
        this.verified   = data!.verified
        this.approved   = data!.approved

        this.pharmacyToPharmacyRO = data!
        this.deliveringPharmacyName = data!.deliveringPharmacy?.name

        this.msgBox.showSuccessMessage('Order approved successifuly')
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async submitOrder(){
    if(!window.confirm('Confirm submit order. Confirm?')){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var order = {
      id  : this.id,
      no : this.no,
      requestingPharmacy : {id : this.pharmacyId }
    }
    this.spinner.show()
    await this.http.post<IPharmacyToPharmacyRO>(API_URL+'/pharmacy_to_pharmacy_r_os/submit', order, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.id         = data?.id
        this.no         = data!.no
        this.orderDate  = data!.orderDate
        this.validUntil = data!.validUntil
        this.status     = data!.status
        this.statusDescription = data!.statusDescription
        this.created    = data!.created
        this.verified   = data!.verified
        this.approved   = data!.approved

        this.pharmacyToPharmacyRO = data!
        this.deliveringPharmacyName = data!.deliveringPharmacy?.name

        this.msgBox.showSuccessMessage('Order submitted successifuly')
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async loadMedicineNames(){
    this.medicineNames = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<string[]>(API_URL+'/medicines/get_names', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        data?.forEach(element => {
          this.medicineNames.push(element)
        })
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, 'Could not load medicine names')
      }
    )
  }

  postOrder(){

  }

  cancelOrder(){
    
  }

  lock(){
    this.noLocked = true

  }

  unlock(){
    this.noLocked = false

  }

  async openToEdit(){
    if(this.id == null){
      this.noLocked = false
      this.no = ''
    }else{
      this.noLocked = true
    }
  }

  clear(){
    this.id         = null
    this.no         = ''
    this.orderDate! = new Date()
    this.validUntil!  = ''
    this.status     = ''
    this.statusDescription = ''
    this.created    = ''
    this.verified   = ''
    this.approved   = ''
    this.pharmacyToPharmacyRO!
    this.deliveringPharmacyName = ''
    this.deliveringPharmacyId = null
    this.deliveringPharmacyCode = ''
  }


  async searchOrder(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    this.spinner.show()
    await this.http.get<IPharmacyToPharmacyRO>(API_URL+'/pharmacy_to_pharmacy_r_os/search_by_no?no='+this.no+'&pharmacy_id='+this.pharmacyId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.id         = data?.id
        this.no         = data!.no
        this.orderDate  = data!.orderDate
        this.validUntil = data!.validUntil
        this.status     = data!.status
        this.statusDescription = data!.statusDescription
        this.created    = data!.created
        this.verified   = data!.verified
        this.approved   = data!.approved

        this.pharmacyToPharmacyRO = data!
        this.deliveringPharmacyName = data!.deliveringPharmacy?.name

        this.lock()
        console.log(data)
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async search(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    this.spinner.show()
    await this.http.get<IPharmacyToPharmacyRO>(API_URL+'/pharmacy_to_pharmacy_r_os/search?id='+id+'&pharmacy_id='+this.pharmacyId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.id         = data?.id
        this.no         = data!.no
        this.orderDate  = data!.orderDate
        this.validUntil = data!.validUntil
        this.status     = data!.status
        this.statusDescription = data!.statusDescription
        this.created    = data!.created
        this.verified   = data!.verified
        this.approved   = data!.approved

        this.pharmacyToPharmacyRO = data!
        this.deliveringPharmacyName = data!.deliveringPharmacy?.name

        this.lock()
        console.log(data)
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async loadOrdersByRequestingPharmacy(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    this.spinner.show()
    await this.http.get<IPharmacyToPharmacyRO[]>(API_URL+'/pharmacy_to_pharmacy_r_os/load_pharmacy_orders_by_requesting_pharmacy?pharmacy_id='+this.pharmacyId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.pharmacyToPharmacyROs = data!
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
    if(this.detailCode != ''){
      this.spinner.show()
      await this.http.get<IMedicine>(API_URL+'/medicines/get_by_code?code='+this.detailCode, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.clearDetail()
          this.detailCode = data!.code
          this.detailName = data!.name
        },
        error => {
          console.log(error)
          this.msgBox.showErrorMessage(error, '')
        }
      )
    }else{
      this.spinner.show()
      await this.http.get<IMedicine>(API_URL+'/medicines/get_by_name?name='+this.detailName, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.clearDetail()
          this.detailCode = data!.code
          this.detailName = data!.name
        },
        error => {
          console.log(error)
          this.msgBox.showErrorMessage(error, '')
        }
      )
    }
    
  }

  clearDetail(){
    this.detailId = null
    this.detailCode = ''
    this.detailName = ''
    this.detailOrderedQty = 0
    this.detailReceivedQty = 0
  }



  deliveringPharmacyId : any =  null
  deliveringPharmacyCode : string = ''
  deliveringPharmacyName : string = ''
  deliveringPharmacyCodeAndName : string = ''
  pharmacies : IPharmacy[] = []
  async loadPharmaciesLike(value : string){
    this.deliveringPharmacyId = null
    this.pharmacies = []
    if(value.length < 2){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.get<IPharmacy[]>(API_URL+'/pharmacies/load_pharmacies_like?name_like='+value, options)
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.pharmacies = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async getPharmacy(id : any){
    this.deliveringPharmacyId = null
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.pharmacies = []
    this.spinner.show()
    await this.http.get<IPharmacy>(API_URL+'/pharmacies/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {
        this.deliveringPharmacyId = data?.id
        this.deliveringPharmacyCode = data!.code
        this.deliveringPharmacyName = data!.name
        this.deliveringPharmacyCodeAndName = data!.code +' | '+ data!.name
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
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

