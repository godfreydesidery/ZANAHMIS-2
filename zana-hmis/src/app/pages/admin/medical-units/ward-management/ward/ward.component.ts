import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { Router, RouterLink } from '@angular/router';
import { NgxSpinnerModule, NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { AuthService } from 'src/app/auth.service';
import { IWard } from 'src/app/domain/ward';
import { IWardCategory } from 'src/app/domain/ward-category';
import { IWardType } from 'src/app/domain/ward-type';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-ward',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './ward.component.html',
  styleUrls: ['./ward.component.scss']
})
export class WardComponent {
  

  id              : any
  code            : string = ''
  name            : string = ''

  noOfBeds        : number = 0

  active          : boolean = true

  wards : IWard[] = []
  wardTypes : IWardType[] = []
  wardCategories : IWardCategory[] = []

  wardCategoryName : string = ''
  wardTypeName : string = ''

  filterRecords : string = ''

  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService,
    private router : Router
  ) { }

  ngOnInit(): void {
    this.loadWards()
    this.loadWardTypes()
    this.loadWardCategories()
  }

  public async saveWard(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var categoryId = null
    var typeId = null

    this.wardCategories.forEach(element => {
      if(element.name === this.wardCategoryName){
        categoryId = element.id
      }
    })

    this.wardTypes.forEach(element => {
      if(element.name === this.wardTypeName){
        typeId = element.id
      }
    })

    var ward = {
      id              : this.id,
      code            : this.code,
      name            : this.name,
      noOfBeds        : this.noOfBeds,
      wardCategory    : {
        id : categoryId
      },
      wardType        : {
        id : typeId
      },
      active          : true
    }
    if(this.id == null || this.id == ''){
      //save a new ward
      this.spinner.show()
      await this.http.post<IWard>(API_URL+'/wards/save', ward, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id           = data?.id
          this.code         = data!.code
          this.name         = data!.name
          this.noOfBeds     = data!.noOfBeds
          this.msgBox.showSuccessMessage('Ward created successifully')
          this.loadWards()
          this.clear()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not create ward')
        }
      )

    }else{
      //update an existing ward
      this.spinner.show()
      await this.http.post<IWard>(API_URL+'/wards/save', ward, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id           = data?.id
          this.code         = data!.code
          this.name         = data!.name
          this.noOfBeds     = data!.noOfBeds
          this.msgBox.showSuccessMessage('Ward updated successifully')
          this.loadWards()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not update ward')
        }
      )
    }
  }

  async loadWardTypes(){
    this.wards = []
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

  async loadWardCategories(){
    this.wards = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IWardCategory[]>(API_URL+'/ward_categories', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.wardCategories.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load ward categories')
      }
    )
  }

  async loadWards(){
    this.wards = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IWard[]>(API_URL+'/wards', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.wards.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load wards')
      }
    )
  }

  clear(){
    this.id           = null
    this.code         = ''
    this.name         = ''
    this.wardTypeName = ''
    this.wardCategoryName = ''
    this.noOfBeds     = 0
  }

  async getWard(key: string) {
    if(key == ''){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IWard>(API_URL+'/wards/get?id='+key, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        console.log(data)
        this.id           = data?.id
        this.code         = data!.code
        this.name         = data!.name
        this.noOfBeds     = data!.noOfBeds
        this.wardCategoryName = data!.wardCategory.name
        this.wardTypeName = data!.wardType.name
      }
    )
    .catch(
      error=>{
        console.log(error)        
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

  configureWard(id : any){
    localStorage.setItem('ward-id', id)
    this.router.navigate(['ward-configuration'])
  }
}
