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
import { ILabTestType } from 'src/app/domain/lab-test-type';
import { ILabTestTypeRange } from 'src/app/domain/lab-test-type-range';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-lab-test-type-range',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './lab-test-type-range.component.html',
  styleUrls: ['./lab-test-type-range.component.scss']
})
export class LabTestTypeRangeComponent implements OnInit {
  name        : string = ''
  active      : boolean = true

  labTestType! : ILabTestType

  labTestTypes : ILabTestType[] = []

  labTestTypeRanges : ILabTestTypeRange[] = []

  labTestTypeNames : string[] = []

  labTestTypeName : string = ''

  filterRecords : string = ''

  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
  ) { }

  ngOnInit(): void {
    this.loadLabTestTypes()
    //this.loadLabTestTypeRanges()
  }

  labTestTypeRangeName : string = ''
  public async saveLabTestTypeRange(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var labTestTypeRange = {
      name        : this.labTestTypeRangeName,
      labTestType : { id : this.labTestTypeId},
      active      : true
    }
    this.spinner.show()
      await this.http.post<ILabTestTypeRange>(API_URL+'/lab_test_type_ranges/save', labTestTypeRange, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.loadLabTestTypeRanges(this.labTestTypeId)
          this.msgBox.showSuccessMessage('Range created successifully')
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, '')
        }
      )
    this.clear()
  }

  async loadLabTestTypes(){
    this.labTestTypes = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<ILabTestType[]>(API_URL+'/lab_test_types', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.labTestTypes = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not Lab Test Types')
      }
    )
  }

  async loadLabTestTypeRanges(id : any){
    this.labTestTypeRanges = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<ILabTestTypeRange[]>(API_URL+'/lab_test_type_ranges?lab_test_type_id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.labTestTypeRanges.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  clear(){
    this.labTestTypeRangeName = ''
  }

  labTestTypeId : any = null
  async getLabTestType(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<ILabTestType>(API_URL+'/lab_test_types/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.labTestTypeId = data?.id
        this.labTestType = data!
        this.loadLabTestTypeRanges(this.labTestTypeId)
      }
    )
    .catch(
      error => {
        this.labTestTypeId = null
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async deleteRange(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<ILabTestType>(API_URL+'/lab_test_type_ranges/delete?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Deleted')
        this.loadLabTestTypeRanges(this.labTestTypeId)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
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