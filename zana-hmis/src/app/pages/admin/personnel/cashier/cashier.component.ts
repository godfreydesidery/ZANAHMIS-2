import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { ICashier } from 'src/app/domain/cashier';
import { IClinic } from 'src/app/domain/clinic';

import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-cashier',
  standalone : true,
  templateUrl: './cashier.component.html',
  styleUrls: ['./cashier.component.scss'],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ]
})
export class CashierComponent {


  id : any
  code : string = ''
  firstName : string = ''
  middleName : string = ''
  lastName : string = ''
  type : string = ''
  active : boolean = true

  public clinics           : IClinic[]


  cashiers : ICashier[] = []


  userCode : string = ''

  filterRecords : string = ''


  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
  ) {
    this.clinics           = []
  }

  ngOnInit(): void {
    this.loadCashiers()
    this.loadClinics()
  }

  public async saveCashier(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var cashierClinics : IClinic[] = []
    this.clinics.forEach(clinic => { /**Get the roles */
      if(clinic.assigned == true){
        cashierClinics.push(clinic)
      }
    })
    var cashier = {
      id          : this.id,
      code        : this.code,
      firstName   : this.firstName,
      middleName  : this.middleName,
      lastName    : this.lastName,
      type        : this.type,
      clinics     : cashierClinics,
      active      : true
    }
    if(this.id == null || this.id == ''){
      //save a new clinic
      this.spinner.show()
      await this.http.post<ICashier>(API_URL+'/cashiers/save', cashier, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id         = data?.id
          this.code       = data!.code
          this.firstName  = data!.firstName
          this.middleName = data!.middleName
          this.lastName   = data!.lastName
          this.type       = data!.type
          this.active     = data!.active
          this.msgBox.showSuccessMessage('Cashier created successifully')
          this.loadCashiers()
          this.clear()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, '')
        }
      )

    }else{
      //update an existing clinic
      this.spinner.show()
      await this.http.post<ICashier>(API_URL+'/cashiers/save', cashier, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id         = data?.id
          this.code       = data!.code
          this.firstName  = data!.firstName
          this.middleName = data!.middleName
          this.lastName   = data!.lastName
          this.type       = data!.type
          this.active     = data!.active
          this.msgBox.showSuccessMessage('Cashier updated successifully')
          this.loadCashiers()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, '')
        }
      )
    }
  }

  async loadCashiers(){
    this.cashiers = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<ICashier[]>(API_URL+'/cashiers/get_all_active', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.cashiers.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load cashiers')
      }
    )
  }

  clear(){
    this.id         = null
    this.code       = ''
    this.firstName  = ''
    this.middleName = ''
    this.lastName   = ''
    this.type       = ''
    this.active     = false
  }

  async getCashier(key: string) {
    if(key == ''){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<ICashier>(API_URL+'/cashiers/get?id='+key, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        console.log(data)
        this.id         = data?.id
        this.code       = data!.code
        this.firstName  = data!.firstName
        this.middleName = data!.middleName
        this.lastName   = data!.lastName
        this.type       = data!.type
        this.active     = data!.active

        
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, 'Could not find Doctor')
      }
    )
  }

  showCashierClinics(clinics : IClinic[], cashierClinics : IClinic[]){
    /**
     * Display user roles, the roles for that particular user are checked
     * args: roles-global user roles, userRoles-roles for a specific user
     */
    /** First uncheck all roles */
    this.clearClinics()
    /** Now, check the respective  roles */
    cashierClinics.forEach(cashierClinic => {
      clinics.forEach(clinic => {        
        if(clinic.name === cashierClinic.name){
          clinic.assigned = true
        }
      })
    })
    this.clinics = clinics
  }

 

  showUserRoles(clinics : IClinic[], cashierClinics : IClinic[]){
    /**
     * Display user roles, the roles for that particular user are checked
     * args: roles-global user roles, userRoles-roles for a specific user
     */
    /** First uncheck all roles */
    this.clearClinics()
    /** Now, check the respective  roles */
    cashierClinics.forEach(cashierClinic => {
      clinics.forEach(clinic => {        
        if(clinic.name === cashierClinic.name){
          clinic.assigned = true
        }
      })
    })
    this.clinics = clinics
  }

  clearClinics(){
    /**Uncheck all roles */
    this.clinics.forEach(clinic => {
      clinic.assigned = false
    })
  }

  async loadClinics(){  
    /**Get all roles */
     let options = {
       headers : new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
     }
     this.spinner.show()
     await this.http.get<IClinic[]>(API_URL+'/clinics', options)
     .pipe(finalize(() => this.spinner.hide()))
     .toPromise()
     .then(
       data => {
         data?.forEach(
           element => {
             console.log(element)
             this.clinics.push(element)
           }
         )
       }
     )
     .catch(error => {
       console.log(error)
     })
   }

   clearUser(){
     this.userCode = ''
   }

   public async assignUserProfile(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    if(this.id != null){
      this.spinner.show()
      await this.http.post<ICashier>(API_URL+'/cashiers/assign_user_profile?id='+this.id+'&code='+this.userCode, null, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id         = data?.id
          this.code       = data!.code
          this.firstName  = data!.firstName
          this.middleName = data!.middleName
          this.lastName   = data!.lastName
          this.type       = data!.type
          this.active     = data!.active
          this.msgBox.showSuccessMessage('Saved successifully')
          this.loadCashiers()
          this.clear()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, '')
        }
      )

    }
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
