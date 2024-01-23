import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IMedicine } from 'src/app/domain/medicine';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';
import { IPharmacyToStoreRO } from 'src/app/domain/pharmacy-to-store-r-o';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { ILocalPurchaseOrder } from 'src/app/domain/local-purchase-order';
import { ISupplier } from 'src/app/domain/supplier';
import { IItem } from 'src/app/domain/item';
import { ILabTestType } from 'src/app/domain/lab-test-type';
import { ILabTestTypeRange } from 'src/app/domain/lab-test-type-range';
import { ISupplierItemPrice } from 'src/app/domain/supplier-item-price';
import { ISupllierItemPriceList } from 'src/app/domain/supplier-item-price-list';

import { Workbook } from 'exceljs';
import * as XLSX from 'xlsx';

const fs = require('file-saver');

const API_URL = environment.apiUrl;


@Component({
  selector: 'app-supplier-item-price-list',
  templateUrl: './supplier-item-price-list.component.html',
  styleUrls: ['./supplier-item-price-list.component.scss'],
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
export class SupplierItemPriceListComponent {


  data!          : [][]
  progress       : boolean = false
  progressStatus : string = ''
  totalRecords   : number = 0
  currentRecord  : number = 0

  @ViewChild('inputFile') inputVariable : ElementRef | undefined





  
  name        : string = ''
  active      : boolean = true

  supplier! : ISupplier

  suppliers : ISupplier[] = []

  supplierItemPriceList! : ISupllierItemPriceList

  //supplierItemPrice : ISupplierItemPrice[] = []

  //supplierName : string = ''

  filterRecords : string = ''
  filterItems : string = ''


  detailId     : any = null
  detailItemId : any = null
  detailItemCode : string = ''
  detailItemName : string = ''
  
  detailItemCodeAndName : string = ''
  detailPrice : number = 0
  detailTerms : string =''

  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
  ) { }

  ngOnInit(): void {
    this.loadSuppliers()
    //this.loadLabTestTypeRanges()
  }

  async loadSuppliers(){
    this.suppliers = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<ISupplier[]>(API_URL+'/suppliers', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.suppliers = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not Suppliers')
      }
    )
  }

  supplierItemPrice : ISupplierItemPrice[] = []
  async getSupplierItems(supplierId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<ISupllierItemPriceList>(API_URL+'/supplier_item_prices/get_item_price_list_by_supplier?supplier_id='+supplierId, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.supplierItemPriceList = data!
          this.supplier = this.supplierItemPriceList.supplier
          console.log(this.supplierItemPriceList)
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, '')
        }
      )
  }

  async saveDetail(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var supplierItemPrice = {
      id: this.detailId,
      price: this.detailPrice,
      terms: this.detailTerms,
      supplier: {id : this.supplier.id},
      item: {id : this.detailItemId, code : this.detailItemCode}
    }
    this.spinner.show()
    await this.http.post<ISupllierItemPriceList>(API_URL+'/supplier_item_prices/save', supplierItemPrice, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.supplierItemPriceList = data!
        this.supplier= this.supplierItemPriceList.supplier
        console.log(this.supplierItemPriceList)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  locked : boolean = false

  

  

  

  clear(){
    this.detailId = null
    this.detailItemId = null
    this.detailItemCode = ''
    this.detailItemName = ''
    this.detailItemCodeAndName = ''
    this.detailPrice = 0
    this.detailTerms = ''
  }


  
  items : IItem[] = []
  async loadItemsLike(value : string){
    this.items = []
    if(value.length < 3){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.get<IItem[]>(API_URL+'/items/load_items_like?name_like='+value, options)
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.items = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }


  item! : IItem
  async getItem(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.items = []
    this.spinner.show()
    await this.http.get<IItem>(API_URL+'/items/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {
        this.detailItemId = data?.id
        this.detailItemCode = data!.code
        this.detailItemName = data!.name
        this.detailItemCodeAndName = data!.code +' | '+ data!.name
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }

  async getDetail(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    this.clear()
    await this.http.get<ISupplierItemPrice>(API_URL+'/supplier_item_prices/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {
        this.detailId = data!.id
        this.detailItemId = data?.item.id
        this.detailItemCode = data!.item.code
        this.detailItemName = data!.item.name
        this.detailPrice = data!.price
        this.detailTerms = data!.terms
        this.detailItemCodeAndName = this.detailItemCode +' | '+ this.detailItemName
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )

  }

  async deleteDetail(id : any, itemId : any){
    if(!window.confirm('Deleting item. Confirm?')){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var supplierItemPrice = {
      id: id,
      item: {id : itemId}
    }
    this.spinner.show()
    this.clear()
    await this.http.post(API_URL+'/supplier_item_prices/delete', supplierItemPrice, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {
        this.msgBox.showSuccessMessage('Deleted')
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
    this.getSupplierItems(this.supplier.id)
  }

  

  

  
  public grant(privilege : string[]) : boolean{
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

  async getTemplateToExcel() {
    let workbook = new Workbook();
    let worksheet = workbook.addWorksheet('ProductSheet')
   
    worksheet.columns = [
      { header: 'ITEM_CODE', key: 'ITEM_CODE'},
      { header: 'ITEM_NAME', key: 'ITEM_NAME'},
      { header: 'PRICE', key: 'PRICE'},
      { header: 'TERMS', key: 'TERMS'}
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
              ITEM_CODE  : element.code,
              ITEM_NAME  : element.name,
              PRICE      : '',
              TERMS      : ''
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
      fs.saveAs(blob, this.supplier!.name + ' Price List Template.xlsx');
    })
   
  }


  async uploadSupplierPriceListFile(evt: any) {

    if(await this.msgBox.showConfirmMessageDialog('Are you sure you want to upload the selected price list to the selected supplier?.', 'Uploading will cause significant updates in the supplier price list', 'question', 'Upload', 'Cancel')  === false){
      return
    }

    var data: [][]
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
      data = (XLSX.utils.sheet_to_json(ws, { header: 1 }))

      this.progress = true
      if (this.validatePriceListFile(data) == true) {
        this.progressStatus = 'Uploading... please wait'
        this.uploadPriceList(data)
      } else {
        alert('Invalid price list file')
      }
      this.progress = false

    }
    reader.readAsBinaryString(target.files[0])
    this.inputVariable!.nativeElement.value = ''
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

  validatePriceListFile(data : any [][]) : boolean{
    this.clearProgress()
    var rows            = data.length
    var cols            = data[0].length
    this.progressStatus = 'Validating file'
    this.totalRecords   = rows
    var valid           = true

    //validate row header
    if( data[0][0] != 'ITEM_CODE'                     ||
        data[0][1] != 'ITEM_NAME'                  ||
        data[0][2] != 'PRICE'        ||
        data[0][3] != 'TERMS'    
        )
    {
      valid = false
    }
    for(let i = 1; i < data.length; i++) {
      this.currentRecord = i
      //checks for empty code and name
      if( data[i][0] == '' ||
          data[i][1] == '' ||
          data[i][2] == ''
        )
      {
        alert('Error in record no '+ i)
        return false
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

  async uploadPriceList(dt : any [][]){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.clearProgress()
    var rows = dt.length
    var cols = dt[0].length
    this.progressStatus = 'Uploading price list file'
    this.totalRecords = rows

    for(let i = 1; i < dt.length; i++) {
      this.currentRecord = i

      var supplierItemPrice = {
        item: {code : dt[i][0]},
        price : dt[i][2],
        terms : dt[i][3],
        supplier: {id : this.supplier.id}
        
      }


      if(dt[i][0] == undefined){
        alert('End of file reached')
        return
      }
        this.spinner.show()
        await this.http.post<ISupllierItemPriceList>(API_URL+'/supplier_item_prices/save_or_update', supplierItemPrice, options)
        .pipe(finalize(() => this.spinner.hide()))
        .toPromise()
        .then(
          data => {
           
          }
        )
        .catch(
          error => {
            console.log(error)
          }
        )
      }
    this.clearProgress()
    this.getSupplierItems(this.supplier.id)
  }
  
}

