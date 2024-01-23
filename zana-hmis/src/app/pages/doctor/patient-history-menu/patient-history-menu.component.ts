import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { AuthService } from 'src/app/auth.service';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { ShowDateTimePipe } from 'src/app/pipes/date_time.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';



const API_URL = environment.apiUrl;

@Component({
  selector: 'app-patient-history-menu',
  templateUrl: './patient-history-menu.component.html',
  styleUrls: ['./patient-history-menu.component.scss'],
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
export class PatientHistoryMenuComponent {

  filterRecords : string = ''

  consultationId : any = null
  admissionId : any = null

  constructor(private auth : AuthService,
              private http :HttpClient,
              private spinner : NgxSpinnerService,
              private msgBox : MsgBoxService
              ) { }

  async ngOnInit(): Promise<void> {
      this.consultationId = localStorage.getItem('consultation-id')
      this.admissionId = localStorage.getItem('admission-id')
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
