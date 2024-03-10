import { CommonModule, formatDate, Time } from '@angular/common';
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
import { ILabTest } from 'src/app/domain/lab-test';
import { IConsultation } from 'src/app/domain/consultation';
import { IRadiology } from 'src/app/domain/radiology';
import { IClinician } from 'src/app/domain/clinician';
import { ICollection } from 'src/app/domain/collection';
import { IUser } from 'src/app/domain/user';
import { ILabTestCollection } from 'src/app/models/labTestCollection';
import { IRadiologyCollection } from 'src/app/models/radiologyCollection';
import { IProcedureCollection } from 'src/app/models/procedureCollection';
import { IConsultationCollection } from 'src/app/models/consultationCollection';
import { IRegistrationCollection } from 'src/app/models/registrationCollection';
import { IAdmissionBedCollection } from 'src/app/models/admissionBedCollection';
var pdfFonts = require('pdfmake/build/vfs_fonts.js'); 
const fs = require('file-saver');

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-collections-report',
  templateUrl: './collections-report.component.html',
  styleUrls: ['./collections-report.component.scss'],
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    ShowDateTimePipe,
    ShowDateOnlyPipe,
    AgePipe,
    RouterLink
  ],
})
export class CollectionsReportComponent {

  address  : any 

  report : string[] = []

  filterRecords : string = ''

  logo!    : any
  documentHeader! : any

  startDate! : Date
  endDate! : Date

  startTime! : Time
  endTime! : Time


  collections : ICollection[] = []

  

  reportType : string = ''

  totalAmount : number = 0

  nickname : string = '--All Cashiers--'

  constructor(
    private auth : AuthService,
    private http : HttpClient,
    private spinner: NgxSpinnerService,
    private msgBox : MsgBoxService,
    private data : DataService,
              
  ) {(window as any).pdfMake.vfs = pdfFonts.pdfMake.vfs;}

  ngOnInit(): void {
    this.loadUsers()
  }


  runReport(){
    this.loadReport(this.startDate, this.endDate)

    this.loadRegistrationCollectionReport(this.startDate, this.endDate)
    this.loadConsultationCollectionReport(this.startDate, this.endDate)
    this.loadLabTestCollectionReport(this.startDate, this.endDate)
    this.loadRadiologyCollectionReport(this.startDate, this.endDate)
    this.loadProcedureCollectionReport(this.startDate, this.endDate)
    this.loadPrescriptionCollectionReport(this.startDate, this.endDate)
    this.loadAdmissionBedCollectionReport(this.startDate, this.endDate)

  }

  async loadReport(from : Date, to : Date){   
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
      from : from,
      to   : to,
      user : {nickname : this.nickname}
    }
    if(args.user.nickname === '--All Cashiers--'){
      args.user.nickname = ''
    }

    this.spinner.show()
    await this.http.post<ICollection[]>(API_URL+'/reports/collections_report', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        
        this.collections = data!
        var sn = 1
        this.totalAmount = 0
        this.collections.forEach(element => {
          this.totalAmount = this.totalAmount + element.amount
          element.sn = sn
          sn = sn + 1

        })
        console.log(this.collections)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }





  clear(){
    this.collections = []
    this.userId = null
    this.userName = ''
    this.userCode = ''
    this.users = []
    this.userLocked = false
    this.collections = []
    this.totalAmount = 0
  }

  userLocked : boolean = false
  userId : any =  null
  userCode : string = ''
  userName : string = ''
  users : IUser[] = []
  async loadUsersLike(value : string){
    this.users = []
    if(value.length < 2){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.get<IUser[]>(API_URL+'/users/load_users_like?name_like='+value, options)
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.users = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async loadUsers(){
    this.users = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.get<IUser[]>(API_URL+'/users', options)
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.users = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async getUser(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.users = []
    this.spinner.show()
    await this.http.get<IUser>(API_URL+'/users/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {
        this.userId = data?.id
        this.userCode = data!.code
        this.userName = data!.firstName + ' ' + data!.middleName + ' ' + data!.lastName
        this.userLocked = true
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }





  runLabTestCollectionReport(){
    this.loadLabTestCollectionReport(this.startDate, this.endDate)
  }
  labTestTotal : number = 0
  labTestCollections : ILabTestCollection[] = []
  async loadLabTestCollectionReport(from : Date, to : Date){   
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
      from : from,
      to   : to,
      user : {nickname : this.nickname}
    }
    if(args.user.nickname === '--All Cashiers--'){
      args.user.nickname = ''
    }

    this.spinner.show()
    await this.http.post<ILabTestCollection[]>(API_URL+'/reports/lab_test_collection_report', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        
        this.labTestCollections = data!
        var sn = 1
        this.labTestTotal = 0
        this.labTestCollections.forEach(element => {
         // this.totalAmount = this.totalAmount + element.amount
          element.sn = sn
          sn = sn + 1
          this.labTestTotal = this.labTestTotal + element.patientBill.amount

        })
        console.log(this.labTestCollections)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }



  runRadiologyCollectionReport(){
    this.loadLabTestCollectionReport(this.startDate, this.endDate)
  }
  radiologyTotal : number = 0
  radiologyCollections : IRadiologyCollection[] = []
  async loadRadiologyCollectionReport(from : Date, to : Date){   
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
      from : from,
      to   : to,
      user : {nickname : this.nickname}
    }
    if(args.user.nickname === '--All Cashiers--'){
      args.user.nickname = ''
    }

    this.spinner.show()
    await this.http.post<IRadiologyCollection[]>(API_URL+'/reports/radiology_collection_report', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        
        this.radiologyCollections = data!
        var sn = 1
        this.radiologyTotal = 0
        this.radiologyCollections.forEach(element => {
         // this.totalAmount = this.totalAmount + element.amount
          element.sn = sn
          sn = sn + 1
          this.radiologyTotal = this.radiologyTotal + element.patientBill.amount

        })
        console.log(this.radiologyCollections)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }



  runProcedureCollectionReport(){
    this.loadProcedureCollectionReport(this.startDate, this.endDate)
  }
  procedureTotal : number = 0
  procedureCollections : IProcedureCollection[] = []
  async loadProcedureCollectionReport(from : Date, to : Date){   
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
      from : from,
      to   : to,
      user : {nickname : this.nickname}
    }
    if(args.user.nickname === '--All Cashiers--'){
      args.user.nickname = ''
    }

    this.spinner.show()
    await this.http.post<IProcedureCollection[]>(API_URL+'/reports/procedure_collection_report', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        
        this.procedureCollections = data!
        var sn = 1
        this.procedureTotal = 0
        this.procedureCollections.forEach(element => {
         // this.totalAmount = this.totalAmount + element.amount
          element.sn = sn
          sn = sn + 1
          this.procedureTotal = this.procedureTotal + element.patientBill.amount

        })
        console.log(this.procedureCollections)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }


  runPrescriptionCollectionReport(){
    this.loadPrescriptionCollectionReport(this.startDate, this.endDate)
  }
  prescriptionTotal : number = 0
  prescriptionCollections : IProcedureCollection[] = []
  async loadPrescriptionCollectionReport(from : Date, to : Date){   
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
      from : from,
      to   : to,
      user : {nickname : this.nickname}
    }
    if(args.user.nickname === '--All Cashiers--'){
      args.user.nickname = ''
    }

    this.spinner.show()
    await this.http.post<IProcedureCollection[]>(API_URL+'/reports/prescription_collection_report', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        
        this.prescriptionCollections = data!
        var sn = 1
        this.prescriptionTotal = 0
        this.prescriptionCollections.forEach(element => {
         // this.totalAmount = this.totalAmount + element.amount
          element.sn = sn
          sn = sn + 1
          this.prescriptionTotal = this.prescriptionTotal + element.patientBill.amount

        })
        console.log(this.prescriptionCollections)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }


  runConsultationCollectionReport(){
    this.loadConsultationCollectionReport(this.startDate, this.endDate)
  }
  consultationTotal : number = 0
  consultationCollections : IConsultationCollection[] = []
  async loadConsultationCollectionReport(from : Date, to : Date){   
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
      from : from,
      to   : to,
      user : {nickname : this.nickname}
    }
    if(args.user.nickname === '--All Cashiers--'){
      args.user.nickname = ''
    }

    this.spinner.show()
    await this.http.post<IConsultationCollection[]>(API_URL+'/reports/consultation_collection_report', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        
        this.consultationCollections = data!
        var sn = 1
        this.consultationTotal = 0
        this.consultationCollections.forEach(element => {
         // this.totalAmount = this.totalAmount + element.amount
          element.sn = sn
          sn = sn + 1
          this.consultationTotal = this.consultationTotal + element.patientBill.amount

        })
        console.log(this.consultationCollections)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }


  runRegistrationCollectionReport(){
    this.loadRegistrationCollectionReport(this.startDate, this.endDate)
  }
  registrationTotal : number = 0
  registrationCollections : IRegistrationCollection[] = []
  async loadRegistrationCollectionReport(from : Date, to : Date){   
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
      from : from,
      to   : to,
      user : {nickname : this.nickname}
    }
    if(args.user.nickname === '--All Cashiers--'){
      args.user.nickname = ''
    }

    this.spinner.show()
    await this.http.post<IRegistrationCollection[]>(API_URL+'/reports/registration_collection_report', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        
        this.registrationCollections = data!
        var sn = 1
        this.registrationTotal = 0
        this.registrationCollections.forEach(element => {
         // this.totalAmount = this.totalAmount + element.amount
          element.sn = sn
          sn = sn + 1
          this.registrationTotal = this.registrationTotal + element.patientBill.amount

        })
        console.log(this.registrationCollections)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  runWardAdmissionCollectionReport(){
    this.loadAdmissionBedCollectionReport(this.startDate, this.endDate)
  }
  admissionBedTotal : number = 0
  admissionBedCollections : IAdmissionBedCollection[] = []
  async loadAdmissionBedCollectionReport(from : Date, to : Date){   
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
      from : from,
      to   : to,
      user : {nickname : this.nickname}
    }
    if(args.user.nickname === '--All Cashiers--'){
      args.user.nickname = ''
    }

    this.spinner.show()
    await this.http.post<IAdmissionBedCollection[]>(API_URL+'/reports/admission_bed_collection_report', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        
        this.admissionBedCollections = data!
        var sn = 1
        this.admissionBedTotal = 0
        this.admissionBedCollections.forEach(element => {
         // this.totalAmount = this.totalAmount + element.amount
          element.sn = sn
          sn = sn + 1
          this.admissionBedTotal = this.admissionBedTotal + element.patientBill.amount

        })
        console.log(this.admissionBedCollections)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }


}
