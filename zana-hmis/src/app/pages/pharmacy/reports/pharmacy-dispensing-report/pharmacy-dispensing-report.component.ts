import { HttpClient } from '@angular/common/http';
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

import { HttpHeaders } from '@angular/common/http';
import { OnInit } from '@angular/core';
import { finalize } from 'rxjs';
import { IPatient } from 'src/app/domain/patient';
import { IPatientBill } from 'src/app/domain/patient-bill';
import { ReceiptItem } from 'src/app/domain/receipt-item';
import { ShowDateTimePipe } from 'src/app/pipes/date_time.pipe';
import { ShowTimePipe } from 'src/app/pipes/show_time.pipe';
import { ShowUserPipe } from 'src/app/pipes/show_user.pipe';
import { PosReceiptPrinterService } from 'src/app/services/pos-receipt-printer.service';

import { HttpClientModule } from '@angular/common/http';
import { NgxSpinnerModule } from 'ngx-spinner';



import { IProcedure } from 'src/app/domain/procedure';
import { ShowDateOnlyPipe } from 'src/app/pipes/date.pipe';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { IMedicine } from 'src/app/domain/medicine';
import { IPrescription } from 'src/app/domain/prescription';
import { IPrescriptionBatch } from 'src/app/domain/prescription-batch';
var pdfFonts = require('pdfmake/build/vfs_fonts.js'); 
const fs = require('file-saver');

const API_URL = environment.apiUrl;


@Component({
  selector: 'app-pharmacy-dispensing-report',
  templateUrl: './pharmacy-dispensing-report.component.html',
  styleUrls: ['./pharmacy-dispensing-report.component.scss'],
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
export class PharmacyDispensingReportComponent {
  
  
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
  prescriptions : IPrescription[] = []
  async loadPrescriptionReport(from : Date, to : Date){
    this.prescriptions = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    if(from === undefined || to === undefined){
      this.msgBox.showErrorMessage3('Could not run. Please select date range')
      return
    }
    if(this.medicineId === null){
      this.msgBox.showErrorMessage3('Could not run. Please select Medicine/Drug')
      return
    }

    if(from > to){
      this.msgBox.showErrorMessage3('Could not run. Start date must be earlier or equal to end date')
      return
    }

    var args = {
      from : from,
      to : to,
      medicine : {id : this.medicineId}
    }

    this.spinner.show()
    await this.http.post<IPrescription[]>(API_URL+'/reports/prescription_report', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        data?.forEach(element => {
          if(element.status === 'GIVEN'){
            this.prescriptions.push(element)
          }
        })
        var sn = 1
        this.prescriptions.forEach(element => {
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
    this.prescriptions.forEach(async element => {
      var batches = await this.loadPrescriptionBatches(element.id)

      element.prescriptionBatches = batches
    })
  }


  async loadPrescriptionBatches(prescriptionId : any) : Promise<IPrescriptionBatch[]>{ 
    var batches :  IPrescriptionBatch[] = [] 
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    this.spinner.show()
    await this.http.get<IPrescriptionBatch[]>(API_URL+'/pharmacies/get_prescription_batches?prescription_id='+prescriptionId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        batches = data!
        //data?.forEach(element => {
          //batches.push(element)
        //})
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
    return batches
  }




  clear(){
    this.prescriptions = []
  }

  medicineId : any =  null
  medicineCode : string = ''
  medicineName : string = ''
  medicines : IMedicine[] = []
  async loadMedicinesLike(value : string){
    this.medicines = []
    if(value.length < 2){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.medicines = []
    await this.http.get<IMedicine[]>(API_URL+'/medicines/load_medicines_like?name_like='+value, options)
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.medicines = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }
  async getMedicine(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.medicines = []
    this.spinner.show()
    await this.http.get<IMedicine>(API_URL+'/medicines/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {
        this.medicineId    = data?.id
        this.medicineCode  = data!.code
        this.medicineName  = data!.name
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }



  print = async () => {

    if(this.prescriptions.length === 0){
      this.msgBox.showErrorMessage3('No data to print')
      return
    }

    this.documentHeader = await this.data.getDocumentHeader()
    var title  = 'Pharmacy Dispensing Report'
    var total : number = 0

    var report = [
      [
        {text : 'SN', fontSize : 6, fillColor : '#bdc6c7'},
        {text : 'Patient Name', fontSize : 6, fillColor : '#bdc6c7'},
        {text : 'File No', fontSize : 6, fillColor : '#bdc6c7'},
        {text : 'Drug', fontSize : 6, fillColor : '#bdc6c7'},
        {text : 'Qty', fontSize : 6, fillColor : '#bdc6c7'},
        {text : 'Pharmacy', fontSize : 6, fillColor : '#bdc6c7'},
        {text : 'Dispensed At/By', fontSize : 6, fillColor : '#bdc6c7'},
      ]
    ]  

    var sn : number = 1

    var total = 0

    
    
    this.prescriptions.forEach((element) => {
      var detail = [
        {text : element?.sn.toString(), fontSize : 6, fillColor : '#ffffff'}, 
        {text : element?.patient?.firstName + ' ' +element?.patient?.middleName + ' ' + element?.patient?.lastName, fontSize : 6, fillColor : '#ffffff'},
        {text : element?.patient?.no, fontSize : 6, fillColor : '#ffffff'},
        {text : element?.medicine?.name, fontSize : 6, fillColor : '#ffffff'},
        {text : element?.issued.toString(), fontSize : 6, fillColor : '#ffffff'},
        {text : element?.issuePharmacy?.name, fontSize : 6, fillColor : '#ffffff'},
        {text : element?.approved, fontSize : 6, fillColor : '#ffffff'},
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
          'Drug: ' + this.medicineName,
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
                widths : [20, 100, 60, 90, 20, 60, 60],
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
