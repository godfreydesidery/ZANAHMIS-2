import { CommonModule, Time } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { PdfViewerModule } from 'ng2-pdf-viewer';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IAdmission } from 'src/app/domain/admission';
import { IClinic } from 'src/app/domain/clinic';
import { IClinicalNote } from 'src/app/domain/clinical-note';
import { IConsultation } from 'src/app/domain/consultation';
import { IConsultationTransfer } from 'src/app/domain/consultation-transfer';
import { IDiagnosisType } from 'src/app/domain/diagnosis-type';
import { IFinalDiagnosis } from 'src/app/domain/final-diagnosis';
import { IGeneralExamination } from 'src/app/domain/general-examination';
import { ILabTest } from 'src/app/domain/lab-test';
import { ILabTestType } from 'src/app/domain/lab-test-type';
import { IMedicine } from 'src/app/domain/medicine';
import { IPatient } from 'src/app/domain/patient';
import { IPatientVital } from 'src/app/domain/patient-vital';
import { IPrescription } from 'src/app/domain/prescription';
import { IProcedure } from 'src/app/domain/procedure';
import { IProcedureType } from 'src/app/domain/procedure-type';
import { IRadiology } from 'src/app/domain/radiology';
import { IRadiologyType } from 'src/app/domain/radiology-type';
import { IWard } from 'src/app/domain/ward';
import { IWardBed } from 'src/app/domain/ward-bed';
import { IWardCategory } from 'src/app/domain/ward-category';
import { IWardType } from 'src/app/domain/ward-type';
import { IWorkingDiagnosis } from 'src/app/domain/working-diagnosis';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { ShowTimePipe } from 'src/app/pipes/show_time.pipe';
import { ShowUserPipe } from 'src/app/pipes/show_user.pipe';
import { DownloadFileService } from 'src/app/services/download-file.service';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

import swal from 'sweetalert2';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-nurse-outpatient-chart',
  templateUrl: './nurse-outpatient-chart.component.html',
  styleUrls: ['./nurse-outpatient-chart.component.scss'],
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    AgePipe,
    ShowUserPipe,
    ShowTimePipe,
    RouterLink,
    PdfViewerModule
  ], 
})
export class NurseOutpatientChartComponent {

  id : any

  consultation! : IConsultation

  /**
   *  General Examination, suffix gE
   */
  vitalId : any
  vitalPressure : string = ''
  vitalTemperature : string = ''
  vitalPulseRate : string = ''
  vitalWeight : string = ''
  vitalHeight : string = ''
  vitalBodyMassIndex : string = ''
  vitalBodyMassIndexComment : string = ''
  vitalBodySurfaceArea : string = ''
  vitalSaturationOxygen : string = ''
  vitalRespiratoryRate : string = ''
  vitalDescription : string = ''
  vitalStatus : string = ''
  
  filterRecords : string = '' // this is composite

  constructor(
    private router : Router,
    private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService,
    private downloadService : DownloadFileService,
    ) { }

  async ngOnInit(): Promise<void> {
    this.id = localStorage.getItem('consultation-id')
    localStorage.removeItem('consultation-id')
    await this.refresh()    
  }

  async setGlobalPatientId(){
    localStorage.setItem('patient-id', this.consultation.patient.id)
    localStorage.setItem('consultation-id', this.id)
    localStorage.removeItem('non-consultation-id')
    localStorage.removeItem('admission-id')
  }

  async refresh(){
    await this.loadConsultation(this.id)
    await this.loadGeneralExaminationByConsultationId(this.id)
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
        this.id = data?.id
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

  async loadGeneralExaminationByConsultationId(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IPatientVital>(API_URL+'/patients/load_patient_vitals_by_consultation_id?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {

        this.vitalId = data?.id
        this.vitalPressure = data!.pressure
        this.vitalTemperature = data!.temperature
        this.vitalPulseRate = data!.pulseRate
        this.vitalWeight = data!.weight
        this.vitalHeight = data!.height
        this.vitalBodyMassIndex = data!.bodyMassIndex
        this.vitalBodyMassIndexComment = data!.bodyMassIndexComment
        this.vitalBodySurfaceArea = data!.bodySurfaceArea
        this.vitalSaturationOxygen = data!.saturationOxygen
        this.vitalRespiratoryRate = data!.respiratoryRate
        this.vitalDescription = data!.description
        this.vitalStatus = data!.status
        
        console.log(data)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load general examination')
        console.log(error)
      }
    )
  }

  async saveVitals(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var vital = {
      id : this.vitalId,
      pressure : this.vitalPressure,
      temperature : this.vitalTemperature,
      weight : this.vitalWeight,
      pulseRate : this.vitalPulseRate,
      height : this.vitalHeight,
      bodyMassIndex : this.vitalBodyMassIndex,
      bodyMassIndexComment : this.vitalBodyMassIndexComment,
      bodySurfaceArea : this.vitalBodySurfaceArea,
      saturationOxygen : this.vitalSaturationOxygen,
      respiratoryRate : this.vitalRespiratoryRate,
      description : this.vitalDescription,
      consultation : { id : this.id},
      admission : { id : 0},
      nonConsultation : {id : 0}
    }
    
    
   
    this.spinner.show()
    await this.http.post<IPatientVital>(API_URL+'/patients/save_patient_vitals', vital, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {

        this.vitalId = data?.id
        this.vitalPressure = data!.pressure
        this.vitalTemperature = data!.temperature
        this.vitalWeight = data!.weight
        this.vitalPulseRate = data!.pulseRate
        this.vitalHeight = data!.height
        this.vitalBodyMassIndex = data!.bodyMassIndex
        this.vitalBodyMassIndexComment = data!.bodyMassIndexComment
        this.vitalBodySurfaceArea = data!.bodySurfaceArea
        this.vitalSaturationOxygen = data!.saturationOxygen
        this.vitalRespiratoryRate = data!.respiratoryRate
        this.vitalDescription = data!.description
        this.vitalStatus = data!.status
        
        this.msgBox.showSuccessMessage('Saved successifully')
      
        console.log(data)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not save')
        console.log(error)
      }
    )
  }

  async submitVitals(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var vital = {
      id : this.vitalId,
      pressure : this.vitalPressure,
      temperature : this.vitalTemperature,
      weight : this.vitalWeight,
      pulseRate : this.vitalPulseRate,
      height : this.vitalHeight,
      bodyMassIndex : this.vitalBodyMassIndex,
      bodyMassIndexComment : this.vitalBodyMassIndexComment,
      bodySurfaceArea : this.vitalBodySurfaceArea,
      saturationOxygen : this.vitalSaturationOxygen,
      respiratoryRate : this.vitalRespiratoryRate,
      description : this.vitalDescription,
      consultation : { id : this.id},
      admission : { id : 0},
      nonConsultation : {id : 0}
    }



    var toSubmit = false

    await swal.fire({
      title : 'Are you sure you want to submit patient vitals to Doctor?',
      text : 'Patient vitals will be submitted to doctor. This can not be undone',
      icon : 'question',
      width : '500',
      showCancelButton : true,
      confirmButtonText : 'Yes, Submit',
      confirmButtonColor : '#009688',
      cancelButtonText : 'No, Do not submit',
      cancelButtonColor : '#e5343d'
      
    }).then(async (result) => {
      if(result.value) {
        toSubmit = true
      }  
    })

    if(toSubmit === false){
      return
    }


    
    this.spinner.show()
    await this.http.post<IPatientVital>(API_URL+'/patients/submit_patient_vitals', vital, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {

        this.vitalId = data?.id
        this.vitalPressure = data!.pressure
        this.vitalTemperature = data!.temperature
        this.vitalWeight = data!.weight
        this.vitalPulseRate = data!.pulseRate
        this.vitalHeight = data!.height
        this.vitalBodyMassIndex = data!.bodyMassIndex
        this.vitalBodyMassIndexComment = data!.bodyMassIndexComment
        this.vitalBodySurfaceArea = data!.bodySurfaceArea
        this.vitalSaturationOxygen = data!.saturationOxygen
        this.vitalRespiratoryRate = data!.respiratoryRate
        this.vitalDescription = data!.description
        this.vitalStatus = data!.status
        
        this.msgBox.showSuccessMessage('Submited successifully')
      
        console.log(data)
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

export interface ICG{
  clinicalNote : IClinicalNote
  generalExamination : IGeneralExamination
}

export interface IAdm {
  patient : IPatient
  wardBed : IWardBed
}
