import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterLink } from '@angular/router';
import { NgxSpinnerModule, NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { AuthService } from 'src/app/auth.service';
import { ILabTestType } from 'src/app/domain/lab-test-type';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

import { Workbook } from 'exceljs';
import * as XLSX from 'xlsx';

const fs = require('file-saver');


const API_URL = environment.apiUrl;

@Component({
  selector: 'app-lab-test-type',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './lab-test-type.component.html',
  styleUrls: ['./lab-test-type.component.scss']
})
export class LabTestTypeComponent implements OnInit {

  data!          : [][]
  progress       : boolean = false
  progressStatus : string = ''
  totalRecords   : number = 0
  currentRecord  : number = 0


  id          : any = null
  code        : string = ''
  name        : string = ''
  description : string = ''
  uom         : string = ''
  price       : number = 0
  active      : boolean = true

  labTestTypes : ILabTestType[] = []

  filterRecords : string = ''

  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
  ) { }

  ngOnInit(): void {
    this.loadLabTestTypes()
  }

  public async saveLabTestType(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var labTestType = {
      id          : this.id,
      code        : this.code,
      name        : this.name,
      description : this.description,
      uom         : this.uom,
      price       : this.price,
      active      : true
    }
    if(this.id == null || this.id == ''){
      //save a new labTestType
      this.spinner.show()
      await this.http.post<ILabTestType>(API_URL+'/lab_test_types/save', labTestType, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id           = data?.id
          this.code         = data!.code
          this.name         = data!.name
          this.description  = data!.description
          this.price        = data!.price
          this.uom          = data!.uom
          this.active       = data!.active
          this.msgBox.showSuccessMessage('Lab Test Type created successifully')
          this.loadLabTestTypes()
          
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not create Lab Test Type')
        }
      )

    }else{
      this.spinner.show()
      await this.http.post<ILabTestType>(API_URL+'/lab_test_types/save', labTestType, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id           = data?.id
          this.code         = data!.code
          this.name         = data!.name
          this.description  = data!.description
          this.price        = data!.price
          this.uom          = data!.uom
          this.active       = data!.active
          this.msgBox.showSuccessMessage('Lab Test Type updated successifully')
          this.loadLabTestTypes()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not update Lab Test Type')
        }
      )
    }
    this.clear()
  }

  async loadLabTestTypes(){
    this.labTestTypes = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<ILabTestType[]>(API_URL+'/lab_test_types', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.labTestTypes.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load Lab Test Types')
      }
    )
  }

  clear(){
    this.id           = null
    this.code         = ''
    this.name         = ''
    this.description  = ''
    this.uom          = ''
    this.price        = 0
    this.active       = false
  }

  async getLabTestType(key: string) {
    if(key == ''){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<ILabTestType>(API_URL+'/lab_test_types/get?id='+key, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        this.id           = data?.id
        this.code         = data!.code
        this.name         = data!.name
        this.description  = data!.description
        this.price        = data!.price
        this.uom          = data!.uom
        this.active       = data!.active
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, 'Could not find Lab Test Type')
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
      { header: 'LAB_TEST_TYPE_CODE', key: 'LAB_TEST_TYPE_CODE'},
      { header: 'LAB_TEST_TYPE_NAME', key: 'LAB_TEST_TYPE_NAME'},
      { header: 'DESCRIPTION', key: 'DESCRIPTION'},
      { header: 'PRICE', key: 'PRICE'},
      { header: 'UOM', key: 'UOM'},
      { header: 'ACTIVE', key: 'ACTIVE'}
    ];
    
    workbook.xlsx.writeBuffer().then((data) => {
      let blob = new Blob([data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
      fs.saveAs(blob, 'LabTest Type Master Template.xlsx');
    })
   
  }

  async exportLabTestTypesToExcel() {
    let workbook = new Workbook();
    let worksheet = workbook.addWorksheet('ProductSheet')
   
    worksheet.columns = [
      { header: 'LAB_TEST_TYPE_CODE', key: 'LAB_TEST_TYPE_CODE'},
      { header: 'LAB_TEST_TYPE_NAME', key: 'LAB_TEST_TYPE_NAME'},
      { header: 'DESCRIPTION', key: 'DESCRIPTION'},
      { header: 'PRICE', key: 'PRICE'},
      { header: 'UOM', key: 'UOM'},
      { header: 'ACTIVE', key: 'ACTIVE'}
    ];

    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<ILabTestType[]>(API_URL+'/lab_test_types', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {         
          worksheet.addRow(
            {
              LAB_TEST_TYPE_CODE          : element.code,
              LAB_TEST_TYPE_NAME          : element.name,
              DESCRIPTION            : element.description,
              PRICE                  : element.price,
              UOM                    : element.uom,
              ACTIVE : element.active
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
      fs.saveAs(blob, 'LabTest Types.xlsx');
    })
   
  }

  async uploadLabTestTypesFile(evt: any) {
    var lab_testTypeData: [][]
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
      lab_testTypeData = (XLSX.utils.sheet_to_json(ws, { header: 1 }))

      this.progress = true
      if (this.validateLabTestTypeMaster(lab_testTypeData) == true) {
        this.progressStatus = 'Uploading... please wait'
        this.uploadLabTestType(lab_testTypeData)
      } else {
        alert('Invalid LabTest type file')
      }
      this.progress = false
    }
    reader.readAsBinaryString(target.files[0])
    
  }

  updateMedicineFile(evt: any) {
    var lab_testTypeData: [][]
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
      lab_testTypeData = (XLSX.utils.sheet_to_json(ws, { header: 1 }))

      this.progress = true
      if (this.validateLabTestTypeMaster(lab_testTypeData) === true) {
        this.progressStatus = 'Updating... please wait'
        this.updateLabTestTypes(lab_testTypeData)
      } else {
        alert('Invalid lab_test type file')
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

  validateLabTestTypeMaster(data : any [][]) : boolean{
    this.clearProgress()
    var rows            = data.length
    var cols            = data[0].length
    this.progressStatus = 'Validating file'
    this.totalRecords   = rows
    var valid           = true

    //validate row header
    if( data[0][0] != 'LAB_TEST_TYPE_CODE' ||
        data[0][1] != 'LAB_TEST_TYPE_NAME' ||
        data[0][2] != 'DESCRIPTION'            
        )
    {
      valid = false
    }
    for(let i = 1; i < data.length; i++) {
      this.currentRecord = i
      //checks for empty code and name
      if( data[i][0] === '' ||
          data[i][1] === ''
        )
      {
        alert('Error in record no '+ i + '.Blank or invalid entry')
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


  async updateLabTestTypes(dt : any [][]){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.clearProgress()
    var rows = dt.length
    var cols = dt[0].length
    this.progressStatus = 'Updating lab_test types file'
    this.totalRecords = rows

    for(let i = 1; i < dt.length; i++) {
      this.currentRecord = i
      var labTestType = {
        code          : dt[i][0],
        //name          : dt[i][1],
        //description   : dt[i][2],
        price         : dt[i][3],
        uom           : dt[i][4],
        type          : dt[i][5],
        category      : dt[i][6],
        active        : dt[i][7]
      }

      if(dt[i][0] == undefined){
        alert('End of file reached')
        return
      }
      this.spinner.show()
      await this.http.put(API_URL+'/lab_test_types/update_by_code', labTestType, options)
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

  async uploadLabTestType(dt : any [][]){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.clearProgress()
    var rows = dt.length
    var cols = dt[0].length
    this.progressStatus = 'Uploading lab_test type file'
    this.totalRecords = rows

    for(let i = 1; i < dt.length; i++) {
      this.currentRecord = i
      var medicine = {
        code          : dt[i][0],
        name          : dt[i][1],
        description   : dt[i][2],
        price         : dt[i][3],
        uom           : dt[i][4],
        active        : dt[i][5]
      }

      if(dt[i][0] == undefined){
        alert('End of file reached')
        return
      }
      this.spinner.show()
      await this.http.post(API_URL+'/lab_test_types/save', medicine, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .catch(
        error => {
          console.log(error)
        }
      )
      }
    this.clearProgress()
    this.loadLabTestTypes()
  }
}
