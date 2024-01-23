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
import { ILabTestType } from 'src/app/domain/lab-test-type';
import { ILabTestTypePrice } from 'src/app/domain/lab-test-type-price';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-lab-test-type-price',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './lab-test-type-price.component.html',
  styleUrls: ['./lab-test-type-price.component.scss']
})
export class LabTestTypePriceComponent {

  insurancePlanId : any = null
  insurancePlanName : string = ''

  labTestTypes : ILabTestType[] = []

  labTestTypePrices : ILabTestTypePrice[] = []

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

    this.loadLabTestTypesByInsurance(this.insurancePlanId)
  }

  async loadLabTestTypesByInsurance(insurancePlanId : any){
    this.labTestTypes = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<ILabTestTypePrice[]>(API_URL+'/insurance_plans/get_lab_test_type_prices?insurance_plan_id=' + insurancePlanId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.labTestTypePrices= data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load Lab test types')
      }
    )
  }

  async changeLabTestTypeCoverage(insurancePlanId : any, labTestTypeId : any, covered : boolean){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var coverage = {
      labTestType : {
        id : labTestTypeId
      },
      labTestTypeInsurancePlan : {
        insurancePlan : {
          id : insurancePlanId
        },
        covered : covered
      },
    }
    this.spinner.show()
    await this.http.post<ILabTestTypePrice>(API_URL+'/insurance_plans/change_lab_test_type_coverage', coverage, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.labTestTypePrices.forEach(element => {
          if(data!.labTestType.id === element.labTestType.id){
            element.labTestTypeInsurancePlan.covered = data!.labTestTypeInsurancePlan.covered
          }
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not chenge coverage')
      }
    )
  }

  async updateLabTestTypePriceByInsurance(insurancePlanId : any, labTestTypeId : any, price : number){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var update = {
      labTestType : {
        id : labTestTypeId
      },
      labTestTypeInsurancePlan : {
        insurancePlan : {
          id : insurancePlanId
        }
      },
      price : price
    }
    this.spinner.show()
    await this.http.post<ILabTestTypePrice>(API_URL+'/insurance_plans/update_lab_test_type_price_by_insurance', update, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.labTestTypePrices.forEach(element => {
          if(data!.labTestType.id === element.labTestType.id){
            element.price = data!.price
            element.covered = data!.covered
          }
        })
        this.msgBox.showSuccessMessage('Price updated successifully')
      }
    )
    .catch(
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, 'Could not update price')
      }
    )
  }

}
