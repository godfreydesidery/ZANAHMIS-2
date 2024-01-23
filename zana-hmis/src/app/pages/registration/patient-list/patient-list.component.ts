import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IPatient } from 'src/app/domain/patient';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { environment } from 'src/environments/environment';

//import {MatTableDataSource} from '@angular/material';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-patient-list',
  templateUrl: './patient-list.component.html',
  styleUrls: ['./patient-list.component.scss'],
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
export class PatientListComponent implements OnInit {

  patients : IPatient[] = []
  patientsToShow : IPatient[] = []

  filterValue : string = ''

  filterRecords : string = ''

  constructor(
    private auth : AuthService,
              private http : HttpClient,
              private modalService: NgbModal,
              private spinner: NgxSpinnerService
  ) { }

  ngOnInit(): void {
    this.loadPatients()
  }

  async loadPatients(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.patients = []
    this.spinner.show()
    await this.http.get<IPatient[]>(API_URL+'/patients', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(data => {
      data?.forEach(element => {
        console.log(element)
        this.patients.push(element)
        //this.patientsToShow.push(element)
      })     
    })
    .catch(error => {
      console.log(error)
      alert('Could not load patients')
    })
  }

  async applyFilter(filterValue: string) {
    let filterValueLower = filterValue.toLowerCase();
    if(filterValue === '' ) {
        this.patientsToShow = this.patients
    } else {
      this.patientsToShow = []
      this.patients.forEach(element => {
        if( element.no.toLocaleLowerCase().includes(filterValueLower) || 
            element.firstName.toLocaleLowerCase().includes(filterValueLower) ||
            element.middleName.toLocaleLowerCase().includes(filterValueLower) ||
            element.lastName.toLocaleLowerCase().includes(filterValueLower)){
          this.patientsToShow.push(element)
        }       
      })
       
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