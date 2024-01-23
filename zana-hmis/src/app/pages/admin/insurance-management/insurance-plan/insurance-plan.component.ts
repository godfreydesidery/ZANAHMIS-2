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
import { IInsurancePlan } from 'src/app/domain/insurance-plan';
import { IInsuranceProvider } from 'src/app/domain/insurance-provider';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;
@Component({
  selector: 'app-insurance-plan',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './insurance-plan.component.html',
  styleUrls: ['./insurance-plan.component.scss']
})
export class InsurancePlanComponent implements OnInit {
  id          : any = null
  code        : string = ''
  name        : string = ''
  description : string = ''
  active      : boolean = true

  insuranceProvider! : IInsuranceProvider

  insurancePlans : IInsurancePlan[] = []

  insuranceProviderNames : string[] = []

  insuranceProviderName : string = ''

  filterRecords : string = ''

  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
  ) { }

  ngOnInit(): void {
    this.loadInsurancePlans()
    this.loadInsuranceProviderNames()
  }

  public async saveInsurancePlan(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var insurancePlan = {
      id          : this.id,
      code          : this.code,
      name        : this.name,
      description : this.description,
      insuranceProvider : { name : this.insuranceProviderName},
      active      : true
    }
    if(this.id == null || this.id == ''){
      //save a new insurancePlan
      this.spinner.show()
      await this.http.post<IInsurancePlan>(API_URL+'/insurance_plans/save', insurancePlan, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id           = data?.id
          this.code         = data!.code
          this.name         = data!.name
          this.description  = data!.description
          this.active       = data!.active
          this.msgBox.showSuccessMessage('Insurance Plan created successifully')
          this.loadInsurancePlans()
          
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not create insurance plan')
        }
      )

    }else{
      //update an existing clinic
      this.spinner.show()
      await this.http.post<IInsurancePlan>(API_URL+'/insurance_plans/save', insurancePlan, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id           = data?.id
          this.code         = data!.code
          this.name         = data!.name
          this.description  = data!.description
          this.active       = data!.active
          this.msgBox.showSuccessMessage('Insurance Plan updated successifully')
          this.loadInsurancePlans()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not update insurance plan')
        }
      )
    }
    this.clear()
  }

  async loadInsurancePlans(){
    this.insurancePlans = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IInsurancePlan[]>(API_URL+'/insurance_plans', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.insurancePlans.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load insurance plans')
      }
    )
  }

  clear(){
    this.id                     = null
    this.code                   = ''
    this.name                   = ''
    this.insuranceProviderName  = ''
    this.description            = ''
    this.active                 = false
  }

  async getInsurancePlan(key: string) {
    if(key == ''){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IInsurancePlan>(API_URL+'/insurance_plans/get?id='+key, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        this.id                     = data?.id
        this.code                   = data!.code
        this.name                   = data!.name
        this.insuranceProviderName  = data!.insuranceProvider.name
        this.description            = data!.description
        this.active                 = data!.active
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, 'Could not find insurance plan')
      }
    )
  }

  async loadInsuranceProviderNames(){
    this.insuranceProviderNames = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<string[]>(API_URL+'/insurance_providers/get_names', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.insuranceProviderNames.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load Providers')
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



