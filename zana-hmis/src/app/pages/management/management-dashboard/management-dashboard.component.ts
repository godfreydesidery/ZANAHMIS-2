import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import * as Chart from 'chart.js';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IClinicianPerformanceReport } from 'src/app/domain/clinician-performance-report';
import { ILabTest } from 'src/app/domain/lab-test';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { DataService } from 'src/app/services/data.service';
import { MsgBoxService } from 'src/app/services/msg-box.service';

import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-management-dashboard',
  templateUrl: './management-dashboard.component.html',
  styleUrls: ['./management-dashboard.component.scss'],
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
export class ManagementDashboardComponent {

  newPatientsCount : number = 0
  existingPatientsCount : number = 0

  inpatientsCount : number = 0
  outpatientsCount : number = 0

  activeUsers : number = 0

  from! : Date
  to! : Date

  clinicianPerformanceReport : IClinicianPerformanceReport[] = []

  constructor(
    private auth : AuthService,
    private http : HttpClient,
    private spinner: NgxSpinnerService,
    private msgBox : MsgBoxService,
    private data : DataService,            
  ){}



  async ngOnInit(): Promise<void> {
    await this.refresh()
  }

  async refresh(){
    await this.loadNewPatientsCountByDate(this.from, this.to)
    await this.loadExistingPatientsCountByDate(this.from, this.to)
    await this.loadInpatientsCountByDate(this.from, this.to)
    await this.loadOutpatientsCountByDate(this.from, this.to)
    await this.loadActiveUsersCount()
    await this.loadClinicianPerformanceByDate(this.from, this.to)
  }


  async loadNewPatientsCountByDate(from : Date, to : Date){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    if(from === undefined || to === undefined){
      from = new Date()
      to = new Date()
      //this.msgBox.showErrorMessage3('Could not run. Please select date range')
      //return
    }
    

    

    var args = {
      from : from,
      to : to,
    }

    this.spinner.show()
    await this.http.post<number>(API_URL+'/reports/new_patients_count_by_dates', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.newPatientsCount = data!
        console.log(data)
        //alert(data)
      })
      .catch(
        error => {
          console.log(error)
          //this.msgBox.showErrorMessage(error, '')
      }
    )
  }



  async loadExistingPatientsCountByDate(from : Date, to : Date){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    if(from === undefined || to === undefined){
      from = new Date()
      to = new Date()
      //this.msgBox.showErrorMessage3('Could not run. Please select date range')
      //return
    }
    

    

    var args = {
      from : from,
      to : to,
    }

    this.spinner.show()
    await this.http.post<number>(API_URL+'/reports/existing_patients_count_by_dates', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.existingPatientsCount = data!
        console.log(data)
        //alert(data)
      })
      .catch(
        error => {
          console.log(error)
          //this.msgBox.showErrorMessage(error, '')
      }
    )
  }



  async loadInpatientsCountByDate(from : Date, to : Date){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    if(from === undefined || to === undefined){
      from = new Date()
      to = new Date()
      //this.msgBox.showErrorMessage3('Could not run. Please select date range')
      //return
    }
    

    

    var args = {
      from : from,
      to : to,
    }

    this.spinner.show()
    await this.http.post<number>(API_URL+'/reports/inpatients_count_by_dates', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.inpatientsCount = data!
        console.log(data)
        //alert(data)
      })
      .catch(
        error => {
          console.log(error)
          //this.msgBox.showErrorMessage(error, '')
      }
    )
  }


  async loadOutpatientsCountByDate(from : Date, to : Date){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    if(from === undefined || to === undefined){
      from = new Date()
      to = new Date()
      //this.msgBox.showErrorMessage3('Could not run. Please select date range')
      //return
    }
    

    

    var args = {
      from : from,
      to : to,
    }

    this.spinner.show()
    await this.http.post<number>(API_URL+'/reports/outpatients_count_by_dates', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.outpatientsCount = data!
        console.log(data)
        //alert(data)
      })
      .catch(
        error => {
          console.log(error)
          //this.msgBox.showErrorMessage(error, '')
      }
    )
  }



  async loadActiveUsersCount(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    this.spinner.show()
    await this.http.post<number>(API_URL+'/reports/active_users', null, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.activeUsers = data!
        console.log(data)
        //alert(data)
      })
      .catch(
        error => {
          console.log(error)
          //this.msgBox.showErrorMessage(error, '')
      }
    )
  }


  async loadClinicianPerformanceByDate(from : Date, to : Date){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    if(from === undefined || to === undefined){
      from = new Date()
      to = new Date()
      //this.msgBox.showErrorMessage3('Could not run. Please select date range')
      //return
    }
    

    

    var args = {
      from : from,
      to : to,
    }

    this.spinner.show()
    await this.http.post<IClinicianPerformanceReport[]>(API_URL+'/reports/clinician_performance_by_dates', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.clinicianPerformanceReport = data!
        console.log(data)
        //alert(data)
      })
      .catch(
        error => {
          console.log(error)
          //this.msgBox.showErrorMessage(error, '')
      }
    )
  }



  // will create charts later on





}
