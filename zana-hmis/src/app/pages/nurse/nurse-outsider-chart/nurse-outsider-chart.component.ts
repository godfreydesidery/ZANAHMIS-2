import { CommonModule, Time } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { PdfViewerModule } from 'ng2-pdf-viewer';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IAdmission } from 'src/app/domain/admission';
import { IClinic } from 'src/app/domain/clinic';
import { IClinicalNote } from 'src/app/domain/clinical-note';
import { IClinician } from 'src/app/domain/clinician';
import { IConsultation } from 'src/app/domain/consultation';
import { IConsultationTransfer } from 'src/app/domain/consultation-transfer';
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
import { ShowDateTimePipe } from 'src/app/pipes/date_time.pipe';
import { DownloadFileService } from 'src/app/services/download-file.service';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { ShowTimePipe } from 'src/app/pipes/show_time.pipe';
import { ShowUserPipe } from 'src/app/pipes/show_user.pipe';
import { INonConsultation } from 'src/app/domain/non-consultation';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-nurse-outsider-chart',
  templateUrl: './nurse-outsider-chart.component.html',
  styleUrls: ['./nurse-outsider-chart.component.scss'],
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
export class NurseOutsiderChartComponent {

  id : any

  nonConsultation! : INonConsultation

  /**
   *  General Examination, suffix gE
   */
  gEId : any
  gEPressure : string = ''
  gETemperature : string = ''
  gEPulseRate : string = ''
  gEWeight : string = ''
  gEHeight : string = ''
  gEBodyMassIndex : string = ''
  gEBodyMassIndexComment : string = ''
  gEBodySurfaceArea : string = ''
  gESaturationOxygen : string = ''
  gERespiratoryRate : string = ''
  gEDescription : string = ''
   /**
   *  Clinical Note, suffix cN
   */
  cNid : any
  cNMainComplain : string = ''
  cNPresentIllnessHistory : string = ''
  cNPastMedicalHistory : string = ''
  cNFamilyAndSocialHistory : string = ''
  cNDrugsAndAllergyHistory : string = ''
  cNReviewOfOtherSystems : string = ''
  cNPhysicalExamination : string = ''
  cNManagementPlan : string = ''
  /**
   * Diagnosis type
   */

  diagnosisDescription : string = ''

  /**
   * Working Diagnosis, prefix wD
   */
  wDId : any
  

  /**
   * Final Diagnosis, prefix fD
   */
    fDId : any
    

  //diagnosisTypeNames : string[] = []
  //labTestTypeNames : string[] = []
  //radiologyTypeNames : string[] = []
  //procedureTypeNames : string[] = []
  //medicineNames : string[] = []

  

  workingDiagnosises : IWorkingDiagnosis[] = []
  finalDiagnosises : IFinalDiagnosis[] = []

  
  
  procedures : IProcedure[] = []
  prescriptions : IPrescription[] = []

  labTests : ILabTest[] = []
  labTestId : any
  labTestDiagnosis : string = ''
  labTestDescription : string = ''
  labTestReport : string = ''

  radiologies : IRadiology[] = []
  radiologyId : any
  radiologyDiagnosis : string = ''
  radiologyDescription : string = ''
  radiologyReport : string = ''



  

  prescriptionUnit        : number = 0
  prescriptionDosage      : string = ''
  prescriptionFrequency   : string = ''
  prescriptionRoute       : string = ''
  prescriptionDays        : string = ''
  prescriptionPrice       : number = 0
  prescriptionQty         : number = 0

  procedureId : any = null
  procedureNote : string = ''
  procedureType : string = ''
  procedureNeedTheatre : boolean = false
  procedureTheatreName : string = ''
  procedureTime! : Time
  procedureDiagnosis : string = ''
  procedureDate! : Date
  procedureHours : number = 0
  procedureMinutes : number = 0



  procedure! : IProcedure

  theatreName : string = ''
  theatreNames : string[] = []

  wardCategories : IWardCategory[] = []
  wardTypes      : IWardType[] = []
  wards          : IWard[] = []
  wardBeds       : IWardBed[] = []

  wardCategoryName : string = ''
  wardTypeName : string = ''
  wardName : string = ''
  wardBedNo : string = ''
  wardBedId : any = null


  filterRecords : string = '' // this is composite

  //clinics : IClinic[] = []

  constructor(
    private router : Router,
    private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService,
    private downloadService : DownloadFileService,
    ) { }

  async ngOnInit(): Promise<void> {
    this.id = localStorage.getItem('non-consultation-id')
    localStorage.removeItem('non-consultation-id')
    await this.refresh()    
  }

  async setGlobalPatientId(){
    localStorage.setItem('patient-id', this.nonConsultation.patient.id)
    localStorage.setItem('non-consultation-id', this.id)
    localStorage.removeItem('consultation-id')
    localStorage.removeItem('admission-id')
  }

  async refresh(){
    await this.loadNonConsultation(this.id)
    //await this.loadClinicalNoteByConsultationId(this.id)
    await this.loadGeneralExaminationByNonConsultationId(this.id)  
    //await this.loadTheatreNames()
    //await this.loadWorkingDiagnosis(this.id)
    //await this.loadFinalDiagnosis(this.id)
    //await this.loadLabTest(this.id, 0, 0)
    //await this.loadRadiologies(this.id, 0, 0)
    await this.loadProcedures(0, this.id, 0)
    //await this.loadPrescriptions(this.id, 0, 0)  
  }

  toggleTheatre(){
    if(this.procedureNeedTheatre === false){
      this.procedureNeedTheatre = true
      this.procedureType = 'THEATRE'
    }else{
      this.procedureNeedTheatre =false
      this.procedureTheatreName = ''
      this.procedureDate!
      this.procedureTime!
      this.procedureHours = 0
      this.procedureMinutes = 0
      this.procedureType = 'NON-THEATRE'
    }
  }

  async loadNonConsultation(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<INonConsultation>(API_URL+'/patients/load_non_consultation?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.id = data?.id
        this.nonConsultation = data!
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

  async loadGeneralExaminationByNonConsultationId(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IGeneralExamination>(API_URL+'/patients/load_general_examination_by_non_consultation_id?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {

        this.gEId = data?.id
        this.gEPressure = data!.pressure
        this.gETemperature = data!.temperature
        this.gEPulseRate = data!.pulseRate
        this.gEWeight = data!.weight
        this.gEHeight = data!.height
        this.gEBodyMassIndex = data!.bodyMassIndex
        this.gEBodyMassIndexComment = data!.bodyMassIndexComment
        this.gEBodySurfaceArea = data!.bodySurfaceArea
        this.gESaturationOxygen = data!.saturationOxygen
        this.gERespiratoryRate = data!.respiratoryRate
        this.gEDescription = data!.description
        
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

  async saveCG(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    var cg = {
      clinicalNote : {
        id : this.cNid,
        mainComplain : this.cNMainComplain,
        presentIllnessHistory : this.cNPresentIllnessHistory,
        pastMedicalHistory : this.cNPastMedicalHistory,
        familyAndSocialHistory : this.cNFamilyAndSocialHistory,
        drugsAndAllergyHistory : this.cNDrugsAndAllergyHistory,
        reviewOfOtherSystems : this.cNReviewOfOtherSystems,
        physicalExamination : this.cNPhysicalExamination,
        managementPlan : this.cNManagementPlan,
        consultation : { id : 0},
        nonConsultation : { id : this.id},
        admission : { id : 0}
      },
      generalExamination : {
        id : this.gEId,
        pressure : this.gEPressure,
        temperature : this.gETemperature,
        weight : this.gEWeight,
        pulseRate : this.gEPulseRate,
        height : this.gEHeight,
        bodyMassIndex : this.gEBodyMassIndex,
        bodyMassIndexComment : this.gEBodyMassIndexComment,
        bodySurfaceArea : this.gEBodySurfaceArea,
        saturationOxygen : this.gESaturationOxygen,
        respiratoryRate : this.gERespiratoryRate,
        description : this.gEDescription,
        consultation : { id : 0},
        nonConsultation : { id : this.id},
        admission : { id : 0}
      }
    } 
   
    this.spinner.show()
    await this.http.post<ICG>(API_URL+'/patients/save_clinical_note_and_general_examination', cg, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {

        this.cNid = data?.clinicalNote.id
        this.cNMainComplain = data!.clinicalNote.mainComplain
        this.cNPresentIllnessHistory = data!.clinicalNote.presentIllnessHistory
        this.cNPastMedicalHistory = data!.clinicalNote.pastMedicalHistory
        this.cNFamilyAndSocialHistory = data!.clinicalNote.familyAndSocialHistory
        this.cNDrugsAndAllergyHistory = data!.clinicalNote.drugsAndAllergyHistory
        this.cNReviewOfOtherSystems = data!.clinicalNote.reviewOfOtherSystems
        this.cNPhysicalExamination = data!.clinicalNote.physicalExamination
        this.cNManagementPlan = data!.clinicalNote.managementPlan

        this.gEId = data?.generalExamination.id
        this.gEPressure = data!.generalExamination.pressure
        this.gETemperature = data!.generalExamination.temperature
        this.gEWeight = data!.generalExamination.weight
        this.gEPulseRate = data!.generalExamination.pulseRate
        this.gEHeight = data!.generalExamination.height
        this.gEBodyMassIndex = data!.generalExamination.bodyMassIndex
        this.gEBodyMassIndexComment = data!.generalExamination.bodyMassIndexComment
        this.gEBodySurfaceArea = data!.generalExamination.bodySurfaceArea
        this.gESaturationOxygen = data!.generalExamination.saturationOxygen
        this.gERespiratoryRate = data!.generalExamination.respiratoryRate
        this.gEDescription = data!.generalExamination.description
        
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

  showProcedureNote(id : any, procedureTypeName : string, note : string){
    this.procedureId = id
    this.procedureTypeName = procedureTypeName
    this.procedureNote = note
  }

  async saveProcedureNote(){

    if(this.procedureNote === '' || this.procedureNote === null){
      this.msgBox.showErrorMessage3('Empty procedure note. Please add procedure note')
      return
    }

    if(!(await this.msgBox.showConfirmMessageDialog('Are you sure you want to save procedure note?', 'Procedure note will be created. This can not be un done', 'question', 'Yes, Save Procedure note', 'No, Do not save'))){
      this.procedureNote = ''
      return
    }

    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var procedure  = {
      id : this.procedureId,
      note : this.procedureNote
    }
    this.spinner.show()
    await this.http.post(API_URL+'/patients/procedures/add_note', procedure, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      () => {
        this.msgBox.showSuccessMessage('Added successifully')
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
    this.loadProcedures(0, this.id, 0)
  }

  async loadProcedures(consultationId : any, nonConsultationId : any, admissionId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.procedures = []
    this.spinner.show()
    await this.http.get<IProcedure[]>(API_URL+'/patients/load_procedures?consultation_id='+consultationId+'&non_consultation_id='+nonConsultationId+'&admission_id='+admissionId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.procedures = data!
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, 'Could not load procedures')
      }
    )
    
  }


  async deleteProcedure(procedureId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/delete_procedure?id='+procedureId, options)
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
    this.loadProcedures(this.id, 0, 0)
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
  
  
  

  
  


  
  

  procedureTypeId : any =  null
  procedureTypeCode : string = ''
  procedureTypeName : string = ''
  procedureTypes : IProcedureType[] = []
  async loadProcedureTypesLike(value : string){
    this.procedureTypes = []
    if(value.length < 2){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.get<IProcedureType[]>(API_URL+'/procedure_types/load_procedure_types_like?name_like='+value, options)
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.procedureTypes = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }
  async getProcedureType(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.procedureTypes = []
    this.spinner.show()
    await this.http.get<IProcedureType>(API_URL+'/procedure_types/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {
        this.procedureTypeId = data?.id
        this.procedureTypeCode = data!.code
        this.procedureTypeName = data!.name
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }


  


  

 attachmentUrl : any = ''

 fileExtension : string = ''


}



export interface ICG{
  clinicalNote : IClinicalNote
  generalExamination : IGeneralExamination
}

export interface IAdm {
  patient : IPatient
  wardBed : IWardBed
}
