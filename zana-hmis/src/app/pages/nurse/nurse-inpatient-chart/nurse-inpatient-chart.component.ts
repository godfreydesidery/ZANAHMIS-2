import { CommonModule, Time } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IAdmission } from 'src/app/domain/admission';
import { IClinicalNote } from 'src/app/domain/clinical-note';
import { IConsultation } from 'src/app/domain/consultation';
import { IConsumable } from 'src/app/domain/consumable';
import { IDiagnosisType } from 'src/app/domain/diagnosis-type';
import { IDressing } from 'src/app/domain/dressing';
import { IFinalDiagnosis } from 'src/app/domain/final-diagnosis';
import { IGeneralExamination } from 'src/app/domain/general-examination';
import { ILabTest } from 'src/app/domain/lab-test';
import { ILabTestType } from 'src/app/domain/lab-test-type';
import { IMedicine } from 'src/app/domain/medicine';
import { IPatient } from 'src/app/domain/patient';
import { IPatientConsumableChart } from 'src/app/domain/patient-consumable-chart';
import { IPatientDressingChart } from 'src/app/domain/patient-dressing-chart';
import { IPatientNursingCarePlan } from 'src/app/domain/patient-nursing-care-plan';
import { IPatientNursingChart } from 'src/app/domain/patient-nursing-chart';
import { IPatientNursingProgressNote } from 'src/app/domain/patient-nursing-progress-note';
import { IPatientObservationChart } from 'src/app/domain/patient-observation-chart';
import { IPatientPrescriptionChart } from 'src/app/domain/patient-prescription-chart';
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
import { ShowDateOnlyPipe } from 'src/app/pipes/date.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { ShowTimePipe } from 'src/app/pipes/show_time.pipe';
import { ShowUserPipe } from 'src/app/pipes/show_user.pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';



const API_URL = environment.apiUrl;

@Component({
  selector: 'app-nurse-inpatient-chart',
  templateUrl: './nurse-inpatient-chart.component.html',
  styleUrls: ['./nurse-inpatient-chart.component.scss'],
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    AgePipe,
    ShowUserPipe,
    ShowTimePipe,
    RouterLink
  ], 
})
export class NurseInpatientChartComponent {

  nurseId : any = null;

  id : any = null

  admission! : IAdmission

  prescriptions : IPrescription[] = []
  labTests      : ILabTest[]      = []
  radiologies   : IRadiology[]    = []
  procedures    : IProcedure[]    = []

  patientObservationCharts : IPatientObservationChart[] = []
  patientNursingCharts : IPatientNursingChart[] = []
  patientNursingProgressNotes : IPatientNursingProgressNote[] = []
  patientNursingCarePlans : IPatientNursingCarePlan[] = []
  patientDressingCharts : IPatientDressingChart[] = []
  patientConsumableCharts : IPatientConsumableChart[] = []
  patientPrescriptionCharts : IPatientPrescriptionChart[] = []
  
  procedureId : any = null
  procedureNote : string = ''
  procedureTypeName : string = ''

  /**Observation Chart */
  observationChartBloodPressure : string = ''
  observationChartMeanArterialPressure : string = ''
  observationChartPressure : string = ''
  observationChartTemperature : string = ''
  observationChartRespiratoryRate : string = ''
  observationChartSaturationOxygen : string = ''


  /**Nursing Chart */
  nursingChartFeeding : string = ''
  nursingChartChangingPosition : string = ''
  nursingChartBedBathing : string = ''
  nursingChartRandomBloodSugar : string = ''
  nursingChartFullBloodSugar : string = ''
  nursingChartDrainageOutput : string = ''
  nursingChartFluidIntake : string = ''
  nursingChartUrineOutput : string = ''


  /**Nursing Progress Note */
  nursingProgressNote : string = ''

  /**Nursing Care Plan */
  nursingCarePlanNursingDiagnosis : string = ''
  nursingCarePlanExpectedOutcome : string = ''
  nursingCarePlanImplementation : string = ''
  nursingCarePlanEvaluation : string = ''

  /**Patient Dressing Chart */
  dressing : string = ''

  /**Patient Consumable Chart */
  consumable : string = ''

  /**Patient Prescription Chart */

  



  constructor(private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
    ) { }

  async ngOnInit(): Promise<void> {
    this.id = localStorage.getItem('admission-id')
    this.nurseId = localStorage.getItem('nurse-id')
    localStorage.removeItem('nurse-id')
    localStorage.removeItem('admission-id')
    await this.refresh() 
  }

  async refresh(){
    await this.loadAdmission(this.id)
    this.loadPrescriptions(0, 0, this.id)
    this.loadProcedures(0, 0, this.id)
    this.loadLabTests(0, 0, this.id)
    this.loadRadiologies(0, 0, this.id)
    /**Load charts */
    this.loadPatientObservationCharts(0, 0, this.id) 
    this.loadPatientNursingCharts(0, 0, this.id)
    this.loadPatientNursingProgressNotes(0, 0, this.id)
    this.loadPatientNursingCarePlans(0, 0, this.id)
    this.loadPatientDressingChart(0, 0, this.id)
    this.loadPatientConsumableChart(0, 0, this.id)
  }

  async setGlobalPatientId(){
    localStorage.setItem('patient-id', this.admission.patient.id)
    localStorage.setItem('admission-id', this.id)
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
        this.id = data?.id
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

  async loadPrescriptions(consultationId : any, nonConsultationId : any, admissionId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IPrescription[]>(API_URL+'/patients/load_prescriptions?consultation_id='+consultationId+'&non_consultation_id='+nonConsultationId+'&admission_id='+admissionId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.prescriptions = data!
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, 'Could not load prescriptions')
      }
    )
    
  }

  async loadLabTests(consultationId : any, nonConsultationId : any, admissionId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<ILabTest[]>(API_URL+'/patients/load_lab_tests?consultation_id='+consultationId+'&non_consultation_id='+nonConsultationId+'&admission_id='+admissionId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.labTests = data!
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, 'Could not load lab tests')
      }
    )
    
  }

  async loadRadiologies(consultationId : any, nonConsultationId : any, admissionId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.prescriptions = []
    this.spinner.show()
    await this.http.get<IRadiology[]>(API_URL+'/patients/load_radiologies?consultation_id='+consultationId+'&non_consultation_id='+nonConsultationId+'&admission_id='+admissionId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.radiologies = data!
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, 'Could not load radiologies')
      }
    )
    
  }

  async loadProcedures(consultationId : any, nonConsultationId : any, admissionId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
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

  showProcedureNote(id : any, procedureTypeName : string, note : string){
    this.procedureId = id
    this.procedureTypeName = procedureTypeName
    this.procedureNote = note
  }

  async savePatientObservationChart(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var chart = {
      bloodPressure         : this.observationChartBloodPressure,
      meanArterialPressure  : this.observationChartMeanArterialPressure,
      pressure              : this.observationChartPressure,
      temperature           : this.observationChartTemperature,
      respiratoryRate       : this.observationChartRespiratoryRate,
      saturationOxygen      : this.observationChartSaturationOxygen,
      admission             : {id : this.id},
      nurse                 : {id : this.nurseId}
    }
    this.spinner.show()
    await this.http.post(API_URL+'/patients/save_patient_observation_chart?consultation_id='+0+'&non_consultation_id='+0+'&admission_id='+this.id+'&nurse_id='+this.nurseId, chart, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Success') 
        this.clearObservationChart()
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.loadPatientObservationCharts(0, 0, this.id) 
  }

  async loadPatientObservationCharts(consultationId : any, nonConsultationId : any, admissionId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IPatientObservationChart[]>(API_URL+'/patients/observation_charts?consultation_id='+consultationId+'&non_consultation_id='+nonConsultationId+'&admission_id='+admissionId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.patientObservationCharts = data!
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, 'Could not load observation charts')
      }
    )
    
  }

  async deleteObservationChart(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.post(API_URL+'/patients/delete_observation_chart?id='+id, null, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Deleted')
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.loadPatientObservationCharts(0, 0, this.id) 
  }

  clearObservationChart(){
    this.observationChartBloodPressure = ''
    this.observationChartMeanArterialPressure = ''
    this.observationChartPressure = ''
    this.observationChartTemperature = ''
    this.observationChartRespiratoryRate = ''
    this.observationChartSaturationOxygen = ''
  }

  clearNursingChart(){
    this.nursingChartFeeding = ''
    this.nursingChartChangingPosition = ''
    this.nursingChartBedBathing = ''
    this.nursingChartRandomBloodSugar = ''
    this.nursingChartFullBloodSugar = ''
    this.nursingChartDrainageOutput = ''
    this.nursingChartFluidIntake = ''
    this.nursingChartUrineOutput = ''
  }

  clearNursingProgressNote(){
    this.nursingProgressNote = ''
  }


  async savePatientNursingChart(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var chart = {
      feeding : this.nursingChartFeeding,
      changingPosition : this.nursingChartChangingPosition,
      bedBathing : this.nursingChartBedBathing,
      randomBloodSugar : this.nursingChartRandomBloodSugar,
      fullBloodSugar : this.nursingChartFullBloodSugar,
      drainageOutput : this.nursingChartDrainageOutput,
      fluidIntake : this.nursingChartFluidIntake,
      urineOutput : this.nursingChartUrineOutput,
      admission : {id : this.id},
      nurse : {id : this.nurseId}
    }
    this.spinner.show()
    await this.http.post(API_URL+'/patients/save_patient_nursing_chart?consultation_id='+0+'&non_consultation_id='+0+'&admission_id='+this.id+'&nurse_id='+this.nurseId, chart, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Success') 
        this.clearNursingChart()
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.loadPatientNursingCharts(0, 0, this.id) 
  }

  async loadPatientNursingCharts(consultationId : any, nonConsultationId : any, admissionId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IPatientNursingChart[]>(API_URL+'/patients/nursing_charts?consultation_id='+consultationId+'&non_consultation_id='+nonConsultationId+'&admission_id='+admissionId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.patientNursingCharts = data!
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, 'Could not load nursing charts')
      }
    )
    
  }

  async deleteNursingChart(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.post(API_URL+'/patients/delete_nursing_chart?id='+id, null, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Deleted')
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.loadPatientNursingCharts(0, 0, this.id) 
  }

  async savePatientNursingProgressNote(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var note = {
      note : this.nursingProgressNote,
      admission : {id : this.id},
      nurse : {id : this.nurseId}
    }
    this.spinner.show()
    await this.http.post(API_URL+'/patients/save_patient_nursing_progress_note?consultation_id='+0+'&non_consultation_id='+0+'&admission_id='+this.id+'&nurse_id='+this.nurseId, note, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Success') 
        this.clearNursingProgressNote()
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.loadPatientNursingProgressNotes(0, 0, this.id) 
  }

  async loadPatientNursingProgressNotes(consultationId : any, nonConsultationId : any, admissionId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IPatientNursingProgressNote[]>(API_URL+'/patients/nursing_progress_notes?consultation_id='+consultationId+'&non_consultation_id='+nonConsultationId+'&admission_id='+admissionId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.patientNursingProgressNotes = data!
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, 'Could not load nursing progress notes')
      }
    )
    
  }

  async deleteNursingProgressNote(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.post(API_URL+'/patients/delete_nursing_progress_note?id='+id, null, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Deleted')
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.loadPatientNursingProgressNotes(0, 0, this.id) 
  }

  async savePatientNursingCarePlan(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var plan = {
      nursingDiagnosis : this.nursingCarePlanNursingDiagnosis,
      expectedOutcome : this.nursingCarePlanExpectedOutcome,
      implementation : this.nursingCarePlanImplementation,
      evaluation : this.nursingCarePlanEvaluation,
      admission : {id : this.id},
      nurse : {id : this.nurseId}
    }
    this.spinner.show()
    await this.http.post(API_URL+'/patients/save_patient_nursing_care_plan?consultation_id='+0+'&non_consultation_id='+0+'&admission_id='+this.id+'&nurse_id='+this.nurseId, plan, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Success') 
        this.clearNursingCarePlan()
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.loadPatientNursingCarePlans(0, 0, this.id) 
  }

  async loadPatientNursingCarePlans(consultationId : any, nonConsultationId : any, admissionId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IPatientNursingCarePlan[]>(API_URL+'/patients/nursing_care_plans?consultation_id='+consultationId+'&non_consultation_id='+nonConsultationId+'&admission_id='+admissionId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.patientNursingCarePlans = data!
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    
  }

  async deleteNursingCarePlan(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.post(API_URL+'/patients/delete_nursing_care_plan?id='+id, null, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Deleted')
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.loadPatientNursingCarePlans(0, 0, this.id) 
  }

  clearNursingCarePlan(){
    this.nursingCarePlanNursingDiagnosis = ''
    this.nursingCarePlanExpectedOutcome = ''
    this.nursingCarePlanImplementation = ''
    this.nursingCarePlanEvaluation = ''
  }



  dressingProcedureTypeId : any =  null
  dressingProcedureTypeName : string = ''
  dressings : IDressing[] = []
  async loadDressingLike(value : string){
    this.dressings = []
    if(value.length < 2){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.get<IDressing[]>(API_URL+'/dressings/load_dressings_like?name_like='+value, options)
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.dressings = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }
  async getDressing(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.dressings = []
    this.spinner.show()
    await this.http.get<IDressing>(API_URL+'/dressings/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {
        this.dressingProcedureTypeId = data?.procedureType.id
        this.dressingProcedureTypeName = data!.procedureType?.name
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }

  clearDressingChart(){
    this.dressingProcedureTypeId = null
    this.dressingProcedureTypeName = ''
  }

  async savePatientDressingChart(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var chart = {
      procedureType : {id : this.dressingProcedureTypeId},
      admission : {id : this.id},
      nurse : {id : this.nurseId}
    }
    this.spinner.show()
    await this.http.post(API_URL+'/patients/save_patient_dressing_chart?consultation_id='+0+'&non_consultation_id='+0+'&admission_id='+this.id+'&nurse_id='+this.nurseId, chart, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Success') 
        this.clearDressingChart()
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.loadPatientDressingChart(0, 0, this.id) 
  }

  async loadPatientDressingChart(consultationId : any, nonConsultationId : any, admissionId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IPatientDressingChart[]>(API_URL+'/patients/dressing_charts?consultation_id='+consultationId+'&non_consultation_id='+nonConsultationId+'&admission_id='+admissionId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.patientDressingCharts = data!
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    
  }

  async deleteDressingChart(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.post(API_URL+'/patients/delete_dressing_chart?id='+id, null, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Deleted')
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.loadPatientDressingChart(0, 0, this.id) 
  }

  consumableMedicineId : any =  null
  consumableMedicineName : string = ''
  consumableQty : number = 0
  consumables : IConsumable[] = []
  async loadConsumableLike(value : string){
    this.consumables = []
    if(value.length < 2){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.get<IConsumable[]>(API_URL+'/consumables/load_consumables_like?name_like='+value, options)
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.consumables = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }
  async getConsumable(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.consumables = []
    this.spinner.show()
    await this.http.get<IConsumable>(API_URL+'/consumables/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {
        this.consumableMedicineId = data?.medicine.id
        this.consumableMedicineName = data!.medicine?.name
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }

  clearConsumableChart(){
    this.consumableMedicineId = null
    this.consumableMedicineName = ''
    this.consumableQty = 0
  }

  async savePatientConsumableChart(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    //if()
    var chart = {
      medicine : {id : this.consumableMedicineId},
      qty : this.consumableQty,
      admission : {id : this.id},
      nurse : {id : this.nurseId}
    }
    this.spinner.show()
    await this.http.post(API_URL+'/patients/save_patient_consumable_chart?consultation_id='+0+'&non_consultation_id='+0+'&admission_id='+this.id+'&nurse_id='+this.nurseId, chart, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Success') 
        this.clearConsumableChart()
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.loadPatientConsumableChart(0, 0, this.id)
  }

  async loadPatientConsumableChart(consultationId : any, nonConsultationId : any, admissionId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IPatientConsumableChart[]>(API_URL+'/patients/consumable_charts?consultation_id='+consultationId+'&non_consultation_id='+nonConsultationId+'&admission_id='+admissionId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.patientConsumableCharts = data!
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    
  }

  async deleteConsumableChart(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.post(API_URL+'/patients/delete_consumable_chart?id='+id, null, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Deleted')
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.loadPatientConsumableChart(0, 0, this.id) 
  }

  prescriptionId : any = null
  prescription! : IPrescription
  async loadPrescription(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.get<IPrescription>(API_URL+'/patients/get_prescription_by_id?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.prescription = data!
        this.prescriptionId = data?.id
        this.loadPrescriptionChart(this.prescription.id)
      }
    )
    .catch(
      (error) => {
        this.prescription!
        this.prescriptionId = null
        this.msgBox.showErrorMessage(error, '')

      }
    )
  }

  prescriptionCharts : IPatientPrescriptionChart[] = []
  async loadPrescriptionChart(prescrId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.prescriptionCharts = []
    await this.http.get<IPatientPrescriptionChart[]>(API_URL+'/patients/prescription_charts_by_id?prescription_id='+prescrId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.prescriptionCharts = data!
      }
    )
    .catch(
      (error) => {
        this.prescription!
        this.msgBox.showErrorMessage(error, '')

      }
    )

  }

  clearPrescriptionChart(){
    this.dosage = ''
    this.output = ''
    this.remark = ''
  }

  dosage : string = ''
  output : string = ''
  remark : string = '' 
  async savePatientPrescriptionChart(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var chart = {
      prescription : {id : this.prescriptionId},
      dosage : this.dosage,
      output : this.output,
      remark : this.remark,
      admission : {id : this.id},
      nurse : {id : this.nurseId}
    }
    this.spinner.show()
    await this.http.post(API_URL+'/patients/save_patient_prescription_chart?consultation_id='+0+'&non_consultation_id='+0+'&admission_id='+this.id+'&nurse_id='+this.nurseId, chart, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Success') 
        this.clearPrescriptionChart()
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, '')
        this.clearPrescriptionChart()
      }
    )
    this.loadPrescriptionChart(this.prescriptionId)
  }

  async deletePrescriptionChart(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.post(API_URL+'/patients/delete_prescription_chart?id='+id, null, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Deleted')
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.loadPrescriptionChart(this.prescriptionId)
  }
}


