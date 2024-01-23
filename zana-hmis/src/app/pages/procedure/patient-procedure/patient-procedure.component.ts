import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IProcedure } from 'src/app/domain/procedure';
import { IPatient } from 'src/app/domain/patient';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';

const API_URL = environment.apiUrl;


@Component({
  selector: 'app-patient-procedure',
  templateUrl: './patient-procedure.component.html',
  styleUrls: ['./patient-procedure.component.scss'],
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
export class PatientProcedureComponent {

  id : any

  patient! : IPatient

  procedures : IProcedure[] = []

  filterRecords : string = ''


  constructor(private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private router : Router,
    private msgBox : MsgBoxService) { }

  ngOnInit(): void {
    this.id = localStorage.getItem('procedure-patient-id')
    localStorage.removeItem('procedure-patient-id')
    this.loadPatient(this.id)
    this.loadProceduresByPatient(this.id)
  }

  async loadPatient(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IPatient>(API_URL+'/patients/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.id = data?.id
        this.patient = data!
        console.log(data)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load patient')
        console.log(error)
      }
    )
  }

  async loadProceduresByPatient(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IProcedure[]>(API_URL+'/patients/get_procedures_by_patient_id?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        
        this.procedures = data!
        console.log(this.procedures)
      }
    )
    .catch(
      error => {
        this.procedures = []
        this.msgBox.showErrorMessage(error, 'Could not load procedures')
      }
    )
  }

  async acceptProcedure(procedure : IProcedure){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/accept_procedure', procedure, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Status changed : ACCEPTED')
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.loadProceduresByPatient(this.id)
  }

  async updateProcedure(procedure : IProcedure){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/update_procedure', procedure, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Updated successifully')
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.loadProceduresByPatient(this.id)
  }


  async rejectProcedure(procedure : IProcedure){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/reject_procedure', procedure, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Status changed : REJECTED')
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.loadProceduresByPatient(this.id)
  }

  async holdProcedure(procedure : IProcedure){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/hold_procedure', procedure, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Status changed : PENDING')
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.loadProceduresByPatient(this.id)
  }

  async collectProcedure(procedure : IProcedure){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/collect_procedure', procedure, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Status changed : COLLECTED')
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.loadProceduresByPatient(this.id)
  }

  async verifyProcedure(procedure : IProcedure){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/verify_procedure', procedure, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Status changed : VERIFIED, Saved successifully')
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.loadProceduresByPatient(this.id)
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
