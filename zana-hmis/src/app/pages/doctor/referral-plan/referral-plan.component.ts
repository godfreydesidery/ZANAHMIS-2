import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IAdmission } from 'src/app/domain/admission';
import { IConsultation } from 'src/app/domain/consultation';
import { IExternalMedicalProvider } from 'src/app/domain/external-medical-provider';
import { IReferralPlan } from 'src/app/domain/referral-plan';
import { IWorkingDiagnosis } from 'src/app/domain/working-diagnosis';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { ShowDateTimePipe } from 'src/app/pipes/date_time.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';



const API_URL = environment.apiUrl;

@Component({
  selector: 'app-referral-plan',
  templateUrl: './referral-plan.component.html',
  styleUrls: ['./referral-plan.component.scss'],
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    AgePipe,
    ShowDateTimePipe,
    RouterLink
  ],
})
export class ReferralPlanComponent {

  id : any = null

  referringDiagnosis : string = ''
  history : string = ''
  investigation : string = ''
  management : string = ''
  operationNote : string = ''
  icuAdmissionNote : string = ''
  generalRecommendation : string = ''


  admissionId : any = null
  consultationId : any = null

  externalMedicalProvider! : IExternalMedicalProvider

  externalMedicalProviderId : any = null

  admission! : IAdmission
  consultation! : IConsultation

  referralPlan! : IReferralPlan

  

  externalMedicalProviderName : string = ''


  constructor(private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
    ) { }

  async ngOnInit(): Promise<void> {
    this.admissionId = localStorage.getItem('admission-id')
    this.consultationId = localStorage.getItem('consultation-id')
    localStorage.removeItem('admission-id')
    localStorage.removeItem('consultation-id')
    if(this.admissionId != null){
      this.consultationId = null
      await this.loadAdmission(this.admissionId) 
      await this.loadReferralPlan(this.admissionId, 0)
    }else{
      this.admissionId = null
      await this.loadConsultation(this.consultationId) 
      await this.loadReferralPlan(0, this.consultationId)
    }  
  }

  async getProvider(id : any){
    this.externalMedicalProviderName = ''
    this.externalMedicalProviderId = null
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IExternalMedicalProvider>(API_URL+'/external_medical_providers/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.externalMedicalProvider = data!
        this.externalMedicalProviderId = data!.id
        this.externalMedicalProviderName = data!.name
        console.log(data)
        this.externalMedicalProviders = []
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }

  async loadAdmission(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IAdmission>(API_URL+'/patients/load_admission?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.admission = data!
        console.log(data)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load admission')
        console.log(error)
      }
    )
  }

  async loadConsultation(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IConsultation>(API_URL+'/patients/load_consultation?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.consultation = data!
        console.log(data)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load consultation')
        console.log(error)
      }
    )
  }

  async loadReferralPlan(admissionId : any, consultationId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IReferralPlan>(API_URL+'/patients/load_referral_plan?admission_id='+admissionId+'&consultation_id='+consultationId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.referralPlan = data!
        this.id = data!.id
        this.referringDiagnosis = data!.referringDiagnosis
        this.history = data!.history
        this.investigation = data!.investigation
        this.management = data!.management
        this.operationNote = data!.operationNote
        this.icuAdmissionNote = data!.icuAdmissionNote
        this.generalRecommendation = data!.generalRecommendation

        this.externalMedicalProvider = data!.externalMedicalProvider
        this.externalMedicalProviderId = data!.externalMedicalProvider.id
        this.externalMedicalProviderName = data!.externalMedicalProvider.name
        console.log(data)
      }
    )
    .catch(
      error => {
        //this.msgBox.showErrorMessage(error['error'])
        console.log(error)
      }
    )
  }

  async saveReferralPlan(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    if(this.history === '' || this.investigation === '' || this.management === ''){
      this.msgBox.showErrorMessage3('The fields marked with * are required fields')
      return
    }

    if(this.id === null){
      if(!window.confirm('Confirm submitting discharge plan. Confirm?')){
        this.id = null
        this.referringDiagnosis = ''
        this.history = ''
        this.investigation = ''
        this.management = ''
        this.operationNote = ''
        this.icuAdmissionNote = ''
        this.generalRecommendation = ''
        
        return
      }
    }
    
    var plan = {
      referringDiagnosis :  this.referringDiagnosis,
      history: this.history,
      investigation: this.investigation,
      management: this.management,
      operationNote: this.operationNote,
      icuAdmissionNote: this.icuAdmissionNote,
      generalRecommendation: this.generalRecommendation,
      admission: {id : this.admissionId},
      consultation: {id : this.consultationId},
      externalMedicalProvider : {id : this.externalMedicalProviderId}
    }
    this.spinner.show()
    await this.http.post<IReferralPlan>(API_URL+'/patients/save_referral_plan', plan, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {
        this.referralPlan = data!
        this.id = data!.id
        this.referringDiagnosis = data!.referringDiagnosis
        this.history = data!.history
        this.investigation = data!.investigation
        this.management = data!.management
        this.operationNote = data!.operationNote
        this.icuAdmissionNote = data!.icuAdmissionNote
        this.generalRecommendation = data!.generalRecommendation
        this.externalMedicalProvider = data!.externalMedicalProvider

        console.log(data)

        this.msgBox.showSuccessMessage('Saved successifully')
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }

  externalMedicalProviders : IExternalMedicalProvider[] = []
  async loadProvidersLike(value : string){
    this.externalMedicalProviders = []
    if(value.length < 3){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.get<IExternalMedicalProvider[]>(API_URL+'/external_medical_providers/load_external_medical_providers_like?name_like='+value, options)
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.externalMedicalProviders = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }
}
