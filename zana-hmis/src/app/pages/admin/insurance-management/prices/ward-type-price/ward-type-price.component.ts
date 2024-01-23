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
import { IWardType } from 'src/app/domain/ward-type';
import { IWardTypePrice } from 'src/app/domain/ward-type-price';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-ward-type-price',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './ward-type-price.component.html',
  styleUrls: ['./ward-type-price.component.scss']
})
export class WardTypePriceComponent implements OnInit {
  
  insurancePlanId : any = null
  insurancePlanName : string = ''

  wardTypes : IWardType[] = []

  wardTypePrices : IWardTypePrice[] = []

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

    this.loadWardTypesByInsurance(this.insurancePlanId)
  }

  async loadWardTypesByInsurance(insurancePlanId : any){
    this.wardTypes = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IWardTypePrice[]>(API_URL+'/insurance_plans/get_ward_type_prices?insurance_plan_id=' + insurancePlanId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.wardTypePrices= data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load plans')
      }
    )
  }

  async changeWardTypeCoverage(insurancePlanId : any, wardTypeId : any, covered : boolean){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var coverage = {
      wardType : {
        id : wardTypeId
      },
      wardTypeInsurancePlan : {
        insurancePlan : {
          id : insurancePlanId
        },
        covered : covered
      },
    }
    this.spinner.show()
    await this.http.post<IWardTypePrice>(API_URL+'/insurance_plans/change_ward_type_coverage', coverage, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.wardTypePrices.forEach(element => {
          if(data!.wardType.id === element.wardType.id){
            element.wardTypeInsurancePlan.covered = data!.wardTypeInsurancePlan.covered
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

  async updateWardTypePriceByInsurance(insurancePlanId : any, wardTypeId : any, price : number){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var update = {
      wardType : {
        id : wardTypeId
      },
      wardTypeInsurancePlan : {
        insurancePlan : {
          id : insurancePlanId
        }
      },
      price : price
    }
    this.spinner.show()
    await this.http.post<IWardTypePrice>(API_URL+'/insurance_plans/update_ward_type_price_by_insurance', update, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.wardTypePrices.forEach(element => {
          if(data!.wardType.id === element.wardType.id){
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