import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IConsultation } from 'src/app/domain/consultation';
import { IMedicine } from 'src/app/domain/medicine';
import { IPatient } from 'src/app/domain/patient';
import { IPharmacyCustomer } from 'src/app/domain/pharmacy-customer';
import { IPharmacySaleOrder } from 'src/app/domain/pharmacy-sale-order';
import { IPharmacySaleOrderDetail } from 'src/app/domain/pharmacy-sale-order-detail';
import { ISingleObject } from 'src/app/domain/single-object';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { noUndefined } from 'src/custom-packages/util';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-pharmacy-sales-order',
  templateUrl: './pharmacy-sales-order.component.html',
  styleUrls: ['./pharmacy-sales-order.component.scss'],
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
export class PharmacySalesOrderComponent {

  id : any

  no : string = ''

  pharmacySaleOrder! : IPharmacySaleOrder

  pharmacySaleOrders : IPharmacySaleOrder[] = []

  status : string = ''

  created : string = ''

  pharmacistId : any


  pharmacyName = localStorage.getItem('selected-pharmacy-name')

  filterRecords : string = ''

  constructor(private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private router : Router,
    private msgBox : MsgBoxService) { }


    pharmacySaleOrderDetailUnit        : number = 0
    pharmacySaleOrderDetailDosage      : string = ''
    pharmacySaleOrderDetailFrequency   : string = ''
    pharmacySaleOrderDetailRoute       : string = ''
    pharmacySaleOrderDetailDays        : string = ''
    pharmacySaleOrderDetailPrice       : number = 0
    pharmacySaleOrderDetailQty         : number = 0
    pharmacySaleOrderDetailInstructions : string = '' 
    
    
    pharmacySaleOrderDetails : IPharmacySaleOrderDetail[] = []


    pharmacyCustomer! : IPharmacyCustomer
    pharmacyCustomerMode : string = 'New Customer'



  async ngOnInit(): Promise<void> {
    await this.loadPharmacist()
  }

  async loadPharmacist(){    
    var username = localStorage.getItem('username')!
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<any>(API_URL+'/pharmacists/load_pharmacist_by_username?username='+username, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.pharmacistId = data
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load pharmacist')
      }
    )
  }


  pharmacyCustomerId : any =  null
  pharmacyCustomerNo : string = ''
  pharmacyCustomerName : string = ''
  pharmacyCustomerPhone : string = ''
  pharmacyCustomerAddress : string = ''
  pharmacyCustomers : IPharmacyCustomer[] = []
  async loadPharmacyCustomerLike(value : string){
    this.medicines = []
    if(value.length < 2){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.get<IPharmacyCustomer[]>(API_URL+'/medicines/load_pharmacy_customers_like?name_like='+value, options)
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.pharmacyCustomers = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }



  medicineId : any =  null
  medicineCode : string = ''
  medicineName : string = ''
  medicines : IMedicine[] = []
  async loadMedicinesLike(value : string){
    this.medicines = []
    if(value.length < 2){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.get<IMedicine[]>(API_URL+'/medicines/load_medicines_like?name_like='+value, options)
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.medicines = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async getMedicine(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.medicines = []
    this.spinner.show()
    await this.http.get<IMedicine>(API_URL+'/medicines/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      async (data) => {
        this.medicineId = data?.id
        this.medicineCode = data!.code
        this.medicineName = data!.name

        await this.getMedicineUnit(this.medicineId)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }

  async getMedicineUnit(id : any){
    this.pharmacySaleOrderDetailUnit = 0
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var medicine = {
      id : id
    }
    this.spinner.show()
    await this.http.post<number>(API_URL+'/medicines/get_available_units', medicine, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.pharmacySaleOrderDetailUnit = data!
        return data
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, 'Could not get units')
        return 0
      }
    )
  }

  async deletePharmacySaleOrderDetail(pharmacySaleDetailId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/delete_pharmacy_sale_order_detail?id='+pharmacySaleDetailId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.loadPharmacySaleOrderDetails(this.id)
  }

  async loadPharmacySaleOrderDetails(pharmacySaleOrderId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.pharmacySaleOrderDetails = []
    this.spinner.show()
    await this.http.get<IPharmacySaleOrderDetail[]>(API_URL+'/pharmacies/load_pharmacy_sale_order_details?id='+pharmacySaleOrderId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.pharmacySaleOrderDetails = data!
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, 'Could not load sales')
      }
    )
    
  }

  async savePharmacySaleOrderDetail(medicineId : string){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var pharmacySaleOrderDetail = {
      medicine : {
        id : this.medicineId,
        code : this.medicineCode,
        name : this.medicineName
      },
      pharmacySaleOrder : {
        id : this.id
      },
      dosage    : this.pharmacySaleOrderDetailDosage,
      frequency : this.pharmacySaleOrderDetailFrequency,
      route     : this.pharmacySaleOrderDetailRoute,
      days      : this.pharmacySaleOrderDetailDays,
      price     : this.pharmacySaleOrderDetailPrice,
      qty       : this.pharmacySaleOrderDetailQty,
      instructions : this.pharmacySaleOrderDetailInstructions
    }

    if( pharmacySaleOrderDetail.medicine.name === '' || 
        pharmacySaleOrderDetail.dosage === '' || 
        pharmacySaleOrderDetail.frequency === '' || 
        pharmacySaleOrderDetail.route === '' || 
        pharmacySaleOrderDetail.days === ''){
      this.msgBox.showErrorMessage3('Can not save, please fill in all the required fields')
      return
    }

    this.spinner.show()
    await this.http.post<IPharmacySaleOrderDetail>(API_URL+'/pharmacies/save_pharmacy_sale_order_detail', pharmacySaleOrderDetail, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {
        this.loadPharmacySaleOrderDetails(this.id)
        this.clearPharmacySaleOrderDetail()
        this.msgBox.showSuccessMessage('Sale Saved successifully')
      }
    )
    .catch(
      error => {
        this.loadPharmacySaleOrderDetails(this.id)
        this.clearPharmacySaleOrderDetail()
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )

  }

  async savePharmacySaleOrder(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var order = {
      id: this.id,
      pharmacyCustomer: {
        id: this.pharmacyCustomerId,
        no: this.pharmacyCustomerNo,
        name: this.pharmacyCustomerName,
        phoneNo: this.pharmacyCustomerPhone,
        address: this.pharmacyCustomerAddress
      },
      status: '',
      paymentType: 'CASH',
      pharmacist: {id : this.pharmacistId},
      pharmacy: {name : this.pharmacyName},
    }

    this.spinner.show()
    await this.http.post<IPharmacySaleOrder>(API_URL+'/pharmacies/save_pharmacy_sale_order', order, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {

        this.pharmacySaleOrder = data!

        this.id = data?.id
        this.no = data!.no
        this.status = data!.status
        this.created = data!.created

        this.pharmacyCustomerId = data!.pharmacyCustomer!.id
        this.pharmacyCustomerNo = data!.pharmacyCustomer!.no
        this.pharmacyCustomerName = data!.pharmacyCustomer!.name
        this.pharmacyCustomerPhone = data!.pharmacyCustomer!.phoneNo

        this.loadPharmacySaleOrderDetails(data!.id)
        this.clearPharmacySaleOrderDetail()
        this.msgBox.showSuccessMessage('Sale Saved successifully')
        
      }
    )
    .catch(
      error => {
        this.loadPharmacySaleOrderDetails(this.id)
        this.clearPharmacySaleOrderDetail()
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )


  }



  clearPharmacyOrder(){
    this.clearPharmacyCustomer()
    this.id = null
    this.no = ''
    this.status = ''
    this.created = ''
    this.clearPharmacySaleOrderDetail()
  }




  

  clearPharmacySaleOrderDetail(){
    this.medicineId = null
    this.medicineCode = ''
    this.medicineName = ''

    this.pharmacySaleOrderDetailUnit         = 0
    this.pharmacySaleOrderDetailDosage       = ''
    this.pharmacySaleOrderDetailFrequency    = ''
    this.pharmacySaleOrderDetailRoute        = ''
    this.pharmacySaleOrderDetailDays         = ''
    this.pharmacySaleOrderDetailPrice        = 0
    this.pharmacySaleOrderDetailQty          = 0
    this.pharmacySaleOrderDetailInstructions = ''

  }

  clearPharmacyCustomer(){

    /**Clear pharmacy Order first */
    this.id = null
    this.no = ''
    this.status = ''

    /**Then clear pharmacy customer */
    this.pharmacyCustomerMode = 'New Customer'
    this.pharmacyCustomerId = null
    this.pharmacyCustomerNo = ''
    this.pharmacyCustomerName = ''
    this.pharmacyCustomerPhone = ''
    this.pharmacyCustomerAddress = ''
    this.pharmacyCustomer!    
  }

  searchingPharmacyCustomer(){
    this.pharmacyCustomerMode = 'Existing Customer'
  }

  async getPharmacyCustomer(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.pharmacyCustomers = []
    this.spinner.show()
    await this.http.get<IPharmacyCustomer>(API_URL+'/pharmacies/pharmacy_customers/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      async (data) => {
        this.pharmacyCustomerId = data?.id
        this.pharmacyCustomerNo = data!.no
        this.pharmacyCustomerName = data!.name
        this.pharmacyCustomerPhone = data!.phoneNo
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }


  async loadPharmacySaleOrders(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.pharmacySaleOrders = []
    this.spinner.show()
    await this.http.get<IPharmacySaleOrder[]>(API_URL+'/pharmacies/pharmacy_sale_orders', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.pharmacySaleOrders = data!
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, 'Could not load sales')
      }
    )
    
  }
}
