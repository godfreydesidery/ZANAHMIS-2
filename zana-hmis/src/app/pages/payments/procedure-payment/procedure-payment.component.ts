import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IPatient } from 'src/app/domain/patient';
import { ReceiptItem } from 'src/app/domain/receipt-item';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { ShowTimePipe } from 'src/app/pipes/show_time.pipe';
import { ShowUserPipe } from 'src/app/pipes/show_user.pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { PosReceiptPrinterService } from 'src/app/services/pos-receipt-printer.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-procedure-payment',
  templateUrl: './procedure-payment.component.html',
  styleUrls: ['./procedure-payment.component.scss'],
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
export class ProcedurePaymentComponent implements OnInit {
  searchKey : string = ''

  id : any = null
  no : string = ''
  firstName : string = ''
  middleName : string = ''
  lastName : string = ''
  type : string = ''
  dateOfBirth! :Date
  gender : string = ''
  paymentType : string = ''
  memberShipNo : string = ''
  phoneNo : string = ''		
	address : string = ''
	email : string = ''
	nationality : string = ''
	nationalId : string = ''	
	passportNo : string = ''
  kinFullName : string = ''
	kinRelationship : string = ''
	kinPhoneNo : string = ''


  patientRecordMode : string = ''
  insurancePlan : string = ''

  registrationFee : number = 0
  registrationFeeStatus = ''
  cardValidationStatus = ''

  registrationBill! : IBill
  consultationBill! : IBill
  
  procedureBills : IBill[] = []

  registrationAmount : number = 0
  consultationAmount : number = 0


  total : number = 0

  amountReceived : number = 0

  lockSearchKey : boolean = false

  filterRecords : string = ''

  constructor(
              private auth : AuthService,
              private http : HttpClient,
              private spinner: NgxSpinnerService,
              private msgBox : MsgBoxService,
              private printer : PosReceiptPrinterService) 
              { }
  

  ngOnInit(): void {
    //this.loadSearchKeys()
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

  searchKeysToDisplay : string[] = []
  searchKeys : string[] = []
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



  async searchBySearchKey(key : string): Promise<void> {
    
    var searchElement = ''
    this.registrationAmount = 0
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

        this.id = data!['id']
        this.no = data!['no']
        this.firstName = data!['firstName']
        this.middleName = data!['middleName']
        this.lastName = data!['lastName']
        this.dateOfBirth = data!['dateOfBirth']
        this.phoneNo = data!['phoneNo']
        this.address = data!['address']

        this.lockSearchKey = true

        this.total = 0
        this.loadRegistrationBill()
        this.loadProcedureBills()       
      }
    )
    .catch(
      error => {
        console.log(error)
        this.clear()
        this.msgBox.showErrorMessage(error, 'Could not find patient')
        return
      }
    )
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
    this.amountReceived = 0
    this.total = 0

    this.lockSearchKey = false
    
    this.patients = []
  }

  reset(){
    this.searchKey = ''
    this.clear()
  }

  async loadRegistrationBill(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    this.spinner.show()
    await this.http.get<IBill>(API_URL+'/bills/get_registration_bill?patient_id='+this.id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.registrationBill = data! 
        if(this.registrationBill != null) {
          this.registrationAmount = this.registrationBill.amount
        }
        this.total = this.total + this.registrationAmount       
      }
    )
    .catch(
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, 'Could not load registration bill')
      }
    )
  }

  async loadConsultationBills(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    this.spinner.show()
    await this.http.get<IBill>(API_URL+'/bills/get_consultation_bill?patient_id='+this.id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.consultationBill = data! 
        if(this.consultationBill != null) {
          this.consultationAmount = this.consultationBill.amount
        }
        this.total = this.total + this.consultationAmount  
      }
    )
    .catch(
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, 'Could not load consultation bill')
      }
    )
  }

  async loadProcedureBills(){
    this.procedureBills = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    this.spinner.show()
    await this.http.get<IBill[]>(API_URL+'/bills/get_procedure_bills?patient_id='+this.id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.procedureBills = data! 
        this.procedureBills.forEach(element => {
          this.total = this.total + element.amount
        })      
      }
    )
    .catch(
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, 'Could not load procedure bill')
      }
    )
  }

  billsToPrint : IBill[] = []
  async confirmBillsPayment(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var bills : IBill[] = []
    /**
     * Add registration bill
     */
    if(this.registrationBill){
      bills.push(this.registrationBill)
    }
    /**
     * Add consultation bills
     */
    if(this.consultationBill){
      bills.push(this.consultationBill)
    }
    /**
     * Add procedure bills
     */
     this.procedureBills.forEach(element => {
      bills.push(element)
    })
   
    this.billsToPrint = bills
    console.log(bills)

    this.spinner.show()
    await this.http.post<IBill>(API_URL+'/bills/confirm_bills_payment?total_amount='+this.total, bills, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.msgBox.showSuccessMessage('Payment successiful')
        var temp = this.searchKey
        this.clear()
        this.searchKey = temp
        this.searchBySearchKey(this.searchKey)

        this.printReceipt()
        
      }
    )
    .catch(
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, 'Could not confirm payment')
      }
    )
  }

  printReceipt(){
    var items : ReceiptItem[] = []
    var item : ReceiptItem

    this.billsToPrint.forEach(element => {
      item = new ReceiptItem()
      item.code = element.id
      item.name = element.description
      item.amount = element.amount
      item.qty = element.qty
      items.push(item)
    })

    this.printer.print(items, 'NA', 0, this.patient)

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
  patient! : IPatient
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

        this.patient = data!

        this.patientId    = data?.id
        this.patientNo = data!.no
        this.patientFirstName = data!.firstName
        this.patientMiddleName = data!.middleName
        this.patientLastName = data!.lastName
        this.patientPhoneNo = data!.phoneNo

        this.searchKey = 'Name: '+ this.patientFirstName + ' ' +  this.patientMiddleName + ' ' + this.patientLastName + ' ' + 'File No: '+this.patientNo

        this.id = data!['id']
        this.no = data!['no']
        this.firstName = data!['firstName']
        this.middleName = data!['middleName']
        this.lastName = data!['lastName']
        this.gender = data!['gender']
        this.dateOfBirth =data!['dateOfBirth']
        this.paymentType = data!['paymentType']
        //this.type = data!['type']
        //this.membershipNo = data!['membershipNo']
        this.phoneNo = data!['phoneNo']
        this.address = data!['address']
        this.email = data!['email']
        this.nationality = data!['nationality']
        this.nationalId = data!['nationalId']
        this.passportNo = data!['passportNo']
        this.kinFullName = data!['kinFullName']
        this.kinRelationship = data!['kinRelationship']
        this.kinPhoneNo = data!['kinPhoneNo']

        //this.insurancePlanName = data!['insurancePlan']?.name

        this.lockSearchKey = true

        this.total = 0
        this.loadRegistrationBill()
        this.loadConsultationBills()
        this.loadProcedureBills() 
      }
    )
    .catch(
      error => {
        this.clear()
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
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
  type : string
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
}

export interface IBill{
  id : any
  qty : number
  amount : number
  description : string
}