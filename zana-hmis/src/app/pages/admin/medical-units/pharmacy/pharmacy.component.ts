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
import { IPharmacy } from 'src/app/domain/pharmacy';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-pharmacy',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './pharmacy.component.html',
  styleUrls: ['./pharmacy.component.scss']
})
export class PharmacyComponent {


  id              : any
  code            : string = ''
  name            : string = ''
  description     : string = ''
  location        : string = ''
  category        : string = ''
  active          : boolean = true

  pharmacies : IPharmacy[] = []

  filterRecords : string = ''

  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
  ) { }

  ngOnInit(): void {
    this.loadPharmacies()
  }

  public async savePharmacy(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var pharmacy = {
      id              : this.id,
      code            : this.code,
      name            : this.name,
      description     : this.description,
      location        : this.location,
      category        : this.category,
      active          : true
    }
    if(this.id == null || this.id == ''){
      //save a new pharmacy
      this.spinner.show()
      await this.http.post<IPharmacy>(API_URL+'/pharmacies/save', pharmacy, options)
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
          this.msgBox.showSuccessMessage('Pharmacy created successifully')
          this.loadPharmacies()
          this.clear()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not create pharmacy')
        }
      )

    }else{
      //update an existing pharmacy
      this.spinner.show()
      await this.http.post<IPharmacy>(API_URL+'/pharmacies/save', pharmacy, options)
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
          this.msgBox.showSuccessMessage('Pharmacy updated successifully')
          this.loadPharmacies()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not update pharmacy')
        }
      )
    }
  }

  async loadPharmacies(){
    this.pharmacies = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IPharmacy[]>(API_URL+'/pharmacies', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.pharmacies.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load pharmacies')
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

  async getPharmacy(key: string) {
    if(key == ''){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IPharmacy>(API_URL+'/pharmacies/get?id='+key, options)
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
        this.msgBox.showErrorMessage(error, 'Could not find pharmacy')
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
