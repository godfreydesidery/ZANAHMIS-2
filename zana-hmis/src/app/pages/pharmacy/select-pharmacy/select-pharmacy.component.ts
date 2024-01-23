import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IPharmacy } from 'src/app/domain/pharmacy';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-select-pharmacy',
  templateUrl: './select-pharmacy.component.html',
  styleUrls: ['./select-pharmacy.component.scss'],
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
export class SelectPharmacyComponent {

  pharmacies : IPharmacy[] = []
  pharmacyName : string = ''
  pharmacy! : IPharmacy

  selectedPharmacyId : any
  selectedPharmacyCode : string = ''
  selectedPharmacyName : string = ''

  filterRecords : string = ''

  constructor(private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService) { }

  async ngOnInit(): Promise<void> {
    if(localStorage.getItem('selected-pharmacy-id') != null){
      this.selectedPharmacyId = localStorage.getItem('selected-pharmacy-id')
    }
    if(localStorage.getItem('selected-pharmacy-code') != null){
      this.selectedPharmacyCode = localStorage.getItem('selected-pharmacy-code')!.toString()
    }
    if(localStorage.getItem('selected-pharmacy-name') != null){
      this.selectedPharmacyName = localStorage.getItem('selected-pharmacy-name')!.toString()
    }  
    this.loadPharmacies()
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
    this.pharmacyName = ''
  }

  async selectPharmacy(name : string){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    localStorage.removeItem('selected-pharmacy-id')
    localStorage.removeItem('selected-pharmacy-code')
    localStorage.removeItem('selected-pharmacy-name')
    this.spinner.show()
    await this.http.get<IPharmacy>(API_URL+'/pharmacies/get_by_name?name='+name, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {
        localStorage.setItem('selected-pharmacy-id', data?.id)
        localStorage.setItem('selected-pharmacy-code', data!.code)
        localStorage.setItem('selected-pharmacy-name', data!.name)

        this.selectedPharmacyId   = data?.id
        this.selectedPharmacyCode = data!.code
        this.selectedPharmacyName = data!.name
      }
    )
    .catch(
      error => {
        localStorage.setItem('selected-pharmacy-id', this.selectedPharmacyId)
        localStorage.setItem('selected-pharmacy-code', this.selectedPharmacyCode)
        localStorage.setItem('selected-pharmacy-name', this.selectedPharmacyName)

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
