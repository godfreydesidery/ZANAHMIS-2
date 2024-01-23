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
import { IProcedureType } from 'src/app/domain/procedure-type';
import { IProcedureTypePrice } from 'src/app/domain/procedure-type-price';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-procedure-type-price',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './procedure-type-price.component.html',
  styleUrls: ['./procedure-type-price.component.scss']
})
export class ProcedureTypePriceComponent {
  insurancePlanId : any = null
  insurancePlanName : string = ''

  procedureTypes : IProcedureType[] = []

  procedureTypePrices : IProcedureTypePrice[] = []

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

    this.loadProcedureTypesByInsurance(this.insurancePlanId)
  }

  async loadProcedureTypesByInsurance(insurancePlanId : any){
    this.procedureTypes = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IProcedureTypePrice[]>(API_URL+'/insurance_plans/get_procedure_type_prices?insurance_plan_id=' + insurancePlanId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.procedureTypePrices= data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load procedure types')
      }
    )
  }

  async changeProcedureTypeCoverage(insurancePlanId : any, procedureTypeId : any, covered : boolean){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var coverage = {
      procedureType : {
        id : procedureTypeId
      },
      procedureTypeInsurancePlan : {
        insurancePlan : {
          id : insurancePlanId
        },
        covered : covered
      },
    }
    this.spinner.show()
    await this.http.post<IProcedureTypePrice>(API_URL+'/insurance_plans/change_procedure_type_coverage', coverage, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.procedureTypePrices.forEach(element => {
          if(data!.procedureType.id === element.procedureType.id){
            element.procedureTypeInsurancePlan.covered = data!.procedureTypeInsurancePlan.covered
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

  async updateProcedureTypePriceByInsurance(insurancePlanId : any, procedureTypeId : any, price : number){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var update = {
      procedureType : {
        id : procedureTypeId
      },
      procedureTypeInsurancePlan : {
        insurancePlan : {
          id : insurancePlanId
        }
      },
      price : price
    }
    this.spinner.show()
    await this.http.post<IProcedureTypePrice>(API_URL+'/insurance_plans/update_procedure_type_price_by_insurance', update, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.procedureTypePrices.forEach(element => {
          if(data!.procedureType.id === element.procedureType.id){
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
