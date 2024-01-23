import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterLink } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerModule, NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { AuthService } from 'src/app/auth.service';
import { IProcedureType } from 'src/app/domain/procedure-type';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';


import { Workbook } from 'exceljs';
import * as XLSX from 'xlsx';

const fs = require('file-saver');

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-procedure-type',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './procedure-type.component.html',
  styleUrls: ['./procedure-type.component.scss']
})
export class ProcedureTypeComponent implements OnInit {

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

  procedureTypes : IProcedureType[] = []

  filterRecords : string = ''

  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
  ) { }

  ngOnInit(): void {
    this.loadProcedureTypes()
  }

  public async saveProcedureType(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var procedureType = {
      id          : this.id,
      code        : this.code,
      name        : this.name,
      description : this.description,
      uom         : this.uom,
      price       : this.price,
      active      : true
    }
    if(this.id == null || this.id == ''){
      //save a new procedureType
      this.spinner.show()
      await this.http.post<IProcedureType>(API_URL+'/procedure_types/save', procedureType, options)
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
          this.msgBox.showSuccessMessage('Procedure Type created successifully')
          this.loadProcedureTypes()
          
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not create Procedure Type')
        }
      )

    }else{
      //update an existing clinic
      this.spinner.show()
      await this.http.post<IProcedureType>(API_URL+'/procedure_types/save', procedureType, options)
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
          this.msgBox.showSuccessMessage('Procedure Type updated successifully')
          this.loadProcedureTypes()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not update Procedure Type')
        }
      )
    }
    this.clear()
  }

  async loadProcedureTypes(){
    this.procedureTypes = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IProcedureType[]>(API_URL+'/procedure_types', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.procedureTypes.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load Procedure Types')
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

  async getProcedureType(key: string) {
    if(key == ''){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IProcedureType>(API_URL+'/procedure_types/get?id='+key, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        this.id             = data?.id
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
        this.msgBox.showErrorMessage(error, 'Could not find Procedure Type')
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
      { header: 'PROCEDURE_TYPE_CODE', key: 'PROCEDURE_TYPE_CODE'},
      { header: 'PROCEDURE_TYPE_NAME', key: 'PROCEDURE_TYPE_NAME'},
      { header: 'DESCRIPTION', key: 'DESCRIPTION'},
      { header: 'PRICE', key: 'PRICE'},
      { header: 'UOM', key: 'UOM'},
      { header: 'ACTIVE', key: 'ACTIVE'}
    ];
    
    workbook.xlsx.writeBuffer().then((data) => {
      let blob = new Blob([data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
      fs.saveAs(blob, 'Procedure Type Master Template.xlsx');
    })
   
  }

  async exportProcedureTypesToExcel() {
    let workbook = new Workbook();
    let worksheet = workbook.addWorksheet('ProductSheet')
   
    worksheet.columns = [
      { header: 'PROCEDURE_TYPE_CODE', key: 'PROCEDURE_TYPE_CODE'},
      { header: 'PROCEDURE_TYPE_NAME', key: 'PROCEDURE_TYPE_NAME'},
      { header: 'DESCRIPTION', key: 'DESCRIPTION'},
      { header: 'PRICE', key: 'PRICE'},
      { header: 'UOM', key: 'UOM'},
      { header: 'ACTIVE', key: 'ACTIVE'}
    ];

    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IProcedureType[]>(API_URL+'/procedure_types', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {         
          worksheet.addRow(
            {
              PROCEDURE_TYPE_CODE          : element.code,
              PROCEDURE_TYPE_NAME          : element.name,
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
      fs.saveAs(blob, 'Procedure Types.xlsx');
    })
   
  }

  async uploadProcedureTypesFile(evt: any) {
    var procedureTypeData: [][]
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
      procedureTypeData = (XLSX.utils.sheet_to_json(ws, { header: 1 }))

      this.progress = true
      if (this.validateProcedureTypeMaster(procedureTypeData) == true) {
        this.progressStatus = 'Uploading... please wait'
        this.uploadProcedureType(procedureTypeData)
      } else {
        alert('Invalid Procedure type file')
      }
      this.progress = false
    }
    reader.readAsBinaryString(target.files[0])
    
  }

  updateMedicineFile(evt: any) {
    var procedureTypeData: [][]
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
      procedureTypeData = (XLSX.utils.sheet_to_json(ws, { header: 1 }))

      this.progress = true
      if (this.validateProcedureTypeMaster(procedureTypeData) === true) {
        this.progressStatus = 'Updating... please wait'
        this.updateProcedureTypes(procedureTypeData)
      } else {
        alert('Invalid procedure type file')
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

  validateProcedureTypeMaster(data : any [][]) : boolean{
    this.clearProgress()
    var rows            = data.length
    var cols            = data[0].length
    this.progressStatus = 'Validating file'
    this.totalRecords   = rows
    var valid           = true

    //validate row header
    if( data[0][0] != 'PROCEDURE_TYPE_CODE' ||
        data[0][1] != 'PROCEDURE_TYPE_NAME' ||
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


  async updateProcedureTypes(dt : any [][]){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.clearProgress()
    var rows = dt.length
    var cols = dt[0].length
    this.progressStatus = 'Updating procedure types file'
    this.totalRecords = rows

    for(let i = 1; i < dt.length; i++) {
      this.currentRecord = i
      var procedureType = {
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
      await this.http.put(API_URL+'/procedure_types/update_by_code', procedureType, options)
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

  async uploadProcedureType(dt : any [][]){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.clearProgress()
    var rows = dt.length
    var cols = dt[0].length
    this.progressStatus = 'Uploading procedure type file'
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
      await this.http.post(API_URL+'/procedure_types/save', medicine, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .catch(
        error => {
          console.log(error)
        }
      )
      }
    this.clearProgress()
    this.loadProcedureTypes()
  }

}
