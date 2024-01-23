import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterLink } from '@angular/router';
import { NgxSpinnerModule, NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { AuthService } from 'src/app/auth.service';
import { ISupplier } from 'src/app/domain/supplier';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;


@Component({
  selector: 'app-supplier-register',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './supplier-register.component.html',
  styleUrls: ['./supplier-register.component.scss']
})
export class SupplierRegisterComponent {
  
  id                  : any
  code                : string = ''
  name                : string = ''
  contactName         : string = ''
  active              : boolean = false
  tin                 : string = ''
  vrn                 : string = ''
  termsOfContract     : string = ''
  physicalAddress     : string = ''
  postCode            : string = ''
  postAddress         : string = ''
  telephone           : string = ''
  mobile              : string = ''
  email               : string = ''
  fax                 : string = ''
  bankAccountName     : string = ''
  bankPhysicalAddress : string = ''
  bankPostCode        : string = ''
  bankPostAddress     : string = ''
  bankName            : string = ''
  bankAccountNo       : string = ''

  supplier! : ISupplier

  names : string[] = []

  filterRecords : string = ''

  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
  ) {
    
  }

  ngOnInit(): void {
    this.loadSupplierNames()
  }

  public async saveSupplier(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    var supplier = {
      id                  : this.id,
      code                : this.code,
      name                : this.name,
      contactName         : this.contactName,
      active              : this.active,
      tin                 : this.tin,
      vrn                 : this.vrn,
      termsOfContract     : this.termsOfContract,
      physicalAddress     : this.physicalAddress,
      postCode            : this.postCode,
      postAddress         : this.postAddress,
      telephone           : this.telephone,
      mobile              : this.mobile,
      email               : this.email,
      fax                 : this.fax,
      bankAccountName     : this.bankAccountName,
      bankPhysicalAddress : this.bankPhysicalAddress,
      bankPostCode        : this.bankPostCode,
      bankPostAddress     : this.bankPostAddress,
      bankName            : this.bankName,
      bankAccountNo       : this.bankAccountNo
    }
    if(this.id == null || this.id == ''){
      //save a new supplier
      this.spinner.show()
      await this.http.post<ISupplier>(API_URL+'/suppliers/save', supplier, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id               = data?.id
          this.code             = data!.code
          this.name             = data!.name
          this.contactName      = data!.contactName
          this.active           = data!.active
          this.tin              = data!.tin
          this.vrn              = data!.vrn
          this.termsOfContract  = data!.termsOfContract
          this.physicalAddress  = data!.physicalAddress
          this.postCode         = data!.postCode
          this.postAddress      = data!.postAddress
          this.telephone        = data!.telephone
          this.mobile           = data!.mobile
          this.email            = data!.email
          this.fax              = data!.fax
          this.bankAccountName  = data!.bankAccountName
          this.bankPhysicalAddress = data!.bankPhysicalAddress
          this.bankPostCode     = data!.bankPostCode
          this.bankPostAddress  = data!.bankPostAddress
          this.bankName         = data!.bankName
          this.bankAccountNo    = data!.bankAccountNo
          this.msgBox.showSuccessMessage('Supplier created successifully')
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, '')
        }
      )

    }else{
      //update an existing supplier
      this.spinner.show()
      await this.http.post<ISupplier>(API_URL+'/suppliers/save', supplier, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id               = data?.id
          this.code             = data!.code
          this.name             = data!.name
          this.contactName      = data!.contactName
          this.active           = data!.active
          this.tin              = data!.tin
          this.vrn              = data!.vrn
          this.termsOfContract  = data!.termsOfContract
          this.physicalAddress  = data!.physicalAddress
          this.postCode         = data!.postCode
          this.postAddress      = data!.postAddress
          this.telephone        = data!.telephone
          this.mobile           = data!.mobile
          this.email            = data!.email
          this.fax              = data!.fax
          this.bankAccountName  = data!.bankAccountName
          this.bankPhysicalAddress = data!.bankPhysicalAddress
          this.bankPostCode     = data!.bankPostCode
          this.bankPostAddress  = data!.bankPostAddress
          this.bankName         = data!.bankName
          this.bankAccountNo    = data!.bankAccountNo
          this.msgBox.showSuccessMessage('Supplier updated successifully')
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, '')
        }
      )
    }
  }

  clear(){
    this.id               = null
    this.code             = ''
    this.name             = ''
    this.contactName      = ''
    this.tin              = ''
    this.vrn              = ''
    this.termsOfContract  = ''
    this.physicalAddress  = ''
    this.postCode         = ''
    this.postAddress      = ''
    this.telephone        = ''
    this.mobile           = ''
    this.email            = ''
    this.fax              = ''
    this.bankAccountName  = ''
    this.bankPhysicalAddress = ''
    this.bankPostCode     = ''
    this.bankPostAddress  = ''
    this.bankName         = ''
    this.bankAccountNo    = ''
    this.active           = false
  }


  async searchSupplier(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var code = this.code
    var name = this.name
    if(code != ''){
      name = ''
    }
    
    this.spinner.show()
    await this.http.get<ISupplier>(API_URL+'/suppliers/search?code='+code+'&name='+name, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.id               = data?.id
        this.code             = data!.code
        this.name             = data!.name
        this.contactName      = data!.contactName
        this.active           = data!.active
        this.tin              = data!.tin
        this.vrn              = data!.vrn
        this.termsOfContract  = data!.termsOfContract
        this.physicalAddress  = data!.physicalAddress
        this.postCode         = data!.postCode
        this.postAddress      = data!.postAddress
        this.telephone        = data!.telephone
        this.mobile           = data!.mobile
        this.email            = data!.email
        this.fax              = data!.fax
        this.bankAccountName  = data!.bankAccountName
        this.bankPhysicalAddress = data!.bankPhysicalAddress
        this.bankPostCode     = data!.bankPostCode
        this.bankPostAddress  = data!.bankPostAddress
        this.bankName         = data!.bankName
        this.bankAccountNo    = data!.bankAccountNo
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }


  async loadSupplierNames(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<string[]>(API_URL+'/suppliers/get_names', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.names = []
        data?.forEach(element => {
          this.names.push(element)
        })
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage3('Could not load supplier names')
      }
    )
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