import { CommonModule, formatDate } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IPatient } from 'src/app/domain/patient';
import { IPatientBill } from 'src/app/domain/patient-bill';
import { ReceiptItem } from 'src/app/domain/receipt-item';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { ShowDateTimePipe } from 'src/app/pipes/date_time.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { ShowTimePipe } from 'src/app/pipes/show_time.pipe';
import { ShowUserPipe } from 'src/app/pipes/show_user.pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { PosReceiptPrinterService } from 'src/app/services/pos-receipt-printer.service';
import { environment } from 'src/environments/environment';

import { HttpClientModule } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerModule } from 'ngx-spinner';
import { Workbook } from 'exceljs';
import { DataService } from 'src/app/services/data.service';


import * as pdfMake from 'pdfmake/build/pdfmake';
import { IProcedure } from 'src/app/domain/procedure';
import { ShowDateOnlyPipe } from 'src/app/pipes/date.pipe';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from 'src/app/app-routing.module';
var pdfFonts = require('pdfmake/build/vfs_fonts.js'); 
const fs = require('file-saver');

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-patient-report',
  templateUrl: './patient-report.component.html',
  styleUrls: ['./patient-report.component.scss'],
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    ShowDateTimePipe,
    AgePipe,
    RouterLink
  ],
})
export class PatientReportComponent {

  //searchKeys : string[] = []
  searchKey : string = ''

  id : any = null
  no : string = ''
  firstName : string = ''
  middleName : string = ''
  lastName : string = ''
  patientType : string = ''
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

  
  registrationAmount : number = 0
  consultationAmount : number = 0


  total : number = 0

  amountReceived : number = 0

  lockSearchKey : boolean = false

  filterRecords : string = ''

  patientBills : IPatientBill[] = []

  constructor(
              private auth : AuthService,
              private http : HttpClient,
              private spinner: NgxSpinnerService,
              private msgBox : MsgBoxService,
              private printer : PosReceiptPrinterService,
              private data : DataService) 
              {(window as any).pdfMake.vfs = pdfFonts.pdfMake.vfs;}
  

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
    this.consultationAmount = 0
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
    this.patientBills = []
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

  from! : Date
  to! : Date
  logo!    : any
  documentHeader! : any
  async loadPatientBillsByDate(from : Date, to : Date){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    if(from === undefined || to === undefined){
      this.msgBox.showErrorMessage3('Could not run. Please select date range')
      return
    }

    if(from > to){
      this.msgBox.showErrorMessage3('Could not run. Start date must be earlier or equal to end date')
      return
    }

    var args = {
      patient : {id : this.id},
      from : from,
      to : to
    }

    this.spinner.show()
    await this.http.post<IPatientBill[]>(API_URL+'/reports/get_patient_bills_by_date', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.patientBills = data!
      }
    )
    .catch(
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  getAmount(patientBill : IPatientBill) : string{
    if(patientBill.status === 'COVERED'){
      return 'Covered'
    }
    return patientBill.amount.toLocaleString('en-US', { minimumFractionDigits: 2 })
  }

  getBillTo(patientBill : IPatientBill) : string{
    if(patientBill.status === 'COVERED'){
      return patientBill?.insurancePlan?.name
    }
    return 'Client'
  }

  getTotal(amount : number) : string{
    if(amount === 0){
      return 'Covered'
    }
    return amount.toLocaleString('en-US', { minimumFractionDigits: 2 })
  }

  print = async () => {

    if(this.patientBills.length === 0){
      this.msgBox.showErrorMessage3('No data to export')
      return
    }

    this.documentHeader = await this.data.getDocumentHeader()
    var header = ''
    var footer = ''
    var title  = 'Patient Report'
    var logo : any = ''
    var total : number = 0
    var discount : number = 0
    var tax : number = 0

    var report = [
      [
        {text : 'SN', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Date', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Service/Item', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Qty', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Amount/Cov', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Billed To', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Status', fontSize : 9, fillColor : '#bdc6c7'},

      ]
    ]  

    var sn : number = 1

    var total = 0

    
    
    this.patientBills.forEach((element) => {
      var detail = [
        {text : sn.toString(), fontSize : 9, fillColor : '#ffffff'}, 
        {text : new ShowDateOnlyPipe().transform(element?.createdAt).toString(), fontSize : 9, fillColor : '#ffffff'},
        {text : element?.description, fontSize : 9, fillColor : '#ffffff'},
        {text : element?.qty.toString(), fontSize : 9, fillColor : '#ffffff'},
        {text : this.getAmount(element), fontSize : 9, alignment : 'right', fillColor : '#ffffff'},
        {text : this.getBillTo(element), fontSize : 9, fillColor : '#ffffff'},
        {text : element?.status, fontSize : 9, fillColor : '#ffffff'},
      ]
      sn = sn + 1
      if(element.status != 'COVERED'){
        total = total + element.amount
      }
      report.push(detail)
    })

    var detailSummary = [
      {text : '', fontSize : 9, fillColor : '#ffffff'}, 
      {text : '', fontSize : 9, fillColor : '#ffffff'},
      {text : 'Total', fontSize : 9, fillColor : '#ffffff'},
      {text : '', fontSize : 9, fillColor : '#ffffff'},
      {text : this.getTotal(total), fontSize : 9, alignment : 'right', bold : true, fillColor : '#ffffff'},
      {text : '', fontSize : 9, fillColor : '#ffffff'},
      {text : '', fontSize : 9, fillColor : '#ffffff'},
    ]
    report.push(detailSummary)
   
    const docDefinition : any = {
      header: '',
      footer: function (currentPage: { toString: () => string; }, pageCount: string) {
        return currentPage.toString() + " of " + pageCount;
      },
      //watermark : { text : '', color: 'blue', opacity: 0.1, bold: true, italics: false },
        content : [
          {
            columns : 
            [
              this.documentHeader
            ]
          },
          '  ',
          {text : title, fontSize : 14, bold : true, alignment : 'center'},
          this.data.getHorizontalLine(),

          {text : 'Name: '+this.firstName.toString() + ' ' +this.middleName?.toString() + ' ' +this.lastName?.toString(), fontSize : 9},
          {text : 'File No: '+this.no.toString(), fontSize : 9}, 
          {text : 'Date of Birth: '+  formatDate(this.dateOfBirth, 'yyyy-MM-dd', 'en-US'), fontSize : 9}, 
          {text : 'Mobile: '+this.phoneNo.toString(), fontSize : 9},
          ' ',
          {
            layout : 'noBorders',
            table : {
              widths : [80, 80],
              body : [
                [
                  {text : 'From: '+this.from.toString(), fontSize : 9}, 
                  {text : 'To: '+this.to.toString(), fontSize : 9} 
                ],
              ]
            },
          },
          '  ',
          {
            //layout : 'noBorders',
            table : {
                headerRows : 1,
                widths : [20, 50, 130, 20, 70, 100, 50],
                body : report
            }
        }, 
      ]     
    };
    pdfMake.createPdf(docDefinition).print()
  }
}
