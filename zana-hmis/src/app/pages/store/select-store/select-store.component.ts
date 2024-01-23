import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IStore } from 'src/app/domain/store';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-select-store',
  templateUrl: './select-store.component.html',
  styleUrls: ['./select-store.component.scss'],
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    AgePipe,
    RouterLink
  ],
})
export class SelectStoreComponent {

  stores : IStore[] = []
  storeName : string = ''
  store! : IStore

  selectedStoreId : any
  selectedStoreCode : string = ''
  selectedStoreName : string = ''

  filterRecords : string = ''

  constructor(private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService) { }

  async ngOnInit(): Promise<void> {
    if(localStorage.getItem('selected-store-id') != null){
      this.selectedStoreId = localStorage.getItem('selected-store-id')
    }
    if(localStorage.getItem('selected-store-code') != null){
      this.selectedStoreCode = localStorage.getItem('selected-store-code')!.toString()
    }
    if(localStorage.getItem('selected-store-name') != null){
      this.selectedStoreName = localStorage.getItem('selected-store-name')!.toString()
    }  
    this.loadStoresByStorePerson()
  }

  async loadStoresByStorePerson(){
    this.stores = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IStore[]>(API_URL+'/stores/load_stores_by_store_person', options)
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
    this.storeName = ''
  }

  async selectStore(name : string){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    localStorage.removeItem('selected-store-id')
    localStorage.removeItem('selected-store-code')
    localStorage.removeItem('selected-store-name')
    this.spinner.show()
    await this.http.get<IStore>(API_URL+'/stores/get_by_name?name='+name, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {
        localStorage.setItem('selected-store-id', data?.id)
        localStorage.setItem('selected-store-code', data!.code)
        localStorage.setItem('selected-store-name', data!.name)

        this.selectedStoreId   = data?.id
        this.selectedStoreCode = data!.code
        this.selectedStoreName = data!.name
      }
    )
    .catch(
      error => {
        localStorage.setItem('selected-store-id', this.selectedStoreId)
        localStorage.setItem('selected-store-code', this.selectedStoreCode)
        localStorage.setItem('selected-store-name', this.selectedStoreName)

        this.msgBox.showErrorMessage(error, '')
        console.log(error)
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
