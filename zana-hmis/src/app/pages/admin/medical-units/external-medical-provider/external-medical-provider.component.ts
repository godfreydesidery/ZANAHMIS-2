import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterLink } from '@angular/router';
import { NgxSpinnerModule, NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { AuthService } from 'src/app/auth.service';
import { IClinic } from 'src/app/domain/clinic';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IExternalMedicalProvider } from 'src/app/domain/external-medical-provider';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-external-medical-provider',
  templateUrl: './external-medical-provider.component.html',
  styleUrls: ['./external-medical-provider.component.scss'],
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
})
export class ExternalMedicalProviderComponent {

  id          : any = null
  code        : string = ''
  name        : string = ''
  address     : string = ''
  telephone   : string = '' 
  fax         : string = ''
  email       : string = ''
  website     : string = ''
  active      : boolean = false

  externalMedicalProviders : IExternalMedicalProvider[] = []

  externalMedicalProviderNames : string[] = []

  filterRecords : string = ''

  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
  ) { }

  ngOnInit(): void {
    this.loadExternalMedicalProviders()
  }

  public async saveExternalMedicalProvider(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var externalMedicalProvider  = {
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
      //save a new externalMedicalProvider
      this.spinner.show()
      await this.http.post<IExternalMedicalProvider>(API_URL+'/external_medical_providers/save', externalMedicalProvider, options)
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
          this.msgBox.showSuccessMessage('External Medical provider created successifully')
          this.loadExternalMedicalProviders()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not create insurance provider')
        }
      )

    }else{
      this.spinner.show()
      await this.http.post<IExternalMedicalProvider>(API_URL+'/external_medical_providers/save', externalMedicalProvider, options)
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
          this.msgBox.showSuccessMessage('External Medical provider updated successifully')
          this.loadExternalMedicalProviders()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not update external medical provider')
        }
      )
    }
    this.clear()
  }

  async loadExternalMedicalProviders(){
    this.externalMedicalProviders = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IExternalMedicalProvider[]>(API_URL+'/external_medical_providers', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.externalMedicalProviders.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load external medical providers')
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

  async getExternalMedicalProvider(key: string) {
    if(key == ''){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IExternalMedicalProvider>(API_URL+'/external_medical_providers/get?id='+key, options)
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
        this.msgBox.showErrorMessage(error, 'Could not find external medical provider')
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
