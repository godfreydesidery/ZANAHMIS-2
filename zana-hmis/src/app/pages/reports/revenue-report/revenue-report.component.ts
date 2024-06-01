import { CommonModule, formatDate } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { ShowDateTimePipe } from 'src/app/pipes/date_time.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';

import { environment } from 'src/environments/environment';

import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerModule, NgxSpinnerService } from 'ngx-spinner';
import { Workbook } from 'exceljs';
import { DataService } from 'src/app/services/data.service';


import * as pdfMake from 'pdfmake/build/pdfmake';
import { IProcedure } from 'src/app/domain/procedure';
import { ShowDateOnlyPipe } from 'src/app/pipes/date.pipe';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { AuthService } from 'src/app/auth.service';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { finalize } from 'rxjs';
import { ILabTestCollection } from 'src/app/models/labTestCollection';
import { IRevenue } from 'src/app/domain/revenue';
var pdfFonts = require('pdfmake/build/vfs_fonts.js'); 
const fs = require('file-saver');

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-revenue-report',
  templateUrl: './revenue-report.component.html',
  styleUrls: ['./revenue-report.component.scss'],
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
export class RevenueReportComponent {

  
  logo!    : any
  documentHeader! : any
  address  : any 

  from! : Date
  to!   : Date

  revenueReport : IRevenue[] = []


  report : string[] = []

  filterRecords : string = ''

  registrationTotal : number = 0
  consultationTotal : number = 0
  labTestTotal : number = 0
  radiologyTotal : number = 0
  procedureTotal : number = 0
  prescriptionTotal : number = 0
  admissionBedTotal : number = 0
  grandTotal : number = 0


  

  paymentMode : string = '--All--'




  constructor(
    //private shortcut : ShortCutHandlerService,
              private auth : AuthService,
              private http : HttpClient,
              private modalService: NgbModal,
              private spinner: NgxSpinnerService,
              private msgBox : MsgBoxService,
              private data : DataService,
              
  ) {(window as any).pdfMake.vfs = pdfFonts.pdfMake.vfs;}


  async ngOnInit(): Promise<void> {
    this.loadInsurancePlanNames()
  }


  insurancePlanNames : string[] = []

  async loadInsurancePlanNames(){   
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    this.insurancePlanNames = []

    this.spinner.show()
    await this.http.get<string[]>(API_URL+'/insurance_plans/get_names', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.insurancePlanNames = data!
      }
    )
    .catch(
      error => {
        //this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }


  async loadRevenueReport(from : Date, to : Date){   
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    if(from > to){
      this.msgBox.showErrorMessage3('Could not run. Start date must be earlier or equal to end date')
      return
    }

    if(from === undefined || to === undefined){
      from = new Date()
      to = new Date()
    }

    var args = {
      from : from,
      to   : to,
      paymentMode : this.paymentMode
    }

    this.spinner.show()
    await this.http.post<IRevenue[]>(API_URL+'/reports/revenue_report', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {

        var r = data
        var sn = 0
        r?.forEach(element => {
          sn = sn + 1
          element.sn = sn
          element.total = element.registration + element.consultation + element.labTest + element.radiology + element.procedure + element.prescription + element.admissionBed
        })




        this.revenueReport = r!

        this.registrationTotal = 0
        this.consultationTotal = 0
        this.labTestTotal = 0
        this.radiologyTotal = 0
        this.procedureTotal = 0
        this.prescriptionTotal = 0
        this.admissionBedTotal = 0
        this.grandTotal = 0

        this.revenueReport.forEach(element => {
          this.registrationTotal = this.registrationTotal + element.registration
          this.consultationTotal = this.consultationTotal + element.consultation
          this.labTestTotal = this.labTestTotal + element.labTest
          this.radiologyTotal = this.radiologyTotal + element.radiology
          this.procedureTotal = this.procedureTotal + element.procedure
          this.prescriptionTotal = this.prescriptionTotal + element.prescription
          this.admissionBedTotal = this.admissionBedTotal + element.admissionBed
          this.grandTotal = this.grandTotal + element.total
        })
        console.log(this.revenueReport)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }

  exportToPdf = async () => {
    this.documentHeader = await this.data.getDocumentHeader()
    var header = ''
    var footer = ''
    var title  = 'Revenue Report'
    var logo : any = ''
    var total : number = 0
    var discount : number = 0
    var tax : number = 0
    
    /*this.report.forEach((element) => {
      total = total + element.amount
      discount = discount + element.discount
      tax = tax + element.tax
      var detail = [
        {text : formatDate(element.date, 'yyyy-MM-dd', 'en-US'), fontSize : 9, fillColor : '#ffffff'}, 
        {text : element.amount.toLocaleString('en-US', { minimumFractionDigits: 2 }), fontSize : 9, alignment : 'right', fillColor : '#ffffff'},
        {text : element.discount.toLocaleString('en-US', { minimumFractionDigits: 2 }), fontSize : 9, alignment : 'right', fillColor : '#ffffff'},  
        {text : element.tax.toLocaleString('en-US', { minimumFractionDigits: 2 }), fontSize : 9, alignment : 'right', fillColor : '#ffffff'},
      ]
      report.push(detail)
    })*/
    /*var detailSummary = [
      {text : 'Total', fontSize : 9, fillColor : '#CCCCCC'}, 
      {text : total.toLocaleString('en-US', { minimumFractionDigits: 2 }), fontSize : 9, alignment : 'right', fillColor : '#CCCCCC'},
      {text : discount.toLocaleString('en-US', { minimumFractionDigits: 2 }), fontSize : 9, alignment : 'right', fillColor : '#CCCCCC'},  
      {text : tax.toLocaleString('en-US', { minimumFractionDigits: 2 }), fontSize : 9, alignment : 'right', fillColor : '#CCCCCC'},        
    ]
    report.push(detailSummary)*/
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
          '  ',
          {text : title, fontSize : 14, bold : true, alignment : 'center'},
          this.data.getHorizontalLine(),
          '  ',
          '  ',
          '  ',
          {text : title, fontSize : 12, bold : true},
          '  ',
          {
            layout : 'noBorders',
            table : {
              widths : [75, 300],
              body : [
                [
                  {text : 'From', fontSize : 9}, 
                  {text : this.from, fontSize : 9} 
                ],
                [
                  {text : 'To', fontSize : 9}, 
                  {text : this.to, fontSize : 9} 
                ],
                [
                  {text : 'Payment Mode', fontSize : 9}, 
                  {text : this.paymentMode, fontSize : 9} 
                ],
              ]
            },
          },
          '  ',
          //{
            //layout : 'noBorders',
            //table : {
                //headerRows : 1,
                //widths : [100, 100, 100, 100, 100],
                //body : report
            //}
        //},                   
      ]     
    };
    pdfMake.createPdf(docDefinition).print()
  }

  async exportToSpreadsheet() {
    let workbook = new Workbook();
    let worksheet = workbook.addWorksheet('Daily Sales Report')
   
    worksheet.columns = [
      { header: 'DATE', key: 'DATE'},
      { header: 'AMOUNT', key: 'AMOUNT'},
      { header: 'DISCOUNT', key: 'DISCOUNT'},
      { header: 'TAX', key: 'TAX'}
      
    ];
    this.spinner.show()
    this.report.forEach(element => {
      worksheet.addRow(
        {
          DATE      : formatDate('', 'yyyy-MM-dd', 'en-US'),
          AMOUNT    : '',
          DISCOUNT  : '',
          TAX       : ''
        },"n"
      )
    })
    worksheet.addRow(
      {
        CODE         : '',
        DESCRIPTION         : '',
        QTY         : 'Total',
        AMOUNT       : ''
      },"n"
    )
    
    this.spinner.hide()
    workbook.xlsx.writeBuffer().then((data) => {
      let blob = new Blob([data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
      fs.saveAs(blob, 'Report Template '+this.from+' to '+this.to+'.xlsx');
    })
   
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
