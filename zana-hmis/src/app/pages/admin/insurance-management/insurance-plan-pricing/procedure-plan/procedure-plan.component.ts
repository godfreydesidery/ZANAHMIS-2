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
import { IInsurancePlan } from 'src/app/domain/insurance-plan';
import { IProcedureTypeInsurancePlan } from 'src/app/domain/procedure-type-insurance-plan';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-procedure-plan',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './procedure-plan.component.html',
  styleUrls: ['./procedure-plan.component.scss']
})
export class ProcedurePlanComponent implements OnInit {
  id              : any = null

  insurancePlan!  : IInsurancePlan
  price           : number = 0

  insuranceProviderName : string = ''
  insurancePlanName     : string = ''

  insuranceProviderNames : string[] = []
  insurancePlanNames      : string[] = []

 procedureTypeName  : string = ''
 procedureTypeNames : string[] = []

 procedureTypeInsurancePlans : IProcedureTypeInsurancePlan[] = []

 insurancePlans : IInsurancePlan[] = []

 filterRecords : string = ''
 
  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
  ) { }

  ngOnInit(): void {
    //this.loadProcedureTypeInsurancePlans()
    //this.loadInsuranceProviderNames()
    //this.loadProcedureTypeNames()
    this.loadInsurancePlans()
  }

  async loadInsuranceProviderNames(){
    this.insuranceProviderNames = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<string[]>(API_URL+'/insurance_providers/get_names', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.insuranceProviderNames.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load Insurance Providers')
      }
    )
  }

  async loadInsurancePlans(){
    this.insurancePlans = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IInsurancePlan[]>(API_URL+'/insurance_plans', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.insurancePlans = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async loadInsurancePlanNamesByProviders(providerName : string){
    this.insurancePlanNames = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<string[]>(API_URL+'/insurance_plans/get_names_by_insurance_provider?provider_name='+providerName, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.insurancePlanNames.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load Insurance Plans')
      }
    )
  }

  async loadProcedureTypeInsurancePlans(){
    this.procedureTypeInsurancePlans = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IProcedureTypeInsurancePlan[]>(API_URL+'/procedure_type_insurance_plans', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.procedureTypeInsurancePlans.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load Procedure Type Plans')
      }
    )
  }


  public async saveProcedureTypeInsurancePlan(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var procedureTypeInsurancePlan = {
      id          : this.id,
     procedureType : {
        name : this.procedureTypeName
      },
      insurancePlan          : {
        name : this.insurancePlanName
      },
      price        : this.price
    }
    if(this.id == null || this.id == ''){
      //save a new diagnosisType
      this.spinner.show()
      await this.http.post<IProcedureTypeInsurancePlan>(API_URL+'/procedure_type_insurance_plans/save',procedureTypeInsurancePlan, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id           = data?.id
          this.msgBox.showSuccessMessage('Procedure Type Plan created successifully')
          this.loadProcedureTypeInsurancePlans()
          
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not create Procedure Type Plan')
        }
      )

    }else{
      //update an existingprocedureType
      this.spinner.show()
      await this.http.post<IProcedureTypeInsurancePlan>(API_URL+'/procedure_type_insurance_plans/save',procedureTypeInsurancePlan, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id           = data?.id
          
          this.msgBox.showSuccessMessage('Procedure Type Plan updated successifully')
          this.loadProcedureTypeInsurancePlans()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not update Procedure Type Plan')
        }
      )
    }
    this.clear()
  }

  clear(){
    this.id                     = null
    this.procedureTypeName      = ''
    this.insuranceProviderName  = ''
    this.insurancePlanName      = ''
    this.price                  = 0
  }

  async getProcedureTypeInsurancePlan(key: string) {
    if(key == ''){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IProcedureTypeInsurancePlan>(API_URL+'/procedure_type_insurance_plans/get?id='+key, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        console.log(data)
        this.id                     = data?.id
        this.procedureTypeName      = data!.procedureType.name
        this.insuranceProviderName  = data!.insurancePlan.insuranceProvider.name
        this.insurancePlanName      = data!.insurancePlan.name
        this.price                  = data!.price
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, 'Could not find Procedure Type Plan')
      }
    )
  }

  async deleteProcedureTypeInsurancePlan(key: string) {
    if(key == ''){
      return
    }
    if(!window.confirm('Delete this plan? Plan ID: '+key)){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.post<IProcedureTypeInsurancePlan>(API_URL+'/procedure_type_insurance_plans/delete?id='+key, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        console.log(data)
        this.loadProcedureTypeInsurancePlans()
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, 'Could not delete Procedure Type Plan')
      }
    )
  }

  async loadProcedureTypeNames(){
    this.procedureTypeNames = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<string[]>(API_URL+'/procedure_types/get_names', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.procedureTypeNames.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load Procedure Types')
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

  setInsurancePlanId(id : any, name : string){
    localStorage.setItem('insurance_plan_id', id)
    localStorage.setItem('insurance_plan_name', name)
  }

}
