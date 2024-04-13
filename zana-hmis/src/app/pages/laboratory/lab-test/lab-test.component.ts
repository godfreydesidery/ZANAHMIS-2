import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { ILabTest } from 'src/app/domain/lab-test';
import { IPatient } from 'src/app/domain/patient';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

import { UploadFileService } from 'src/app/services/upload-file.service';
import { HttpEventType, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ILabTestAttachment } from 'src/app/domain/lab-test-attachment';
import { DownloadFileService } from 'src/app/services/download-file.service';
import { DomSanitizer } from '@angular/platform-browser';
import { PdfViewerModule } from 'ng2-pdf-viewer';

const API_URL = environment.apiUrl;


@Component({
  selector: 'app-lab-test',
  templateUrl: './lab-test.component.html',
  styleUrls: ['./lab-test.component.scss'],
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    AgePipe,
    PdfViewerModule
  ],
})
export class LabTestComponent implements OnInit {

  id : any


  patient! : IPatient

  labTests : ILabTest[] = []

  rs : number[] = [1,2,3.4,5,6]

  filterRecords : string = ''



  selectedFiles!: FileList;
  currentFile!: File;
  progress = 0;
  message = '';
  name = ''

  fileInfos!: Observable<any>;


  //attachments : ILabTestAttachment[] = []

  constructor(private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private router : Router,
    private msgBox : MsgBoxService,
    private uploadService: UploadFileService,
    private downloadService : DownloadFileService,
    private sanitizer: DomSanitizer,) { }

  async ngOnInit(): Promise<void> {
    this.id = localStorage.getItem('lab-test-patient-id')
    localStorage.removeItem('lab-test-patient-id')
    await this.loadPatient(this.id)
    await this.loadLabTestsByPatient(this.id)

    this.fileInfos = this.uploadService.getFiles();
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

  async loadLabTestsByPatient(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<ILabTest[]>(API_URL+'/patients/get_lab_tests_by_patient_id?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        
        this.labTests = data!
        console.log(this.labTests)
      }
    )
    .catch(
      error => {
        this.labTests = []
        this.msgBox.showErrorMessage(error, 'Could not load lab tests')
      }
    )
  }

  async acceptLabTest(labTest : ILabTest){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var test  = {
      id : labTest.id,
      result : labTest.result,
      range : labTest.range,
      level : labTest.level,
      unit : labTest.unit
    }
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/accept_lab_test', test, options)
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
    this.loadLabTestsByPatient(this.id)
  }

  async rejectLabTest(labTest : ILabTest){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var test  = {
      id : labTest.id,
      result : labTest.result,
      range : labTest.range,
      level : labTest.level,
      unit : labTest.unit
    }
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/reject_lab_test', test, options)
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
    this.loadLabTestsByPatient(this.id)
  }

  async holdLabTest(labTest : ILabTest){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var test  = {
      id : labTest.id,
      result : labTest.result,
      range : labTest.range,
      level : labTest.level,
      unit : labTest.unit
    }
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/hold_lab_test', test, options)
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
    this.loadLabTestsByPatient(this.id)
  }

  async collectLabTest(labTest : ILabTest){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var test  = {
      id : labTest.id,
      result : labTest.result,
      range : labTest.range,
      level : labTest.level,
      unit : labTest.unit
    }
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/collect_lab_test', test, options)
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
    this.loadLabTestsByPatient(this.id)
  }

  async verifyLabTest(labTest : ILabTest){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var test  = {
      id : labTest.id,
      result : labTest.result,
      range : labTest.range,
      level : labTest.level,
      unit : labTest.unit
    }
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/verify_lab_test', test, options)
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
    this.loadLabTestsByPatient(this.id)
  }

  async saveReasonForRejection(id : any, reason : string){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var test  = {
      id          : id,
      rejectComment : reason
    }
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/lab_tests/save_reason_for_rejection', test, options)
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


  selectFile(event : any) {
    this.selectedFiles = event.target.files;
  }

  upload(labTest : ILabTest) {

    //var name = name1

    if(this.name === ''){
      this.msgBox.showErrorMessage3('Please provide tittle name')
      return
    }

    this.progress = 0;

    this.currentFile = this.selectedFiles.item(0)!
    this.uploadService.uploadLabTestAttachment(this.currentFile, labTest, this.name).subscribe(
      async event => {
        if (event.type === HttpEventType.UploadProgress) {
          this.progress = Math.round(100 * event.loaded / event.total!);
        } else if (event instanceof HttpResponse) {
          this.message = event.body.message;
          //this.fileInfos = this.uploadService.getFiles();
        }
        this.labTests = []
        await this.loadLabTestsByPatient(this.id)
        //this.msgBox.showSuccessMessage('Upload Successiful')
      },
      err => {
        console.log(err)
        this.progress = 0;
        this.message = 'Could not upload the file!';
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


  async deleteFile(attachment : ILabTestAttachment){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.post<boolean>(API_URL+'/patients/delete_lab_test_attachment?attachment_id='+attachment.id, null, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      async data => {
        this.msgBox.showSuccessMessage('Success')
        this.labTests = []
        await this.loadLabTestsByPatient(this.id)
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






