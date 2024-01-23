import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { AuthService } from 'src/app/auth.service';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';
import { Workbook } from 'exceljs';
import { CommonModule, formatDate } from '@angular/common';
import { DataService } from 'src/app/services/data.service';

import * as pdfMake from 'pdfmake/build/pdfmake';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { finalize } from 'rxjs';
import { ILabTest } from 'src/app/domain/lab-test';
import { ILabTestType } from 'src/app/domain/lab-test-type';
import { IGoodsReceivedNoteDetail } from 'src/app/domain/goods-received-note-detail';
import { IStoreStockCard } from 'src/app/domain/store-stock-card';

var pdfFonts = require('pdfmake/build/vfs_fonts.js'); 
const fs = require('file-saver');

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-store-stock-card-report',
  templateUrl: './store-stock-card-report.component.html',
  styleUrls: ['./store-stock-card-report.component.scss'],
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
export class StoreStockCardReportComponent {

  
  address  : any 


  report : string[] = []

  filterRecords : string = ''

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
  storeStockCards : IStoreStockCard[] = []
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
      to : to
    }

    this.spinner.show()
    await this.http.post<IStoreStockCard[]>(API_URL+'/reports/store_stock_card_report', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.storeStockCards = data!
        var sn = 1
        this.storeStockCards.forEach(element => {
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
    this.storeStockCards = []
  }

  print = async () => {

    if(this.storeStockCards.length === 0){
      this.msgBox.showErrorMessage3('No data to print')
      return
    }

    this.documentHeader = await this.data.getDocumentHeader()
    var title  = 'Store Stock Card Report'
    var total : number = 0

    var report = [
      [
        {text : 'SN', fontSize : 6, fillColor : '#bdc6c7'},
        {text : 'Item', fontSize : 6, fillColor : '#bdc6c7'},
        {text : 'Store', fontSize : 6, fillColor : '#bdc6c7'},
        {text : 'Qty In', fontSize : 6, fillColor : '#bdc6c7'},
        {text : 'Qty Out', fontSize : 6, fillColor : '#bdc6c7'},
        {text : 'Balance', fontSize : 6, fillColor : '#bdc6c7'},
        {text : 'Reference', fontSize : 6, fillColor : '#bdc6c7'},
        {text : 'Created At/By', fontSize : 6, fillColor : '#bdc6c7'},
      ]
    ]  

    var sn : number = 1

    var total = 0

    
    
    this.storeStockCards.forEach((element) => {
      var detail = [
        {text : element.sn.toString(), fontSize : 6, fillColor : '#ffffff'}, 
        {text : element.item.name, fontSize : 6, fillColor : '#ffffff'},
        {text : element.store.name, fontSize : 6, fillColor : '#ffffff'},
        {text : element.qtyIn.toString(), fontSize : 6, fillColor : '#ffffff'},
        {text : element.qtyOut.toString(), fontSize : 6, fillColor : '#ffffff'},
        {text : element.balance.toString(), fontSize : 6, fillColor : '#ffffff'},
        {text : element.reference, fontSize : 6, fillColor : '#ffffff'},
        {text : element.created, fontSize : 6, fillColor : '#ffffff'},
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
                widths : [20, 120, 80, 20, 20, 30, 60, 80],
                body : report
            }
        }, 
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
