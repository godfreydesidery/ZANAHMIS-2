import { CommonModule, formatDate } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import * as pdfMake from 'pdfmake/build/pdfmake';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IPatientBill } from 'src/app/domain/patient-bill';
import { IPatientInvoice } from 'src/app/domain/patient-invoice';
import { IPatientInvoiceDetail } from 'src/app/domain/patient-invoice-detail';
import { ReceiptItem } from 'src/app/domain/receipt-item';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { ShowDateOnlyPipe } from 'src/app/pipes/date.pipe';
import { ShowDateTimePipe } from 'src/app/pipes/date_time.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { ShowTimePipe } from 'src/app/pipes/show_time.pipe';
import { ShowUserPipe } from 'src/app/pipes/show_user.pipe';
import { DataService } from 'src/app/services/data.service';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { PosReceiptPrinterService } from 'src/app/services/pos-receipt-printer.service';
import { environment } from 'src/environments/environment';
var pdfFonts = require('pdfmake/build/vfs_fonts.js'); 

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-patient-invoice',
  templateUrl: './patient-invoice.component.html',
  styleUrls: ['./patient-invoice.component.scss'],
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    AgePipe,
    ShowUserPipe,
    ShowDateTimePipe,
    ShowTimePipe,
    RouterLink
  ], 
})
export class PatientInvoiceComponent {

  invoiceId : any = null

  invoice! : IPatientInvoice

  constructor(private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private router : Router,
    private msgBox : MsgBoxService,
    private printer : PosReceiptPrinterService,
    private data : DataService) 
    {(window as any).pdfMake.vfs = pdfFonts.pdfMake.vfs;}

  async ngOnInit(): Promise<void> {

    this.invoiceId = localStorage.getItem('patient-invoice-id')
    localStorage.removeItem('patient-invoice-id')
    this.loadPatientInvoice(this.invoiceId)

  }

  async loadPatientInvoice(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IPatientInvoice>(API_URL+'/patients/get_patient_invoice?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.invoice = data!
        console.log(data)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }

  bills : IPatientBill[] = []
  total : number = 0
  amountReceived : number = 0
  listBill(detail : IPatientInvoiceDetail){

    

    if(detail.checked === true){
      var present : boolean = false
      this.bills.forEach(element => {
        if(element.id === detail.patientBill.id){
          present = true
        }
      })
      if(present === false){
        this.bills.push(detail.patientBill)
      }
    }else if(detail.checked === false){
      var tempBills : IPatientBill[] = []
      this.bills.forEach(element => {
        if(element.id != detail.patientBill.id){
          tempBills.push(element)
        }
      })
      this.bills = tempBills      
    }

    this.total = 0
    this.amountReceived = 0
    this.invoice.patientInvoiceDetails.forEach(element => {
      if(element.patientBill.status === 'VERIFIED'){
        this.bills.forEach(e => {
          if(e.id === element.patientBill.id){
            element.checked = true
            this.total = this.total + element.patientBill.amount
          }
        })
      }else{
        element.checked = false
      }
    })
  }

  async confirmBillsPayment(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    this.spinner.show()
    await this.http.post<IPatientBill>(API_URL+'/bills/confirm_bills_payment?total_amount='+this.total, this.bills, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.msgBox.showSuccessMessage('Payment successiful')
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

    this.bills.forEach(element => {
      item = new ReceiptItem()
      item.code = element.id
      item.name = element.description
      item.amount = element.amount
      item.qty = element.qty
      items.push(item)
    })

    

    this.printer.print(items, 'NA', this.total, this.invoice.patient)

  }



  getAmount(patientBill : IPatientBill) : string{
    if(patientBill.status === 'COVERED'){
      //return 'Covered'
    }
    return patientBill.amount.toLocaleString('en-US', { minimumFractionDigits: 2 })
  }

  getTotal(amount : number) : string{
    if(amount === 0){
      //return 'Covered'
    }
    return amount.toLocaleString('en-US', { minimumFractionDigits: 2 })
  }

  logo!    : any
  documentHeader! : any
  print = async () => {

    if(this.invoice.patientInvoiceDetails.length === 0){
      this.msgBox.showErrorMessage3('No data to export')
      return
    }

    this.documentHeader = await this.data.getDocumentHeader()
    var header = ''
    var footer = ''
    var title  = 'Invoice'
    var logo : any = ''
    var total : number = 0
    var discount : number = 0
    var tax : number = 0

    var report = [
      [
        {text : 'SN', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Date-Time', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Service/Item', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Qty', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Amount/Cov', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Status', fontSize : 9, fillColor : '#bdc6c7'},

      ]
    ]  

    var sn : number = 1

    var total = 0

    
    
    this.invoice.patientInvoiceDetails.forEach((element) => {
      var detail = [
        {text : sn.toString(), fontSize : 9, fillColor : '#ffffff'}, 
        {text : (new ShowDateTimePipe).transform(element?.patientBill.createdAt) , fontSize : 9, fillColor : '#ffffff'},
        {text : element?.patientBill.description, fontSize : 9, fillColor : '#ffffff'},
        {text : element?.patientBill.qty.toString(), fontSize : 9, fillColor : '#ffffff'},
        {text : this.getAmount(element.patientBill), fontSize : 9, alignment : 'right', fillColor : '#ffffff'},
        {text : element?.patientBill.status, fontSize : 9, fillColor : '#ffffff'},
      ]
      sn = sn + 1
      //if(element.patientBill.status != 'COVERED'){
        total = total + element.patientBill.amount
      //}
      report.push(detail)
    })

    var detailSummary = [
      {text : '', fontSize : 9, fillColor : '#ffffff'}, 
      {text : '', fontSize : 9, fillColor : '#ffffff'},
      {text : '', fontSize : 9, fillColor : '#ffffff'},
      {text : 'Total', fontSize : 9, fillColor : '#ffffff'},
      {text : this.getTotal(total), fontSize : 9, alignment : 'right', bold : true, fillColor : '#ffffff'},
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
          {
            columns : 
            [
              {
                width : 200,
                layout : 'noBorders',
                table : {
                  widths : [200],
                  body : [
                    [
                      {text : 'Name: '+this.invoice?.patient?.firstName.toString() + ' ' +this.invoice?.patient?.middleName?.toString() + ' ' +this.invoice?.patient?.lastName?.toString(), fontSize : 9},
                    ],
                    [
                      {text : 'File No: '+this.invoice?.patient?.no.toString(), fontSize : 9}, 
                    ],
                    [
                      {text : 'Address: '+this.invoice?.patient?.address.toString(), fontSize : 9},
                    ],
                    [
                      {text : 'Phone No: '+this.invoice?.patient?.phoneNo.toString(), fontSize : 9},
                    ]
                  ]
                }
              },
              {
                width : 100,
                layout : 'noBorders',
                table : {
                  widths : [100],
                  body : [
                    [' ']
                  ]
                }
              },
              {
                width : 200,
                layout : 'noBorders',
                table : {
                  widths : [220],
                  body : [
                    [
                      {text : 'Invoice#: '+this.invoice?.no.toString(), fontSize : 12, bold : true},
                    ],
                    [
                      {text : 'Created: '+(new ShowDateTimePipe).transform(this.invoice.createdAt), fontSize : 9}, 
                    ],
                    [
                      {text : 'Bill To', fontSize : 9},
                    ],
                    [
                      {text : this.getBillTo(this.invoice), fontSize : 9},
                    ]
                  ]
                }
              },
              
              
              
              
              
              
            ]
          },

          
          '  ',
          {
            //layout : 'noBorders',
            table : {
                headerRows : 1,
                widths : [20, 90, 200, 30, 60, 40],
                body : report
            }
        }, 
      ]     
    };
    pdfMake.createPdf(docDefinition).print()
  }

  getBillTo(invoice : IPatientInvoice) : string{
    if(invoice.insurancePlan === null){
      return invoice?.patient?.firstName + ' ' + invoice?.patient?.middleName + ' ' + invoice?.patient?.lastName  + ' ' + invoice?.patient?.no  + ' ' + invoice?.patient?.address  + ' ' + invoice?.patient?.phoneNo
    }else{
      return invoice?.insurancePlan?.name + ' | ' + invoice?.insurancePlan?.insuranceProvider?.name
    }
  }




}
