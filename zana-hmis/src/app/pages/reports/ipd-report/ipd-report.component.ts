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
import { IIPDReport } from 'src/app/domain/ipd-report';
import { AuthService } from 'src/app/auth.service';
import { finalize } from 'rxjs';
import { IInsurancePlan } from 'src/app/domain/insurance-plan';
import { IRevenue } from 'src/app/domain/revenue';
import { MsgBoxService } from 'src/app/services/msg-box.service';
var pdfFonts = require('pdfmake/build/vfs_fonts.js'); 
const fs = require('file-saver');

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-ipd-report',
  templateUrl: './ipd-report.component.html',
  styleUrls: ['./ipd-report.component.scss'],
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
export class IpdReportComponent {

  logo!    : any
  documentHeader! : any
  address  : any 

  from! : Date
  to!   : Date

  ipdReport : IIPDReport[] = []


  report : string[] = []

  filterRecords : string = ''

  coveredTotal : number = 0
  invoiceTotal : number = 0
  paidTotal : number = 0
  balanceTotal : number = 0
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


  async loadIPDReport(from : Date, to : Date){   
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
    await this.http.post<IIPDReport[]>(API_URL+'/reports/ipd_report', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {

        console.log(data)

        var r = data
        var sn = 0
        r?.forEach(element => {
          sn = sn + 1
          element.sn = sn
          element.total = element.covered + element.invoice + element.paid + element.balance
        })




        this.ipdReport = r!

        this.coveredTotal = 0
        this.invoiceTotal = 0
        this.paidTotal = 0
        this.balanceTotal = 0
        this.grandTotal = 0

        this.ipdReport.forEach(element => {
          this.coveredTotal = this.coveredTotal + element.covered
          this.invoiceTotal = this.invoiceTotal + element.invoice
          this.paidTotal = this.paidTotal + element.paid
          this.balanceTotal = this.balanceTotal + element.balance
          this.grandTotal = this.grandTotal + element.total
        })
        console.log(this.ipdReport)
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
    var title  = 'IPD Report'
    var logo : any = ''
    var total : number = 0
    var discount : number = 0
    var tax : number = 0

    var report = [
      [
        {text : 'SN', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Adm Date', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Pat Name', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Admitted By', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Dis Date', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Ward Type', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Payment Type', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Covered', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Invoice', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Paid', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Balance', fontSize : 9, fillColor : '#bdc6c7'},
      ]
    ]  
    
    this.ipdReport.forEach((element) => {
      total = total + element.total

      var detail = [
        {text : element?.sn.toString(), fontSize : 7, alignment : 'left', fillColor : '#ffffff'},
        {text : element?.admittedAt, fontSize : 7, alignment : 'left', fillColor : '#ffffff'},
        {text : element?.admission.patient.firstName + element?.admission.patient.middleName + element?.admission.patient.lastName, fontSize : 7, alignment : 'left', fillColor : '#ffffff'},
        {text : element?.admittedBy, fontSize : 7, alignment : 'left', fillColor : '#ffffff'},
        {text : element?.dischargedAt, fontSize : 7, alignment : 'left', fillColor : '#ffffff'},
        {text : element?.admission?.wardBed?.ward?.wardType?.name, fontSize : 7, alignment : 'left', fillColor : '#ffffff'},
        {text : element?.paymentType, fontSize : 7, alignment : 'left', fillColor : '#ffffff'},
        {text : element?.covered.toLocaleString('en-US', { minimumFractionDigits: 2 }), fontSize : 7, alignment : 'right', fillColor : '#ffffff'},
        {text : element?.invoice.toLocaleString('en-US', { minimumFractionDigits: 2 }), fontSize : 7, alignment : 'right', fillColor : '#ffffff'},
        {text : element?.paid.toLocaleString('en-US', { minimumFractionDigits: 2 }), fontSize : 7, alignment : 'right', fillColor : '#ffffff'},
        {text : element?.balance.toLocaleString('en-US', { minimumFractionDigits: 2 }), fontSize : 7, alignment : 'right', fillColor : '#ffffff'},
      ]
      report.push(detail)
    })
    var detailSummary = [
      {text : '', fontSize : 7, alignment : 'left', fillColor : '#bdc6c7'},
      {text : 'Total', fontSize : 7, alignment : 'left', fillColor : '#bdc6c7'},
      {text : '', fontSize : 7, alignment : 'right', fillColor : '#bdc6c7'},
      {text : '', fontSize : 7, alignment : 'right', fillColor : '#bdc6c7'},
      {text : '', fontSize : 7, alignment : 'right', fillColor : '#bdc6c7'},
      {text : '', fontSize : 7, alignment : 'right', fillColor : '#bdc6c7'},
      {text : '', fontSize : 7, alignment : 'right', fillColor : '#bdc6c7'},
      {text : this.coveredTotal.toLocaleString('en-US', { minimumFractionDigits: 2 }), fontSize : 7, alignment : 'right', fillColor : '#bdc6c7'},
      {text : this.invoiceTotal.toLocaleString('en-US', { minimumFractionDigits: 2 }), fontSize : 7, alignment : 'right', fillColor : '#bdc6c7'},
      {text : this.paidTotal.toLocaleString('en-US', { minimumFractionDigits: 2 }), fontSize : 7, alignment : 'right', fillColor : '#bdc6c7'},
      {text : this.balanceTotal.toLocaleString('en-US', { minimumFractionDigits: 2 }), fontSize : 7, alignment : 'right', fillColor : '#bdc6c7'},      
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
          '  ',
          {text : title, fontSize : 14, bold : true, alignment : 'center'},
          this.data.getHorizontalLine(),
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
          {
            layout : 'noBorders',
            table : {
                headerRows : 1,
                widths : [20, 40, 40, 50, 50, 40, 40, 40, 40, 40, 60],
                body : report
            }
        },                   
      ]     
    };
    pdfMake.createPdf(docDefinition).print()
    
  }

  getPaymentMode(plan : IInsurancePlan) : string{
    if(plan != null){
      return plan.name
    }else{
      return 'CASH'
    }
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
