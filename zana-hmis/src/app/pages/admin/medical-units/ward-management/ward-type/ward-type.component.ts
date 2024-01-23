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
import { IWardType } from 'src/app/domain/ward-type';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-ward-type',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './ward-type.component.html',
  styleUrls: ['./ward-type.component.scss']
})
export class WardTypeComponent implements OnInit {


  id              : any
  code            : string = ''
  name            : string = ''
  description     : string = ''

  price : number = 0

  active          : boolean = true

  wardTypes : IWardType[] = []

  filterRecords : string = ''

  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
  ) { }

  ngOnInit(): void {
    this.loadWardTypes()
  }

  public async saveWardType(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var wardType = {
      id              : this.id,
      code            : this.code,
      name            : this.name,
      price           : this.price,
      description     : this.description,
      active          : true
    }
    if(this.id == null || this.id == ''){
      //save a new wardType
      this.spinner.show()
      await this.http.post<IWardType>(API_URL+'/ward_types/save', wardType, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id           = data?.id
          this.code         = data!.code
          this.name         = data!.name
          this.price        = data!.price
          this.description  = data!.description
          this.msgBox.showSuccessMessage('Ward type created successifully')
          this.loadWardTypes()
          this.clear()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not create ward type')
        }
      )

    }else{
      //update an existing wardType
      this.spinner.show()
      await this.http.post<IWardType>(API_URL+'/ward_types/save', wardType, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id           = data?.id
          this.code         = data!.code
          this.name         = data!.name
          this.price        = data!.price
          this.description  = data!.description
          this.msgBox.showSuccessMessage('Ward type updated successifully')
          this.loadWardTypes()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not update ward type')
        }
      )
    }
  }

  async loadWardTypes(){
    this.wardTypes = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IWardType[]>(API_URL+'/ward_types', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.wardTypes.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load ward types')
      }
    )
  }

  clear(){
    this.id           = null
    this.code         = ''
    this.name         = ''
    this.price        = 0
    this.description  = ''
  }

  async getWardType(key: string) {
    if(key == ''){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IWardType>(API_URL+'/ward_types/get?id='+key, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        this.id           = data?.id
        this.code         = data!.code
        this.name         = data!.name
        this.price        = data!.price
        this.description  = data!.description
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, 'Could not find ward type')
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