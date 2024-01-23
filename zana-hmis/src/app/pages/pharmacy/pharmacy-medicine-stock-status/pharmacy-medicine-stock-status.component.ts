import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IPharmacyMedicine } from 'src/app/domain/pharmacy-medicine';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { DataService } from 'src/app/services/data.service';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';
var pdfFonts = require('pdfmake/build/vfs_fonts.js'); 
import * as pdfMake from 'pdfmake/build/pdfmake';
import { IPharmacyMedicineBatch } from 'src/app/domain/pharmacy-medicine-batch';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-pharmacy-medicine-stock-status',
  templateUrl: './pharmacy-medicine-stock-status.component.html',
  styleUrls: ['./pharmacy-medicine-stock-status.component.scss'],
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
export class PharmacyMedicineStockStatusComponent {

  id : any = null
  pharmacyMedicineCode : string = ''
  pharmacyMedicineName : string = ''
  pharmacyMedicineStock : number = 0



  pharmacyName = localStorage.getItem('selected-pharmacy-name')

  pharmacyMedicines : IPharmacyMedicine[] = []
  pharmacyMedicinesToShow : IPharmacyMedicine[] = []

  filterRecords : string = ''

  constructor(private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private router : Router,
    private msgBox : MsgBoxService,
    private data : DataService,) {(window as any).pdfMake.vfs = pdfFonts.pdfMake.vfs;}

  async ngOnInit(): Promise<void> {
    await this.loadPharmacyMedicines()
  }

  async loadPharmacyMedicines(){    
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    //this.spinner.show()
    await this.http.get<IPharmacyMedicine[]>(API_URL+'/pharmacies/get_pharmacy_medicine_list?pharmacy_name='+this.pharmacyName, options)
    //.pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.pharmacyMedicines = []
        this.pharmacyMedicinesToShow = []
        data?.forEach(element => {
          this.pharmacyMedicines.push(element)
          this.pharmacyMedicinesToShow.push(element)
        })
        var sn = 1
        this.pharmacyMedicinesToShow.forEach(element => {
          element.sn = sn
          sn = sn + 1
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
    //this.pharmacyMedicinesToShow.forEach(async element => {
      //var batches : IPharmacyMedicineBatch[]  = await this.loadPharmacyMedicineBatches(element.pharmacy.id, element.medicine.id)

      //element.pharmacyMedicineBatches = batches
    //})
  }

  //pharmacyMedicineBatches : IPharmacyMedicineBatch[] = []

  async loadPharmacyMedicineBatches(pharmacyId : any, medicineId : any) : Promise<IPharmacyMedicineBatch[]>{ 
    var batches :  IPharmacyMedicineBatch[] = [] 
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    this.spinner.show()
    await this.http.get<IPharmacyMedicineBatch[]>(API_URL+'/pharmacies/get_pharmacy_medicine_batches?pharmacy_id='+pharmacyId+'&medicine_id='+medicineId, options)
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

  category : string = 'ALL'
  filterByCategory(value : string){
    this.pharmacyMedicinesToShow = []
    if(this.category === 'ALL'){
      this.pharmacyMedicinesToShow = this.pharmacyMedicines
    }else{
      this.pharmacyMedicines.forEach(element => {
        if(element.medicine.category === value){
          this.pharmacyMedicinesToShow.push(element)
        }
      })
    }
    
  }


  setValues(id : any, code : string, name : string, stock : number){
    this.id = id
    this.pharmacyMedicineCode = code
    this.pharmacyMedicineName = name
    this.pharmacyMedicineStock = stock
  }

  async updateStock(){    
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var pm = {
      id : this.id,
      stock : this.pharmacyMedicineStock
    }
    //this.spinner.show()
    await this.http.post(API_URL+'/pharmacies/update_stock', pm, options)
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
    await this.loadPharmacyMedicines()
    this.filterByCategory(this.category)
  }



  logo!    : any
  documentHeader! : any
  address  : any 
  print = async () => {

   
    if(this.pharmacyMedicinesToShow.length === 0){
      this.msgBox.showErrorMessage3('No data to print')
      return
    }

    this.documentHeader = await this.data.getDocumentHeader()
    var header = ''
    var footer = ''
    var title  = 'Pharmacy Stock Status Report'
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
    
    this.pharmacyMedicinesToShow.forEach((element) => {
      var detail = [
        {text : element?.sn.toString(), fontSize : 7, fillColor : '#ffffff'}, 
        {text : element?.medicine?.code, fontSize : 7, fillColor : '#ffffff'}, 
        {text : element?.medicine?.name, fontSize : 7, fillColor : '#ffffff'}, 
        {text : element?.medicine?.category, fontSize : 7, fillColor : '#ffffff'}, 
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
          'Pharmacy Name: ' + this.pharmacyName,
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
