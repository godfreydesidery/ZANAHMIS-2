import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IPatient } from 'src/app/domain/patient';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { ShowTimePipe } from 'src/app/pipes/show_time.pipe';
import { ShowUserPipe } from 'src/app/pipes/show_user.pipe';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-patient-payment',
  templateUrl: './patient-payment.component.html',
  styleUrls: ['./patient-payment.component.scss'],
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
export class PatientPaymentComponent implements OnInit {
  id : any
  no : string
  firstName : string
  middleName : string
  lastName : string
  dateOfBirth! :Date
  gender : string
  paymentType : string
  memberShipNo : string
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
  insurancePlan : string = ''

  registrationFee : number = 0
  registrationFeeStatus = ''
  cardValidationStatus = ''

  filterRecords : string = ''

  constructor(
    //private shortcut : ShortCutHandlerService,
              private auth : AuthService,
              private http : HttpClient,
              private modalService: NgbModal,
              private spinner: NgxSpinnerService
  ) {
    this.id = null
    this.no = ''
    this.firstName = ''
    this.middleName = ''
    this.lastName = ''
    this.gender = ''
    this.paymentType = ''
    this.memberShipNo = ''
    this.phoneNo = ''
    this.address = ''
    this.email = ''
    this.nationality = ''
    this.nationalId = ''
    this.passportNo = ''
    this.kinFullName = ''
    this.kinRelationship = ''
    this.kinPhoneNo = ''
  }

  ngOnInit(): void {
  }

  clear(){
    this.id = null
    this.no = ''
    this.firstName = ''
    this.middleName = ''
    this.lastName = ''
    this.gender = ''
    this.paymentType = ''
    this.memberShipNo = ''
    this.phoneNo = ''
    this.address = ''
    this.email = ''
    this.nationality = ''
    this.nationalId = ''
    this.passportNo = ''
    this.kinFullName = ''
    this.kinRelationship = ''
    this.kinPhoneNo = ''
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

  changePaymentType(paymentType : string){
    this.paymentType = paymentType
  }

  searchPatient(regNo : string){
    this.id = 1
  }

  async registerPatient(){
    



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
      dateOfBirth         : this.dateOfBirth,
      paymentType         : this.paymentType,
      memberShipNo        : this.memberShipNo,
      phoneNo             : this.phoneNo,
      address             : this.address,
      email               : this.email,
      nationality         : this.nationality,
      nationalId          : this.nationalId,
      passportNo          : this.passportNo,
      kinFullName         : this.kinFullName,
      kinRelationship     : this.kinRelationship,
      kinPhoneNo          : this.kinPhoneNo,
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
          alert('Patient Registered successifully. Please note patients File No')

          this.id = data!['id']
          this.no = data!['no']
          this.firstName = data!['firstName']
          this.middleName = data!['middleName']
          this.lastName = data!['lastName']
          this.gender = data!['gender']
          this.paymentType = data!['paymentType']
          this.phoneNo = data!['phoneNo']
          this.address = data!['address']
          this.email = data!['email']
          this.nationality = data!['nationality']
          this.nationalId = data!['nationalId']
          this.passportNo = data!['passportNo']
          this.kinFullName = data!['kinFullName']
          this.kinRelationship = data!['kinRelationship']
          this.kinPhoneNo = data!['kinPhoneNo']

          this.patientRecordMode = ''
        }
      )
      .catch(
        error => {
          console.log(error)
          alert('Could not register patient')
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

export interface IPatient1 {
  id : any
  no : string
  firstName : string
  middleName : string
  lastName : string
  dateOfBirth :Date
  gender : string
  paymentType : string
  memberShipNo : string
  phoneNo : string		
	address : string
	email : string
	nationality : string
	nationalId : string	
	passportNo : string
  kinFullName : string
	kinRelationship : string
	kinPhoneNo : string
  patientRecordMode : string
  paymentMode : string
  insurancePlan : string 

  registrationFee : number
  registrationFeeStatus : string
  cardValidationStatus : string
}
