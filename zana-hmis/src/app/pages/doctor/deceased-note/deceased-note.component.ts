import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { AuthService } from 'src/app/auth.service';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { ShowDateTimePipe } from 'src/app/pipes/date_time.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { ShowTimePipe } from 'src/app/pipes/show_time.pipe';
import { ShowUserPipe } from 'src/app/pipes/show_user.pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';

import { Time } from '@angular/common';
import { HttpHeaders } from '@angular/common/http';
import { OnInit } from '@angular/core';
import { finalize } from 'rxjs';
import { IClinicalNote } from 'src/app/domain/clinical-note';
import { IAdmission } from 'src/app/domain/admission';
import { IDiagnosisType } from 'src/app/domain/diagnosis-type';
import { IFinalDiagnosis } from 'src/app/domain/final-diagnosis';
import { IGeneralExamination } from 'src/app/domain/general-examination';
import { ILabTest } from 'src/app/domain/lab-test';
import { ILabTestType } from 'src/app/domain/lab-test-type';
import { IMedicine } from 'src/app/domain/medicine';
import { IPatient } from 'src/app/domain/patient';
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
import { environment } from 'src/environments/environment';
import { IDischargePlan } from 'src/app/domain/discharge-plan';
import { IConsultation } from 'src/app/domain/consultation';
import { IDeceasedNote } from 'src/app/domain/deceased-note';



const API_URL = environment.apiUrl;


@Component({
  selector: 'app-deceased-note',
  templateUrl: './deceased-note.component.html',
  styleUrls: ['./deceased-note.component.scss'],
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    AgePipe,
    ShowTimePipe,
    ShowUserPipe,
    ShowDateTimePipe,
    RouterLink
  ],
})
export class DeceasedNoteComponent {

  id : any = null

  patientSummary : string = ''
  causeOfDeath : string = ''

  time!    : Time
  date!    : Date



  admissionId : any = null
  consultationId : any = null

  admission! : IAdmission
  consultation! : IConsultation

  deceasedNote! : IDeceasedNote


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
      await this.loadAdmission(this.admissionId) 
    }else if(this.consultationId != null){
      await this.loadConsultation(this.consultationId) 
    }
    await this.loadDeceasedNote(this.admission.id, this.consultation.id)
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

  async loadDeceasedNote(admissionId : any, consultationId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IDeceasedNote>(API_URL+'/patients/load_deceased_note?admission_id='+admissionId+'&consultation_id='+consultationId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.deceasedNote = data!
        this.id = data!.id
        this.patientSummary = data!.patientSummary
        this.causeOfDeath = data!.causeOfDeath
        this.date = data!.date
        this.time = data!.time
        console.log(data)
      }
    )
    .catch(
      error => {
        //this.msgBox.showErrorMessage('Could not load admission')
        console.log(error)
      }
    )
  }

  async saveDeceasedNote(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    if(this.patientSummary === '' || this.causeOfDeath === '' || this.date === null){
      this.msgBox.showErrorMessage3('The fields marked with * are required fields')
      return
    }

    if(this.id === null){
      if(!window.confirm('Confirm submitting discharge plan. Confirm?')){
        this.id = null
        this.patientSummary = ''
        this.causeOfDeath = ''
        this.date!
        this.time!
        return
      }
    }
    
    var note = {
      patientSummary: this.patientSummary,
      causeOfDeath: this.causeOfDeath,
      date: this.date,
      time: this.time,
      admission: {id : this.admissionId},
      consultation: {id : this.consultationId}
    }
    this.spinner.show()
    await this.http.post<IDeceasedNote>(API_URL+'/patients/save_deceased_note', note, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {
        this.deceasedNote = data!
        this.id = data!.id
        this.patientSummary = data!.patientSummary
        this.causeOfDeath = data!.causeOfDeath
        this.date = data!.date
        this.time = data!.time

        this.msgBox.showSuccessMessage('Saved successifully')
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not save')
        console.log(error)
      }
    )
  }
}
