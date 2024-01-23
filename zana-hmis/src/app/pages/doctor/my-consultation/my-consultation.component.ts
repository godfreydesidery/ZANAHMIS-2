import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IConsultation } from 'src/app/domain/consultation';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { ShowDateTimePipe } from 'src/app/pipes/date_time.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;
@Component({
  selector: 'app-my-consultation',
  templateUrl: './my-consultation.component.html',
  styleUrls: ['./my-consultation.component.scss'],
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    AgePipe,
    ShowDateTimePipe,
    RouterLink
  ],
})
export class MyConsultationComponent implements OnInit {

  clinicianId : any = null

  consultations : IConsultation[] = []

  filterRecords : string = ''

  constructor(private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private router : Router,
    private msgBox : MsgBoxService) { }

  async ngOnInit(): Promise<void> {
    await this.loadClinician()
    if(this.clinicianId != null){
      await this.loadOpenedList(this.clinicianId)
    }else{
      this.msgBox.showErrorMessage3('User not found in doctors register')
    }
  }

  async loadClinician(){    
    var username = localStorage.getItem('username')!
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<any>(API_URL+'/clinicians/load_clinician_by_username?username='+username, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.clinicianId = data
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load doctor')
      }
    )
  }

  async loadOpenedList(clinicianIid : any){    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IConsultation[]>(API_URL+'/patients/load_in_process_consultations_by_clinician_id?clinician_id='+clinicianIid, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.consultations = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }

  async postConsultation(id : any){
    /**
     * Set a global value consultation id to be accessed accross components
     */
    localStorage.setItem('consultation-id', id)
    this.router.navigate(['doctor-cracking'])    
  }

  async cancelTransfer(id : any){
    if(!window.confirm('Confirm canceling this transfer?')){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get(API_URL+'/patients/cancel_consultation_transfer?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.loadOpenedList(this.clinicianId)
        
      }
    )
    .catch(
      error => {
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
