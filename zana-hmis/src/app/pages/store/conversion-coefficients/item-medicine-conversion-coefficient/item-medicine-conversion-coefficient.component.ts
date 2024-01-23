import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IClinic } from 'src/app/domain/clinic';
import { IClinician } from 'src/app/domain/clinician';
import { IItem } from 'src/app/domain/item';
import { IItemMedicineCoefficient } from 'src/app/domain/item-medicine-coefficient';
import { IMedicine } from 'src/app/domain/medicine';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-item-medicine-conversion-coefficient',
  templateUrl: './item-medicine-conversion-coefficient.component.html',
  styleUrls: ['./item-medicine-conversion-coefficient.component.scss'],
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
export class ItemMedicineConversionCoefficientComponent {

  id : any

  itemNames : string[] = []
  medicineNames : string[] = []

  itemId : any
  itemCode : string = ''
  itemBarcode : string = ''
  itemName : string = ''
  itemQty : number = 0

  medicineId : any
  medicineCode : string = ''
  medicineBarcode : string = ''
  medicineName : string = ''
  medicineQty : number =  0

  itemMedicineCoefficients : IItemMedicineCoefficient[] = []

  filterRecords : string = ''


  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
  ) {}

  ngOnInit(): void {
    this.loadItemNames()
    this.loadMedicineNames()
    this.loadCoefficients()
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
        this.itemNames = []
        data?.forEach(element => {
          this.itemNames.push(element)
        })
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, 'Could not load item names')
      }
    )
  }

  async loadMedicineNames(){
    this.medicineNames = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<string[]>(API_URL+'/medicines/get_names', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        data?.forEach(element => {
          this.medicineNames.push(element)
        })
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, 'Could not load medicine names')
      }
    )
  }

  async searchItem(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var code = this.itemCode
    var barcode = this.itemBarcode
    var name = this.itemName
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
        this.itemId                   = data?.id
        this.itemCode                 = data!.code
        this.itemBarcode              = data!.barcode
        this.itemName                 = data!.name
        
      },
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async searchMedicine() {
    
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IMedicine>(API_URL+'/medicines/get_by_name?name='+this.medicineName, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        this.medicineId           = data?.id
        this.medicineCode         = data!.code
        this.medicineName         = data!.name
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, 'Could not find Medicine')
      }
    )
  }

  async saveCoefficient(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var coef = {
      id          : this.id,
      coefficient : 0,
      item        : { name : this.itemName},
      medicine    : { name : this.medicineName},
      itemQty     : this.itemQty,
      medicineQty : this.medicineQty
    }

    this.spinner.show()
    await this.http.post<IItemMedicineCoefficient>(API_URL+'/item_medicine_coefficients/save', coef, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        this.id = data?.id

        this.itemId = data!.item!.id
        this.itemCode = data!.item!.code
        this.itemName = data!.item!.name
        this.itemQty = data!.itemQty

        this.medicineId           = data!.medicine!.id
        this.medicineCode         = data!.medicine!.code
        this.medicineName         = data!.medicine!.name
        this.medicineQty         = data!.medicineQty

        this.loadCoefficients()
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  clear(){
    this.id           = null
    this.itemId       = null
    this.itemCode     = ''
    this.itemName     = ''
    this.itemQty      = 0
    this.medicineId   = null
    this.medicineCode = ''
    this.medicineName = ''
    this.medicineQty  = 0
  }

  async loadCoefficients(){
    this.itemMedicineCoefficients = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IItemMedicineCoefficient[]>(API_URL+'/item_medicine_coefficients', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(element => {
          this.itemMedicineCoefficients.push(element)
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async getCoefficient(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IItemMedicineCoefficient>(API_URL+'/item_medicine_coefficients/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.id = data?.id

        this.itemId = data!.item!.id
        this.itemCode = data!.item!.code
        this.itemName = data!.item!.name
        this.itemQty = data!.itemQty

        this.medicineId           = data!.medicine!.id
        this.medicineCode         = data!.medicine!.code
        this.medicineName         = data!.medicine!.name
        this.medicineQty         = data!.medicineQty
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
