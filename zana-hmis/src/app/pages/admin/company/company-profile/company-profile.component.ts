import { Byte } from 'src/custom-packages/util';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Component, NgModule } from '@angular/core';
import { AuthService } from 'src/app/auth.service';
import { environment } from 'src/environments/environment';
import { BrowserModule, DomSanitizer } from '@angular/platform-browser';
import { NgxSpinnerModule, NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs/operators';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { ICompanyProfile } from 'src/app/domain/company';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { RouterLink } from '@angular/router';


const API_URL = environment.apiUrl;

@Component({
  selector: 'app-company-profile',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './company-profile.component.html',
  styleUrls: ['./company-profile.component.scss']
})
export class CompanyProfileComponent {


  id              : any;
  companyName     : string;
  contactName     : string;
  logo            : Byte[];
  tin             : string;
  vrn             : string;
  physicalAddress : string;
  postCode        : string;
  postAddress     : string;
  telephone       : string;
  mobile          : string;
  email           : string;
  website         : string;
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
  bankPostCode3    : string;
  bankPostAddress3 : string;
  bankName3        : string;
  bankAccountNo3   : string;

  quotationNotes   : string
  salesInvoiceNotes : string

  logoUrl     : any

  registrationFee : number

  publicPath : string

  constructor(private http : HttpClient, 
    private auth : AuthService, 
    private sanitizer: DomSanitizer, 
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService) {
this.id               = ''
this.companyName      = ''
this.contactName      = ''
this.logo             = []
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

this.logoUrl = ''

this.registrationFee = 0

this.publicPath = ''
}

ngOnInit(): void {
  this.getCompanyProfile()
  this.getLogo()
}

async getCompanyProfile() {
  let options = {
    headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
  }
  this.spinner.show()
  await this.http.get<ICompanyProfile>(API_URL+'/company_profile/get', options)
  .pipe(finalize(() => this.spinner.hide()))
  .toPromise()
  .then(
    data => {
      this.id               = data?.id
      this.companyName      = data!.companyName
      this.contactName      = data!.contactName
      this.logo             = data!.logo
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
      this.bankAccountName  = data!.bankAccountName
      this.bankPhysicalAddress      = data!.bankPhysicalAddress
      this.bankPostCode     = data!.bankPostCode
      this.bankPostAddress  = data!.bankPostAddress
      this.bankName         = data!.bankName
      this.bankAccountNo    = data!.bankAccountNo

      this.bankAccountName2  = data!.bankAccountName2
      this.bankPhysicalAddress2      = data!.bankPhysicalAddress2
      this.bankPostCode2     = data!.bankPostCode2
      this.bankPostAddress2  = data!.bankPostAddress2
      this.bankName2         = data!.bankName2
      this.bankAccountNo2    = data!.bankAccountNo2

      this.bankAccountName3  = data!.bankAccountName3
      this.bankPhysicalAddress3      = data!.bankPhysicalAddress3
      this.bankPostCode3     = data!.bankPostCode3
      this.bankPostAddress3  = data!.bankPostAddress3
      this.bankName3         = data!.bankName3
      this.bankAccountNo3    = data!.bankAccountNo3

      this.quotationNotes    = data!.quotationNotes
      this.salesInvoiceNotes = data!.salesInvoiceNotes

      this.registrationFee = data!.registrationFee
      this.publicPath = data!.publicPath
      if(this.companyName == null){
        this.msgBox.showErrorMessage3('Could not find company details')
      }       
    }
  )
  .catch(
    (error) => {
      console.log(error)
      this.msgBox.showErrorMessage(error, 'Could not load company information')
    }
  )
  this.getLogo()
}


public getSantizeUrl(url : string) {
  return this.sanitizer.bypassSecurityTrustUrl(url);
}

arrayBufferToBase64(buffer: any) {
  var binary = '';
  var bytes = [].slice.call(new Uint8Array(buffer));
  bytes.forEach((b) => binary += String.fromCharCode(b));
  return window.btoa(binary);
};


async saveCompanyProfile() {
  let options = {
    headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
  }
  var profile = {
    id               : this.id,
    companyName      : this.companyName,
    contactName      : this.contactName,
    //logo             : this.logo,
    tin              : this.tin,
    vrn              : this.vrn,
    physicalAddress  : this.physicalAddress,
    postCode         : this.postCode,
    postAddress      : this.postAddress,
    telephone        : this.telephone,
    mobile           : this.mobile,
    email            : this.email,
    website          : this.website,
    fax              : this.fax,
    bankAccountName  : this.bankAccountName,
    bankPhysicalAddress      : this.bankPhysicalAddress,
    bankPostCode     : this.bankPostCode,
    bankPostAddress  : this.bankPostAddress,
    bankName         : this.bankName,
    bankAccountNo    : this.bankAccountNo,
    bankAccountName2  : this.bankAccountName2,
    bankPhysicalAddress2      : this.bankPhysicalAddress2,
    bankPostCode2     : this.bankPostCode2,
    bankPostAddress2  : this.bankPostAddress2,
    bankName2         : this.bankName2,
    bankAccountNo2    : this.bankAccountNo2,
    bankAccountName3  : this.bankAccountName3,
    bankPhysicalAddress3      : this.bankPhysicalAddress3,
    bankPostCode3     : this.bankPostCode3,
    bankPostAddress3  : this.bankPostAddress3,
    bankName3         : this.bankName3,
    bankAccountNo3    : this.bankAccountNo3,
    quotationNotes    : this.quotationNotes,
    salesInvoiceNotes : this.salesInvoiceNotes,
    registrationFee   : this.registrationFee,
    publicPath        : this.publicPath
  }
  this.spinner.show()
  await this.http.post<ICompanyProfile>(API_URL+'/company_profile/save', profile, options)
  .pipe(finalize(() => this.spinner.hide()))
  .toPromise()
  .then(
    data => {
      this.id               = data?.id
      this.companyName      = data!.companyName
      this.contactName      = data!.contactName
      this.logo             = data!.logo
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
      this.bankAccountName  = data!.bankAccountName
      this.bankPhysicalAddress      = data!.bankPhysicalAddress
      this.bankPostCode     = data!.bankPostCode
      this.bankPostAddress  = data!.bankPostAddress
      this.bankName         = data!.bankName
      this.bankAccountNo    = data!.bankAccountNo

      this.bankAccountName2  = data!.bankAccountName2
      this.bankPhysicalAddress2      = data!.bankPhysicalAddress2
      this.bankPostCode2     = data!.bankPostCode2
      this.bankPostAddress2  = data!.bankPostAddress2
      this.bankName2         = data!.bankName2
      this.bankAccountNo2    = data!.bankAccountNo2

      this.bankAccountName3  = data!.bankAccountName3
      this.bankPhysicalAddress3      = data!.bankPhysicalAddress3
      this.bankPostCode3     = data!.bankPostCode3
      this.bankPostAddress3  = data!.bankPostAddress3
      this.bankName3         = data!.bankName3
      this.bankAccountNo3    = data!.bankAccountNo3

      this.quotationNotes    = data!.quotationNotes
      this.salesInvoiceNotes = data!.salesInvoiceNotes

      this.registrationFee   = data!.registrationFee

      this.publicPath = data!.publicPath
      try{
        this.onUpload()
      }catch(e : any){}
      alert('Company details saved successifully')
    }
  )
  .catch(
    error => {
      console.log(error)
      this.msgBox.showErrorMessage(error, 'Could not save company details')
    }
  )
  this.getLogo()
}


selectedFile!: File;
retrievedImage!: any;
base64Data: any;
retrieveResponse: any;
message!: string;
imageName: any;
//Gets called when the user selects an image
public onFileChanged(event : any) {
  //Select File
  this.selectedFile = event.target.files[0];
}
//Gets called when the user clicks on submit to upload the image
onUpload() {   
  let options = {
    headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
  }
  console.log(this.selectedFile);
  //FormData API provides methods and properties to allow us easily prepare form data to be sent with POST HTTP requests.
  const uploadImageData = new FormData();
  uploadImageData.append('logo', this.selectedFile, this.selectedFile.name);
  //Make a call to the Spring Boot Application to save the image
  this.spinner.show()
  this.http.post(API_URL+'/company_profile/save_logo', uploadImageData, options)
  .pipe(finalize(() => this.spinner.hide()))
    .subscribe(() => {
      this.getCompanyProfile()
      this.msgBox.showSuccessMessage('Upload succesiful')
    },
    error =>{
      this.msgBox.showErrorMessage(error, 'Upload failed')
    });
    
}
  //Gets called when the user clicks on retieve image button to get the image from back end
  async getLogo() {
  //Make a call to Sprinf Boot to get the Image Bytes.
  //this.spinner.show()
  await this.http.get(API_URL+'/company_profile/get_logo')
  //.pipe(finalize(() => this.spinner.hide()))
  .toPromise()
    .then(
      res => {
        this.retrieveResponse = res
        this.base64Data = this.retrieveResponse.logo
        this.retrievedImage = 'data:image/png;base64,'+this.base64Data
        console.log(this.retrievedImage)
      }
    )
    .catch(error => {
      console.log(error)
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
