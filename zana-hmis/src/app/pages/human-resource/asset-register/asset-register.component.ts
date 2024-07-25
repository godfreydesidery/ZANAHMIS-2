import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import * as pdfMake from 'pdfmake/build/pdfmake';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IAsset } from 'src/app/domain/asset';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { DataService } from 'src/app/services/data.service';
import { MsgBoxService } from 'src/app/services/msg-box.service';


import { environment } from 'src/environments/environment';

var pdfFonts = require('pdfmake/build/vfs_fonts.js'); 

const API_URL = environment.apiUrl;


@Component({
  selector: 'app-asset-register',
  templateUrl: './asset-register.component.html',
  styleUrls: ['./asset-register.component.scss'],
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
})
export class AssetRegisterComponent {

  id : any
  code : string = ''
  name : string = ''
  middleName : string = ''
  qty : number = 0
  price : number = 0
  multiple : boolean = false
  

  assets : IAsset[] = []


  filterRecords : string = ''


  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private router : Router,
    private msgBox : MsgBoxService,
    private data : DataService) 
    {(window as any).pdfMake.vfs = pdfFonts.pdfMake.vfs;}

  ngOnInit(): void {
    this.loadAssets()
  }

  public async saveAsset(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    var asset = {
      id          : this.id,
      code          : this.code,
      name   : this.name,
      qty  : this.qty,
      price : this.price,
      multiple    : this.multiple
    }
    if(this.id == null || this.id == ''){
      //save a new clinic
      this.spinner.show()
      await this.http.post<IAsset>(API_URL+'/assets/save', asset, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id = data?.id
          this.code = data!.code
          this.name = data!.name
          this.qty = data!.qty
          this.price = data!.price
          this.multiple = data!.multiple
         
          this.msgBox.showSuccessMessage('Asset created successifully')
          this.loadAssets()
          this.clear()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, '')
        }
      )

    }else{
      //update an existing clinic
      this.spinner.show()
      await this.http.post<IAsset>(API_URL+'/assets/save', asset, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id = data?.id
          this.code = data!.code
          this.name = data!.name
          this.qty = data!.qty
          this.price = data!.price
          this.multiple = data!.multiple
          this.msgBox.showSuccessMessage('Asset updated successifully')
          this.loadAssets()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, '')
        }
      )
    }
  }

  async loadAssets(){
    this.assets = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IAsset[]>(API_URL+'/assets', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        var sn = 1
        data?.forEach(element => {
          element.sn = sn
          this.assets.push(element)
          sn = sn + 1
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load assets')
      }
    )
  }

  clear(){
    this.id = null
    this.code = ''
    this.name = ''
    this.qty = 0
    this.price = 0
    this.multiple = false
    
  }

  async getAsset(key: string) {
    if(key == ''){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IAsset>(API_URL+'/assets/get?id='+key, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        console.log(data)
        this.id = data?.id
        this.id = data?.id
        this.code = data!.code
        this.name = data!.name
        this.qty = data!.qty
        this.price = data!.price
        this.multiple = data!.multiple

        
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, 'Could not find Doctor')
      }
    )
  }

  async deleteAsset(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var asset = {
      id : id
    }

    if(!(await this.msgBox.showConfirmMessageDialog('Remove asset from register?', '', 'question', 'Yes, Remove', 'No, Do not remove'))){
      return
    }
    
    this.spinner.show()
    await this.http.post(API_URL+'/assets/delete', asset, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.loadAssets()
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


  logo!    : any
  documentHeader! : any
  async print(){
    
    this.documentHeader = await this.data.getDocumentHeader()
    var header = ''
    var footer = ''
    var title  = 'Asset List'
    var logo : any = ''

    


    var report = [
      [
        {text : 'SN', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Code', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Asset Name', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Qty', fontSize : 9, fillColor : '#bdc6c7'},
        {text : 'Unit Price', fontSize : 9, fillColor : '#bdc6c7'}
      ]
    ]  

    this.assets.forEach((element) => {
      var detail = [
        {text : element?.sn.toString(), fontSize : 7, fillColor : '#ffffff'},
        {text : element?.code, fontSize : 7, fillColor : '#ffffff'},
        {text : element?.name, fontSize : 7, fillColor : '#ffffff'},
        {text : element?.qty.toString(), fontSize : 7, fillColor : '#ffffff'},
        {text : element?.price.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 }), fontSize : 7, alignment : 'right', fillColor : '#ffffff'}
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
          
          '  ',
          {
            //layout : 'noBorders',
            table : {
                headerRows : 1,
                widths : [30, 80, 200, 50, 80],
                body : report
            }
        },
       
         
      ]     
    };
    pdfMake.createPdf(docDefinition).print()
    
  }


}
