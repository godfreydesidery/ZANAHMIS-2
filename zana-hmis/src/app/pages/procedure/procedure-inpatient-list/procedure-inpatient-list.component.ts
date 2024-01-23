import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IConsultation } from 'src/app/domain/consultation';
import { IPatient } from 'src/app/domain/patient';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-procedure-inpatient-list',
  templateUrl: './procedure-inpatient-list.component.html',
  styleUrls: ['./procedure-inpatient-list.component.scss'],
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
export class ProcedureInpatientListComponent {

  pharmacistId : any

  patients : IPatient[] = []

  filterRecords : string = ''

  constructor(private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private router : Router,
    private msgBox : MsgBoxService) { }

  async ngOnInit(): Promise<void> {
    //await this.loadPharmacist()
    await this.loadOutpatientList()
  }

  async loadPharmacist(){    
    var username = localStorage.getItem('username')!
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<any>(API_URL+'/pharmacists/load_pharmacist_by_username?username='+username, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.pharmacistId = data
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load pharmacist')
      }
    )
  }

  async loadOutpatientList(){    
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IPatient[]>(API_URL+'/patients/get_procedure_inpatient_list', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        data?.forEach(element => {
          this.patients.push(element)
        })
        
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }

  attend(id : any){
    localStorage.setItem('procedure-patient-id', id)
    this.router.navigate(['patient-procedure'])
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
