import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { DataService } from 'src/app/services/data.service';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';
var pdfFonts = require('pdfmake/build/vfs_fonts.js'); 
import * as pdfMake from 'pdfmake/build/pdfmake';
import { IStoreItem } from 'src/app/domain/store-item';
import { IStoreItemBatch } from 'src/app/domain/store-item-batch';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-store-item-stock-status',
  templateUrl: './store-item-stock-status.component.html',
  styleUrls: ['./store-item-stock-status.component.scss'],
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
export class StoreItemStockStatusComponent {
  id : any = null
  storeItemCode : string = ''
  storeItemName : string = ''
  storeItemStock : number = 0



  storeId : any = localStorage.getItem('selected-store-id')
  storeCode : string = localStorage.getItem('selected-store-code')!
  storeName : string = localStorage.getItem('selected-store-name')!

  storeItems : IStoreItem[] = []
  storeItemsToShow : IStoreItem[] = []

  filterRecords : string = ''

  constructor(private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private router : Router,
    private msgBox : MsgBoxService,
    private data : DataService,) {(window as any).pdfMake.vfs = pdfFonts.pdfMake.vfs;}

  async ngOnInit(): Promise<void> {
    await this.loadStoreItems()
  }

  async loadStoreItems(){    
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    //this.spinner.show()
    await this.http.get<IStoreItem[]>(API_URL+'/stores/get_store_item_list?store_name='+this.storeName, options)
    //.pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.storeItems = []
        this.storeItemsToShow = []
        data?.forEach(element => {

          //this.loadStoreItemBatches(element.store.id, element.item.id)


         
          //element.storeItemBatches = this.loadStoreItemBatches(element.store.id, element.item.id)


          this.storeItems.push(element)
          this.storeItemsToShow.push(element)
        })
        var sn = 1
        this.storeItemsToShow.forEach(element => {
          element.sn = sn
          sn = sn + 1
        })
        console.log(data)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )

    //this.storeItemsToShow.forEach(async element => {
      //var batches : IStoreItemBatch[]  = await this.loadStoreItemBatches(element.store.id, element.item.id)

      //element.storeItemBatches = batches
    //})

  }

  //storeItemBatches : IStoreItemBatch[] = []

  async loadStoreItemBatches(storeId : any, itemId : any) : Promise<IStoreItemBatch[]>{ 
    var batches :  IStoreItemBatch[] = [] 
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    this.spinner.show()
    await this.http.get<IStoreItemBatch[]>(API_URL+'/stores/get_store_item_batches?store_id='+storeId+'&item_id='+itemId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        batches = data!
        //data?.forEach(element => {
          //batches.push(element)
        //})
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
    return batches
  }

  async updateStoreItemRegister(){
    if(!window.confirm('Updating store item register might take time. Confirm?')){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var store = {
      id : this.storeId,
      code : this.storeCode,
      name : this.storeName
    }
    
    this.spinner.show()
    await this.http.post(API_URL+'/stores/update_store_item_register', store, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
    this.category = 'ALL'
    this.loadStoreItems()
  }

  category : string = 'ALL'
  filterByCategory(value : string){
    this.storeItemsToShow = []
    if(this.category === 'ALL'){
      this.storeItemsToShow = this.storeItems
    }else{
      this.storeItems.forEach(element => {
        if(element.item.category === value){
          this.storeItemsToShow.push(element)
        }
      })
    }
    
  }


  setValues(id : any, code : string, name : string, stock : number){
    this.id = id
    this.storeItemCode = code
    this.storeItemName = name
    this.storeItemStock = stock
  }

  async updateStock(){    
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var pm = {
      id : this.id,
      stock : this.storeItemStock
    }
    //this.spinner.show()
    await this.http.post(API_URL+'/stores/update_stock', pm, options)
    //.pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.msgBox.showSuccessMessage('Updated successifully')
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
    await this.loadStoreItems()
    this.filterByCategory(this.category)
  }



  logo!    : any
  documentHeader! : any
  address  : any 
  print = async () => {

   
    if(this.storeItemsToShow.length === 0){
      this.msgBox.showSimpleErrorMessage('No data to print')
      return
    }

    this.documentHeader = await this.data.getDocumentHeader()
    var header = ''
    var footer = ''
    var title  = 'Store Stock Status Report'
    var logo : any = ''
    var total : number = 0
    var discount : number = 0
    var tax : number = 0

    var report = [
      [
        {text : 'SN', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Code', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Name', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Category', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Qty', fontSize : 9, fillColor : '#bdc6c7'},
      ]
    ]  
    
    this.storeItemsToShow.forEach((element) => {
      var detail = [
        {text : element?.sn.toString(), fontSize : 7, fillColor : '#ffffff'}, 
        {text : element?.item?.code, fontSize : 7, fillColor : '#ffffff'}, 
        {text : element?.item?.name, fontSize : 7, fillColor : '#ffffff'}, 
        {text : element?.item?.category, fontSize : 7, fillColor : '#ffffff'}, 
        {text : element?.stock.toString(), fontSize : 7, fillColor : '#ffffff'}, 
      ]
      report.push(detail)
    })
   
    const docDefinition : any = {
      header: '',
      footer: function (currentPage: { toString: () => string; }, pageCount: string) {
        return currentPage.toString() + " of " + pageCount;
      },
      //watermark : { text : '', color: 'blue', opacity: 0.1, bold: true, italics: false },
        content : [
          {
            columns : 
            [
              this.documentHeader
            ]
          },
          '  ',
          {text : title, fontSize : 14, bold : true, alignment : 'center'},
          this.data.getHorizontalLine(),
          ' ',
          'Store Name: ' + this.storeName,
          ' ',
          'Category: ' + this.category,
          '  ',
          {
            //layout : 'noBorders',
            table : {
                headerRows : 1,
                widths : [40, 80, 200, 80, 70],
                body : report
            }
        }, 
      ]     
    };
    pdfMake.createPdf(docDefinition).print()
  }

}
