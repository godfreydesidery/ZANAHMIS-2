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
import { IPharmacySale } from 'src/app/domain/pharmacy-sale';
import { IPharmacySaleDetail } from 'src/app/domain/pharmacy-sale-detail';
import { ISingleObject } from 'src/app/domain/single-object';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
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

  pharmacySale! : IPharmacySale

  pharmacistId : any

  pharmacyCustomers : IPharmacyCustomer[] = []

  pharmacyName = localStorage.getItem('selected-pharmacy-name')

  filterRecords : string = ''

  constructor(private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private router : Router,
    private msgBox : MsgBoxService) { }


    pharmacySaleDetailUnit        : number = 0
    pharmacySaleDetailDosage      : string = ''
    pharmacySaleDetailFrequency   : string = ''
    pharmacySaleDetailRoute       : string = ''
    pharmacySaleDetailDays        : string = ''
    pharmacySaleDetailPrice       : number = 0
    pharmacySaleDetailQty         : number = 0
    pharmacySaleDetailInstructions : string = '' 
    
    
    pharmacySaleDetails : IPharmacySaleDetail[] = []

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
    this.pharmacySaleDetailUnit = 0
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
        this.pharmacySaleDetailUnit = data!
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

  async deletePharmacySaleDetail(pharmacySaleDetailId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/delete_pharmacy_sale_detail?id='+pharmacySaleDetailId, options)
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
    this.loadPharmacySaleDetails(this.id)
  }

  async loadPharmacySaleDetails(pharmacySaleId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.pharmacySaleDetails = []
    this.spinner.show()
    await this.http.get<IPharmacySaleDetail[]>(API_URL+'/patients/load_pharmacy_sale_details?pharmacy_sale_id='+pharmacySaleId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.pharmacySaleDetails = data!
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, 'Could not load sales')
      }
    )
    
  }

  async savePharmacySaleDetail(medicineId : string){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var pharmacySaleDetail = {
      medicine : {
        id : this.medicineId,
        code : this.medicineCode,
        name : this.medicineName
      },
      dosage    : this.pharmacySaleDetailDosage,
      frequency : this.pharmacySaleDetailFrequency,
      route     : this.pharmacySaleDetailRoute,
      days      : this.pharmacySaleDetailDays,
      price     : this.pharmacySaleDetailPrice,
      qty       : this.pharmacySaleDetailQty,
      instructions : this.pharmacySaleDetailInstructions
    }

    if( pharmacySaleDetail.medicine.name === '' || 
        pharmacySaleDetail.dosage === '' || 
        pharmacySaleDetail.frequency === '' || 
        pharmacySaleDetail.route === '' || 
        pharmacySaleDetail.days === ''){
      this.msgBox.showErrorMessage3('Can not save, please fill in all the required fields')
      return
    }

    this.spinner.show()
    await this.http.post<IPharmacySaleDetail>(API_URL+'/patients/save_pharmacy_sale_detail?pharmacy_sale_id='+this.id, pharmacySaleDetail, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {
        this.loadPharmacySaleDetails(this.id)
        this.clearPharmacySaleDetail()
        this.msgBox.showSuccessMessage('Sale Saved successifully')
      }
    )
    .catch(
      error => {
        this.loadPharmacySaleDetails(this.id)
        this.clearPharmacySaleDetail()
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )

  }




  

  clearPharmacySaleDetail(){
    this.medicineId = null
    this.medicineCode = ''
    this.medicineName = ''

    this.pharmacySaleDetailUnit         = 0
    this.pharmacySaleDetailDosage       = ''
    this.pharmacySaleDetailFrequency    = ''
    this.pharmacySaleDetailRoute        = ''
    this.pharmacySaleDetailDays         = ''
    this.pharmacySaleDetailPrice        = 0
    this.pharmacySaleDetailQty          = 0
    this.pharmacySaleDetailInstructions = ''

  }
}
