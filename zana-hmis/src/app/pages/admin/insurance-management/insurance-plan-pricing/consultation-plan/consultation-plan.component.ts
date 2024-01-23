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
import { IConsultationInsurancePlan } from 'src/app/domain/consultation-insurance-plan';
import { IInsurancePlan } from 'src/app/domain/insurance-plan';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;
@Component({
  selector: 'app-consultation-plan',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './consultation-plan.component.html',
  styleUrls: ['./consultation-plan.component.scss']
})
export class ConsultationPlanComponent implements OnInit {

  id : any = null
  consultationFee : number = 0
  insurancePlan! : IInsurancePlan
  

  insuranceProviderName : string = ''
  insurancePlanName : string = ''

  insuranceProviderNames : string[] = []
  insurancePlanNames : string[] = []

  clinicName : string = ''
  clinicNames : string[] = []

  consultationPlans : IConsultationInsurancePlan[] = []

  insurancePlans : IInsurancePlan[] = []

  filterRecords : string = ''

  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
  ) { }

  ngOnInit(): void {
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
        this.msgBox.showErrorMessage(error, 'Could not load plans')
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

  async loadConsultationPlans(){
    this.consultationPlans = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IConsultationInsurancePlan[]>(API_URL+'/consultation_insurance_plans', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.consultationPlans.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load Consultation Plans')
      }
    )
  }


  public async saveConsultationPlan(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var consultationPlan = {
      id: this.id,
      clinic: {
        name: this.clinicName
      },
      insurancePlan: {
        name: this.insurancePlanName
      },
      consultationFee: this.consultationFee,
    }
    if(this.id == null || this.id == ''){
      //save a new diagnosisType
      this.spinner.show()
      await this.http.post<IConsultationInsurancePlan>(API_URL+'/consultation_insurance_plans/save', consultationPlan, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id           = data?.id
          this.msgBox.showSuccessMessage('Consultation plan created successifully')
          this.loadConsultationPlans()
          
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not create Consultation Plan')
        }
      )

    }else{
      //update an existing plan
      this.spinner.show()
      await this.http.post<IConsultationInsurancePlan>(API_URL+'/consultation_insurance_plans/save', consultationPlan, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id           = data?.id
          
          this.msgBox.showSuccessMessage('Consultation Plan updated successifully')
          this.loadConsultationPlans()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not update Consultation Plan')
        }
      )
    }
    this.clear()
  }

  clear(){
    this.id = null
    this.clinicName = ''
    this.insuranceProviderName = ''
    this.insurancePlanName = ''
    this.consultationFee = 0
  }

  async getConsultationPlan(key: string) {
    if(key == ''){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IConsultationInsurancePlan>(API_URL+'/consultation_insurance_plans/get?id='+key, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        console.log(data)
        this.id                    = data?.id
        this.clinicName            = data!.clinic.name
        this.insuranceProviderName = data!.insurancePlan.insuranceProvider.name
        this.insurancePlanName     = data!.insurancePlan.name
        this.consultationFee       = data!.consultationFee
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, 'Could not find Consultation Plan')
      }
    )
  }

  async deleteConsultationPlan(key: string) {
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
    await this.http.post<IConsultationInsurancePlan>(API_URL+'/consultation_insurance_plans/delete?id='+key, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        console.log(data)
        this.loadConsultationPlans()
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, 'Could not delete Consultation Plan')
      }
    )
  }

  async loadClinicNames(){
    this.clinicNames = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<string[]>(API_URL+'/clinics/get_names', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.clinicNames.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load clinics')
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
