import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IConsultation } from 'src/app/domain/consultation';
import { IMedicine } from 'src/app/domain/medicine';
import { IPatient } from 'src/app/domain/patient';
import { IPatientBill } from 'src/app/domain/patient-bill';
import { IPharmacyCustomer } from 'src/app/domain/pharmacy-customer';
import { IPharmacySaleOrder } from 'src/app/domain/pharmacy-sale-order';
import { IPharmacySaleOrderDetail } from 'src/app/domain/pharmacy-sale-order-detail';
import { ReceiptItem } from 'src/app/domain/receipt-item';
import { ISingleObject } from 'src/app/domain/single-object';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { noUndefined } from 'src/custom-packages/util';
import { environment } from 'src/environments/environment';
import { IBill } from '../../payments/lab-test-payment/lab-test-payment.component';
import { PharmacyPosReceiptService } from 'src/app/services/pharmacy-pos-receipt.service';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-pharmacy-sales-order',
  templateUrl: './pharmacy-sales-order.component.html',
  styleUrls: ['./pharmacy-sales-order.component.scss'],
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
export class PharmacySalesOrderComponent {

  id : any = null

  no : string = ''

  pharmacySaleOrder! : IPharmacySaleOrder

  pharmacySaleOrders : IPharmacySaleOrder[] = []

  status : string = ''

  created : string = ''
  approved : string = ''
  canceled : string = ''

  pharmacistId : any


  pharmacyName = localStorage.getItem('selected-pharmacy-name')
  pharmacyId = localStorage.getItem('selected-pharmacy-id')

  filterRecords : string = ''


  pharmacySaleBills : IPatientBill[] = []

  amountReceived : number = 0

  constructor(private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private router : Router,
    private msgBox : MsgBoxService,
    private printer : PharmacyPosReceiptService) { }


    pharmacySaleOrderDetailUnit        : number = 0
    pharmacySaleOrderDetailDosage      : string = ''
    pharmacySaleOrderDetailFrequency   : string = ''
    pharmacySaleOrderDetailRoute       : string = ''
    pharmacySaleOrderDetailDays        : string = ''
    pharmacySaleOrderDetailPrice       : number = 0
    pharmacySaleOrderDetailQty         : number = 0
    pharmacySaleOrderDetailInstructions : string = '' 
    
    
    pharmacySaleOrderDetails : IPharmacySaleOrderDetail[] = []


    pharmacyCustomer! : IPharmacyCustomer
    pharmacyCustomerMode : string = 'New Customer'



  async ngOnInit(): Promise<void> {
    this.autoArchivePharmacySaleOrders()
    await this.loadPharmacist()
  }

  async loadPharmacist(){    
    var username = localStorage.getItem('username')!
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<any>(API_URL+'/pharmacists/load_pharmacist_by_username?username='+username, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.pharmacistId = data
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load pharmacist')
      }
    )
  }


  pharmacyCustomerId : any =  null
  pharmacyCustomerNo : string = ''
  pharmacyCustomerName : string = ''
  pharmacyCustomerPhone : string = ''
  pharmacyCustomerAddress : string = ''
  pharmacyCustomers : IPharmacyCustomer[] = []
  async loadPharmacyCustomerLike(value : string){
    this.medicines = []
    if(value.length < 2){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.get<IPharmacyCustomer[]>(API_URL+'/medicines/load_pharmacy_customers_like?name_like='+value, options)
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.pharmacyCustomers = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }



  medicineId : any =  null
  medicineCode : string = ''
  medicineName : string = ''
  medicines : IMedicine[] = []
  async loadMedicinesLike(value : string){
    this.medicines = []
    if(value.length < 2){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.get<IMedicine[]>(API_URL+'/medicines/load_medicines_like?name_like='+value, options)
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.medicines = data!
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async getMedicine(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.medicines = []
    this.spinner.show()
    await this.http.get<IMedicine>(API_URL+'/medicines/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      async (data) => {
        this.medicineId = data?.id
        this.medicineCode = data!.code
        this.medicineName = data!.name

        await this.getMedicineUnit(this.medicineId)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }

  async getMedicineUnit(id : any){
    this.pharmacySaleOrderDetailUnit = 0
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    var medicine = {
      id : id
    }
    this.spinner.show()
    await this.http.post<number>(API_URL+'/medicines/get_available_units', medicine, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.pharmacySaleOrderDetailUnit = data!
        return data
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, 'Could not get units')
        return 0
      }
    )
  }

  async deletePharmacySaleOrderDetail(pharmacySaleDetailId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<boolean>(API_URL+'/patients/delete_pharmacy_sale_order_detail?id='+pharmacySaleDetailId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.getPharmacySaleOrder(this.id)
        
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
    this.loadPharmacySaleOrderDetails(this.id)
  }

  async loadPharmacySaleOrderDetails(pharmacySaleOrderId : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.pharmacySaleOrderDetails = []
    this.spinner.show()
    await this.http.get<IPharmacySaleOrderDetail[]>(API_URL+'/pharmacies/load_pharmacy_sale_order_details?id='+pharmacySaleOrderId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.pharmacySaleOrderDetails = data!

        this.calculateTotal()
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, 'Could not load sales')
        this.calculateTotal()
      }
    )
    
  }

  async savePharmacySaleOrderDetail(medicineId : string){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var pharmacySaleOrderDetail = {
      medicine : {
        id : this.medicineId,
        code : this.medicineCode,
        name : this.medicineName
      },
      pharmacySaleOrder : {
        id : this.id
      },
      dosage    : this.pharmacySaleOrderDetailDosage,
      frequency : this.pharmacySaleOrderDetailFrequency,
      route     : this.pharmacySaleOrderDetailRoute,
      days      : this.pharmacySaleOrderDetailDays,
      price     : this.pharmacySaleOrderDetailPrice,
      qty       : this.pharmacySaleOrderDetailQty,
      instructions : this.pharmacySaleOrderDetailInstructions
    }

    if( pharmacySaleOrderDetail.medicine.name === '' || 
        pharmacySaleOrderDetail.dosage === '' || 
        pharmacySaleOrderDetail.frequency === '' || 
        pharmacySaleOrderDetail.route === '' || 
        pharmacySaleOrderDetail.days === ''){
      this.msgBox.showErrorMessage3('Can not save, please fill in all the required fields')
      return
    }

    this.spinner.show()
    await this.http.post<IPharmacySaleOrderDetail>(API_URL+'/pharmacies/save_pharmacy_sale_order_detail', pharmacySaleOrderDetail, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {
        this.loadPharmacySaleOrderDetails(this.id)
        this.clearPharmacySaleOrderDetail()
        this.msgBox.showSuccessMessage('Sale Saved successifully')
      }
    )
    .catch(
      error => {
        this.loadPharmacySaleOrderDetails(this.id)
        this.clearPharmacySaleOrderDetail()
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )

  }

  async savePharmacySaleOrder(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var order = {
      id: this.id,
      pharmacyCustomer: {
        id: this.pharmacyCustomerId,
        no: this.pharmacyCustomerNo,
        name: this.pharmacyCustomerName,
        phoneNo: this.pharmacyCustomerPhone,
        address: this.pharmacyCustomerAddress
      },
      status: '',
      paymentType: 'CASH',
      pharmacist: {id : this.pharmacistId},
      pharmacy: {name : this.pharmacyName},
    }

    this.spinner.show()
    await this.http.post<IPharmacySaleOrder>(API_URL+'/pharmacies/save_pharmacy_sale_order', order, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      (data) => {


        this.clearPharmacySaleOrderDetail()
        this.msgBox.showSuccessMessage('Sale Saved successifully')
        
        this.getPharmacySaleOrder(data!.id)

        /*this.pharmacySaleOrder = data!

        this.id = data?.id
        this.no = data!.no
        this.status = data!.status
        this.created = data!.created

        this.pharmacyCustomerId = data!.pharmacyCustomer!.id
        this.pharmacyCustomerNo = data!.pharmacyCustomer!.no
        this.pharmacyCustomerName = data!.pharmacyCustomer!.name
        this.pharmacyCustomerPhone = data!.pharmacyCustomer!.phoneNo

        this.pharmacySaleOrderDetails = data!.pharmacySaleOrderDetails*/


        //this.loadPharmacySaleOrderDetails(data!.id)
        
        
      }
    )
    .catch(
      error => {
        this.loadPharmacySaleOrderDetails(this.id)
        this.clearPharmacySaleOrderDetail()
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )


  }



  clearPharmacyOrder(){
    this.clearPharmacyCustomer()
    this.id = null
    this.no = ''
    this.status = ''
    this.created = ''
    this.approved = ''
    this.canceled = ''
    this.clearPharmacySaleOrderDetail()
  }




  

  clearPharmacySaleOrderDetail(){
    this.medicineId = null
    this.medicineCode = ''
    this.medicineName = ''

    this.pharmacySaleOrderDetailUnit         = 0
    this.pharmacySaleOrderDetailDosage       = ''
    this.pharmacySaleOrderDetailFrequency    = ''
    this.pharmacySaleOrderDetailRoute        = ''
    this.pharmacySaleOrderDetailDays         = ''
    this.pharmacySaleOrderDetailPrice        = 0
    this.pharmacySaleOrderDetailQty          = 0
    this.pharmacySaleOrderDetailInstructions = ''

  }

  clearPharmacyCustomer(){

    /**Clear pharmacy Order first */
    this.id = null
    this.no = ''
    this.status = ''

    /**Then clear pharmacy customer */
    this.pharmacyCustomerMode = 'New Customer'
    this.pharmacyCustomerId = null
    this.pharmacyCustomerNo = ''
    this.pharmacyCustomerName = ''
    this.pharmacyCustomerPhone = ''
    this.pharmacyCustomerAddress = ''
    this.pharmacyCustomer!    
  }

  searchingPharmacyCustomer(){
    this.pharmacyCustomerMode = 'Existing Customer'
  }

  async getPharmacyCustomer(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.pharmacyCustomers = []
    this.spinner.show()
    await this.http.get<IPharmacyCustomer>(API_URL+'/pharmacies/pharmacy_customers/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      async (data) => {
        this.pharmacyCustomerId = data?.id
        this.pharmacyCustomerNo = data!.no
        this.pharmacyCustomerName = data!.name
        this.pharmacyCustomerPhone = data!.phoneNo
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }


  async getPharmacySaleOrder(id : any){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.pharmacyCustomers = []
    this.spinner.show()
    await this.http.get<IPharmacySaleOrder>(API_URL+'/pharmacies/pharmacy_sale_orders/get?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      async (data) => {
        this.id = data?.id
        this.no = data!.no
        this.status = data!.status
        this.pharmacyCustomerId = data!.pharmacyCustomer!.id
        this.pharmacyCustomerNo = data!.pharmacyCustomer!.no
        this.pharmacyCustomerName = data!.pharmacyCustomer!.name
        this.pharmacyCustomerPhone = data!.pharmacyCustomer!.phoneNo
        this.pharmacyCustomerMode = 'Existing Customer'

        this.pharmacySaleOrderDetails = data!.pharmacySaleOrderDetails

        this.created = data!.created
        this.approved = data!.approved
        this.canceled = data!.canceled

        this.pharmacySaleOrder = data!
        
        this.calculateTotal()
      }
      
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }

  async cancelPharmacySaleOrder(id : any){

    if(!(await this.msgBox.showConfirmMessageDialog('Are you sure you want to cancel this order?', 'Order will be canceled and rendered unusable.', 'question', 'Yes, Cancel', 'No, Do not cancel'))){
      return
    }

    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.pharmacyCustomers = []
    this.spinner.show()
    await this.http.get<IPharmacySaleOrder>(API_URL+'/pharmacies/pharmacy_sale_orders/cancel?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      async (data) => {
        this.id = data?.id
        this.no = data!.no
        this.status = data!.status
        this.pharmacyCustomerId = data!.pharmacyCustomer!.id
        this.pharmacyCustomerNo = data!.pharmacyCustomer!.no
        this.pharmacyCustomerName = data!.pharmacyCustomer!.name
        this.pharmacyCustomerPhone = data!.pharmacyCustomer!.phoneNo
        this.pharmacyCustomerMode = 'Existing Customer'

        this.pharmacySaleOrderDetails = data!.pharmacySaleOrderDetails

        this.created = data!.created
        this.approved = data!.approved
        this.canceled = data!.canceled
        console.log(data)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }


  async archivePharmacySaleOrder(id : any){

    if(!(await this.msgBox.showConfirmMessageDialog('Are you sure you want to archive this order?', 'Order will be hidden.', 'question', 'Yes, Archive', 'No, Do not archive'))){
      return
    }

    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.pharmacyCustomers = []
    this.spinner.show()
    await this.http.get<IPharmacySaleOrder>(API_URL+'/pharmacies/pharmacy_sale_orders/archive?id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      async (data) => {
        this.id = data?.id
        this.no = data!.no
        this.status = data!.status
        this.pharmacyCustomerId = data!.pharmacyCustomer!.id
        this.pharmacyCustomerNo = data!.pharmacyCustomer!.no
        this.pharmacyCustomerName = data!.pharmacyCustomer!.name
        this.pharmacyCustomerPhone = data!.pharmacyCustomer!.phoneNo
        this.pharmacyCustomerMode = 'Existing Customer'

        this.pharmacySaleOrderDetails = data!.pharmacySaleOrderDetails

        this.created = data!.created
        this.approved = data!.approved
        this.canceled = data!.canceled
        console.log(data)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }


  autoArchivePharmacySaleOrders(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    this.http.get<null>(API_URL+'/pharmacies/pharmacy_sale_orders/archive_all', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      () => {}       
    )
    .catch(
      error => {
        //this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }


  async loadPharmacySaleOrders(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.pharmacySaleOrders = []
    this.spinner.show()
    await this.http.get<IPharmacySaleOrder[]>(API_URL+'/pharmacies/pharmacy_sale_orders', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.pharmacySaleOrders = data!
      }
    )
    .catch(
      (error) => {
        this.msgBox.showErrorMessage(error, 'Could not load sales')
      }
    )
    
  }


  







  total : number = 0
  async loadPharmacySalesBills(){
    this.pharmacySaleBills = []
    this.total = 0
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    this.spinner.show()
    await this.http.get<IPatientBill[]>(API_URL+'/bills/get_pharmacy_sale_order_bills?order_id='+this.id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.pharmacySaleBills = data! 
        this.pharmacySaleBills.forEach(element => {
          this.total = this.total + element.amount
        })      
      }
    )
    .catch(
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, 'Could not load pharmacy sales bills')
      }
    )
  }

  

  billsToPrint : IBill[] = []
  async confirmBillsPayment(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    var bills : IPatientBill[] = []
    
    /**
     * Add medicine bills
     */

    var size = 0
    this.pharmacySaleOrderDetails.forEach(element => {
      size = 1
      bills.push(element.patientBill)
    })

    if(size == 0){
      alert('Invalid operation')
      return
    }


    this.billsToPrint = bills
    console.log(bills)

    this.spinner.show()
    await this.http.post<IPatientBill>(API_URL+'/bills/confirm_bills_payment?total_amount='+this.total, bills, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        console.log(data)
        this.msgBox.showSuccessMessage('Payment successiful')
        
        this.printReceipt()

        this.getPharmacySaleOrder(this.id)
        
      }
    )
    .catch(
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, 'Could not confirm payment')
      }
    )
  }

  printReceipt(){
    var items : ReceiptItem[] = []
    var item : ReceiptItem

    this.billsToPrint.forEach(element => {
      item = new ReceiptItem()
      item.code = element.id
      item.name = element.description
      item.amount = element.amount
      item.qty = element.qty
      items.push(item)
    })



    

    this.printer.print(items, 'Order#: '+this.pharmacySaleOrder!.no, 0, this.pharmacySaleOrder.pharmacyCustomer)

  }




calculateTotal(){
  this.total = 0
  this.pharmacySaleOrderDetails.forEach(element => {
    this.total = this.total + element.patientBill.amount
  })
}






async giveMedicine(){
  let options = {
    headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
  }

  if(!(await this.msgBox.showConfirmMessageDialog('Are you sure you want to give medicine?', 'Medicine will be deducted from stock.', 'question', 'Yes, Give', 'No, Do not give'))){
    return
  }

  this.spinner.show()
  await this.http.get<boolean>(API_URL+'/patients/give_medicine?order_id='+this.id + '&pharmacy_id='+this.pharmacyId, options)
  .pipe(finalize(() => this.spinner.hide()))
  .toPromise()
  .then(
    data => {
      this.msgBox.showSuccessMessage('Medicine given successifully')
      this.clearPharmacyOrder()
    }
  )
  .catch(
    error => {
      console.log(error)
      this.msgBox.showErrorMessage(error, '')
    }
  )
}


}
