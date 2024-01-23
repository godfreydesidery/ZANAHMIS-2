import { CommonModule, Time } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IClinician } from 'src/app/domain/clinician';
import { IConsultation } from 'src/app/domain/consultation';
import { IConsultationTransfer } from 'src/app/domain/consultation-transfer';
import { IDiagnosisType } from 'src/app/domain/diagnosis-type';
import { IInsurancePlan } from 'src/app/domain/insurance-plan';
import { ILabTest } from 'src/app/domain/lab-test';
import { ILabTestType } from 'src/app/domain/lab-test-type';
import { IPatient } from 'src/app/domain/patient';
import { IProcedure } from 'src/app/domain/procedure';
import { IProcedureType } from 'src/app/domain/procedure-type';
import { IRadiology } from 'src/app/domain/radiology';
import { IRadiologyType } from 'src/app/domain/radiology-type';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { ShowDateTimePipe } from 'src/app/pipes/date_time.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

import swal from 'sweetalert2';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-patient-register',
  templateUrl: './patient-register.component.html',
  styleUrls: ['./patient-register.component.scss'],
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
export class PatientRegisterComponent implements OnInit {


  id : any
  no : string
  searchKey : string
  firstName : string
  middleName : string
  lastName : string
  type : string = ''
  dateOfBirth! :Date
  gender : string
  paymentType : string
  membershipNo : string
  phoneNo : string		
	address : string
	email : string
	nationality : string
	nationalId : string	
	passportNo : string
  kinFullName : string
	kinRelationship : string
	kinPhoneNo : string


  patientRecordMode : string = ''
  insurancePlan! : IInsurancePlan

  insurancePlanName : string = ''

  searchKeys : string[] = []
  searchKeysToDisplay : string[] = []
  clinicNames : string[] = []
  clinicianNames : string[] = []

  clinicName : string = ''
  clinicianName : string = ''

  consultationFee : number = 0

  insurancePlanNames : string[] = []

  consultations : IConsultation[] = []

  lastVisitDate! : Date


  editType : string = ''

  editPaymentType : string = ''
  editInsurancePlanName : string = ''
  editMembershipNo : string = ''

  lockSearchKey : boolean = false

  nonConsultationId : number = 0

  labTests : ILabTest[] = []
  labTestTypeNames : string[] = []
  
  radiologies : IRadiology[] = []
  radiologyTypeNames : string[] = []

  procedures : IProcedure[] = []
  procedureTypeNames : string[] = []
  procedureTypeName : string = ''

  diagnosisTypeNames : string[] = []


  labTotal : number = 0
  radiologyTotal : number = 0
  procedureTotal : number = 0

  procedureId : any
  procedureNote : string = ''
  procedureType : string = ''
  procedureNeedTheatre : boolean = false
  procedureTheatreName : string = ''
  procedureTime! : Time
  procedureDiagnosis : string = ''
  procedureDate! : Date
  procedureHours : number = 0
  procedureMinutes : number = 0

  theatreName : string = ''
  theatreNames : string[] = []

  filterRecords : string = ''
  
  constructor(
    //private shortcut : ShortCutHandlerService,
              private auth : AuthService,
              private http : HttpClient,
              private modalService: NgbModal,
              private spinner: NgxSpinnerService,
              private msgBox : MsgBoxService
  ) {
    this.id = null
    this.no = ''
    this.searchKey = ''
    this.firstName = ''
    this.middleName = ''
    this.lastName = ''
    this.type = ''
    this.gender = ''
    this.paymentType = ''
    this.membershipNo = ''
    this.phoneNo = ''
    this.address = ''
    this.email = ''
    this.nationality = 'Tanzania'
    this.nationalId = ''
    this.passportNo = ''
    this.kinFullName = ''
    this.kinRelationship = ''
    this.kinPhoneNo = ''
  }

  ngOnInit(): void {
    this.loadClinicNames()
    this.loadInsurancePlanNames()
    this.loadTheatreNames()
  }

  clear(){
    this.id = null
    this.no = ''
    this.searchKey = ''
    this.firstName = ''
    this.middleName = ''
    this.lastName = ''
    this.type = ''
    this.gender = ''
    this.paymentType = ''
    this.membershipNo = ''
    this.phoneNo = ''
    this.address = ''
    this.email = ''
    this.nationality = 'Tanzania'
    this.nationalId = ''
    this.passportNo = ''
    this.kinFullName = ''
    this.kinRelationship = ''
    this.kinPhoneNo = ''
    this.insurancePlanName = ''
    this.clinicName = ''
    this.clinicianName = ''
    this.consultationFee = 0
    this.lockSearchKey = false

    this.nonConsultationId = 0

    this.labTests = []
    this.labTestTypeName = ''
    this.procedures = []
    this.procedureTypeName = ''
    this.radiologies = []
    this.radiologyTypeName = ''

    this.labTotal = 0
    this.radiologyTotal = 0
    this.procedureTotal  = 0

    this.patients = []

    this.showFree = false

  }

  newPatientPrompt(){
    this.patientRecordMode = 'new'
    this.id = null
    this.clear()
  }

  existingPatientPrompt(){
    this.patientRecordMode = ''
    this.clear()
  }

  clearEditType(){
    this.editType = ''
  }

  changePaymentType(paymentType : string){
    this.paymentType = paymentType
  }

  changeEditPaymentType(paymentType : string){
    this.editPaymentType = paymentType
  }

  clearEditPaymentType(){
    this.editPaymentType = ''
  }

  clearEditInsurancePlanName(){
    this.editInsurancePlanName = ''
  }

  clearEditMembershipNo(){
    this.editMembershipNo = ''
  }

  async savePaymentType(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var patient = {
      id                  : this.id, //
      no                  : this.no,
      paymentType         : this.editPaymentType, //
      membershipNo        : this.editMembershipNo, //
      insurancePlan   : {
        name : this.editInsurancePlanName //
      }
    }
    
    this.spinner.show()
      await this.http.post<IPatient>(API_URL+'/patients/change_payment_type', patient, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          var temp = this.patientId
          this.clear()
          this.patientId = temp
          this.getPatient(this.patientId)
          
          this.msgBox.showSuccessMessage('Payment Type changed successifuly')
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, '')
        }
      )
  }

  async registerPatient(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var patient = {
      id                  : this.id,
      no                  : this.no,
      searchKey           : "NA",
      firstName           : this.firstName,
      middleName          : this.middleName,
      lastName            : this.lastName,
      gender              : this.gender,
      type                : this.type,
      dateOfBirth         : this.dateOfBirth,
      paymentType         : this.paymentType,
      membershipNo        : this.membershipNo,
      phoneNo             : this.phoneNo,
      address             : this.address,
      email               : this.email,
      nationality         : this.nationality,
      nationalId          : this.nationalId,
      passportNo          : this.passportNo,
      kinFullName         : this.kinFullName,
      kinRelationship     : this.kinRelationship,
      kinPhoneNo          : this.kinPhoneNo,

      insurancePlan   : {
        name : this.insurancePlanName
      }
    }

    if(this.id == null || this.id == ''){
      if(this.no == ''){
        this.no = 'NA'
      }
      /**
       * Save a new record
       */
      this.spinner.show()
      await this.http.post<IPatient>(API_URL+'/patients/register', patient, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          //swal.fire()
          this.msgBox.showSuccessMessage('Patient Registered successifully. Please note patients File No')

          this.id               = data!['id']
          this.no               = data!['no']
          this.firstName        = data!['firstName']
          this.middleName       = data!['middleName']
          this.lastName         = data!['lastName']
          this.gender           = data!['gender']
          this.paymentType      = data!['paymentType']
          this.type             = data!['type']
          this.membershipNo     = data!['membershipNo']
          this.phoneNo          = data!['phoneNo']
          this.address          = data!['address']
          this.email            = data!['email']
          this.nationality      = data!['nationality']
          this.nationalId       = data!['nationalId']
          this.passportNo       = data!['passportNo']
          this.kinFullName      = data!['kinFullName']
          this.kinRelationship  = data!['kinRelationship']
          this.kinPhoneNo       = data!['kinPhoneNo']

          
          
          this.insurancePlanName = data!['insurancePlan']?.name

          this.patientRecordMode = ''


          if(this.type === 'OUTPATIENT'){
            this.loadActiveConsultation(this.id)
          }else if(this.type === 'OUTSIDER'){
            this.loadNonConsultationId(this.id)
          }
          
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, '')
        }
      )

    }
  }

  async updatePatient() : Promise<boolean>{
    var updated : boolean = false
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var patient = {
      id                  : this.id,
      no                  : this.no,
      firstName           : this.firstName,
      middleName          : this.middleName,
      lastName            : this.lastName,
      gender              : this.gender,
      type                : this.type,
      dateOfBirth         : this.dateOfBirth,
      paymentType         : this.paymentType,
      membershipNo        : this.membershipNo,
      phoneNo             : this.phoneNo,
      address             : this.address,
      email               : this.email,
      nationality         : this.nationality,
      nationalId          : this.nationalId,
      passportNo          : this.passportNo,
      kinFullName         : this.kinFullName,
      kinRelationship     : this.kinRelationship,
      kinPhoneNo          : this.kinPhoneNo,
      insurancePlan : {
        name : this.insurancePlanName
      }
    }
    /**
     * Update
     */
    this.spinner.show()
    await this.http.post<IPatient>(API_URL+'/patients/update', patient, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Patient updated successifully. Please note patients File No')

        this.id               = data!['id']
        this.no               = data!['no']
        this.firstName        = data!['firstName']
        this.middleName       = data!['middleName']
        this.lastName         = data!['lastName']
        this.gender           = data!['gender']
        this.paymentType      = data!['paymentType']
        this.type             = data!['type']
        this.membershipNo     = data!['membershipNo']
        this.phoneNo          = data!['phoneNo']
        this.address          = data!['address']
        this.email            = data!['email']
        this.nationality      = data!['nationality']
        this.nationalId       = data!['nationalId']
        this.passportNo       = data!['passportNo']
        this.kinFullName      = data!['kinFullName']
        this.kinRelationship  = data!['kinRelationship']
        this.kinPhoneNo       = data!['kinPhoneNo']


        this.insurancePlanName = data!['insurancePlan']?.name
        

        this.patientRecordMode = ''

        updated = true
      }
    )
    .catch(
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
        updated = false
      }
      
    )

    if(this.type === 'OUTPATIENT'){
      this.loadActiveConsultation(this.id)
    }else if(this.type === 'OUTSIDER'){
      this.loadNonConsultationId(this.id)
    }

    return updated
  }

  validatePatient(patient : IPatient) : boolean{

    var valid = true
    if(patient.dateOfBirth == null){
      this.msgBox.showErrorMessage3('Date of birth is a required value')
      return false
    }

    return true
  }

  async loadSearchKeys(){//for unpaid registration
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<string[]>(API_URL+'/patients/get_all_search_keys', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.searchKeys = []
        data?.forEach(element => {
          this.searchKeys.push(element)
        })
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, 'Could not load patients')
      }
    )
  }

  search(){
    alert('Success')
  }

  onInput() {
    var val = this.searchKey
    var opts = this.searchKeys
    for (var i = 0; i < opts.length; i++) {
      if (opts[i] === val) {
        // An item was selected from the list!
        // yourCallbackHere()
        //alert(opts[i]);
        break;
      }
    }
  }


  async searchBySearchKey(key : string): Promise<void> {
    var searchElement = ''
    //var val = key
    for (var i = 0; i < this.searchKeys.length; i++) {
      if (this.searchKeys[i] === key) {
        // An item was selected from the list!
        searchElement = key
        break
      }
    }
    if(searchElement.length === 0){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IPatient>(API_URL+'/patients/get_by_search_key?search_key=' + searchElement, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {

        this.searchKey = key

        this.id                 = data!['id']
        this.no                 = data!['no']
        this.firstName          = data!['firstName']
        this.middleName         = data!['middleName']
        this.lastName           = data!['lastName']
        this.gender             = data!['gender']
        this.dateOfBirth        = data!['dateOfBirth']
        this.paymentType        = data!['paymentType']
        this.type               = data!['type']
        this.membershipNo       = data!['membershipNo']
        this.phoneNo            = data!['phoneNo']
        this.address            = data!['address']
        this.email              = data!['email']
        this.nationality        = data!['nationality']
        this.nationalId         = data!['nationalId']
        this.passportNo         = data!['passportNo']
        this.kinFullName        = data!['kinFullName']
        this.kinRelationship    = data!['kinRelationship']
        this.kinPhoneNo         = data!['kinPhoneNo']

        this.insurancePlanName  = data!['insurancePlan']?.name

        this.lockSearchKey      = true
      }
    )
    .catch(
      error => {
        console.log(error)
        this.clear()
        this.msgBox.showErrorMessage(error, '')
      }
    )
    if(this.type === 'OUTPATIENT'){
      await this.loadActiveConsultation(this.id)
    }else if(this.type === 'OUTSIDER'){
      await this.loadNonConsultationId(this.id)
      await this.loadLabTest(0, this.nonConsultationId, 0)
      await this.loadRadiologies(0, this.nonConsultationId, 0)
      await this.loadProcedures(0, this.nonConsultationId, 0)
    }
    await this.getLastVisitDate()   
  }

  async deleteLabTest(labTestId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/delete_lab_test?id='+labTestId, null, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.loadLabTest(0, this.nonConsultationId, 0)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        this.loadLabTest(0, this.nonConsultationId, 0)
      }
    )    
  }

  async deleteRadiology(radiologyId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/delete_radiology?id='+radiologyId, null, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.loadRadiologies(0, this.nonConsultationId, 0)
      }
    )
    .catch(
      error => {
        this.loadRadiologies(0, this.nonConsultationId, 0)
        this.msgBox.showErrorMessage(error, '')
      }
    )
    
  }

  async deleteProcedure(procedureId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/delete_procedure?id='+procedureId, null, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.loadProcedures(0, this.nonConsultationId, 0)
      }
    )
    .catch(
      error => {
        this.loadProcedures(0, this.nonConsultationId, 0)
        this.msgBox.showErrorMessage(error, '')
      }
    )
    
  }

  async loadRadiologies(consultationId : any, nonConsultationId : any, admissionId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.radiologies = []
    this.radiologyTotal = 0
    this.spinner.show()
    await this.http.get<IRadiology[]>(API_URL+'/patients/load_radiologies?consultation_id='+consultationId+'&non_consultation_id='+nonConsultationId+'&admission_id='+admissionId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.radiologyTotal = this.radiologyTotal + element.patientBill.amount
          this.radiologies.push(element)
        })
        console.log(this.radiologies)
        
      }
    )
    .catch(
      (error) => {
        console.log(error, '')
        this.msgBox.showErrorMessage(error, 'Could not load radiologies')
      }
    )
    
  }

  async loadProcedures(consultationId : any, nonConsultationId : any, admissionId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.procedures = []
    this.procedureTotal = 0
    this.spinner.show()
    await this.http.get<IProcedure[]>(API_URL+'/patients/load_procedures?consultation_id='+consultationId+'&non_consultation_id='+nonConsultationId+'&admission_id='+admissionId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.procedureTotal = this.procedureTotal + element.patientBill!.amount
          this.procedures.push(element)
        })
        console.log(this.procedures)
        
      }
    )
    .catch(
      (error) => {
        console.log(error, '')
        this.msgBox.showErrorMessage(error, 'Could not load procedures')
      }
    )
    
  }

  async loadClinicNames(){//for unpaid registration
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<string[]>(API_URL+'/clinics/get_names', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.clinicNames = []
        data?.forEach(element => {
          this.clinicNames.push(element)
        })
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, 'Could not load clinics')
      }
    )
  }

  async loadConsultationFee(){//for unpaid registration
    if(this.paymentType === 'INSURANCE'){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.consultationFee = 0
    if(this.clinicName === ''){
      this.consultationFee = 0
      return
    }
    this.spinner.show()
    await this.http.get<number>(API_URL+'/clinics/get_consultation_fee?clinic_name='+this.clinicName, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.consultationFee = data!
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, 'Could not get consultation fee')
      }
    )
  }

  async loadActiveConsultation(patient_id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.consultations = []
    
    this.spinner.show()
    await this.http.get<IConsultation[]>(API_URL+'/patients/get_active_consultations?patient_id='+patient_id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.consultations = data!
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, 'Could not load active consultations')
      }
    )
  }

  async loadClinicianNames(clinicName : string){
    /**
     * Gets a list of class names
     */
    //this.clinicNames = []
    this.clinicianNames = []   
    this.clinicianName = ''
    if(clinicName == ''){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IClinician[]>(API_URL+'/clinicians/get_by_clinic_name?clinic_name='+this.clinicName, options)
    //await this.http.get<IClinician[]>(API_URL+'/clinicians', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.clinicianNames.push(element.nickname)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load doctors')
      }
    )
  }

  async loadInsurancePlanNames(){
    this.insurancePlanNames = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<string[]>(API_URL+'/insurance_plans/get_names', options)
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
        this.msgBox.showErrorMessage(error, 'Could not load insurance Plans')
      }
    )
  }

  async doConsultation(){
    //if(!window.confirm('Send the selected patient to doctor?')){
      //return
    //}
    if(!(await this.msgBox.showConfirmMessageDialog('Are you sure you want to send this patient to doctor?', 'A doctor consultation will be created. An uncovered patient will be required to clear consultation bill before seeing the doctor.', 'question', 'Yes, Send to Doctor', 'No, Do not send'))){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var paymentType : IPaymentType = {
      name: this.paymentType,
      insurancePlanName: this.insurancePlanName,
      insuranceMembershipNo: this.membershipNo
    }
    this.spinner.show()
    await this.http.post<IPatient>(API_URL+'/patients/do_consultation?patient_id='+this.id+'&clinic_name='+this.clinicName+'&clinician_name='+this.clinicianName, paymentType, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Patient sent to doctor successifuly')
        var temp = this.searchKey
        this.clear()
        this.searchKey = temp
        this.searchBySearchKey(this.searchKey)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )

  }

  async cancelConsultation(consultation : IConsultation){
    if(!window.confirm('Cancel this consultation?')){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    this.spinner.show()
    await this.http.post<IPatient>(API_URL+'/patients/cancel_consultation?id='+consultation.id, null, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Consultation canceled successifully')
        var temp = this.searchKey
        this.clear()
        this.searchKey = temp
        this.searchBySearchKey(this.searchKey)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )

  }

  freeShow(){
    this.showFree = true
  }

  showFree : boolean = false

  regNoToFree : string = ''

  async freeInProcessConsultation(consultation : IConsultation){
    //if(!window.confirm('Free patient from this consultation?')){
      //return
    //}

    if(await this.msgBox.showConfirmMessageDialog('Free patient from this consultation?', 'You are about to free the patient', 'question', 'Continue to free', 'Do not free')  === false){
      return
    }

    //if(!window.confirm('Freing the patient will remove the patient from doctor list!. Continue freing the patient?')){
      //return
    //}

    if(await this.msgBox.showConfirmMessageDialog('Freeing the patient will remove the patient from doctor list!. Continue freeing the patient?', 'Patient will be freed from the active consultation', 'question', 'Free the Patient', 'Do not free')  === false){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    this.spinner.show()
    await this.http.post<IPatient>(API_URL+'/patients/free_consultation?id='+consultation.id + '&no='+this.regNoToFree, null, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Patient freed successifuly')
        var temp : string = this.id
        this.clear()
        this.id = temp
        this.getPatient(this.id)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.regNoToFree = ''
  }

  async freeConsultation(consultation : IConsultation){
    if(!window.confirm('Free patient from this consultation?')){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    this.spinner.show()
    await this.http.post<IPatient>(API_URL+'/patients/free_consultation?id='+consultation.id + '&no='+this.regNoToFree, null, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Patient freed successifuly')
        var temp : string = this.clinicName
        var temp2 = this.searchKey
        this.clear()
        this.clinicName = temp
        this.searchKey = temp2
        this.processTransfer(consultation.patient.id, this.clinicName)
        this.loadConsultationFee()
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.regNoToFree = ''

  }

  async getLastVisitDate(){
  
    var date : Date 
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    this.spinner.show()
    await this.http.get<Date>(API_URL+'/patients/last_visit_date_time?patient_id='+this.id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.lastVisitDate = data!
        
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
       console.log(error)
      }
    )
  }

  async changeType(){

    var newPatientType = ''
    if(this.type === 'OUTPATIENT'){
      newPatientType = 'OUTSIDER'
    }else if(this.type === 'OUTSIDER'){
      newPatientType = 'OUTPATIENT'
    }else{
      return
    }


    /*var toChange = false

    await swal.fire({
      title : 'Are you sure?',
      text : 'Patient type will be changed',
      icon : 'question',
      showCancelButton : true,
      confirmButtonText : 'Yes',
      cancelButtonText : 'No'
    }).then(async (result) => {
      if(result.value) {
        toChange = true
        await swal.fire(
          'Changed',
          'Changed successifuly',
          'success'
        )
      } else if (result.dismiss === swal.DismissReason.cancel){
         await swal.fire(
          'Not changed',
          'Operation canceled',
          'error'
        )
        
      }
    })*/

    var toChange = false

    await swal.fire({
      title : 'Are you sure you want to change patient type?',
      text : 'Patient type will be changed to ' + newPatientType,
      icon : 'question',
      width : '500',
      showCancelButton : true,
      confirmButtonText : 'Yes, Change Patient type',
      confirmButtonColor : '#009688',
      cancelButtonText : 'No, Do not change',
      cancelButtonColor : '#e5343d'
      
    }).then(async (result) => {
      if(result.value) {
        toChange = true
      }  
    })

    if(toChange === false){
      return
    }

    

   

    //if(!window.confirm('Confirm changing patient type. Confirm?')){
      //return
    //}
  
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var patient = {
      id : this.id
    }
    
    this.spinner.show()
    await this.http.post(API_URL+'/patients/change_type', patient, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {        
        var temp = this.patientId
        this.clear()
        this.patientId = temp
        this.getPatient(this.patientId)
        //this.searchBySearchKey(this.searchKey) 
        this.msgBox.showSuccessMessage('Patient Type changed to ' + newPatientType)       
      }
    )
    .catch(
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
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
        console.log(data)
        data?.forEach(element => {
          this.procedureTypeNames.push(element)
        })
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, 'Could not load procedure types names')
      }
    )
  }


  async saveLabTest(){
    if(this.type != 'OUTSIDER'){
      this.msgBox.showErrorMessage3('Only allowed for outsiders')
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var labTest  = {
      labTestType : {
        id : this.labTestTypeId,
        code : this.labTestTypeCode,
        name : this.labTestTypeName
      },
      diagnosisType : {
        id : this.diagnosisTypeId,
        code : this.diagnosisTypeCode,
        name : this.diagnosisTypeName
      } 
    }
    this.spinner.show()
    await this.http.post(API_URL+'/patients/save_lab_test?consultation_id='+0+'&non_consultation_id='+this.nonConsultationId+'&admission_id='+0, labTest, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      () => {
        this.loadLabTest(0, this.nonConsultationId, 0)
        this.msgBox.showSuccessMessage('Lab Test Saved successifully')
      }
    )
    .catch(
      error => {
        this.loadLabTest(0, this.nonConsultationId, 0)
        this.msgBox.showErrorMessage(error, 'Could not save Lab Test')
        console.log(error)
      }
    )
    
  }

  async saveProcedure(){
    if(this.type != 'OUTSIDER'){
      this.msgBox.showErrorMessage3('Only allowed for outsiders')
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var procedure  = {
      procedureType : {
        id : null,
        code : '',
        name : this.procedureTypeName
      },
      type      : this.procedureType,
      theatre   : { name : this.theatreName },
      diagnosisType : { name : this.diagnosisTypeName},
      time      : this.procedureTime,
      date      : this.procedureDate,
      hours     : this.procedureHours,
      minutes   : this.procedureMinutes
    }
    this.spinner.show()
    await this.http.post(API_URL+'/patients/save_procedure?consultation_id='+0+'&non_consultation_id='+this.nonConsultationId+'&admission_id='+0, procedure, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      () => {
        this.loadProcedures(0, this.nonConsultationId, 0)
        this.msgBox.showSuccessMessage('Procedure Saved successifully')
      }
    )
    .catch(
      error => {
        this.loadProcedures(0, this.nonConsultationId, 0)
        this.msgBox.showErrorMessage(error, 'Could not save Procedure')
        console.log(error)
      }
    )
    
  }

  async loadLabTest(consultationId : any, nonConsultationId : any, admissionId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.labTests = []
    this.labTotal = 0
    this.spinner.show()
    await this.http.get<ILabTest[]>(API_URL+'/patients/load_lab_tests?consultation_id='+consultationId+'&non_consultation_id='+nonConsultationId+'&admission_id='+admissionId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.labTotal = this.labTotal + element.patientBill.amount
          this.labTests.push(element)
        })
        console.log(data)
        
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, 'Could not load lab tests')
      }
    )   
  }

  async saveRadiology(){
    if(this.type != 'OUTSIDER'){
      this.msgBox.showErrorMessage3('Only allowed for outsiders')
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var radiology  = {
      radiologyType : {
        id    : this.radiologyTypeId,
        code  : this.radiologyTypeName,
        name  : this.radiologyTypeName,
      },
      diagnosisType : {
        id    : this.diagnosisTypeId,
        code  : this.diagnosisTypeCode,
        name  : this.diagnosisTypeName}   
    }
    this.spinner.show()
    await this.http.post(API_URL+'/patients/save_radiology?consultation_id='+0+'&non_consultation_id='+this.nonConsultationId+'&admission_id='+0, radiology, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      () => {
        this.loadRadiology(0, this.nonConsultationId, 0)
        this.msgBox.showSuccessMessage('Radiology Saved successifully')
      }
    )
    .catch(
      error => {
        this.loadRadiology(0, this.nonConsultationId, 0)
        this.msgBox.showErrorMessage(error, 'Could not save Radiology')
        console.log(error)
      }
    )
    
  }

  async loadRadiology(consultationId : any, nonConsultationId : any, admissionId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.radiologies = []
    this.radiologyTotal = 0
    this.spinner.show()
    await this.http.get<IRadiology[]>(API_URL+'/patients/load_radiologies?consultation_id='+consultationId+'&non_consultation_id='+nonConsultationId+'&admission_id='+admissionId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.radiologyTotal = this.radiologyTotal + element.patientBill.amount
          this.radiologies.push(element)
        })
        console.log(data)
        
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, 'Could not load radiologies')
      }
    )   
  }

  async loadRadiologyTypeNames(){
    this.radiologyTypeNames = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<string[]>(API_URL+'/radiology_types/get_names', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        data?.forEach(element => {
          this.radiologyTypeNames.push(element)
        })
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, 'Could not load radiology types names')
      }
    )
  }

  async loadNonConsultationId(patientId : any){
    if(this.type != 'OUTSIDER'){
      this.nonConsultationId = 0
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.nonConsultationId = 0
    this.spinner.show()
    await this.http.get<number>(API_URL+'/patients/load_non_consultation_id?patient_id='+patientId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.nonConsultationId = data!
      }
        
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, '')
      }
    ) 

  }

  async loadLabTestTypeNames(){
    this.labTestTypeNames = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<string[]>(API_URL+'/lab_test_types/get_names', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        data?.forEach(element => {
          this.labTestTypeNames.push(element)
        })
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, 'Could not load lab test types names')
      }
    )
  }

  async loadDiagnosisTypeNames(){
    this.diagnosisTypeNames = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<string[]>(API_URL+'/diagnosis_types/get_names', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        data?.forEach(element => {
          this.diagnosisTypeNames.push(element)
        })
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, 'Could not load diagnosis types names')
      }
    )
  }

  clearTests(){
    this.labTestTypeName    = ''
    this.diagnosisTypeName  = ''
    this.radiologyTypeName  = ''
    this.procedureTypeName  = ''
  }

  async loadTheatreNames(){
    this.theatreNames = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<string[]>(API_URL+'/theatres/get_names', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        data?.forEach(element => {
          this.theatreNames.push(element)
        })
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, 'Could not load theatre names')
      }
    )
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

  filterSearchKeys(value : string){

    this.searchKeysToDisplay = []
    if(value.length < 4){
      return
    }

    this.searchKeys.forEach(element => {
      var elementToLower = element.toLowerCase()
      var valueToLower = value.toLowerCase()
      if(elementToLower.includes(valueToLower)){
        this.searchKeysToDisplay.push(element)
      }
    })
  }

  public grant(privilege : string[]) : boolean{
    /**Allow user to perform an action if the user has that priviledge */

    var granted : boolean = false
    privilege.forEach(
      element => {
        if(this.auth.checkPrivilege(element)){45
          granted = true
        }
      }
    )
    return granted
  }

  patientId : any =  null
  patientNo : string = ''
  patientFirstName : string = ''
  patientMiddleName : string = ''
  patientLastName : string = ''
  patientPhoneNo : string = ''

  patients : IPatient[] = []
  async loadPatientsLike(value : string){
    this.patients = []
    if(value.length < 3){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.get<IPatient[]>(API_URL+'/patients/load_patients_like?name_like='+value, options)
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.patients = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }
  async getPatient(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.patients = []
    this.spinner.show()
    await this.http.get<IPatient>(API_URL+'/patients/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {
        this.patientId    = data?.id
        this.patientNo = data!.no
        this.patientFirstName = data!.firstName
        this.patientMiddleName = data!.middleName
        this.patientLastName = data!.lastName
        this.patientPhoneNo = data!.phoneNo

        this.searchKey = this.patientFirstName + ' ' +  this.patientMiddleName + ' ' + this.patientLastName + ' | ' + 'File No: '+this.patientNo

        this.id = data!['id']
        this.no = data!['no']
        this.firstName = data!['firstName']
        this.middleName = data!['middleName']
        this.lastName = data!['lastName']
        this.gender = data!['gender']
        this.dateOfBirth =data!['dateOfBirth']
        this.paymentType = data!['paymentType']
        this.type = data!['type']
        this.membershipNo = data!['membershipNo']
        this.phoneNo = data!['phoneNo']
        this.address = data!['address']
        this.email = data!['email']
        this.nationality = data!['nationality']
        this.nationalId = data!['nationalId']
        this.passportNo = data!['passportNo']
        this.kinFullName = data!['kinFullName']
        this.kinRelationship = data!['kinRelationship']
        this.kinPhoneNo = data!['kinPhoneNo']

        this.insurancePlanName = data!['insurancePlan']?.name

        this.lockSearchKey = true
      }
    )
    .catch(
      error => {
        this.clear()
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
    if(this.type === 'OUTPATIENT'){
      await this.loadActiveConsultation(this.id)
    }else if(this.type === 'OUTSIDER'){
      await this.loadNonConsultationId(this.id)
      await this.loadLabTest(0, this.nonConsultationId, 0)
      await this.loadRadiologies(0, this.nonConsultationId, 0)
      await this.loadProcedures(0, this.nonConsultationId, 0)
    }
    await this.getLastVisitDate()  
  }

  nationalities : string[] = [
    "Tanzania",
    "Afghanistan",
    "Albania",
    "Algeria",
    "American Samoa",
    "Andorra",
    "Angola",
    "Anguilla",
    "Antarctica",
    "Antigua and Barbuda",
    "Argentina",
    "Armenia",
    "Aruba",
    "Australia",
    "Austria",
    "Azerbaijan",
    "Bahamas (the)",
    "Bahrain",
    "Bangladesh",
    "Barbados",
    "Belarus",
    "Belgium",
    "Belize",
    "Benin",
    "Bermuda",
    "Bhutan",
    "Bolivia (Plurinational State of)",
    "Bonaire, Sint Eustatius and Saba",
    "Bosnia and Herzegovina",
    "Botswana",
    "Bouvet Island",
    "Brazil",
    "British Indian Ocean Territory (the)",
    "Brunei Darussalam",
    "Bulgaria",
    "Burkina Faso",
    "Burundi",
    "Cabo Verde",
    "Cambodia",
    "Cameroon",
    "Canada",
    "Cayman Islands (the)",
    "Central African Republic (the)",
    "Chad",
    "Chile",
    "China",
    "Christmas Island",
    "Cocos (Keeling) Islands (the)",
    "Colombia",
    "Comoros (the)",
    "Congo (the Democratic Republic of the)",
    "Congo (the)",
    "Cook Islands (the)",
    "Costa Rica",
    "Croatia",
    "Cuba",
    "Curaçao",
    "Cyprus",
    "Czechia",
    "Côte d'Ivoire",
    "Denmark",
    "Djibouti",
    "Dominica",
    "Dominican Republic (the)",
    "Ecuador",
    "Egypt",
    "El Salvador",
    "Equatorial Guinea",
    "Eritrea",
    "Estonia",
    "Eswatini",
    "Ethiopia",
    "Falkland Islands (the) [Malvinas]",
    "Faroe Islands (the)",
    "Fiji",
    "Finland",
    "France",
    "French Guiana",
    "French Polynesia",
    "French Southern Territories (the)",
    "Gabon",
    "Gambia (the)",
    "Georgia",
    "Germany",
    "Ghana",
    "Gibraltar",
    "Greece",
    "Greenland",
    "Grenada",
    "Guadeloupe",
    "Guam",
    "Guatemala",
    "Guernsey",
    "Guinea",
    "Guinea-Bissau",
    "Guyana",
    "Haiti",
    "Heard Island and McDonald Islands",
    "Holy See (the)",
    "Honduras",
    "Hong Kong",
    "Hungary",
    "Iceland",
    "India",
    "Indonesia",
    "Iran (Islamic Republic of)",
    "Iraq",
    "Ireland",
    "Isle of Man",
    "Israel",
    "Italy",
    "Jamaica",
    "Japan",
    "Jersey",
    "Jordan",
    "Kazakhstan",
    "Kenya",
    "Kiribati",
    "Korea (the Democratic People's Republic of)",
    "Korea (the Republic of)",
    "Kuwait",
    "Kyrgyzstan",
    "Lao People's Democratic Republic (the)",
    "Latvia",
    "Lebanon",
    "Lesotho",
    "Liberia",
    "Libya",
    "Liechtenstein",
    "Lithuania",
    "Luxembourg",
    "Macao",
    "Madagascar",
    "Malawi",
    "Malaysia",
    "Maldives",
    "Mali",
    "Malta",
    "Marshall Islands (the)",
    "Martinique",
    "Mauritania",
    "Mauritius",
    "Mayotte",
    "Mexico",
    "Micronesia (Federated States of)",
    "Moldova (the Republic of)",
    "Monaco",
    "Mongolia",
    "Montenegro",
    "Montserrat",
    "Morocco",
    "Mozambique",
    "Myanmar",
    "Namibia",
    "Nauru",
    "Nepal",
    "Netherlands (the)",
    "New Caledonia",
    "New Zealand",
    "Nicaragua",
    "Niger (the)",
    "Nigeria",
    "Niue",
    "Norfolk Island",
    "Northern Mariana Islands (the)",
    "Norway",
    "Oman",
    "Pakistan",
    "Palau",
    "Palestine, State of",
    "Panama",
    "Papua New Guinea",
    "Paraguay",
    "Peru",
    "Philippines (the)",
    "Pitcairn",
    "Poland",
    "Portugal",
    "Puerto Rico",
    "Qatar",
    "Republic of North Macedonia",
    "Romania",
    "Russian Federation (the)",
    "Rwanda",
    "Réunion",
    "Saint Barthélemy",
    "Saint Helena, Ascension and Tristan da Cunha",
    "Saint Kitts and Nevis",
    "Saint Lucia",
    "Saint Martin (French part)",
    "Saint Pierre and Miquelon",
    "Saint Vincent and the Grenadines",
    "Samoa",
    "San Marino",
    "Sao Tome and Principe",
    "Saudi Arabia",
    "Senegal",
    "Serbia",
    "Seychelles",
    "Sierra Leone",
    "Singapore",
    "Sint Maarten (Dutch part)",
    "Slovakia",
    "Slovenia",
    "Solomon Islands",
    "Somalia",
    "South Africa",
    "South Georgia and the South Sandwich Islands",
    "South Sudan",
    "Spain",
    "Sri Lanka",
    "Sudan (the)",
    "Suriname",
    "Svalbard and Jan Mayen",
    "Sweden",
    "Switzerland",
    "Syrian Arab Republic",
    "Taiwan",
    "Tajikistan",
    "Tanzania, United Republic of",
    "Thailand",
    "Timor-Leste",
    "Togo",
    "Tokelau",
    "Tonga",
    "Trinidad and Tobago",
    "Tunisia",
    "Turkey",
    "Turkmenistan",
    "Turks and Caicos Islands (the)",
    "Tuvalu",
    "Uganda",
    "Ukraine",
    "United Arab Emirates (the)",
    "United Kingdom of Great Britain and Northern Ireland (the)",
    "United States Minor Outlying Islands (the)",
    "United States of America (the)",
    "Uruguay",
    "Uzbekistan",
    "Vanuatu",
    "Venezuela (Bolivarian Republic of)",
    "Viet Nam",
    "Virgin Islands (British)",
    "Virgin Islands (U.S.)",
    "Wallis and Futuna",
    "Western Sahara",
    "Yemen",
    "Zambia",
    "Zimbabwe",
    "Åland Islands"
  ];


  labTestTypeId : any =  null
  labTestTypeCode : string = ''
  labTestTypeName : string = ''
  labTestTypes : ILabTestType[] = []
  async loadLabTestTypesLike(value : string){
    this.labTestTypes = []
    if(value.length < 3){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.labTestTypes = []
    await this.http.get<ILabTestType[]>(API_URL+'/lab_test_types/load_lab_test_types_like?name_like='+value, options)
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.labTestTypes = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }
  async getLabTestType(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.labTestTypes = []
    this.spinner.show()
    await this.http.get<ILabTestType>(API_URL+'/lab_test_types/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {
        this.labTestTypeId    = data?.id
        this.labTestTypeCode  = data!.code
        this.labTestTypeName  = data!.name
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }

  radiologyTypeId : any =  null
  radiologyTypeCode : string = ''
  radiologyTypeName : string = ''
  radiologyTypes : IRadiologyType[] = []
  async loadRadiologyTypesLike(value : string){
    this.radiologyTypes = []
    if(value.length < 3){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.radiologyTypes = []
    await this.http.get<IRadiologyType[]>(API_URL+'/radiology_types/load_radiology_types_like?name_like='+value, options)
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.radiologyTypes = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }
  async getRadiologyType(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.radiologyTypes = []
    this.spinner.show()
    await this.http.get<IProcedureType>(API_URL+'/radiology_types/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {
        this.radiologyTypeId = data?.id
        this.radiologyTypeCode = data!.code
        this.radiologyTypeName = data!.name
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }

  diagnosisTypeId : any =  null
  diagnosisTypeCode : string = ''
  diagnosisTypeName : string = ''
  diagnosisTypes : IDiagnosisType[] = []
  async loadDiagnosisTypesLike(value : string){
    this.diagnosisTypes = []
    if(value.length < 3){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.get<IDiagnosisType[]>(API_URL+'/diagnosis_types/load_diagnosis_types_like?name_like='+value, options)
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.diagnosisTypes = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }
  async getDiagnosisType(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.diagnosisTypes = []
    this.spinner.show()
    await this.http.get<IProcedureType>(API_URL+'/diagnosis_types/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {
        this.diagnosisTypeId = data?.id
        this.diagnosisTypeCode = data!.code
        this.diagnosisTypeName = data!.name
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }

  consultationTransfers : IConsultationTransfer[] = []
  async loadTransfers(){//for unpaid registration
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.consultationTransfers = []
    this.spinner.show()
    await this.http.get<IConsultationTransfer[]>(API_URL+'/patients/get_consultation_transfers', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.consultationTransfers = data!
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  processTransfer(patientId : any, clinicName : string | undefined){
    this.getPatient(patientId)
    this.clinicName = clinicName!
    this.loadClinicianNames(clinicName!)
  }

}

export interface IPaymentType{
  name : string
  insurancePlanName : string
  insuranceMembershipNo : string
}
