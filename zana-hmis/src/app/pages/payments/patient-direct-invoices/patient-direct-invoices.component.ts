import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IConsultation } from 'src/app/domain/consultation';
import { IPatientInvoice } from 'src/app/domain/patient-invoice';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { ShowTimePipe } from 'src/app/pipes/show_time.pipe';
import { ShowUserPipe } from 'src/app/pipes/show_user.pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-patient-direct-invoices',
  templateUrl: './patient-direct-invoices.component.html',
  styleUrls: ['./patient-direct-invoices.component.scss'],
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    AgePipe,
    ShowUserPipe,
    ShowTimePipe,
    RouterLink
  ], 
})
export class PatientDirectInvoicesComponent {


  invoices : IPatientInvoice[] = []

  filterRecords : string = ''

  constructor(private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private router : Router,
    private msgBox : MsgBoxService) { }

  async ngOnInit(): Promise<void> {
    this.loadPatientDirectInvoices()
  }

  async loadPatientDirectInvoices(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IPatientInvoice[]>(API_URL+'/patients/get_patient_direct_pending_invoices', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.invoices = data!
        console.log(data)
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
     /**
      * Open patients consultation
      */
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var opened : boolean = false
    this.spinner.show()
    await this.http.get<boolean>(API_URL+'/patients/open_consultation?consultation_id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        opened = true
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
    if(opened){
      this.router.navigate(['doctor-cracking'])
    }else{
      this.msgBox.showErrorMessage3('Could not open')
    }
  }

  async setInvoice(id : any){
    localStorage.setItem('patient-invoice-id', id)
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
