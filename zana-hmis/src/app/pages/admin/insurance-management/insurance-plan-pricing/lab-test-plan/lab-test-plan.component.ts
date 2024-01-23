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
import { IInsurancePlan } from 'src/app/domain/insurance-plan';
import { ILabTestTypeInsurancePlan } from 'src/app/domain/lab-test-type-insurance-plan';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-lab-test-plan',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './lab-test-plan.component.html',
  styleUrls: ['./lab-test-plan.component.scss']
})
export class LabTestPlanComponent implements OnInit {
  id : any = null
  insurancePlan! : IInsurancePlan
  price : number = 0

  insuranceProviderName : string = ''
  insurancePlanName : string = ''

  insuranceProviderNames : string[] = []
  insurancePlanNames : string[] = []

  labTestTypeName : string = ''
  labTestTypeNames : string[] = []

  labTestTypeInsurancePlans : ILabTestTypeInsurancePlan[] = []

  insurancePlans : IInsurancePlan[] = []

  filterRecords : string = ''

  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
  ) { }

  ngOnInit(): void {
    this.loadInsurancePlans()
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
        this.insurancePlans = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async loadInsurancePlanNamesByProviders(providerName : string){
    this.insurancePlanNames = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<string[]>(API_URL+'/insurance_plans/get_names_by_insurance_provider?provider_name='+providerName, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.insurancePlanNames.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load Plans')
      }
    )
  }

  async loadLabTestTypeInsurancePlans(){
    this.labTestTypeInsurancePlans = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<ILabTestTypeInsurancePlan[]>(API_URL+'/lab_test_type_insurance_plans', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.labTestTypeInsurancePlans.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load labTestType plans')
      }
    )
  }


  public async saveLabTestTypeInsurancePlan(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var labTestTypeInsurancePlan = {
      id          : this.id,
      labTestType : {
        name : this.labTestTypeName
      },
      insurancePlan          : {
        name : this.insurancePlanName
      },
      price        : this.price
    }
    if(this.id == null || this.id == ''){
      //save a new diagnosisType
      this.spinner.show()
      await this.http.post<ILabTestTypeInsurancePlan>(API_URL+'/lab_test_type_insurance_plans/save', labTestTypeInsurancePlan, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id           = data?.id
          this.msgBox.showSuccessMessage('LabTestType plan created successifully')
          this.loadLabTestTypeInsurancePlans()
          
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not create labTestType plan')
        }
      )

    }else{
      //update an existing labTestType
      this.spinner.show()
      await this.http.post<ILabTestTypeInsurancePlan>(API_URL+'/lab_test_type_insurance_plans/save', labTestTypeInsurancePlan, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id           = data?.id
          
          this.msgBox.showSuccessMessage('LabTestType plan updated successifully')
          this.loadLabTestTypeInsurancePlans()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not update labTestType plan')
        }
      )
    }
    this.clear()
  }

  clear(){
    this.id = null
    this.labTestTypeName = ''
    this.insuranceProviderName = ''
    this.insurancePlanName = ''
    this.price = 0
  }

  async getLabTestTypeInsurancePlan(key: string) {
    if(key == ''){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<ILabTestTypeInsurancePlan>(API_URL+'/lab_test_type_insurance_plans/get?id='+key, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        console.log(data)
        this.id                    = data?.id
        this.labTestTypeName            = data!.labTestType.name
        this.insuranceProviderName = data!.insurancePlan.insuranceProvider.name
        this.insurancePlanName     = data!.insurancePlan.name
        this.price       = data!.price
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, 'Could not find labTestType plan')
      }
    )
  }

  async deleteLabTestTypeInsurancePlan(key: string) {
    if(key == ''){
      return
    }
    if(!window.confirm('Delete this plan? Plan ID: '+key)){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.post<ILabTestTypeInsurancePlan>(API_URL+'/lab_test_type_insurance_plans/delete?id='+key, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        console.log(data)
        this.loadLabTestTypeInsurancePlans()
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, 'Could not delete labTestType plan')
      }
    )
  }

  async loadLabTestTypeNames(){
    this.labTestTypeNames = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<string[]>(API_URL+'/lab_test_types/get_names', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.labTestTypeNames.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load lab_test_types')
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

  setInsurancePlanId(id : any, name : string){
    localStorage.setItem('insurance_plan_id', id)
    localStorage.setItem('insurance_plan_name', name)
  }

}



