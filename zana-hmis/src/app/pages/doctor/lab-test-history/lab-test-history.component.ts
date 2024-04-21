import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { PdfViewerModule } from 'ng2-pdf-viewer';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { ILabTest } from 'src/app/domain/lab-test';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { ShowDateTimePipe } from 'src/app/pipes/date_time.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { DownloadFileService } from 'src/app/services/download-file.service';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';



const API_URL = environment.apiUrl;

@Component({
  selector: 'app-lab-test-history',
  templateUrl: './lab-test-history.component.html',
  styleUrls: ['./lab-test-history.component.scss'],
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    AgePipe,
    ShowDateTimePipe,
    RouterLink,
    PdfViewerModule
  ],
})
export class LabTestHistoryComponent {
  
  patientId : any = null

  labTests : ILabTest[] = []

  filterRecords : string = ''

  consultationId : any = null
  nonConsultationId : any = null
  admissionId : any = null

  constructor(private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService,
    private downloadService : DownloadFileService,
    ) { }

  async ngOnInit(): Promise<void> {
    this.patientId = localStorage.getItem('patient-id')
    this.getLabTestHistory()

    this.consultationId = localStorage.getItem('consultation-id')
    this.nonConsultationId = localStorage.getItem('non-consultation-id')
    this.admissionId = localStorage.getItem('admission-id')
  } 
  
  async getLabTestHistory(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<ILabTest[]>(API_URL+'/patients/get_all_lab_tests_by_patient_id?patient_id='+this.patientId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.labTests = data!
        console.log(data)

      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load lab tests')
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


  attachmentUrl : any = ''

 fileExtension : string = ''

  async downloadLabTestFile(fileName : string) {
    
    //calling service
    
    (await (this.downloadService.downloadLabTestAttachment(fileName)))
    .subscribe(response => {

        console.log(response)
        var binaryData = []
        binaryData.push(response.data)
        var url = window.URL.createObjectURL(new Blob(binaryData, {type: "application/*"}))
        
        this.attachmentUrl = url
        
        var ext = response.filename.substr(response.filename.lastIndexOf('.') + 1)
        if(ext === 'pdf'){
          this.fileExtension = 'pdf'
        }else{
          this.fileExtension = ext
        }
        console.log(ext)

    }, (error: any) => {

        console.log(error)
    })
  }


}
