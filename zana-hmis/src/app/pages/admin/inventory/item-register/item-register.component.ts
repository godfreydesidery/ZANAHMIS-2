import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterLink } from '@angular/router';
import { NgxSpinnerModule, NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { AuthService } from 'src/app/auth.service';
import { IItem } from 'src/app/domain/item';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

import { Workbook } from 'exceljs';
import * as XLSX from 'xlsx';

const fs = require('file-saver');


const API_URL = environment.apiUrl;

@Component({
  selector: 'app-item-register',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './item-register.component.html',
  styleUrls: ['./item-register.component.scss']
})
export class ItemRegisterComponent {


  data!          : [][]
  progress       : boolean = false
  progressStatus : string = ''
  totalRecords   : number = 0
  currentRecord  : number = 0



  id                  : any
	code                : string = ''
  barcode             : string = ''
	name         : string = ''
	shortName    : string = ''
	commonName          : string = ''
	vat                 : number = 0
	uom                 : string = ''
	packSize            : number = 0
  costPriceVatIncl : number = 0
  sellingPriceVatIncl : number = 0
	//stock               : number = 0
	//minimumInventory    : number = 0
	//maximumInventory    : number = 0
	//defaultReorderQty   : number = 0
	//defaultReorderLevel : number = 0
	active              : boolean = false
	ingredients         : string = ''


  item! : IItem

  names : string[] = []

  items : IItem[] = []

  filterRecords : string = ''


  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
  ) {}

  ngOnInit(): void {
    this.loadItemNames()
    this.loadItems()
  }


  public async saveItem(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    var item = {
      id                  : this.id,
      code                : this.code,
      barcode             : this.barcode,
      name                : this.name,
      shortName           : this.shortName,
      commonName          : this.commonName,
      vat                 : this.vat,
      uom                 : this.uom,
      packSize            : this.packSize,
      costPriceVatIncl : this.costPriceVatIncl,
      sellingPriceVatIncl : this.sellingPriceVatIncl,
      active              : this.active,
      ingredients         : this.ingredients
    }
    if(this.id == null || this.id == ''){
      //save a new clinic
      this.spinner.show()
      await this.http.post<IItem>(API_URL+'/items/save', item, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id                 = data?.id
          this.code               = data!.code
          this.barcode            = data!.barcode
          this.name        = data!.name
          this.shortName   = data!.shortName
          this.commonName         = data!.commonName
          this.vat                = data!.vat
          this.uom                = data!.uom
          this.packSize           = data!.packSize
          this.costPriceVatIncl   = data!.costPriceVatIncl
          this.sellingPriceVatIncl = data!.sellingPriceVatIncl
          this.active             = data!.active
          this.ingredients        = data!.ingredients
          this.msgBox.showSuccessMessage('Item created successifully')
        }
      )
      .catch(
        error => {
          console.log(error)
          this.msgBox.showErrorMessage(error, 'Could not create item!')
        }
      )

    }else{
      //update an existing item
      this.spinner.show()
      await this.http.post<IItem>(API_URL+'/items/save', item, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id                 = data?.id
          this.code               = data!.code
          this.barcode            = data!.barcode
          this.name        = data!.name
          this.shortName   = data!.shortName
          this.commonName         = data!.commonName
          this.vat                = data!.vat
          this.uom                = data!.uom
          this.packSize           = data!.packSize
          this.costPriceVatIncl   = data!.costPriceVatIncl
          this.sellingPriceVatIncl = data!.sellingPriceVatIncl
          this.active             = data!.active
          this.ingredients        = data!.ingredients
          this.msgBox.showSuccessMessage('Item updated successifully')
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, 'Could not update item')
        }
      )
    }
  }

  async loadItems(){
    this.items = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IItem[]>(API_URL+'/items', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.items.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load Items')
      }
    )
  }

  clear(){
    this.id                 = null
    this.code               = ''
    this.barcode            = ''
    this.name        = ''
    this.shortName   = ''
    this.commonName         = ''
    this.vat                = 0
    this.uom                = ''
    this.packSize           = 0
    this.costPriceVatIncl   = 0
    this.sellingPriceVatIncl = 0
    this.active             = false
    this.ingredients        = ''
  }

  async loadItemNames(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<string[]>(API_URL+'/items/get_names', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.names = []
        data?.forEach(element => {
          this.names.push(element)
        })
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, 'Could not load item names')
      }
    )
  }

  async searchItem(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var code = this.code
    var barcode = this.barcode
    var name = this.name
    if(code != ''){
      barcode = ''
      name = ''
    }
    if(barcode != ''){
      name = ''
    }

    this.spinner.show()
    await this.http.get<IItem>(API_URL+'/items/search?code='+code+'&barcode='+barcode+'&name='+name, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.id                   = data?.id
        this.code                 = data!.code
        this.barcode              = data!.barcode
        this.name                 = data!.name
        this.shortName            = data!.shortName
        this.commonName           = data!.commonName
        this.vat                  = data!.vat
        this.uom                  = data!.uom
        this.packSize             = data!.packSize
        this.costPriceVatIncl     = data!.costPriceVatIncl
        this.sellingPriceVatIncl  = data!.sellingPriceVatIncl
        this.active               = data!.active
        this.ingredients          = data!.ingredients
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, 'Could not find item')
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
      { header: 'ITEM_CODE', key: 'ITEM_CODE'},
      { header: 'BARCODE', key: 'BARCODE'},
      { header: 'ITEM_NAME', key: 'ITEM_NAME'},
      { header: 'SHORT_NAME', key: 'SHORT_NAME'},
      { header: 'COMMON_NAME', key: 'COMMON_NAME'},
      { header: 'VAT', key: 'VAT'},
      { header: 'COST_PRICE_VAT_INCL', key: 'COST_PRICE_VAT_INCL'},
      { header: 'SELLING_PRICE_VAT_INCL', key: 'SELLING_PRICE_VAT_INCL'},
      { header: 'UOM', key: 'UOM'},
      { header: 'PACK_SIZE', key: 'PACK_SIZE'},
      { header: 'INGREDIENTS', key: 'INGREDIENTS'},
      { header: 'ACTIVE', key: 'ACTIVE'}
    ];
    
    workbook.xlsx.writeBuffer().then((data) => {
      let blob = new Blob([data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
      fs.saveAs(blob, 'ItemMasterTemplate.xlsx');
    })
   
  }

  async exportItemsToExcel() {
    let workbook = new Workbook();
    let worksheet = workbook.addWorksheet('ProductSheet')
   
    worksheet.columns = [
      { header: 'ITEM_CODE', key: 'ITEM_CODE'},
      { header: 'BARCODE', key: 'BARCODE'},
      { header: 'ITEM_NAME', key: 'ITEM_NAME'},
      { header: 'SHORT_NAME', key: 'SHORT_NAME'},
      { header: 'COMMON_NAME', key: 'COMMON_NAME'},
      { header: 'VAT', key: 'VAT'},
      { header: 'COST_PRICE_VAT_INCL', key: 'COST_PRICE_VAT_INCL'},
      { header: 'SELLING_PRICE_VAT_INCL', key: 'SELLING_PRICE_VAT_INCL'},
      { header: 'UOM', key: 'UOM'},
      { header: 'PACK_SIZE', key: 'PACK_SIZE'},
      { header: 'INGREDIENTS', key: 'INGREDIENTS'},
      { header: 'ACTIVE', key: 'ACTIVE'}
    ];

    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IItem[]>(API_URL+'/items', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {         
          worksheet.addRow(
            {
              ITEM_CODE              : element.code,
              BARCODE                : element.barcode,
              ITEM_NAME              : element.name,
              SHORT_NAME             : element.shortName,
              COMMON_NAME            : element.commonName,
              VAT                    : element.vat,
              COST_PRICE_VAT_INCL    : element.costPriceVatIncl,
              SELLING_PRICE_VAT_INCL : element.sellingPriceVatIncl,
              UOM                    : element.uom,
              PACK_SIZE              : element.packSize,
              INGREDIENTS            :element.ingredients,
              ACTIVE                 : element.active
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
      fs.saveAs(blob, 'Items.xlsx');
    })
   
  }

  uploadItemsFile(evt: any) {
    var itemsData: [][]
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
      itemsData = (XLSX.utils.sheet_to_json(ws, { header: 1 }))

      this.progress = true
      if (this.validateItemsMaster(itemsData) == true) {
        this.progressStatus = 'Uploading... please wait'
        this.uploadItems(itemsData)
      } else {
        alert('Invalid items file')
      }
      this.progress = false

    }
    reader.readAsBinaryString(target.files[0])
  }

  updateItemsFile(evt: any) {
    var itemsData: [][]
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
      itemsData = (XLSX.utils.sheet_to_json(ws, { header: 1 }))

      this.progress = true
      if (this.validateItemsMaster(itemsData) == true) {
        this.progressStatus = 'Updating... please wait'
        this.updateItems(itemsData)
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

  validateItemsMaster(data : any [][]) : boolean{
    this.clearProgress()
    var rows            = data.length
    var cols            = data[0].length
    this.progressStatus = 'Validating file'
    this.totalRecords   = rows
    var valid           = true

    //validate row header
    if( data[0][0] != 'ITEM_CODE'                     ||
        data[0][1] != 'BARCODE'                  ||
        data[0][2] != 'ITEM_NAME'            
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


  async updateItems(dt : any [][]){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.clearProgress()
    var rows = dt.length
    var cols = dt[0].length
    this.progressStatus = 'Updating items file'
    this.totalRecords = rows

    for(let i = 1; i < dt.length; i++) {
      this.currentRecord = i
      var item = {
        code                : dt[i][0],
        barcode             : dt[i][1],
        vat                 : dt[i][5],
        costPriceVatIncl    : dt[i][6],
        sellingPriceVatIncl : dt[i][7],
        uom                 : dt[i][8],
        packSize            : dt[i][9],
        ingredients         : dt[i][10],
        active              : dt[i][11]
      }

      if(dt[i][0] == undefined){
        alert('End of file reached')
        return
      }
      this.spinner.show()
      await this.http.put(API_URL+'/items/update_by_code', item, options)
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

  async uploadItems(dt : any [][]){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.clearProgress()
    var rows = dt.length
    var cols = dt[0].length
    this.progressStatus = 'Uploading item file'
    this.totalRecords = rows

    for(let i = 1; i < dt.length; i++) {
      this.currentRecord = i
      var item = {
        code                : dt[i][0],
        barcode             : dt[i][1],
        name                : dt[i][2],
        shortName           : dt[i][3],
        commonName          : dt[i][4],
        vat                 : dt[i][5],
        costPriceVatIncl    : dt[i][6],
        sellingPriceVatIncl : dt[i][7],
        uom                 : dt[i][8],
        packSize            : dt[i][9],
        ingredients         : dt[i][10],
        active              : dt[i][11],
      }

      if(dt[i][0] == undefined){
        alert('End of file reached')
        return
      }
      this.spinner.show()
      await this.http.post(API_URL+'/items/save', item, options)
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
