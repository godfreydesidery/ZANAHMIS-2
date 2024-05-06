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
import { IManagement } from 'src/app/domain/management';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-management',
  templateUrl: './management.component.html',
  styleUrls: ['./management.component.scss'],
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
})
export class ManagementComponent {


  id : any
  code : string = ''
  firstName : string = ''
  middleName : string = ''
  lastName : string = ''
  type : string = ''
  active : boolean = true

  public clinics           : IClinic[]


  managements : IManagement[] = []

  managementClinics : IClinic[] = []

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
    this.loadActiveManagements()
    this.loadClinics()
  }

  public async saveManagement(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var managementClinics : IClinic[] = []
    this.clinics.forEach(clinic => { /**Get the roles */
      if(clinic.assigned == true){
        managementClinics.push(clinic)
      }
    })
    var management = {
      id          : this.id,
      code        : this.code,
      firstName   : this.firstName,
      middleName  : this.middleName,
      lastName    : this.lastName,
      type        : this.type,
      clinics     : managementClinics,
      active      : true
    }
    if(this.id == null || this.id == ''){
      //save a new clinic
      this.spinner.show()
      await this.http.post<IManagement>(API_URL+'/managements/save', management, options)
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
          this.msgBox.showSuccessMessage('Management created successifully')
          this.loadActiveManagements()
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
      await this.http.post<IManagement>(API_URL+'/managements/save', management, options)
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
          this.msgBox.showSuccessMessage('Management updated successifully')
          this.loadActiveManagements()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, '')
        }
      )
    }
  }

  async loadActiveManagements(){
    this.managements = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IManagement[]>(API_URL+'/managements/get_all_active', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.managements.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load active managements')
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

  async getManagement(key: string) {
    if(key == ''){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IManagement>(API_URL+'/managements/get?id='+key, options)
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

  
 

  showUserRoles(clinics : IClinic[], managementClinics : IClinic[]){
    /**
     * Display user roles, the roles for that particular user are checked
     * args: roles-global user roles, userRoles-roles for a specific user
     */
    /** First uncheck all roles */
    this.clearClinics()
    /** Now, check the respective  roles */
    managementClinics.forEach(managementClinic => {
      clinics.forEach(clinic => {        
        if(clinic.name === managementClinic.name){
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
      await this.http.post<IManagement>(API_URL+'/managements/assign_user_profile?id='+this.id+'&code='+this.userCode, null, options)
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
          this.loadActiveManagements()
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
