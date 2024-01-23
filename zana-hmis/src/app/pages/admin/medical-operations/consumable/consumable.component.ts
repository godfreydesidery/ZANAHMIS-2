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
import { IConsumable } from 'src/app/domain/consumable';
import { IMedicine } from 'src/app/domain/medicine';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-consumable',
  templateUrl: './consumable.component.html',
  styleUrls: ['./consumable.component.scss'],
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
})
export class ConsumableComponent {
  id          : any = null
  medicine! : IMedicine


  filterRecords : string = ''

  consumables : IConsumable[] = []

  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
  ) { }

  ngOnInit(): void {
    this.loadConsumables()
  }

  public async addConsumable(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var consumable = {
      medicine : {id : this.medicineId}
    }

    this.spinner.show()
    await this.http.post<IConsumable>(API_URL+'/consumables/add', consumable, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.id           = data?.id
        this.medicineId = data?.medicine?.id
        this.msgBox.showSuccessMessage('Consumable created successifully')
        this.loadConsumables()
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not create consumable')
      }
    )

    this.clear()
  }

  async loadConsumables(){
    this.consumables = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IConsumable[]>(API_URL+'/consumables', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.consumables.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load consumables')
      }
    )
  }

  clear(){
    this.id           = null
    this.medicineId = null
    this.medicineName = ''
    this.medicineCode = ''
  }

  async deleteConsumable(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.post(API_URL+'/consumables/delete?id='+id, null, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Deleted')
        this.loadConsumables()
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

  medicineId : any =  null
  medicineCode : string = ''
  medicineName : string = ''
  medicines : IMedicine[] = []
  async loadMedicinesLike(value : string){
    this.medicines = []
    if(value.length < 2){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.get<IMedicine[]>(API_URL+'/medicines/load_medicines_like?name_like='+value, options)
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.medicines = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }
  async getMedicine(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.medicines = []
    this.spinner.show()
    await this.http.get<IMedicine>(API_URL+'/medicines/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {
        this.medicineId = data?.id
        this.medicineCode = data!.code
        this.medicineName = data!.name
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
