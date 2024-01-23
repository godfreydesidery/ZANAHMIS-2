import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IPatient } from 'src/app/domain/patient';
import { IRadiology } from 'src/app/domain/radiology';
import { IRadiologyType } from 'src/app/domain/radiology-type';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-radiology',
  templateUrl: './radiology.component.html',
  styleUrls: ['./radiology.component.scss'],
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
export class RadiologyComponent {
  id : any


  patient! : IPatient

  radiologies : IRadiology[] = []

  diagnosisTypeNames : string[] = []

  filterRecords : string = ''

  constructor(private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private router : Router,
    private msgBox : MsgBoxService) { }

  async ngOnInit(): Promise<void> {
    this.id = localStorage.getItem('radiology-patient-id')
    localStorage.removeItem('radiology-patient-id')
    await this.loadPatient(this.id)
    await this.loadRadiologiesByPatient(this.id)
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

  async loadRadiologiesByPatient(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IRadiology[]>(API_URL+'/patients/get_radiologies_by_patient_id?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        
        this.radiologies = data!
        console.log(this.radiologies)
      }
    )
    .catch(
      error => {
        this.radiologies = []
        this.msgBox.showErrorMessage(error, 'Could not load radiologies')
      }
    )
  }

  async acceptRadiology(radiology : IRadiology){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var radio  = {
      id          : radiology.id
    }
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/accept_radiology', radio, options)
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
    this.loadRadiologiesByPatient(this.id)
  }

  async rejectRadiology(radiology : IRadiology){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var radio  = {
      id          : radiology.id
    }
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/reject_radiology', radio, options)
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
    this.loadRadiologiesByPatient(this.id)
  }

  async saveReasonForRejection(id : any, reason : string){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var radio  = {
      id          : id,
      rejectComment : reason
    }
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/radiologies/save_reason_for_rejection', radio, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Success')
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async holdRadiology(radiology : IRadiology){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var radio  = {
      id          : radiology.id
    }
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/hold_radiology', radio, options)
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
    this.loadRadiologiesByPatient(this.id)
  }

  async collectRadiology(radiology : IRadiology){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var radio  = {
      id          : radiology.id
    }
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/collect_radiology', radio, options)
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
    this.loadRadiologiesByPatient(this.id)
  }

  async verifyRadiology(radiology : IRadiology){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var radio  = {
      id          : radiology.id,
      result      : radiology.result
    }
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/verify_radiology', radio, options)
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
    this.loadRadiologiesByPatient(this.id)
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

