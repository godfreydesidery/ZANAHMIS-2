//import { Byte } from '@angular/compiler/src/util';

import { Byte } from "src/custom-packages/util"

export interface ICompanyProfile {
  id              : any
  companyName     : string
  retrievedImage  : any
  contactName     : string
  logo            : Byte[]
  tin             : string
  vrn             : string
  physicalAddress : string
  postCode        : string
  postAddress     : string
  telephone       : string
  mobile          : string
  email           : string
  website         : string
  fax             : string
  bankAccountName : string
  bankPhysicalAddress : string
  bankPostCode    : string
  bankPostAddress : string
  bankName        : string
  bankAccountNo   : string
  bankAccountName2 : string
  bankPhysicalAddress2 : string
  bankPostCode2    : string
  bankPostAddress2 : string
  bankName2        : string
  bankAccountNo2  : string
  bankAccountName3 : string
  bankPhysicalAddress3 : string
  bankPostCode3    : string
  bankPostAddress3 : string
  bankName3        : string
  bankAccountNo3   : string

  quotationNotes   : string
  salesInvoiceNotes : string

  registrationFee : number

  publicPath : string

  employeePrefix : string
}

export interface ILogo{
    logo : Blob
  }