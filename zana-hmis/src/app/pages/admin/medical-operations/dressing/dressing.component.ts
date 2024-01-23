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
import { IDressing } from 'src/app/domain/dressing';
import { IProcedureType } from 'src/app/domain/procedure-type';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-dressing',
  templateUrl: './dressing.component.html',
  styleUrls: ['./dressing.component.scss'],
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
})
export class DressingComponent {
  id          : any = null
  procedureType! : IProcedureType


  filterRecords : string = ''

  dressings : IDressing[] = []

  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
  ) { }

  ngOnInit(): void {
    this.loadDressings()
  }

  public async addDressing(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var dressing = {
      procedureType : {id : this.procedureTypeId}
    }

    this.spinner.show()
    await this.http.post<IDressing>(API_URL+'/dressings/add', dressing, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.id           = data?.id
        this.procedureTypeId = data?.procedureType?.id
        this.msgBox.showSuccessMessage('Dressing created successifully')
        this.loadDressings()
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )

    this.clear()
  }

  async loadDressings(){
    this.dressings = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IDressing[]>(API_URL+'/dressings', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.dressings.push(element)
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
    this.id           = null
    this.procedureTypeId = null
    this.procedureTypeName = ''
    this.procedureTypeCode = ''
  }

  async deleteDressing(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.post(API_URL+'/dressings/delete?id='+id, null, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Deleted')
        this.loadDressings()
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

  procedureTypeId : any =  null
  procedureTypeCode : string = ''
  procedureTypeName : string = ''
  procedureTypes : IProcedureType[] = []
  async loadProcedureTypesLike(value : string){
    this.procedureTypes = []
    if(value.length < 2){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.get<IProcedureType[]>(API_URL+'/procedure_types/load_procedure_types_like?name_like='+value, options)
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.procedureTypes = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }
  async getProcedureType(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.procedureTypes = []
    this.spinner.show()
    await this.http.get<IProcedureType>(API_URL+'/procedure_types/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {
        this.procedureTypeId = data?.id
        this.procedureTypeCode = data!.code
        this.procedureTypeName = data!.name
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }

  
}
