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
import { IStorePerson } from 'src/app/domain/store-person';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-store-person',
  templateUrl: './store-person.component.html',
  styleUrls: ['./store-person.component.scss'],
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
})
export class StorePersonComponent {
  

  id : any
  code : string = ''
  firstName : string = ''
  middleName : string = ''
  lastName : string = ''
  type : string = ''
  active : boolean = true

  public stores : IStore[]


  storePersons : IStorePerson[] = []

  storePersonStores : IStore[] = []

  userCode : string = ''

  filterRecords : string = ''


  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
  ) {
    this.stores           = []
  }

  ngOnInit(): void {
    this.loadActiveStorePersons()
    this.loadStores()
  }

  public async saveStorePerson(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var storePersonStores : IStore[] = []
    this.stores.forEach(store => { /**Get the roles */
      if(store.assigned == true){
        storePersonStores.push(store)
      }
    })
    var storePerson = {
      id          : this.id,
      code        : this.code,
      firstName   : this.firstName,
      middleName  : this.middleName,
      lastName    : this.lastName,
      type        : this.type,
      stores      : storePersonStores,
      active      : true
    }
    if(this.id == null || this.id == ''){
      //save a new clinic
      this.spinner.show()
      await this.http.post<IStorePerson>(API_URL+'/store_persons/save', storePerson, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id         = data?.id
          this.code       = data!.code
          this.firstName  = data!.firstName
          this.middleName = data!.middleName
          this.lastName   = data!.lastName
          this.type       = data!.type
          //this.clinics    = data!.clinics
          this.active     = data!.active
          this.msgBox.showSuccessMessage('StorePerson created successifully')
          this.loadActiveStorePersons()
          this.clear()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, '')
        }
      )

    }else{
      //update an existing clinic
      this.spinner.show()
      await this.http.post<IStorePerson>(API_URL+'/store_persons/save', storePerson, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id         = data?.id
          this.code       = data!.code
          this.firstName  = data!.firstName
          this.middleName = data!.middleName
          this.lastName   = data!.lastName
          this.type       = data!.type
          //this.clinics    = data!.clinics
          this.active     = data!.active
          this.msgBox.showSuccessMessage('StorePerson updated successifully')
          this.loadActiveStorePersons()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, '')
        }
      )
    }
  }

  async loadActiveStorePersons(){
    this.storePersons = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IStorePerson[]>(API_URL+'/store_persons/get_all_active', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.storePersons.push(element)
        })
      }
    )
    .catch(
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, 'Could not load store_persons')
      }
    )
  }

  clear(){
    this.id         = null
    this.code       = ''
    this.firstName  = ''
    this.middleName = ''
    this.lastName   = ''
    this.type       = ''
    this.active     = false
  }

  async getStorePerson(key: string) {
    if(key == ''){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IStorePerson>(API_URL+'/store_persons/get?id='+key, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        console.log(data)
        this.id         = data?.id
        this.code       = data!.code
        this.firstName  = data!.firstName
        this.middleName = data!.middleName
        this.lastName   = data!.lastName
        this.type       = data!.type
        this.active     = data!.active

        this.showUserRoles(this.stores, data!['stores'])
        
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, 'Could not find Store Person')
      }
    )
  }

  showStorePersonStores(stores : IStore[], storePersonStores : IStore[]){
    /**
     * Display user roles, the roles for that particular user are checked
     * args: roles-global user roles, userRoles-roles for a specific user
     */
    /** First uncheck all roles */
    this.clearStores()
    /** Now, check the respective  roles */
    storePersonStores.forEach(storePersonStore => {
      stores.forEach(store => {        
        if(store.name === storePersonStore.name){
          store.assigned = true
        }
      })
    })
    this.stores = stores
  }

 

  showUserRoles(stores : IStore[], storePersonStores : IStore[]){
    /**
     * Display user roles, the roles for that particular user are checked
     * args: roles-global user roles, userRoles-roles for a specific user
     */
    /** First uncheck all roles */
    this.clearStores()
    /** Now, check the respective  roles */
    storePersonStores.forEach(storePersonStore => {
      stores.forEach(store => {        
        if(store.name === storePersonStore.name){
          store.assigned = true
        }
      })
    })
    this.stores = stores
  }

  clearStores(){
    /**Uncheck all roles */
    this.stores.forEach(store => {
      store.assigned = false
    })
  }

  async loadStores(){  
    /**Get all roles */
     let options = {
       headers : new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
     }
     this.spinner.show()
     await this.http.get<IStore[]>(API_URL+'/stores', options)
     .pipe(finalize(() => this.spinner.hide()))
     .toPromise()
     .then(
       data => {
         data?.forEach(
           element => {
             console.log(element)
             this.stores.push(element)
           }
         )
       }
     )
     .catch(error => {
       console.log(error)
     })
   }

   clearUser(){
     this.userCode = ''
   }

   public async assignUserProfile(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    if(this.id != null){
      this.spinner.show()
      await this.http.post<IStorePerson>(API_URL+'/store_persons/assign_user_profile?id='+this.id+'&code='+this.userCode, null, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id         = data?.id
          this.code       = data!.code
          this.firstName  = data!.firstName
          this.middleName = data!.middleName
          this.lastName   = data!.lastName
          this.type       = data!.type
          this.stores    = data!.stores
          this.active     = data!.active
          this.msgBox.showSuccessMessage('Saved successifully')
          this.loadActiveStorePersons()
          this.clear()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, '')
        }
      )

    }
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
