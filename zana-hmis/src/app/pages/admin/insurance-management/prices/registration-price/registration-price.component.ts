import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterLink } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerModule, NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { AuthService } from 'src/app/auth.service';
import { IClinic } from 'src/app/domain/clinic';
import { IRegistrationPrice } from 'src/app/domain/registration-price';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-registration-price',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './registration-price.component.html',
  styleUrls: ['./registration-price.component.scss']
})
export class RegistrationPriceComponent {

  insurancePlanId : any = null
  insurancePlanName : string = ''

  clinics : IClinic[] = []

  registrationPrices : IRegistrationPrice[] = []

  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
  ) { }

  ngOnInit(): void {
    this.insurancePlanId = localStorage.getItem('insurance_plan_id')!
    this.insurancePlanName = localStorage.getItem('insurance_plan_name')!
    localStorage.removeItem('insurance_plan_id')
    localStorage.removeItem('insurance_plan_name')

    this.loadRegistrationsByInsurance(this.insurancePlanId)
  }

  async loadRegistrationsByInsurance(insurancePlanId : any){
    this.clinics = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IRegistrationPrice[]>(API_URL+'/insurance_plans/get_registration_prices?insurance_plan_id=' + insurancePlanId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.registrationPrices= data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load registration plans')
      }
    )
  }

  async changeRegistrationCoverage(insurancePlanId : any, covered : boolean){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var coverage = {
      registrationInsurancePlan : {
        insurancePlan : {
          id : insurancePlanId
        },
        covered : covered
      },
    }
    this.spinner.show()
    await this.http.post<IRegistrationPrice>(API_URL+'/insurance_plans/change_registration_coverage', coverage, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.registrationPrices.forEach(element => {
          element.registrationInsurancePlan.covered = data!.registrationInsurancePlan.covered
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not change coverage')
      }
    )
  }

  async updateRegistrationPriceByInsurance(insurancePlanId : any, price : number){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var update = {
      registrationInsurancePlan : {
        insurancePlan : {
          id : insurancePlanId
        }
      },
      price : price
    }
    this.spinner.show()
    await this.http.post<IRegistrationPrice>(API_URL+'/insurance_plans/update_registration_price_by_insurance', update, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.registrationPrices.forEach(element => {
          element.price = data!.price
          element.covered = data!.covered
        })
        this.msgBox.showSuccessMessage('Price updated successifully')
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not update price')
      }
    )
  }

}
