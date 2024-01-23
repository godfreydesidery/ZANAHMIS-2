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
import { IRadiologyType } from 'src/app/domain/radiology-type';
import { IRadiologyTypePrice } from 'src/app/domain/radiology-type-price';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-radiology-type-price',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './radiology-type-price.component.html',
  styleUrls: ['./radiology-type-price.component.scss']
})
export class RadiologyTypePriceComponent {
  insurancePlanId : any = null
  insurancePlanName : string = ''

  radiologyTypes : IRadiologyType[] = []

  radiologyTypePrices : IRadiologyTypePrice[] = []

  filterRecords : string = ''

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

    this.loadRadiologyTypesByInsurance(this.insurancePlanId)
  }

  async loadRadiologyTypesByInsurance(insurancePlanId : any){
    this.radiologyTypes = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IRadiologyTypePrice[]>(API_URL+'/insurance_plans/get_radiology_type_prices?insurance_plan_id=' + insurancePlanId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.radiologyTypePrices= data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load Radiology types')
      }
    )
  }

  async changeRadiologyTypeCoverage(insurancePlanId : any, radiologyTypeId : any, covered : boolean){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var coverage = {
      radiologyType : {
        id : radiologyTypeId
      },
      radiologyTypeInsurancePlan : {
        insurancePlan : {
          id : insurancePlanId
        },
        covered : covered
      },
    }
    this.spinner.show()
    await this.http.post<IRadiologyTypePrice>(API_URL+'/insurance_plans/change_radiology_type_coverage', coverage, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.radiologyTypePrices.forEach(element => {
          if(data!.radiologyType.id === element.radiologyType.id){
            element.radiologyTypeInsurancePlan.covered = data!.radiologyTypeInsurancePlan.covered
          }
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not change coverage')
      }
    )
  }

  async updateRadiologyTypePriceByInsurance(insurancePlanId : any, radiologyTypeId : any, price : number){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var update = {
      radiologyType : {
        id : radiologyTypeId
      },
      radiologyTypeInsurancePlan : {
        insurancePlan : {
          id : insurancePlanId
        }
      },
      price : price
    }
    this.spinner.show()
    await this.http.post<IRadiologyTypePrice>(API_URL+'/insurance_plans/update_radiology_type_price_by_insurance', update, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.radiologyTypePrices.forEach(element => {
          if(data!.radiologyType.id === element.radiologyType.id){
            element.price = data!.price
            element.covered = data!.covered
          }
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