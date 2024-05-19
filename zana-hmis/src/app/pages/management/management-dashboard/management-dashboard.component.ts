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
import { IMonthlySummaryReport } from 'src/app/domain/monthly-summary-report';
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

  years : number[] = []
  currentYear : number = new Date().getFullYear();
  selectedYear : number = this.currentYear

  newPatientsCount : number = 0
  existingPatientsCount : number = 0
  dischargedPatientsCount : number = 0

  inpatientsCount : number = 0
  outpatientsCount : number = 0

  totalPatientsCount : number = 0

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
    this.generateYears()
    await this.loadNewPatientsCountByDate(this.from, this.to)
    await this.loadExistingPatientsCountByDate(this.from, this.to)
    await this.loadInpatientsCountByDate(this.from, this.to)
    await this.loadOutpatientsCountByDate(this.from, this.to)
    await this.loadActiveUsersCount()
    //await this.loadClinicianPerformanceByDate(this.from, this.to)
    //await this.loadMonthlySummaryReportByDate(this.from, this.to)
    this.totalPatientsCount = this.inpatientsCount + this.outpatientsCount
  }

  generateYears(){
    for(let y = 2023; y <= 2100; y++){
      this.years.push(y)
    }
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


  async loadDischargedPatientsCountByDate(from : Date, to : Date){
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
    await this.http.post<number>(API_URL+'/reports/discharged_patients_count_by_dates', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.dischargedPatientsCount = data!
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


  max : number = 0
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


        this.clinicianPerformanceReport.sort((a, b) => a.name.localeCompare(b.name))

        this.max = 0

        this.clinicianPerformanceReport.forEach(element => {
          if(this.max < element.total){
            this.max = element.total
          }
        })

        this.clinicianPerformanceReport.forEach(element => {
          element.percentage = (element.total/this.max) * 100
        })

        var value : string = ''
        this.clinicianPerformanceReport.forEach(element => {
          if(value != element.name){
            value = element.name
          }else{
            element.name = ''
          }
        })


        console.log(this.clinicianPerformanceReport)
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







  async loadMonthlySummaryReportByDate(from : Date, to : Date, month : number){
    var report! : IMonthlySummaryReport
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var args = {
      from : from,
      to : to,
    }

    this.spinner.show()
    await this.http.post<IMonthlySummaryReport>(API_URL+'/reports/monthly_summary', args, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        report = data!
        console.log(data)

        if(month === 1){
          this.januaryOutpatientCount = data!.outpatientCount
          this.januaryInpatientCount = data!.inpatientCount
          this.januaryOutsiderCount = data!.outsiderCount
        }

        if(month === 2){
          this.februaryOutpatientCount = data!.outpatientCount
          this.februaryInpatientCount = data!.inpatientCount
          this.februaryOutsiderCount = data!.outsiderCount
        }

        if(month === 3){
          this.marchOutpatientCount = data!.outpatientCount
          this.marchInpatientCount = data!.inpatientCount
          this.marchOutsiderCount = data!.outsiderCount
        }

        if(month === 4){
          this.aprilOutpatientCount = data!.outpatientCount
          this.aprilInpatientCount = data!.inpatientCount
          this.aprilOutsiderCount = data!.outsiderCount
        }

        if(month === 5){
          this.mayOutpatientCount = data!.outpatientCount
          this.mayInpatientCount = data!.inpatientCount
          this.mayOutsiderCount = data!.outsiderCount
        }

        if(month === 6){
          this.juneOutpatientCount = data!.outpatientCount
          this.juneInpatientCount = data!.inpatientCount
          this.juneOutsiderCount = data!.outsiderCount
        }

        if(month === 7){
          this.julyOutpatientCount = data!.outpatientCount
          this.julyInpatientCount = data!.inpatientCount
          this.julyOutpatientCount = data!.outsiderCount
        }

        if(month === 8){
          this.augustOutpatientCount = data!.outpatientCount
          this.augustInpatientCount = data!.inpatientCount
          this.augustOutsiderCount = data!.outsiderCount
        }

        if(month === 9){
          this.septemberOutpatientCount = data!.outpatientCount
          this.septemberInpatientCount = data!.inpatientCount
          this.septemberOutsiderCount = data!.outsiderCount
        }

        if(month === 10){
          this.octoberOutpatientCount = data!.outpatientCount
          this.octoberInpatientCount = data!.inpatientCount
          this.octoberOutsiderCount = data!.outsiderCount
        }

        if(month === 11){
          this.novemberOutpatientCount = data!.outpatientCount
          this.novemberInpatientCount = data!.inpatientCount
          this.novemberOutsiderCount = data!.outsiderCount
        }

        if(month === 12){
          this.decemberOutpatientCount = data!.outpatientCount
          this.decemberInpatientCount = data!.inpatientCount
          this.decemberOutsiderCount = data!.outsiderCount
        }


      })
      .catch(
        error => {
          console.log(error)
          //this.msgBox.showErrorMessage(error, '')
      }
    )
    return report
  }



  januaryOutpatientCount : number = 0
  januaryInpatientCount : number = 0
  januaryOutsiderCount : number = 0

  februaryOutpatientCount : number = 0
  februaryInpatientCount : number = 0
  februaryOutsiderCount : number = 0

  marchOutpatientCount : number = 0
  marchInpatientCount : number = 0
  marchOutsiderCount : number = 0

  aprilOutpatientCount : number = 0
  aprilInpatientCount : number = 0
  aprilOutsiderCount : number = 0

  mayOutpatientCount : number = 0
  mayInpatientCount : number = 0
  mayOutsiderCount : number = 0

  juneOutpatientCount : number = 0
  juneInpatientCount : number = 0
  juneOutsiderCount : number = 0

  julyOutpatientCount : number = 0
  julyInpatientCount : number = 0
  julyOutsiderCount : number = 0

  augustOutpatientCount : number = 0
  augustInpatientCount : number = 0
  augustOutsiderCount : number = 0

  septemberOutpatientCount : number = 0
  septemberInpatientCount : number = 0
  septemberOutsiderCount : number = 0

  octoberOutpatientCount : number = 0
  octoberInpatientCount : number = 0
  octoberOutsiderCount : number = 0

  novemberOutpatientCount : number = 0
  novemberInpatientCount : number = 0
  novemberOutsiderCount : number = 0

  decemberOutpatientCount : number = 0
  decemberInpatientCount : number = 0
  decemberOutsiderCount : number = 0

  maxForMonths : number = 0


  
  async updateMonthlyReport(year : number){
    var startDate : number = 1
    var endDate : number = 31
    //January
    var from = new Date(year.toString()+'-01-01')
    var to = new Date(year.toString()+'-01-31')
    this.loadMonthlySummaryReportByDate(from, to, year)
    //February
    var from = new Date(year.toString()+'-02-01')
    var to = new Date(year.toString()+'-02-31')
    this.loadMonthlySummaryReportByDate(from, to, year)
    //March
    var from = new Date(year.toString()+'-03-01')
    var to = new Date(year.toString()+'-03-31')
    this.loadMonthlySummaryReportByDate(from, to, year)
    //April
    var from = new Date(year.toString()+'-04-01')
    var to = new Date(year.toString()+'-04-31')
    this.loadMonthlySummaryReportByDate(from, to, year)
    //May
    var from = new Date(year.toString()+'-05-01')
    var to = new Date(year.toString()+'-05-31')
    this.loadMonthlySummaryReportByDate(from, to, year)
    //June
    var from = new Date(year.toString()+'-06-01')
    var to = new Date(year.toString()+'-06-31')
    this.loadMonthlySummaryReportByDate(from, to, year)
    //July
    var from = new Date(year.toString()+'-07-01')
    var to = new Date(year.toString()+'-07-31')
    this.loadMonthlySummaryReportByDate(from, to, year)
    //August
    var from = new Date(year.toString()+'-08-01')
    var to = new Date(year.toString()+'-08-31')
    this.loadMonthlySummaryReportByDate(from, to, year)
    //September
    var from = new Date(year.toString()+'-09-01')
    var to = new Date(year.toString()+'-09-31')
    this.loadMonthlySummaryReportByDate(from, to, year)
    //October
    var from = new Date(year.toString()+'-10-01')
    var to = new Date(year.toString()+'-10-31')
    this.loadMonthlySummaryReportByDate(from, to, year)
    //November
    var from = new Date(year.toString()+'-11-01')
    var to = new Date(year.toString()+'-11-31')
    this.loadMonthlySummaryReportByDate(from, to, year)
    //December
    var from = new Date(year.toString()+'-12-01')
    var to = new Date(year.toString()+'-12-31')
    this.loadMonthlySummaryReportByDate(from, to, year)

      if(this.maxForMonths < this.januaryOutpatientCount){
        this.maxForMonths = this.januaryOutpatientCount
      }
      if(this.maxForMonths < this.januaryInpatientCount){
        this.maxForMonths = this.januaryInpatientCount
      }
      if(this.maxForMonths < this.januaryOutsiderCount){
        this.maxForMonths = this.januaryOutsiderCount
      }

      if(this.maxForMonths < this.februaryOutpatientCount){
        this.maxForMonths = this.februaryOutpatientCount
      }
      if(this.maxForMonths < this.februaryInpatientCount){
        this.maxForMonths = this.februaryInpatientCount
      }
      if(this.maxForMonths < this.februaryOutsiderCount){
        this.maxForMonths = this.februaryOutsiderCount
      }

      if(this.maxForMonths < this.marchOutpatientCount){
        this.maxForMonths = this.marchOutpatientCount
      }
      if(this.maxForMonths < this.marchInpatientCount){
        this.maxForMonths = this.marchInpatientCount
      }
      if(this.maxForMonths < this.marchOutsiderCount){
        this.maxForMonths = this.marchOutsiderCount
      }

      if(this.maxForMonths < this.aprilOutpatientCount){
        this.maxForMonths = this.aprilOutpatientCount
      }
      if(this.maxForMonths < this.aprilInpatientCount){
        this.maxForMonths = this.aprilInpatientCount
      }
      if(this.maxForMonths < this.aprilOutsiderCount){
        this.maxForMonths = this.aprilOutsiderCount
      }

      if(this.maxForMonths < this.mayOutpatientCount){
        this.maxForMonths = this.mayOutpatientCount
      }
      if(this.maxForMonths < this.mayInpatientCount){
        this.maxForMonths = this.mayInpatientCount
      }
      if(this.maxForMonths < this.mayOutsiderCount){
        this.maxForMonths = this.mayOutsiderCount
      }

      if(this.maxForMonths < this.juneOutpatientCount){
        this.maxForMonths = this.juneOutpatientCount
      }
      if(this.maxForMonths < this.juneInpatientCount){
        this.maxForMonths = this.juneInpatientCount
      }
      if(this.maxForMonths < this.juneOutsiderCount){
        this.maxForMonths = this.juneOutsiderCount
      }

      if(this.maxForMonths < this.julyOutpatientCount){
        this.maxForMonths = this.julyOutpatientCount
      }
      if(this.maxForMonths < this.julyInpatientCount){
        this.maxForMonths = this.julyInpatientCount
      }
      if(this.maxForMonths < this.julyOutsiderCount){
        this.maxForMonths = this.julyOutsiderCount
      }

      if(this.maxForMonths < this.augustOutpatientCount){
        this.maxForMonths = this.augustOutpatientCount
      }
      if(this.maxForMonths < this.augustInpatientCount){
        this.maxForMonths = this.augustInpatientCount
      }
      if(this.maxForMonths < this.augustOutsiderCount){
        this.maxForMonths = this.augustOutsiderCount
      }

      if(this.maxForMonths < this.septemberOutpatientCount){
        this.maxForMonths = this.septemberOutpatientCount
      }
      if(this.maxForMonths < this.septemberInpatientCount){
        this.maxForMonths = this.septemberInpatientCount
      }
      if(this.maxForMonths < this.septemberOutsiderCount){
        this.maxForMonths = this.septemberOutsiderCount
      }

      if(this.maxForMonths < this.octoberOutpatientCount){
        this.maxForMonths = this.octoberOutpatientCount
      }
      if(this.maxForMonths < this.octoberInpatientCount){
        this.maxForMonths = this.octoberInpatientCount
      }
      if(this.maxForMonths < this.octoberOutsiderCount){
        this.maxForMonths = this.octoberOutsiderCount
      }

      if(this.maxForMonths < this.novemberOutpatientCount){
        this.maxForMonths = this.novemberOutpatientCount
      }
      if(this.maxForMonths < this.novemberInpatientCount){
        this.maxForMonths = this.novemberInpatientCount
      }
      if(this.maxForMonths < this.novemberOutsiderCount){
        this.maxForMonths = this.novemberOutsiderCount
      }

      if(this.maxForMonths < this.decemberOutpatientCount){
        this.maxForMonths = this.decemberOutpatientCount
      }
      if(this.maxForMonths < this.decemberInpatientCount){
        this.maxForMonths = this.decemberInpatientCount
      }
      if(this.maxForMonths < this.decemberOutsiderCount){
        this.maxForMonths = this.decemberOutsiderCount
      }

      alert(this.maxForMonths)

    console.log(this.januaryOutpatientCount)
    console.log(this.januaryInpatientCount)
    console.log(this.januaryOutsiderCount)

    console.log(this.februaryOutpatientCount)
    console.log(this.februaryInpatientCount)
    console.log(this.februaryOutsiderCount)

    console.log(this.marchOutpatientCount)
    console.log(this.marchInpatientCount)
    console.log(this.marchOutsiderCount)

    console.log(this.aprilOutpatientCount)
    console.log(this.aprilInpatientCount)
    console.log(this.aprilOutsiderCount)

    console.log(this.marchOutpatientCount)
    console.log(this.marchInpatientCount)
    console.log(this.marchOutsiderCount)

    console.log(this.julyOutpatientCount)
    console.log(this.julyInpatientCount)
    console.log(this.julyOutsiderCount)

    console.log(this.julyOutpatientCount)
    console.log(this.julyInpatientCount)
    console.log(this.julyOutsiderCount)

    console.log(this.augustOutpatientCount)
    console.log(this.augustInpatientCount)
    console.log(this.augustOutpatientCount)

    console.log(this.septemberOutpatientCount)
    console.log(this.septemberInpatientCount)
    console.log(this.septemberOutsiderCount)

    console.log(this.octoberOutpatientCount)
    console.log(this.octoberInpatientCount)
    console.log(this.octoberOutsiderCount)

    console.log(this.novemberOutpatientCount)
    console.log(this.novemberInpatientCount)
    console.log(this.novemberOutsiderCount)

    console.log(this.decemberOutpatientCount)
    console.log(this.decemberInpatientCount)
    console.log(this.decemberOutsiderCount)

  }



}
