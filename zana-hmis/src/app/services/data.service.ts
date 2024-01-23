import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Byte } from 'src/custom-packages/util';
import { Injectable } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AuthService } from '../auth.service';
import { ICompanyProfile } from '../domain/company';

const API_URL = environment.apiUrl;

@Injectable({
  providedIn: 'root'
})
export class DataService {
  companyId       : any;
  companyName     : string;
  contactName     : string;
  tin             : string;
  vrn             : string;
  physicalAddress : string;
  postCode        : string;
  postAddress     : string;
  telephone       : string;
  mobile          : string;
  email           : string;
  website         : string
  fax             : string;
  bankAccountName : string;
  bankPhysicalAddress: string;
  bankPostCode    : string;
  bankPostAddress : string;
  bankName        : string;
  bankAccountNo   : string;
  bankAccountName2 : string;
  bankPhysicalAddress2: string;
  bankPostCode2    : string;
  bankPostAddress2 : string;
  bankName2        : string;
  bankAccountNo2   : string;
  bankAccountName3 : string;
  bankPhysicalAddress3: string;
  bankPostCode3   : string;
  bankPostAddress3 : string;
  bankName3        : string;
  bankAccountNo3   : string;

  quotationNotes   : string
  salesInvoiceNotes : string

  constructor(private http : HttpClient, 
              private auth : AuthService, 
              private sanitizer: DomSanitizer,
              private spinner : NgxSpinnerService) {
    this.companyId        = ''
    this.companyName      = ''
    this.contactName      = ''
    this.tin              = ''
    this.vrn              = ''
    this.physicalAddress  = ''
    this.postCode         = ''
    this.postAddress      = ''
    this.telephone        = ''
    this.mobile           = ''
    this.email            = ''
    this.website          = ''
    this.fax              = ''
    this.bankAccountName  = ''
    this.bankPhysicalAddress = ''
    this.bankPostCode     = ''
    this.bankPostAddress  = ''
    this.bankName         = ''
    this.bankAccountNo    = ''
    this.bankAccountName2  = ''
    this.bankPhysicalAddress2 = ''
    this.bankPostCode2     = ''
    this.bankPostAddress2  = ''
    this.bankName2         = ''
    this.bankAccountNo2    = ''
    this.bankAccountName3  = ''
    this.bankPhysicalAddress3 = ''
    this.bankPostCode3     = ''
    this.bankPostAddress3  = ''
    this.bankName3         = ''
    this.bankAccountNo3    = ''

    this.quotationNotes    = ''
    this.salesInvoiceNotes = ''
  }

  async getLogo() : Promise<string> {
    var logo : any = ''
    await this.http.get<ICompany>(API_URL+'/company_profile/get_logo')
    .toPromise()
    .then(
      res => {
        var retrieveResponse : any = res
        var base64Data = retrieveResponse.logo
        logo = 'data:image/png;base64,'+base64Data
      }
    )
    .catch(error => {
      console.log(error)
    }) 
    return logo
  }

  async getCompanyProfile() {
    var company! : ICompanyProfile
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<ICompanyProfile>(API_URL+'/company_profile/get', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.companyId        = data?.id
        this.companyName      = data!.companyName
        this.contactName      = data!.contactName
        this.tin              = data!.tin
        this.vrn              = data!.vrn
        this.physicalAddress  = data!.physicalAddress
        this.postCode         = data!.postCode
        this.postAddress      = data!.postAddress
        this.telephone        = data!.telephone
        this.mobile           = data!.mobile
        this.email            = data!.email
        this.website          = data!.website
        this.fax              = data!.fax

        /**
         * Document notes
         */
        this.quotationNotes = data!.quotationNotes
        this.salesInvoiceNotes = data!.salesInvoiceNotes
        


        this.bankAccountName  = data!.bankAccountName
        this.bankPhysicalAddress = data!.bankPhysicalAddress
        this.bankPostCode     = data!.bankPostCode
        this.bankPostAddress  = data!.bankPostAddress
        this.bankName         = data!.bankName
        this.bankAccountNo    = data!.bankAccountNo 
        this.bankAccountName2  = data!.bankAccountName2
        this.bankPhysicalAddress2 = data!.bankPhysicalAddress2
        this.bankPostCode2     = data!.bankPostCode2
        this.bankPostAddress2  = data!.bankPostAddress2
        this.bankName2         = data!.bankName2
        this.bankAccountNo2    = data!.bankAccountNo2  
        this.bankAccountName3  = data!.bankAccountName3
        this.bankPhysicalAddress3 = data!.bankPhysicalAddress3
        this.bankPostCode3     = data!.bankPostCode3
        this.bankPostAddress3  = data!.bankPostAddress3
        this.bankName3         = data!.bankName3
        this.bankAccountNo3    = data!.bankAccountNo3  
        
        company.bankAccountName = this.bankAccountName
        company.bankPhysicalAddress = this.bankPhysicalAddress
        company.bankPostCode = this.bankPostCode
        company.bankPostAddress = this.bankPostAddress
        company.bankName = this.bankName
        company.bankAccountNo = this.bankAccountNo

        company.bankAccountName2 = this.bankAccountName2
        company.bankPhysicalAddress2 = this.bankPhysicalAddress2
        company.bankPostCode2 = this.bankPostCode2
        company.bankPostAddress2 = this.bankPostAddress2
        company.bankName2 = this.bankName2
        company.bankAccountNo2 = this.bankAccountNo2

        company.bankAccountName3 = this.bankAccountName3
        company.bankPhysicalAddress3 = this.bankPhysicalAddress3
        company.bankPostCode3 = this.bankPostCode3
        company.bankPostAddress3 = this.bankPostAddress3
        company.bankName3 = this.bankName3
        company.bankAccountNo3 = this.bankAccountNo3
      }
    )
    .catch(
      (error) => {
        console.log(error)
      }
    )
    return company
  }

  async getDocumentHeader(){
    await this.getCompanyProfile()
    var cName = this.companyName
    var cPostalAddress = 'P.O. Box '+this.postCode
    var cPhysicalAddress = this.physicalAddress
    var cTelephone = 'Tel: '+this.telephone
    var cMobile = 'Mob: '+this.mobile
    var cFax = 'Fax: '+this.fax
    var cEmail = 'Email: '+this.email
    var cWebsite = this.website
    var tin = 'TIN: '+this.tin
    var vrn = 'VRN: '+this.vrn

    var logo : any = await this.getLogo()

    if(logo == ''){
      logo = { text : '', width : 70, height : 70, absolutePosition : {x : 40, y : 40}}
    }else{
      logo = {image : logo, width : 70, absolutePosition : {x : 40, y : 40}}
    }

    var address = [
      {text : cName, fontSize : 18, bold : true, alignment : 'center'},
      {table : {
        headerRows : 0,
        widths: ['100%'],
        body : [[{text : '.', fontSize : 9, fillColor : '#546f9c', height : 30}]]
      },
      layout : 'headerLineOnly'},
      {text : cPhysicalAddress + ' ' + cPostalAddress + ' ' + cTelephone + ' ' + cEmail + ' ' + cWebsite, fontSize : 9,  alignment : 'center'},
    ]

    var header : any = {
      columns : 
      [
        logo,
        {width : 10, columns : [[]]},
        {
          width : 400,
          columns : [
            address
          ]
        },
      ]
    }

    return header
  }

  getHorizontalLine() : any{
    try{
      return {table : {headerRows : 1,widths: ['100%'], body : [[''],['']]},layout : 'headerLineOnly'}
    }catch(error){
      return ''
    }    
  }


  async getAddress(){
    await this.getCompanyProfile()
    var cName = this.companyName
    var cPostalAddress = 'P.O. Box '+this.postCode
    var cPhysicalAddress = this.physicalAddress
    var cTelephone = 'Tel: '+this.telephone
    var cMobile = 'Mob: '+this.mobile
    var cFax = 'Fax: '+this.fax
    var cEmail = 'Email: '+this.email
    var cWebsite = this.website
    var tin = 'TIN: '+this.tin
    var vrn = 'VRN: '+this.vrn
    
    var address = [
      {text : cName, fontSize : 16, bold : true, alignment : 'center'},
      {table : {
        headerRows : 0,
        widths: ['100%'],
        body : [
                [{text : '.', fontSize : 9, fillColor : '#546f9c', height : 30}],
                ['']
                ]
    },
    layout : 'headerLineOnly'},
      {text : cPhysicalAddress + ' ' + cPostalAddress + ' ' + cTelephone + ' ' + cEmail + ' ' + cWebsite, fontSize : 9,  alignment : 'center'},
    ]
    return address
  }

  async getReceiptHeader(receiptNo : string){
    await this.getCompanyProfile()
    var cName = this.companyName
    var cPostalAddress = 'P.O. Box '+this.postCode
    var cPhysicalAddress = this.physicalAddress
    var cTelephone = this.telephone
    var cMobile = this.mobile
    var cFax = 'Fax: '+this.fax
    var cEmail = this.email
    var cWebsite = this.website
    var tin = 'TIN: '+this.tin
    var vrn = 'VRN: '+this.vrn
    
    /*var address = [
      {text : cName, fontSize : 12, bold : true, alignment : 'center', width : 200},
      {text : cPostalAddress, fontSize : 9, alignment : 'center', width : 200},
      {text : cPhysicalAddress, fontSize : 9, alignment : 'center', width : 200},
      {text : cTelephone, fontSize : 9, alignment : 'center', width : 200},
      {text : cEmail, fontSize : 9, italic : true, alignment : 'center', width : 200},
      {text : cWebsite, fontSize : 9, italic : true, alignment : 'center', width : 200},
      {text : tin, fontSize : 9, italic : true, alignment : 'center', width : 200},
      {text : vrn, fontSize : 9, italic : true, alignment : 'center', width : 200},
    ]*/

    var address = {
      headerRows : 0,
      widths : [200],
      body : [
        [{text : 'Receipt#: '+receiptNo, fontSize : 12, bold : true, alignment : 'right'}],
        [{text : ' ', fontSize : 7, bold : true, alignment : 'center'}],
        [{text : cName, fontSize : 12, bold : true, alignment : 'center'}],
        [{text : cPostalAddress, fontSize : 9, alignment : 'center'}],
        [{text : cPhysicalAddress, fontSize : 9, alignment : 'center'}],
        [{text : cTelephone, fontSize : 9, alignment : 'center'}],
        [{text : cEmail, fontSize : 9, italic : true, alignment : 'center'}],
        //[{text : cWebsite, fontSize : 9, italic : true, alignment : 'center'}],
        //[{text : tin, fontSize : 9, italic : true, alignment : 'center'}],
        //[{text : vrn, fontSize : 9, italic : true, alignment : 'center'}]
      ]
    }
    return address
  }

  async getCompanyName() : Promise<string>{
    await this.getCompanyProfile()
    return this.companyName
  }


  async getPaymentDetails(){
    await this.getCompanyProfile()
    var bankName = this.bankName
    var accNo = this.bankAccountNo
    var accName = this.bankAccountName
    var bankName2 = this.bankName2
    var accNo2 = this.bankAccountNo2
    var accName2 = this.bankAccountName2
    var bankName3 = this.bankName3
    var accNo3 = this.bankAccountNo3
    var accName3 = this.bankAccountName3

    var details = [
      [
        {text : 'Bank Name: ' + bankName, fontSize : 8}, 
        {text : 'Acc No: ' + accNo, fontSize : 8},
        {text : 'Acc Name: ' + accName, fontSize : 8}
      ],
      [
        {text : 'Bank Name: ' + bankName2, fontSize : 8}, 
        {text : 'Acc No: ' + accNo2, fontSize : 8},
        {text : 'Acc Name: ' + accName2, fontSize : 8} 
      ],
      [
        {text : 'Bank Name: ' + bankName3, fontSize : 8}, 
        {text : 'Acc No: ' + accNo3, fontSize : 8}, 
        {text : 'Acc Name: ' + accName3, fontSize : 8} 
      ]
    ]  
    return details
  }

  async getQuotationNotes() : Promise<string>{
    await this.getCompanyProfile()
    return this.quotationNotes
  }

  async getSalesInvoiceNotes() : Promise<string>{
    await this.getCompanyProfile()
    return this.salesInvoiceNotes
  }
}




export interface ICompany{
  logo : Byte[]
} 

