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
import { IWardBed } from 'src/app/domain/ward-bed';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-ward-configuration',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './ward-configuration.component.html',
  styleUrls: ['./ward-configuration.component.scss']
})
export class WardConfigurationComponent implements OnInit {

  id : any = null

  bedId : any = null
  bedNo : string = ''

  ward! : IWard

  wardBeds : IWardBed[] = []

  filterRecords : string = ''

  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService,
    private router : Router
  ) { }

  async ngOnInit(): Promise<void> {
    this.id = localStorage.getItem('ward-id')
    localStorage.removeItem('ward-id')
    await this.getWard(this.id)
    await this.loadWardBeds(this.id)
  }

  async getWard(id : any) {
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IWard>(API_URL+'/wards/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        this.ward = data!
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async registerBed(no : string) {
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var wardBed = {
      no : no,
      ward : {
        id : this.id
      },
    }

    this.spinner.show()
    await this.http.post<IWardBed>(API_URL+'/wards/register_bed', wardBed, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        this.wardBeds.push(data!)
        this.msgBox.showSuccessMessage('Bed/ Room created scuccessifully')
        this.clear()
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, '')
        this.clear()
      }
    )
  }

  async activateBed(id : any) {
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var wardBed = {
      id : id
    }

    this.spinner.show()
    await this.http.post<IWardBed>(API_URL+'/wards/activate_bed', wardBed, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        this.wardBeds.forEach(element => {
          if(element.id === data!.id){
            element.active = data!.active
          }
        })
        //this.msgBox.showSuccessMessage('Bed/ Room activated successifully')
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async deactivateBed(id : any) {
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var wardBed = {
      id : id
    }

    this.spinner.show()
    await this.http.post<IWardBed>(API_URL+'/wards/deactivate_bed', wardBed, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        this.wardBeds.forEach(element => {
          if(element.id === data!.id){
            element.active = data!.active
          }
        })
        //this.msgBox.showSuccessMessage('Bed/ Room deactivated successifully')
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async loadWardBeds(wardId : any) {
    
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    this.spinner.show()
    await this.http.get<IWardBed[]>(API_URL+'/wards/get_ward_beds?ward_id='+wardId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        console.log(data)
        this.wardBeds = data!
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  clear(){
    this.bedNo = ''
  }

  async getWardBed(id: any) {
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IWardBed>(API_URL+'/wards/get_ward_bed?bed_id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        this.bedId     = data?.id
        this.bedNo  = data!.no
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, 'Could not find ward bed/room')
      }
    )
  }

  public async updateBed(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var wardBed = {
      id : this.bedId,
      no : this.bedNo
    }
    this.spinner.show()
    await this.http.post<IWardBed>(API_URL+'/wards/update_bed', wardBed, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.wardBeds.forEach(element => {
            if(element.id === data?.id){
              element.no = data!.no
            }
          })
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, '')
        }
      )

    
  }

}
