import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IAdmission } from 'src/app/domain/admission';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { ShowDateTimePipe } from 'src/app/pipes/date_time.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;


@Component({
  selector: 'app-doctor-inpatient-list',
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
  templateUrl: './doctor-inpatient-list.component.html',
  styleUrls: ['./doctor-inpatient-list.component.scss']
})
export class DoctorInpatientListComponent {
  admissions : IAdmission[] = []

  filterRecords : string = ''

  clinicianId : any = null
  
  constructor(private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private router : Router,
    private msgBox : MsgBoxService) { }


    async ngOnInit(): Promise<void> {
      await this.loadClinician()
      if(this.clinicianId != null){
        this.loadInpatientList()
      }else{
        this.msgBox.showErrorMessage3('User not found in doctors register')
      }
    }
  
    async loadInpatientList(){   
      this.admissions = [] 
      let options = {
        headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
      }
      this.spinner.show()
      await this.http.get<IAdmission[]>(API_URL+'/patients/get_doctor_inpatient_list', options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          
          this.admissions = data!
          console.log(this.admissions)
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not load patients')
        }
      )
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

    async postAdmission(id : any){
      
      localStorage.setItem('admission-id', id)
      this.router.navigate(['doctor-inpatient'])    
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
