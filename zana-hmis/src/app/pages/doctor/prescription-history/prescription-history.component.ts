import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IPrescription } from 'src/app/domain/prescription';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { ShowDateTimePipe } from 'src/app/pipes/date_time.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';



const API_URL = environment.apiUrl;

@Component({
  selector: 'app-prescription-history',
  templateUrl: './prescription-history.component.html',
  styleUrls: ['./prescription-history.component.scss'],
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
export class PrescriptionHistoryComponent {
  
  patientId : any = null

  prescriptions : IPrescription[] = []

  filterRecords : string = ''

  consultationId : any = null
  admissionId : any = null

  constructor(private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
    ) { }

  async ngOnInit(): Promise<void> {
    this.patientId = localStorage.getItem('patient-id')
    this.getPrescriptionHistory()

    this.consultationId = localStorage.getItem('consultation-id')
    this.admissionId = localStorage.getItem('admission-id')
  }  
  
  async getPrescriptionHistory(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IPrescription[]>(API_URL+'/patients/get_all_prescriptions_by_patient_id?patient_id='+this.patientId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.prescriptions = data!
        console.log(data)

      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load prescriptions')
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
