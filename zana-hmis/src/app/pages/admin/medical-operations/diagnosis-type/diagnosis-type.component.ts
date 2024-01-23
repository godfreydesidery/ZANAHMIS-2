import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgxSpinnerModule, NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IDiagnosisType } from 'src/app/domain/diagnosis-type';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';
import { Workbook } from 'exceljs';
import * as XLSX from 'xlsx';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { RouterLink } from '@angular/router';

import {MatPaginatorModule} from '@angular/material/paginator';
import { NgbPaginationModule, NgbTypeaheadModule } from '@ng-bootstrap/ng-bootstrap';

const fs = require('file-saver');

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-diagnosis-type',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './diagnosis-type.component.html',
  styleUrls: ['./diagnosis-type.component.scss']
})
export class DiagnosisTypeComponent implements OnInit {

  data!          : [][]
  progress       : boolean = false
  progressStatus : string = ''
  totalRecords   : number = 0
  currentRecord  : number = 0


  id          : any = null
  code        : string = ''
  name        : string = ''
  description : string = ''
  active      : boolean = true

  diagnosisTypes : IDiagnosisType[] = []

  filterRecords : string = ''

  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
  ) { }

  ngOnInit(): void {
    this.loadDiagnosisTypes()
  }

  public async saveDiagnosisType(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var diagnosisType = {
      id          : this.id,
      code        : this.code,
      name        : this.name,
      description : this.description,
      active      : true
    }
    if(this.id == null || this.id == ''){
      //save a new diagnosisType
      this.spinner.show()
      await this.http.post<IDiagnosisType>(API_URL+'/diagnosis_types/save', diagnosisType, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id           = data?.id
          this.code         = data!.code
          this.name         = data!.name
          this.description  = data!.description
          this.active       = data!.active
          this.msgBox.showSuccessMessage('Diagnosis Type created successifully')
          this.loadDiagnosisTypes()
          
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not create Diagnosis Type')
        }
      )

    }else{
      //update an existing clinic
      this.spinner.show()
      await this.http.post<IDiagnosisType>(API_URL+'/diagnosis_types/save', diagnosisType, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id           = data?.id
          this.code         = data!.code
          this.name         = data!.name
          this.description  = data!.description
          this.active       = data!.active
          this.msgBox.showSuccessMessage('Diagnosis Type updated successifully')
          this.loadDiagnosisTypes()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not update Diagnosis Type')
        }
      )
    }
    this.clear()
  }

  async loadDiagnosisTypes(){
    this.diagnosisTypes = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IDiagnosisType[]>(API_URL+'/diagnosis_types', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.diagnosisTypes.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load Diagnosis Types')
      }
    )
  }

  clear(){
    this.id           = null
    this.code         = ''
    this.name         = ''
    this.description  = ''
    this.active       = false
  }

  async getDiagnosisType(key: string) {
    if(key == ''){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IDiagnosisType>(API_URL+'/diagnosis_types/get?id='+key, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        this.id             = data?.id
          this.code         = data!.code
          this.name         = data!.name
          this.description  = data!.description
          this.active       = data!.active
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, 'Could not find diagnosis type')
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


  exportTemplateToExcel() {
    let workbook = new Workbook();
    let worksheet = workbook.addWorksheet('TemplateSheet')
   
    worksheet.columns = [
      { header: 'CODE', key: 'CODE'},
      { header: 'NAME', key: 'NAME'},
      { header: 'DESCRIPTION', key: 'DESCRIPTION'}
    ];
    
    workbook.xlsx.writeBuffer().then((data) => {
      let blob = new Blob([data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
      fs.saveAs(blob, 'ProductMasterTemplate.xlsx');
    })
   
  }

  async exportDiagnosisToExcel() {
    let workbook = new Workbook();
    let worksheet = workbook.addWorksheet('ProductSheet')
   
    worksheet.columns = [
      { header: 'CODE', key: 'CODE'},
      { header: 'NAME', key: 'NAME'},
      { header: 'DESCRIPTION', key: 'DESCRIPTION'}
    ];

    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IDiagnosisType[]>(API_URL+'/diagnosis_types', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {         
          worksheet.addRow(
            {
              CODE                   : element.code,
              NAME                : element.name,
              DESCRIPTION            : element.description
            },"n"
          )
        })
        this.spinner.hide()
      }
    )
    .catch(
      error => {
        console.log(error)
      }
    )
   
    workbook.xlsx.writeBuffer().then((data) => {
      let blob = new Blob([data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
      fs.saveAs(blob, 'DiagnosisTypes.xlsx');
    })
   
  }

  uploadDiagnosisFile(evt: any) {
    var diagnosisData: [][]
    if (this.progress == true) {
      alert('Could not process, a mass operation going on')
      return
    }
    const target: DataTransfer = <DataTransfer>(evt.target)
    if (target.files.length !== 1) {
      alert("Can not use multiple files")
      return
    }
    const reader: FileReader = new FileReader()
    reader.onload = (e: any) => {
      const bstr: string = e.target.result
      const wb: XLSX.WorkBook = XLSX.read(bstr, { type: 'binary' })
      const wsname: string = wb.SheetNames[0]
      const ws: XLSX.WorkSheet = wb.Sheets[wsname]
      diagnosisData = (XLSX.utils.sheet_to_json(ws, { header: 1 }))

      this.progress = true
      if (this.validateDiagnosisMaster(diagnosisData) == true) {
        this.progressStatus = 'Uploading... please wait'
        this.uploadDiagnosis(diagnosisData)
      } else {
        alert('Invalid product file')
      }
      this.progress = false

    }
    reader.readAsBinaryString(target.files[0])
  }

  updateDiagnoosisFile(evt: any) {
    var diagnosisData: [][]
    if (this.progress == true) {
      alert('Could not process, a mass operation going on')
      return
    }
    const target: DataTransfer = <DataTransfer>(evt.target)
    if (target.files.length !== 1) {
      alert("Can not use multiple files")
      return
    }
    const reader: FileReader = new FileReader()
    reader.onload = (e: any) => {
      const bstr: string = e.target.result
      const wb: XLSX.WorkBook = XLSX.read(bstr, { type: 'binary' })
      const wsname: string = wb.SheetNames[0]
      const ws: XLSX.WorkSheet = wb.Sheets[wsname]
      diagnosisData = (XLSX.utils.sheet_to_json(ws, { header: 1 }))

      this.progress = true
      if (this.validateDiagnosisMaster(diagnosisData) == true) {
        this.progressStatus = 'Updating... please wait'
        this.updateDiagnosis(diagnosisData)
      } else {
        alert('Invalid product file')
      }
      this.progress = false
    }
    reader.readAsBinaryString(target.files[0])
  }

  onFileChange(evt: any) {
    if (this.progress == true) {
      alert('Could not process, a mass operation going on')
      return
    }
    const target: DataTransfer = <DataTransfer>(evt.target)
    if (target.files.length !== 1) {
      alert("Cannot use multiple files")
      return
    }
    const reader: FileReader = new FileReader()
    reader.onload = (e: any) => {
      const bstr: string = e.target.result
      const wb: XLSX.WorkBook = XLSX.read(bstr, { type: 'binary' })
      const wsname: string = wb.SheetNames[0]
      const ws: XLSX.WorkSheet = wb.Sheets[wsname]
      this.data = (XLSX.utils.sheet_to_json(ws, { header: 1 }))
      this.progress = true
      if (this.validateData(this.data) == true) {
        this.uploadData(this.data)
      }
      this.progress = false
    }
    reader.readAsBinaryString(target.files[0])
  }

  validateData(data :  [][]) : boolean{
    var valid = true
    for(let j = 0; j < data[0].length; j++){
      //validate the row header
    }
    for(let i = 1; i < data.length; i++) {
      for(let j = 0; j < data[i].length; j++) {
        //validate content
        //alert((data[i][j]))

      }
    }
    return valid;
  }
  uploadData(data : [][]){
    var object : any
    for(let i = 1; i < data.length; i++) {
      

    }
  }

  validateDiagnosisMaster(data : any [][]) : boolean{
    this.clearProgress()
    var rows            = data.length
    var cols            = data[0].length
    this.progressStatus = 'Validating file'
    this.totalRecords   = rows
    var valid           = true

    //validate row header
    if( data[0][0] != 'CODE'                     ||
        data[0][1] != 'NAME'                  ||
        data[0][2] != 'DESCRIPTION'            
        )
    {
      valid = false
    }
    for(let i = 1; i < data.length; i++) {
      this.currentRecord = i
      //checks for empty code and name
      if( data[i][0] == '' ||
          data[i][2] == ''
        )
      {
        alert('Error in record no '+ i)
        valid = false
      }
    }
    this.clearProgress()
    return valid;
  }

  clearProgress(){
    this.progressStatus = ''
    this.totalRecords   = 0
    this.currentRecord  = 0
  }


  async updateDiagnosis(dt : any [][]){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.clearProgress()
    var rows = dt.length
    var cols = dt[0].length
    this.progressStatus = 'Updating product file'
    this.totalRecords = rows

    for(let i = 1; i < dt.length; i++) {
      this.currentRecord = i
      var product = {
        code                : dt[i][0],
        name                : dt[i][1],
        description         : dt[i][2]
      }

      if(dt[i][0] == undefined){
        alert('End of file reached')
        return
      }
      this.spinner.show()
      await this.http.put(API_URL+'/products/update_by_code', product, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .catch(
        error => {
          console.log(error)
        }
      )
      }
    this.clearProgress()
  }

  async uploadDiagnosis(dt : any [][]){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.clearProgress()
    var rows = dt.length
    var cols = dt[0].length
    this.progressStatus = 'Uploading product file'
    this.totalRecords = rows

    for(let i = 1; i < dt.length; i++) {
      this.currentRecord = i
      var product = {
        code                : dt[i][0],
        name             : dt[i][1],
        description         : dt[i][2]
      }

      if(dt[i][0] == undefined){
        alert('End of file reached')
        return
      }
      this.spinner.show()
      await this.http.post(API_URL+'/diagnosis_types/save', product, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .catch(
        error => {
          console.log(error)
        }
      )
      }
    this.clearProgress()
  }

}
