import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterLink } from '@angular/router';
import { NgxSpinnerModule, NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { AuthService } from 'src/app/auth.service';
import { IClinic } from 'src/app/domain/clinic';
import { IClinician } from 'src/app/domain/clinician';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-clinician',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './clinician.component.html',
  styleUrls: ['./clinician.component.scss']
})
export class ClinicianComponent implements OnInit {


  id : any
  code : string = ''
  firstName : string = ''
  middleName : string = ''
  lastName : string = ''
  type : string = ''
  active : boolean = true

  public clinics           : IClinic[]


  clinicians : IClinician[] = []

  clinicianClinics : IClinic[] = []

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
    this.loadActiveClinicians()
    this.loadClinics()
  }

  public async saveClinician(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var clinicianClinics : IClinic[] = []
    this.clinics.forEach(clinic => { /**Get the roles */
      if(clinic.assigned == true){
        clinicianClinics.push(clinic)
      }
    })
    var clinician = {
      id          : this.id,
      code        : this.code,
      firstName   : this.firstName,
      middleName  : this.middleName,
      lastName    : this.lastName,
      type        : this.type,
      clinics     : clinicianClinics,
      active      : true
    }
    if(this.id == null || this.id == ''){
      //save a new clinic
      this.spinner.show()
      await this.http.post<IClinician>(API_URL+'/clinicians/save', clinician, options)
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
          //this.clinics    = data!.clinics
          this.active     = data!.active
          this.msgBox.showSuccessMessage('Clinician created successifully')
          this.loadActiveClinicians()
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
      await this.http.post<IClinician>(API_URL+'/clinicians/save', clinician, options)
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
          //this.clinics    = data!.clinics
          this.active     = data!.active
          this.msgBox.showSuccessMessage('Clinician updated successifully')
          this.loadActiveClinicians()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, '')
        }
      )
    }
  }

  async loadActiveClinicians(){
    this.clinicians = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IClinician[]>(API_URL+'/clinicians/get_all_active', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.clinicians.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load clinicians')
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

  async getClinician(key: string) {
    if(key == ''){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IClinician>(API_URL+'/clinicians/get?id='+key, options)
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

        this.showUserRoles(this.clinics, data!['clinics'])
        
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, 'Could not find Doctor')
      }
    )
  }

  showClinicianClinics(clinics : IClinic[], clinicianClinics : IClinic[]){
    /**
     * Display user roles, the roles for that particular user are checked
     * args: roles-global user roles, userRoles-roles for a specific user
     */
    /** First uncheck all roles */
    this.clearClinics()
    /** Now, check the respective  roles */
    clinicianClinics.forEach(clinicianClinic => {
      clinics.forEach(clinic => {        
        if(clinic.name === clinicianClinic.name){
          clinic.assigned = true
        }
      })
    })
    this.clinics = clinics
  }

 

  showUserRoles(clinics : IClinic[], clinicianClinics : IClinic[]){
    /**
     * Display user roles, the roles for that particular user are checked
     * args: roles-global user roles, userRoles-roles for a specific user
     */
    /** First uncheck all roles */
    this.clearClinics()
    /** Now, check the respective  roles */
    clinicianClinics.forEach(clinicianClinic => {
      clinics.forEach(clinic => {        
        if(clinic.name === clinicianClinic.name){
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
      await this.http.post<IClinician>(API_URL+'/clinicians/assign_user_profile?id='+this.id+'&code='+this.userCode, null, options)
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
          this.clinics    = data!.clinics
          this.active     = data!.active
          this.msgBox.showSuccessMessage('Saved successifully')
          this.loadActiveClinicians()
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
