import { CommonModule } from '@angular/common';
import { HttpClient, HttpEventType, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { Router, RouterLink } from '@angular/router';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PdfViewerModule } from 'ng2-pdf-viewer';
import { NgxSpinnerService } from 'ngx-spinner';
import { Observable, finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { ILabTest } from 'src/app/domain/lab-test';
import { ILabTestAttachment } from 'src/app/domain/lab-test-attachment';
import { IPatient } from 'src/app/domain/patient';
import { IRadiology } from 'src/app/domain/radiology';
import { IRadiologyAttachment } from 'src/app/domain/radiology-attachment';
import { IRadiologyType } from 'src/app/domain/radiology-type';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { DownloadFileService } from 'src/app/services/download-file.service';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { UploadFileService } from 'src/app/services/upload-file.service';
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
    RouterLink,
    PdfViewerModule
  ],
})
export class RadiologyComponent {
  id : any


  patient! : IPatient

  radiologies : IRadiology[] = []

  diagnosisTypeNames : string[] = []

  filterRecords : string = ''


  
  selectedFiles!: FileList;
  currentFile!: File;
  progress = 0;
  //message = '';
  name = ''

  fileInfos!: Observable<any>;


  constructor(private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private router : Router,
    private uploadService: UploadFileService,
    private downloadService : DownloadFileService,
    private msgBox : MsgBoxService,
    private sanitizer: DomSanitizer,) { }

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


  async saveRadiologyResults(radiology : IRadiology){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var radio  = {
      id          : radiology.id,
      result      : radiology.result
    }
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/save_radiology_results', radio, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Updated successifully')
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.loadRadiologiesByPatient(this.id)
  }





  selectFile(event : any) {
    this.selectedFiles = event.target.files;
  }

  upload(radiology : IRadiology) {

    if(this.name === ''){
      this.msgBox.showErrorMessage3('Please provide title name')
      return
    }

    this.progress = 0;

    this.currentFile = this.selectedFiles.item(0)!
    this.uploadService.uploadRadiologyAttachment(this.currentFile, radiology, this.name).subscribe(
      async event => {
        if (event.type === HttpEventType.UploadProgress) {
          //this.progress = Math.round(100 * event.loaded / event.total!);
        } else if (event instanceof HttpResponse) {
          //this.message = event.body.message;
          //this.fileInfos = this.uploadService.getFiles();
        }
        this.radiologies = []
        await this.loadRadiologiesByPatient(this.id)
        //this.msgBox.showSuccessMessage('Uploaded Successifully')
      },
      err => {
        console.log(err)
        this.progress = 0;
        //this.message = 'Could not upload the file!';
        this.currentFile = undefined!;
        this.msgBox.showErrorMessage(err, '')
      });
  
    this.selectedFiles = undefined!;
    this.name = ''
  }


  showAttachment(fileName : string){
    alert(fileName)
  }


 attachmentUrl : any = ''

 fileExtension : string = ''

  async downloadFile(fileName : string) {
    
    //calling service
    
    (await (this.downloadService.downloadRadiologyAttachment(fileName)))
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


  async deleteFile(attachment : IRadiologyAttachment){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/delete_radiology_attachment?attachment_id='+attachment.id, null, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      async data => {
        this.msgBox.showSuccessMessage('Success')
        this.radiologies = []
        await this.loadRadiologiesByPatient(this.id)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
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

