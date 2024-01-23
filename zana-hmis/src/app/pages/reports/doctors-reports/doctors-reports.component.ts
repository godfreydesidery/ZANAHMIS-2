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
var pdfFonts = require('pdfmake/build/vfs_fonts.js'); 
const fs = require('file-saver');

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-doctors-reports',
  templateUrl: './doctors-reports.component.html',
  styleUrls: ['./doctors-reports.component.scss'],
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
export class DoctorsReportsComponent {

  address  : any 

  report : string[] = []

  filterRecords : string = ''

  logo!    : any
  documentHeader! : any

  startDate! : Date
  endDate! : Date

  startTime! : Time
  endTime! : Time


  consultations : IConsultation[] = []
  labTests : ILabTest[] = []
  radiologies : IRadiology[] = []
  procedures : IProcedure[] = []

  reportType : string = ''

  constructor(
    private auth : AuthService,
    private http : HttpClient,
    private spinner: NgxSpinnerService,
    private msgBox : MsgBoxService,
    private data : DataService,
              
  ) {(window as any).pdfMake.vfs = pdfFonts.pdfMake.vfs;}


  async ngOnInit(): Promise<void> {
    
  }

  runReport(){
    if(this.reportType === 'Consultation'){
      this.loadConsultationReport(this.startDate, this.endDate)
    }
    if(this.reportType === 'Laboratory'){
      this.loadLabTestReport(this.startDate, this.endDate)
    }
    if(this.reportType === 'Radiology'){
      this.loadRadiologyReport(this.startDate, this.endDate)
    }
    if(this.reportType === 'Procedure'){
      this.loadProcedureReport(this.startDate, this.endDate)
    }
  }


  async loadConsultationReport(from : Date, to : Date){   
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

    if(this.clinicianId === null){
      this.msgBox.showErrorMessage3('Could not run. Please select Doctor')
    }

    var args = {
      from : from,
      to   : to,
      clinician : {id : this.clinicianId}
    }

    this.spinner.show()
    await this.http.post<IConsultation[]>(API_URL+'/reports/consultation_report', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        
        this.consultations = data!
        var sn = 1
        this.consultations.forEach(element => {
          element.sn = sn
          sn = sn + 1
        })
        console.log(this.consultations)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async loadLabTestReport(from : Date, to : Date){   
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

    if(this.clinicianId === null){
      this.msgBox.showErrorMessage3('Could not run. Please select Doctor')
    }

    var args = {
      from : from,
      to   : to,
      clinician : {id : this.clinicianId}
    }

    this.spinner.show()
    await this.http.post<ILabTest[]>(API_URL+'/reports/doctor_to_laboratory_report', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        
        this.labTests = data!
        var sn = 1
        this.labTests.forEach(element => {
          element.sn = sn
          sn = sn + 1
        })
        console.log(this.labTests)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async loadRadiologyReport(from : Date, to : Date){   
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

    if(this.clinicianId === null){
      this.msgBox.showErrorMessage3('Could not run. Please select Doctor')
    }

    var args = {
      from : from,
      to   : to,
      clinician : {id : this.clinicianId}
    }

    this.spinner.show()
    await this.http.post<IRadiology[]>(API_URL+'/reports/doctor_to_radiology_report', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        
        this.radiologies = data!
        var sn = 1
        this.radiologies.forEach(element => {
          element.sn = sn
          sn = sn + 1
        })
        console.log(this.radiologies)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async loadProcedureReport(from : Date, to : Date){   
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

    if(this.clinicianId === null){
      this.msgBox.showErrorMessage3('Could not run. Please select Doctor')
    }

    var args = {
      from : from,
      to   : to,
      clinician : {id : this.clinicianId}
    }

    this.spinner.show()
    await this.http.post<IProcedure[]>(API_URL+'/reports/procedure_report', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        
        this.procedures = data!
        var sn = 1
        this.procedures.forEach(element => {
          element.sn = sn
          sn = sn + 1
        })
        console.log(this.procedures)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  clear(){
    this.consultations = []
    this.labTests = []
    this.radiologies = []
    this.procedures = []
    this.clinicianId = null
    this.clinicianName = ''
    this.clinicianCode = ''
    this.clinicians = []
    this.clinicianLocked = false
  }

  clinicianLocked : boolean = false
  clinicianId : any =  null
  clinicianCode : string = ''
  clinicianName : string = ''
  clinicians : IClinician[] = []
  async loadCliniciansLike(value : string){
    this.clinicians = []
    if(value.length < 2){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.get<IClinician[]>(API_URL+'/clinicians/load_clinicians_like?name_like='+value, options)
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.clinicians = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }
  async getClinician(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.clinicians = []
    this.spinner.show()
    await this.http.get<IClinician>(API_URL+'/clinicians/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {
        this.clinicianId = data?.id
        this.clinicianCode = data!.code
        this.clinicianName = data!.firstName + ' ' + data!.middleName + ' ' + data!.lastName
        this.clinicianLocked = true
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }






  print(){
    if(this.reportType === ''){
      this.msgBox.showErrorMessage3('Please select report to print')
      return
    }
    if(this.reportType === 'Consultation'){
      this.printConsultationReport()
    }
    if(this.reportType === 'Laboratory'){
      this.printLabTestReport()
    }
    if(this.reportType === 'Radiology'){
      this.printRadiologyReport()
    }
    if(this.reportType === 'Procedure'){
      this.printProcedureReport()
    }
  }


  printConsultationReport = async () => {

   
    if(this.consultations.length === 0){
      this.msgBox.showErrorMessage3('No data to print')
      return
    }

    this.documentHeader = await this.data.getDocumentHeader()
    var header = ''
    var footer = ''
    var title  = 'Consultations Report'
    var logo : any = ''
    var total : number = 0
    var discount : number = 0
    var tax : number = 0

    var report = [
      [
        {text : 'SN', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Patient Name', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Phone', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Reg#', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Mode', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Date', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Status', fontSize : 9, fillColor : '#bdc6c7'},
      ]
    ]  
    
    this.consultations.forEach((element) => {
      var detail = [
        {text : element?.sn.toString(), fontSize : 7, fillColor : '#ffffff'},  
        {text : (element?.patient?.firstName +' '+ element.patient?.middleName +' '+ element.patient?.lastName).toString(), fontSize : 7, fillColor : '#ffffff'}, 
        {text : element?.patient?.phoneNo.toString(), fontSize : 7, fillColor : '#ffffff'}, 
        {text : element?.patient?.no.toString(), fontSize : 7, fillColor : '#ffffff'},  
        {text : this.getConsultationPaymentType(element), fontSize : 7, fillColor : '#ffffff'}, 
        {text : new ShowDateOnlyPipe().transform(element?.createdAt), fontSize : 7, fillColor : '#ffffff'}, 
        {text : element?.status.toString(), fontSize : 7, fillColor : '#ffffff'}, 
      ]
      report.push(detail)
    })
   
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
          ' ',
          'Doctor: ' + this.clinicianName,
          ' ',
          {
            layout : 'noBorders',
            table : {
              widths : [80, 80],
              body : [
                [
                  {text : 'From: '+this.startDate.toString(), fontSize : 9}, 
                  {text : 'To: '+this.endDate.toString(), fontSize : 9} 
                ],
              ]
            },
          },
          '  ',
          {
            //layout : 'noBorders',
            table : {
                headerRows : 1,
                widths : [30, 135, 60, 80, 50, 40, 60],
                body : report
            }
        }, 
      ]     
    };
    pdfMake.createPdf(docDefinition).print()
  }

  getConsultationPaymentType(consultation : IConsultation) : string{
    if(consultation.patientBill.status === 'COVERED'){
      return consultation.insurancePlan.name
    }
    return 'Cash'
  }


  printLabTestReport = async () => {

    if(this.labTests.length === 0){
      this.msgBox.showErrorMessage3('No data to export')
      return
    }

    this.documentHeader = await this.data.getDocumentHeader()
    var header = ''
    var footer = ''
    var title  = 'Lab Tests Report'
    var logo : any = ''
    var total : number = 0
    var discount : number = 0
    var tax : number = 0

    var report = [
      [
        {text : 'SN', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Patient Name', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Phone', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Reg#', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Lab Test', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Mode', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Date', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Status', fontSize : 9, fillColor : '#bdc6c7'},
      ]
    ]  
    
    this.labTests.forEach((element) => {
      var detail = [
        {text : element?.sn.toString(), fontSize : 7, fillColor : '#ffffff'},  
        {text : (element?.patient?.firstName +' '+ element.patient?.middleName +' '+ element.patient?.lastName).toString(), fontSize : 7, fillColor : '#ffffff'}, 
        {text : element?.patient?.phoneNo.toString(), fontSize : 7, fillColor : '#ffffff'}, 
        {text : element?.patient?.no.toString(), fontSize : 7, fillColor : '#ffffff'},  
        {text : element?.labTestType?.name, fontSize : 7, fillColor : '#ffffff'}, 
        {text : this.getLabTestPaymentType(element), fontSize : 7, fillColor : '#ffffff'},  
        {text : new ShowDateOnlyPipe().transform(element?.createdAt).toString(), fontSize : 7, fillColor : '#ffffff'}, 
        {text : element.status.toString(), fontSize : 7, fillColor : '#ffffff'}, 
      ]
      report.push(detail)
    })
   
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
          ' ',
          'Doctor: ' + this.clinicianName,
          ' ',
          {
            layout : 'noBorders',
            table : {
              widths : [80, 80],
              body : [
                [
                  {text : 'From: '+this.startDate.toString(), fontSize : 9}, 
                  {text : 'To: '+this.endDate.toString(), fontSize : 9} 
                ],
              ]
            },
          },
          '  ',
          {
            //layout : 'noBorders',
            table : {
                headerRows : 1,
                widths : [30, 120, 50, 70, 50, 50, 40, 40],
                body : report
            }
        }, 
      ]     
    };
    pdfMake.createPdf(docDefinition).print()
  }

  getLabTestPaymentType(labTest : ILabTest) : string{
    if(labTest.patientBill.status === 'COVERED'){
      return labTest.insurancePlan.name
    }
    return 'Cash'
  }


  printRadiologyReport = async () => {

    if(this.radiologies.length === 0){
      this.msgBox.showErrorMessage3('No data to print')
      return
    }

    this.documentHeader = await this.data.getDocumentHeader()
    var header = ''
    var footer = ''
    var title  = 'Radiologies Report'
    var logo : any = ''
    var total : number = 0
    var discount : number = 0
    var tax : number = 0

    var report = [
      [
        {text : 'SN', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Patient Name', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Phone', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Reg#', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Radiology', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Mode', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Date', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Status', fontSize : 9, fillColor : '#bdc6c7'},
      ]
    ]  
    
    this.radiologies.forEach((element) => {
      var detail = [
        {text : element?.sn.toString(), fontSize : 7, fillColor : '#ffffff'},
        {text : (element?.patient?.firstName +' '+ element.patient?.middleName +' '+ element.patient?.lastName).toString(), fontSize : 7, fillColor : '#ffffff'}, 
        {text : element?.patient?.phoneNo.toString(), fontSize : 7, fillColor : '#ffffff'}, 
        {text : element?.patient?.no.toString(), fontSize : 7, fillColor : '#ffffff'},  
        {text : element?.radiologyType?.name.toString(), fontSize : 7, fillColor : '#ffffff'},  
        {text : this.getRadiologyPaymentType(element), fontSize : 7, fillColor : '#ffffff'}, 
        {text : new ShowDateOnlyPipe().transform(element?.createdAt).toString(), fontSize : 7, fillColor : '#ffffff'}, 
        {text : element.patient.type.toString(), fontSize : 7, fillColor : '#ffffff'}, 
      ]
      report.push(detail)
    })
   
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
          ' ',
          'Doctor: ' + this.clinicianName,
          ' ',
          {
            layout : 'noBorders',
            table : {
              widths : [80, 80],
              body : [
                [
                  {text : 'From: '+this.startDate.toString(), fontSize : 9}, 
                  {text : 'To: '+this.endDate.toString(), fontSize : 9} 
                ],
              ]
            },
          },
          '  ',
          {
            //layout : 'noBorders',
            table : {
                headerRows : 1,
                widths : [30, 120, 50, 70, 50, 50, 40, 40],
                body : report
            }
        }, 
      ]     
    };
    pdfMake.createPdf(docDefinition).print()
  }

  getRadiologyPaymentType(radiology : IRadiology) : string{
    if(radiology.patientBill.status === 'COVERED'){
      return radiology.insurancePlan.name
    }
    return 'Cash'
  }


  printProcedureReport = async () => {

    if(this.procedures.length === 0){
      this.msgBox.showErrorMessage3('No data to export')
      return
    }

    this.documentHeader = await this.data.getDocumentHeader()
    var header = ''
    var footer = ''
    var title  = 'Procedures Report'
    var logo : any = ''
    var total : number = 0
    var discount : number = 0
    var tax : number = 0

    var report = [
      [
        {text : 'SN', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Patient Name', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Phone', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Reg#', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Procedure', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Mode', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Date', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Status', fontSize : 9, fillColor : '#bdc6c7'},
      ]
    ]  
    
    this.procedures.forEach((element) => {
      var detail = [
        {text : element?.sn.toString(), fontSize : 7, fillColor : '#ffffff'}, 
        {text : (element?.patient?.firstName +' '+ element.patient?.middleName +' '+ element.patient?.lastName).toString(), fontSize : 7, fillColor : '#ffffff'}, 
        {text : element?.patient?.phoneNo.toString(), fontSize : 7, fillColor : '#ffffff'}, 
        {text : element?.patient?.no.toString(), fontSize : 7, fillColor : '#ffffff'},  
        {text : element?.procedureType?.name.toString(), fontSize : 7, fillColor : '#ffffff'},  
        {text : this.getProcedurePaymentType(element), fontSize : 7, fillColor : '#ffffff'}, 
        {text : new ShowDateOnlyPipe().transform(element?.createdAt).toString(), fontSize : 7, fillColor : '#ffffff'}, 
        {text : element.status.toString(), fontSize : 7, fillColor : '#ffffff'}, 
      ]
      report.push(detail)
    })
   
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
          ' ',
          'Doctor: ' + this.clinicianName,
          ' ',
          {
            layout : 'noBorders',
            table : {
              widths : [80, 80],
              body : [
                [
                  {text : 'From: '+this.startDate.toString(), fontSize : 9}, 
                  {text : 'To: '+this.endDate.toString(), fontSize : 9} 
                ],
              ]
            },
          },
          '  ',
          {
            //layout : 'noBorders',
            table : {
                headerRows : 1,
                widths : [30, 120, 50, 70, 50, 50, 40, 40],
                body : report
            }
        }, 
      ]     
    };
    pdfMake.createPdf(docDefinition).print()
  }

  getProcedurePaymentType(procedure : IProcedure) : string{
    if(procedure.patientBill?.status === 'COVERED'){
      return procedure.insurancePlan.name
    }
    return 'Cash'
  }

  



}
