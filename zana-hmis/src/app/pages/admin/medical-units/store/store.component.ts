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
import { IStore } from 'src/app/domain/store';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;
@Component({
  selector: 'app-store',
  templateUrl: './store.component.html',
  styleUrls: ['./store.component.scss'],
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ]
})
export class StoreComponent {


  id              : any
  code            : string = ''
  name            : string = ''
  description     : string = ''
  location        : string = ''
  category        : string = ''
  active          : boolean = true

  stores : IStore[] = []

  filterRecords : string = ''

  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
  ) { }

  ngOnInit(): void {
    this.loadStores()
  }

  public async saveStore(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var store = {
      id              : this.id,
      code            : this.code,
      name            : this.name,
      description     : this.description,
      location        : this.location,
      category        : this.category,
      active          : true
    }
    if(this.id == null || this.id == ''){
      //save a new store
      this.spinner.show()
      await this.http.post<IStore>(API_URL+'/stores/save', store, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id           = data?.id
          this.code         = data!.code
          this.name         = data!.name
          this.description  = data!.description
          this.location     = data!.location
          this.category     = data!.category
          this.active       = data!.active
          this.msgBox.showSuccessMessage('Store created successifully')
          this.loadStores()
          this.clear()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not create store')
        }
      )

    }else{
      //update an existing store
      this.spinner.show()
      await this.http.post<IStore>(API_URL+'/stores/save', store, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id           = data?.id
          this.code         = data!.code
          this.name         = data!.name
          this.description  = data!.description
          this.location     = data!.location
          this.category     = data!.category
          this.active       = data!.active
          this.msgBox.showSuccessMessage('Store updated successifully')
          this.loadStores()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not update store')
        }
      )
    }
  }

  async loadStores(){
    this.stores = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IStore[]>(API_URL+'/stores', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.stores.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load stores')
      }
    )
  }

  clear(){
    this.id           = null
    this.code         = ''
    this.name         = ''
    this.description  = ''
    this.location     = ''
    this.category     = ''
  }

  async getStore(key: string) {
    if(key == ''){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IStore>(API_URL+'/stores/get?id='+key, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        this.id           = data?.id
        this.code         = data!.code
        this.name         = data!.name
        this.description  = data!.description
        this.location     = data!.location
        this.category     = data!.category
        this.active       = data!.active
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, 'Could not find store')
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
