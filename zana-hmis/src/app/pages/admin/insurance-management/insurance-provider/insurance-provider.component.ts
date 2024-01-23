import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterLink } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerModule, NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { AuthService } from 'src/app/auth.service';
import { IInsuranceProvider } from 'src/app/domain/insurance-provider';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-insurance-provider',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './insurance-provider.component.html',
  styleUrls: ['./insurance-provider.component.scss']
})
export class InsuranceProviderComponent implements OnInit {

  id          : any = null
  code        : string = ''
  name        : string = ''
  address     : string = ''
  telephone   : string = '' 
  fax         : string = ''
  email       : string = ''
  website     : string = ''
  active      : boolean = false

  insuranceProviders : IInsuranceProvider[] = []

  insuranceProviderNames : string[] = []

  filterRecords : string = ''

  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
  ) { }

  ngOnInit(): void {
    this.loadInsuranceProviders()
  }

  public async saveInsuranceProvider(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var insuranceProvider  = {
      id        : this.id,
      code      : this.code,
      name      : this.name,
      address   : this.address,
      telephone : this.telephone,
      fax       : this.fax,
      email     : this.email,
      website   : this.website,
      active    : false
    }
    if(this.id == null || this.id == ''){
      //save a new insuranceProvider
      this.spinner.show()
      await this.http.post<IInsuranceProvider>(API_URL+'/insurance_providers/save', insuranceProvider, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id         = data?.id
          this.code       = data!.code
          this.name       = data!.name
          this.address    = data!.address
          this.telephone  = data!.telephone
          this.fax        = data!.fax
          this.email      = data!.email
          this.website    = data!.website
          this.active     = data!.active
          this.msgBox.showSuccessMessage('Insurance provider created successifully')
          this.loadInsuranceProviders()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not create insurance provider')
        }
      )

    }else{
      this.spinner.show()
      await this.http.post<IInsuranceProvider>(API_URL+'/insurance_providers/save', insuranceProvider, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id         = data?.id
          this.code       = data!.code
          this.name       = data!.name
          this.address    = data!.address
          this.telephone  = data!.telephone
          this.fax        = data!.fax
          this.email      = data!.email
          this.website    = data!.website
          this.active     = data!.active
          this.msgBox.showSuccessMessage('Insurance provider updated successifully')
          this.loadInsuranceProviders()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not update insurance provider')
        }
      )
    }
    this.clear()
  }

  async loadInsuranceProviders(){
    this.insuranceProviders = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IInsuranceProvider[]>(API_URL+'/insurance_providers', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.insuranceProviders.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load insurance providers')
      }
    )
  }

  clear(){
    this.id         = null
    this.code       = ''
    this.name       = ''
    this.address    = ''
    this.telephone  = ''
    this.fax        = ''
    this.email      = ''
    this.website    = ''
    this.active     = false
  }

  async getInsuranceProvider(key: string) {
    if(key == ''){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IInsuranceProvider>(API_URL+'/insurance_providers/get?id='+key, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        this.id         = data?.id
        this.code       = data!.code
        this.name       = data!.name
        this.address    = data!.address
        this.telephone  = data!.telephone
        this.fax        = data!.fax
        this.email      = data!.email
        this.website    = data!.website
        this.active     = data!.active
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, 'Could not find insurance provider')
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
