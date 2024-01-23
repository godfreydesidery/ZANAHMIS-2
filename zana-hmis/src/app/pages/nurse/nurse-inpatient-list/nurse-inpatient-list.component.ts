import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IAdmission } from 'src/app/domain/admission';
import { IPatient } from 'src/app/domain/patient';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-nurse-inpatient-list',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './nurse-inpatient-list.component.html',
  styleUrls: ['./nurse-inpatient-list.component.scss']
})
export class NurseInpatientListComponent {

  nurseId : any = null

  admissions : IAdmission[] = []

  filterRecords : string = ''
  
  constructor(private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private router : Router,
    private msgBox : MsgBoxService) { }


    async ngOnInit(): Promise<void> {
      await this.loadNurse()
      if(this.nurseId != null){
        this.loadInpatientList()
      }else{
        this.msgBox.showErrorMessage3('User not found in nurse register')
      }
    }
  
    async loadInpatientList(){   
      this.admissions = [] 
      let options = {
        headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
      }
      this.spinner.show()
      await this.http.get<IAdmission[]>(API_URL+'/patients/get_nurse_inpatient_list', options)
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
    
    async loadNurse(){    
      var username = localStorage.getItem('username')!
      let options = {
        headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
      }
      this.spinner.show()
      await this.http.get<any>(API_URL+'/nurses/load_nurse_by_username?username='+username, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.nurseId = data
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not load nurse')
        }
      )
    }

    async postAdmission(id : any){     
      localStorage.setItem('admission-id', id)
      localStorage.setItem('nurse-id', this.nurseId)
      this.router.navigate(['nurse-inpatient-chart'])    
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
