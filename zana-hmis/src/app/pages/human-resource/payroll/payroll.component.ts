import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import * as pdfMake from 'pdfmake/build/pdfmake';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IItem } from 'src/app/domain/item';
import { IPayroll } from 'src/app/domain/payroll';
import { IMedicine } from 'src/app/domain/medicine';
import { IStore } from 'src/app/domain/store';
import { ISupplier } from 'src/app/domain/supplier';
import { DataService } from 'src/app/services/data.service';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SSL_OP_SSLEAY_080_CLIENT_DH_BUG } from 'constants';

var pdfFonts = require('pdfmake/build/vfs_fonts.js'); 

const API_URL = environment.apiUrl;


@Component({
  selector: 'app-payroll',
  templateUrl: './payroll.component.html',
  styleUrls: ['./payroll.component.scss'],
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
})
export class PayrollComponent {
  id : any = null
  no : string = ''
  name : string = ''
  description : string = ''
  startDate! : Date | string
  endDate! : Date | string
  status : string = ''
  statusDescription : string = ''
  comments : string = ''

  created : string = ''
  verified : string = ''
  approved : string = ''


  orderDate! : Date
  validUntil! : Date | string
  
  

  payroll! : IPayroll

  supplier! : ISupplier
  store! : IStore

  noLocked = false

  payrolls : IPayroll[] = []

  detailId          : any
  detailBasicSalary : number = 0
  detailGrossSalary : number = 0
  detailNetSalary : number = 0
  detailAddOns : number = 0
  detailDeductions : number = 0
  detailEmployerContributions : number = 0

  filterRecords : string = ''
  filterOrders : string = ''


  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private router : Router,
    private msgBox : MsgBoxService,
    private data : DataService) 
    {(window as any).pdfMake.vfs = pdfFonts.pdfMake.vfs;}

  async ngOnInit(): Promise<void> {
    
  }

  async loadPayrolls(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    this.spinner.show()
    await this.http.get<IPayroll[]>(API_URL+'/payrolls', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.payrolls = data!
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async requestNo(){
    this.clear()
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<any>(API_URL+'/payrolls/request_no', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.no = data!['no']
        this.noLocked = true
      }
    )
    .catch(
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.payroll.payrollDetails = []
  }

  async saveOrder(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var order = {
      id          : this.id,
      no          : this.no,
      name        : this.name,
      description : this.description,
      startDate   : this.startDate,
      endDate     : this.endDate,
      comments    : this.comments
    }
    this.spinner.show()
    await this.http.post<IPayroll>(API_URL+'/payrolls/save', order, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.id         = data?.id
        this.no         = data!.no
        this.name = data!.name
        this.description = data!.description
        this.startDate = data!.startDate
        this.endDate = data!.endDate
        this.status     = data!.status
        this.statusDescription = data!.statusDescription
        this.comments = data!.comments
        this.created    = data!.created
        this.verified   = data!.verified
        this.approved   = data!.approved

        this.payroll = data!

        this.msgBox.showSuccessMessage('Success')
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async saveDetail(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var detail = {
      id                    : this.detailId,
      payroll               : {id : this.id},
      basicSalary           : this.detailBasicSalary,
      grossSalary           : this.detailGrossSalary,
      netSalary             : this.detailNetSalary,
      addOns                : this.detailAddOns,
      deductions            : this.detailDeductions,
      employerContributions : this.detailEmployerContributions
    }
    this.spinner.show()
    await this.http.post(API_URL+'/payrolls/save_detail', detail, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.search(this.id)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async deleteDetail(detailId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var detail = {
      id : detailId,
      payroll : {id : this.id}
    }
    
    this.spinner.show()
    await this.http.post(API_URL+'/payrolls/delete_detail', detail, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.search(this.id)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async verifyOrder(){
    if(!window.confirm('Confirm verify order. Confirm?')){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var order = {
      id  : this.id,
      no : this.no
    }
    this.spinner.show()
    await this.http.post<IPayroll>(API_URL+'/payrolls/verify', order, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.id         = data?.id
        this.no         = data!.no
        this.name = data!.name
        this.description = data!.description
        this.startDate = data!.startDate
        this.endDate = data!.endDate
        this.status     = data!.status
        this.statusDescription = data!.statusDescription
        this.comments = data!.comments
        this.created    = data!.created
        this.verified   = data!.verified
        this.approved   = data!.approved

        this.payroll = data!

        this.msgBox.showSuccessMessage('Order verified successifuly')
      }
    )
    .catch(
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async approveOrder(){
    if(!window.confirm('Confirm approve order. Confirm?')){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var order = {
      id  : this.id,
      no : this.no
    }
    this.spinner.show()
    await this.http.post<IPayroll>(API_URL+'/payrolls/approve', order, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.id         = data?.id
        this.no         = data!.no
        this.name = data!.name
        this.description = data!.description
        this.startDate = data!.startDate
        this.endDate = data!.endDate
        this.status     = data!.status
        this.statusDescription = data!.statusDescription
        this.comments = data!.comments
        this.created    = data!.created
        this.verified   = data!.verified
        this.approved   = data!.approved

        this.payroll = data!

        this.msgBox.showSuccessMessage('Order approved successifuly')
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async submitOrder(){
    if(!window.confirm('Confirm submit order. Confirm?')){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var order = {
      id  : this.id,
      no : this.no
    }
    this.spinner.show()
    await this.http.post<IPayroll>(API_URL+'/payrolls/submit', order, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.id         = data?.id
        this.no         = data!.no
        this.name = data!.name
        this.description = data!.description
        this.startDate = data!.startDate
        this.endDate = data!.endDate
        this.status     = data!.status
        this.statusDescription = data!.statusDescription
        this.comments = data!.comments
        this.created    = data!.created
        this.verified   = data!.verified
        this.approved   = data!.approved

        this.payroll = data!

        this.msgBox.showSuccessMessage('Order submitted successifuly')
        this.printOrder()
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }


  async importEmployees(id : any){
    if(!window.confirm('Confirm importing employees. Confirm?')){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var order = {
      id  : this.id,
      no : this.no
    }
    this.spinner.show()
    await this.http.post<IPayroll>(API_URL+'/payrolls/import_employees', order, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        
        this.search(id)

        this.msgBox.showSuccessMessage('Import successifuly')
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  

  postOrder(){

  }

  cancelOrder(){
    
  }

  lock(){
    this.noLocked = true

  }

  unlock(){
    this.noLocked = false

  }

  async openToEdit(){
    if(this.id == null){
      this.noLocked = false
      this.no = ''
    }else{
      this.noLocked = true
    }
  }

  clear(){
    this.id         = null
    this.no         = ''
    this.name = ''
    this.description = ''
    this.startDate! = ''
    this.endDate! = ''
    this.status = ''
    this.statusDescription = ''
    this.comments = ''
    this.created    = ''
    this.verified   = ''
    this.approved   = ''
    this.payroll!
  }


  async searchOrder(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    this.spinner.show()
    await this.http.get<IPayroll>(API_URL+'/payrolls/search_by_no?no='+this.no, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.id         = data?.id
        this.no         = data!.no
        this.name = data!.name
        this.description = data!.description
        this.startDate = data!.startDate
        this.endDate = data!.endDate
        this.status     = data!.status
        this.statusDescription = data!.statusDescription
        this.comments = data!.comments
        this.created    = data!.created
        this.verified   = data!.verified
        this.approved   = data!.approved

        this.payroll = data!
        this.lock()
        console.log(data)
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async search(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    this.spinner.show()
    await this.http.get<IPayroll>(API_URL+'/payrolls/search?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.id         = data?.id
        this.no         = data!.no
        this.name = data!.name
        this.description = data!.description
        this.startDate = data!.startDate
        this.endDate = data!.endDate
        this.status     = data!.status
        this.statusDescription = data!.statusDescription
        this.comments = data!.comments
        this.created    = data!.created
        this.verified   = data!.verified
        this.approved   = data!.approved

        this.payroll = data!

        this.lock()
        console.log(data)
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  clearDetail(){
    this.detailId = null
    this.detailBasicSalary = 0
    this.detailGrossSalary = 0
    this.detailNetSalary = 0
    this.detailAddOns = 0
    this.detailDeductions = 0
    this.detailEmployerContributions = 0
  }

  logo!    : any
  documentHeader! : any
  async printOrder(){
    /*
    if(this.payroll.payrollDetails.length === 0){
      this.msgBox.showErrorMessage3('Can not print an empty LPO')
      return
    }

    this.documentHeader = await this.data.getDocumentHeader()
    var header = ''
    var footer = ''
    var title  = 'Local Purchase Order(LPO)'
    var logo : any = ''
    var total : number = 0
    var discount : number = 0
    var tax : number = 0

    var report = [
      [
        {text : 'SN', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Code', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Name', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Qty', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Price', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Amount', fontSize : 9, fillColor : '#bdc6c7'},

      ]
    ]  

    var sn : number = 1

    var total = 0

    
    
    this.payroll.payrollDetails.forEach((element) => {
      var detail = [
        {text : sn.toString(), fontSize : 9, fillColor : '#ffffff'}, 
        {text : element?.item?.code, fontSize : 9, fillColor : '#ffffff'},
        {text : element?.item?.name, fontSize : 9, fillColor : '#ffffff'},
        {text : element?.qty.toString(), fontSize : 9, fillColor : '#ffffff'},
        {text : element.price.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 }), fontSize : 9, alignment : 'right', fillColor : '#ffffff'},
        {text : (element?.qty * element?.price).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 }), fontSize : 9, alignment : 'right', fillColor : '#ffffff'},
      ]
      sn = sn + 1
      total = total + element?.qty * element?.price
      report.push(detail)
    })

    var detailSummary = [
      {text : '', fontSize : 9, fillColor : '#ffffff'}, 
      {text : '', fontSize : 9, fillColor : '#ffffff'},
      {text : '', fontSize : 9, fillColor : '#ffffff'},
      {text : '', fontSize : 9, fillColor : '#ffffff'},
      {text : 'Total', fontSize : 9, fillColor : '#ffffff'},
      {text : total.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 }), fontSize : 9, alignment : 'right', bold : true, fillColor : '#ffffff'},
      
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
                      {text : 'Ship To: ', fontSize : 9},
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
                      {text : 'LPO#: '+this.payroll?.no.toString(), fontSize : 12, bold : true},
                    ],
                    [
                      {text : 'Supplier: '+this.payroll?.supplier?.name.toString(), fontSize : 9},
                    ],
                    //[
                      //{text : 'Created: '+(new ShowDateTimePipe).transform(this.invoice.createdAt), fontSize : 9}, 
                    //],
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
                widths : [20, 50, 180, 30, 60, 80],
                body : report
            }
        },
        ' ',
        'Approved',
        ' ',
        '...........................'
         
      ]     
    };
    pdfMake.createPdf(docDefinition).print()
    */
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
