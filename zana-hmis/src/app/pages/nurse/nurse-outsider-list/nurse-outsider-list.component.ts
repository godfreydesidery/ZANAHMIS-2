import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { INonConsultation } from 'src/app/domain/non-consultation';
import { IPatient } from 'src/app/domain/patient';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-nurse-outsider-list',
  templateUrl: './nurse-outsider-list.component.html',
  styleUrls: ['./nurse-outsider-list.component.scss'],
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
export class NurseOutsiderListComponent {
  nonConsultations : INonConsultation[] = []

  filterRecords : string = ''
  
  constructor(private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private router : Router,
    private msgBox : MsgBoxService) { }


    ngOnInit(): void {
      this.loadInpatientList()
    }
  
    attend(id : any){
      localStorage.setItem('radiology-patient-id', id)
      this.router.navigate(['radiology'])
    }
  
    async loadInpatientList(){   
      this.nonConsultations = [] 
      let options = {
        headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
      }
      this.spinner.show()
      await this.http.get<INonConsultation[]>(API_URL+'/patients/get_nurse_outsider_list', options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          
          this.nonConsultations = data!
          console.log(this.nonConsultations)
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not load patients')
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