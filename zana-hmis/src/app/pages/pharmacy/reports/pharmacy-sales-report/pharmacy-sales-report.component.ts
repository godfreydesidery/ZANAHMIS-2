import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';

import { environment } from 'src/environments/environment';
import { Workbook } from 'exceljs';
import { formatDate } from '@angular/common';
import { DataService } from 'src/app/services/data.service';

import * as pdfMake from 'pdfmake/build/pdfmake';

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { OnInit } from '@angular/core';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { IDrugMovement } from 'src/app/domain/drug-movement-report';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { IPharmacySalesReport } from 'src/app/domain/pharmacy-sales-report';


var pdfFonts = require('pdfmake/build/vfs_fonts.js'); 
const fs = require('file-saver');

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-pharmacy-sales-report',
  templateUrl: './pharmacy-sales-report.component.html',
  styleUrls: ['./pharmacy-sales-report.component.scss'],
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    AgePipe,
    RouterLink
  ],
})
export class PharmacySalesReportComponent {
  address  : any 


  report : string[] = []

  filterRecords : string = ''

  total : number = 0

  constructor(
    private auth : AuthService,
    private http : HttpClient,
    private spinner: NgxSpinnerService,
    private msgBox : MsgBoxService,
    private data : DataService,
              
  ) {(window as any).pdfMake.vfs = pdfFonts.pdfMake.vfs;}


  async ngOnInit(): Promise<void> {
    
  }

  from! : Date
  to! : Date
  logo!    : any
  documentHeader! : any
  pharmacySalesReport : IPharmacySalesReport[] = []
  async loadPharmacySalesReport(from : Date, to : Date){
    this.pharmacySalesReport = []
    this.total = 0
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
      to : to,
    }

    

    this.spinner.show()
    await this.http.post<IPharmacySalesReport[]>(API_URL+'/reports/pharmacy_sales_report', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        data?.forEach(element => {
          this.total = this.total + element.amount
          this.pharmacySalesReport.push(element)
        })
        var sn = 1
        this.pharmacySalesReport.forEach(element => {
          element.sn = sn
          sn = sn + 1
        })
      }
    )
    .catch(
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  clear(){
    this.pharmacySalesReport = []
    this.total = 0
  }

  print = async () => {

    if(this.pharmacySalesReport.length === 0){
      this.msgBox.showErrorMessage3('No data to print')
      return
    }

    this.documentHeader = await this.data.getDocumentHeader()
    var title  = 'Pharmacy Sales Report'
    var total : number = 0

    var report = [
      [
        {text : 'SN', fontSize : 6, fillColor : '#bdc6c7'},
        {text : 'Customer Name', fontSize : 6, fillColor : '#bdc6c7'},
        {text : 'Phone', fontSize : 6, fillColor : '#bdc6c7'},
        //{text : 'Address', fontSize : 6, fillColor : '#bdc6c7'},
        {text : 'Medicine', fontSize : 6, fillColor : '#bdc6c7'},
        {text : 'Qty', fontSize : 6, fillColor : '#bdc6c7'},
        {text : 'Amount', fontSize : 6, fillColor : '#bdc6c7'},
        {text : 'Sold By', fontSize : 6, fillColor : '#bdc6c7'},
      ]
    ]  

    var sn : number = 1


    var summary = [
      {text : '', fontSize : 6, fillColor : '#ffffff'}, 
      {text : '', fontSize : 6, fillColor : '#ffffff'},
      {text : '', fontSize : 6, fillColor : '#ffffff'},
      //{text : element?.customerAddress, fontSize : 6, fillColor : '#ffffff'},
      {text : '', fontSize : 6, fillColor : '#ffffff'},
      {text : 'Total', fontSize : 6, fillColor : '#ffffff', bold : true},
      {text : this.total.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 }), fontSize : 6, fillColor : '#ffffff', bold : true, alignment : 'right'},
      {text : '', fontSize : 6, fillColor : '#ffffff'},
    ]
    
    this.pharmacySalesReport.forEach((element) => {
      var detail = [
        {text : element?.sn.toString(), fontSize : 6, fillColor : '#ffffff'}, 
        {text : element?.customerName, fontSize : 6, fillColor : '#ffffff'},
        {text : element?.customerPhone, fontSize : 6, fillColor : '#ffffff'},
        //{text : element?.customerAddress, fontSize : 6, fillColor : '#ffffff'},
        {text : element?.medicineName, fontSize : 6, fillColor : '#ffffff'},
        {text : element?.qty.toString(), fontSize : 6, fillColor : '#ffffff'},
        {text : element?.amount.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 }), fontSize : 6, fillColor : '#ffffff', alignment : 'right'},
        {text : element?.sold, fontSize : 6, fillColor : '#ffffff'},
      ]
      report.push(detail)
    })
    report.push(summary)

    
   
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
                widths : [20, 60, 60, 110, 30, 50, 90],
                body : report
            }
        }, 
      ]     
    };
    pdfMake.createPdf(docDefinition).print()
  }

  async exportToSpreadsheet() {
    let workbook = new Workbook();
    let worksheet = workbook.addWorksheet('Pharmacy Sales Report')
   
    worksheet.columns = [
      { header: 'DATE', key: 'DATE'},
      { header: 'AMOUNT', key: 'AMOUNT'},
      { header: 'DISCOUNT', key: 'DISCOUNT'},
      { header: 'TAX', key: 'TAX'}
      
    ];
    this.spinner.show()
    this.report.forEach(() => {
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