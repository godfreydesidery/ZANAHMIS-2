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
import { IMedicineInsurancePlan } from 'src/app/domain/medicine-insurance-plan';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-medicine-plan',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './medicine-plan.component.html',
  styleUrls: ['./medicine-plan.component.scss']
})
export class MedicinePlanComponent implements OnInit {
  id : any = null
  insurancePlan! : IInsurancePlan
  price : number = 0

  insuranceProviderName : string = ''
  insurancePlanName : string = ''

  insuranceProviderNames : string[] = []
  insurancePlanNames : string[] = []

 medicineName : string = ''
 medicineNames : string[] = []

 medicineInsurancePlans : IMedicineInsurancePlan[] = []

 insurancePlans : IInsurancePlan[] = []

 filterRecords : string = ''

  constructor(
    private auth : AuthService,
    private http :HttpClient,
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
        this.msgBox.showErrorMessage(error, 'Could not load Insurance Providers')
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
        this.msgBox.showErrorMessage(error, 'Could not load Insurance Plans')
      }
    )
  }

  async loadMedicineInsurancePlans(){
    this.medicineInsurancePlans = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IMedicineInsurancePlan[]>(API_URL+'/medicine_insurance_plans', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.medicineInsurancePlans.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load Medicines')
      }
    )
  }


  public async saveMedicineInsurancePlan(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var medicinePlan = {
      id          : this.id,
     medicine : {
        name : this.medicineName
      },
      insurancePlan          : {
        name : this.insurancePlanName
      },
      price        : this.price
    }
    if(this.id == null || this.id == ''){
      this.spinner.show()
      await this.http.post<IMedicineInsurancePlan>(API_URL+'/medicine_insurance_plans/save',medicinePlan, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id           = data?.id
          this.msgBox.showSuccessMessage('Medicine Insurance Plan created successifully')
          this.loadMedicineInsurancePlans()
          
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not create Medicine Insurance Plan')
        }
      )

    }else{
      //update an existingmedicine
      this.spinner.show()
      await this.http.post<IMedicineInsurancePlan>(API_URL+'/medicine_insurance_plans/save',medicinePlan, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id           = data?.id
          
          this.msgBox.showSuccessMessage('Medicine Insurance Plan updated successifully')
          this.loadMedicineInsurancePlans()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not update Medicine Insurance Plan')
        }
      )
    }
    this.clear()
  }

  clear(){
    this.id                     = null
    this.medicineName           = ''
    this.insuranceProviderName  = ''
    this.insurancePlanName      = ''
    this.price                  = 0
  }

  async getMedicineInsurancePlan(key: string) {
    if(key == ''){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IMedicineInsurancePlan>(API_URL+'/medicine_insurance_plans/get?id='+key, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        console.log(data)
        this.id                     = data?.id
        this.medicineName           = data!.medicine.name
        this.insuranceProviderName  = data!.insurancePlan.insuranceProvider.name
        this.insurancePlanName      = data!.insurancePlan.name
        this.price                  = data!.price
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, 'Could not find Medicine Insurance Plan')
      }
    )
  }

  async deleteMedicineInsurancePlan(key: string) {
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
    await this.http.post<IMedicineInsurancePlan>(API_URL+'/medicine_insurance_plans/delete?id='+key, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        console.log(data)
        this.loadMedicineInsurancePlans()
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, 'Could not delete Medicine Insurance Plan')
      }
    )
  }

  async loadMedicineNames(){
    this.medicineNames = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<string[]>(API_URL+'/medicines/get_names', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.medicineNames.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load Medicines')
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

