import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IClinic } from 'src/app/domain/clinic';
import { IEmployee } from 'src/app/domain/employee-register';
import { INurse } from 'src/app/domain/nurse';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';

import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-employee-register',
  templateUrl: './employee-register.component.html',
  styleUrls: ['./employee-register.component.scss'],
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
})
export class EmployeeRegisterComponent {
  
  id : any
  no : string = ''
  firstName : string = ''
  middleName : string = ''
  lastName : string = ''
  nationalIdNo : string = ''
  phoneNo : string = ''
  email : string = ''
  gender : string = ''
  dateOfBirth! : Date
  active : boolean = true
  payable : boolean = true
  physicalAddress : string = ''
  postalAddress : string = ''
  permanentResidence : string = ''
  temporaryResidence : string = ''
  kinName : string = ''
  kinPhoneNo : string = ''
  tinNo : string = ''
  basicSalary : number = 0
  joiningDate : Date | string = ''
  employmentDate : Date | string = ''
  terminationDate : Date | string = ''
  bankAccountNo : string = ''
  bankAccountName : string = ''
  bankName : string = ''
  socialSecurityNo : string = ''
  socialSecurityName : string = ''

  employees : IEmployee[] = []


  filterRecords : string = ''


  constructor(
    private auth : AuthService,
    private http :HttpClient,
    private spinner : NgxSpinnerService,
    private msgBox : MsgBoxService
  ) {
    
  }

  ngOnInit(): void {
    this.loadEmployees()
  }

  public async saveEmployee(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    var employee = {
      id          : this.id,
      no          : this.no,
      firstName   : this.firstName,
      middleName  : this.middleName,
      lastName    : this.lastName,
      nationalIdNo : this.nationalIdNo,
      phoneNo : this.phoneNo,
      email : this.email,
      gender : this.gender,
      dateOfBirth : this.dateOfBirth,
      active : this.active,
      payable : this.payable,
      physicalAddress : this.physicalAddress,
      postalAddress : this.postalAddress,
      permanentResidence : this.permanentResidence,
      temporaryResidence : this.temporaryResidence,
      kinName : this.kinName,
      kinPhoneNo : this.kinPhoneNo,
      tinNo : this.tinNo,
      basicSalary : this.basicSalary,
      joiningDate : this.joiningDate,
      employmentDate : this.employmentDate,
      terminationDate : this.terminationDate,
      bankAccountNo : this.bankAccountName,
      bankAccountName : this.bankAccountName,
      bankName : this.bankName,
      socialSecurityNo : this.socialSecurityNo,
      socialSecurityName : this.socialSecurityName
    }
    if(this.id == null || this.id == ''){
      //save a new clinic
      this.spinner.show()
      await this.http.post<IEmployee>(API_URL+'/employees/save', employee, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id = data?.id
          this.no = data!.no
          this.firstName = data!.firstName
          this.middleName = data!.middleName
          this.lastName = data!.lastName
          this.nationalIdNo = data!.nationalIdNo
          this.phoneNo = data!.phoneNo
          this.email = data!.email
          this.gender = data!.gender
          this.dateOfBirth = data!.dateOfBirth
          this.active = data!.active
          this.payable = data!.payable
          this.physicalAddress = data!.physicalAddress
          this.postalAddress = data!.postalAddress
          this.permanentResidence = data!.permanentResidence
          this.temporaryResidence = data!.temporaryResidence
          this.kinName = data!.kinName
          this.kinPhoneNo = data!.kinPhoneNo
          this.tinNo = data!.tinNo
          this.basicSalary = data!.basicSalary
          this.joiningDate = data!.joiningDate
          this.employmentDate = data!.employmentDate
          this.terminationDate = data!.terminationDate
          this.bankAccountNo = data!.bankAccountNo
          this.bankAccountName = data!.bankAccountName
          this.bankName = data!.bankName
          this.socialSecurityNo = data!.socialSecurityNo
          this.socialSecurityName = data!.socialSecurityName

          this.msgBox.showSuccessMessage('Employee created successifully')
          this.loadEmployees()
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
      await this.http.post<IEmployee>(API_URL+'/employees/save', employee, options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.id = data?.id
          this.no = data!.no
          this.firstName = data!.firstName
          this.middleName = data!.middleName
          this.lastName = data!.lastName
          this.nationalIdNo = data!.nationalIdNo
          this.phoneNo = data!.phoneNo
          this.email = data!.email
          this.gender = data!.gender
          this.dateOfBirth = data!.dateOfBirth
          this.active = data!.active
          this.payable = data!.payable
          this.physicalAddress = data!.physicalAddress
          this.postalAddress = data!.postalAddress
          this.permanentResidence = data!.permanentResidence
          this.temporaryResidence = data!.temporaryResidence
          this.kinName = data!.kinName
          this.kinPhoneNo = data!.kinPhoneNo
          this.tinNo = data!.tinNo
          this.basicSalary = data!.basicSalary
          this.joiningDate = data!.joiningDate
          this.employmentDate = data!.employmentDate
          this.terminationDate = data!.terminationDate
          this.bankAccountNo = data!.bankAccountNo
          this.bankAccountName = data!.bankAccountName
          this.bankName = data!.bankName
          this.socialSecurityNo = data!.socialSecurityNo
          this.socialSecurityName = data!.socialSecurityName
          this.msgBox.showSuccessMessage('Employee updated successifully')
          this.loadEmployees()
        }
      )
      .catch(
        error => {
          this.msgBox.showErrorMessage(error, '')
        }
      )
    }
  }


  public async changeStatus(employee : IEmployee){
    

    var msg = ''
    if(employee.active === true){
      msg = 'Deactivate Employee: ' + employee.firstName + ' ' + employee.middleName + ' ' + employee.lastName + '?'
    }else{
      msg = 'Activate Employee: ' + employee.firstName + ' ' + employee.middleName + ' ' + employee.lastName + '?'
    }

    if(!(await this.msgBox.showConfirmMessageDialog(msg, '', 'question', 'Yes Do', 'No, Do not'))){
      return
    }

    var employeeId : any = employee.id


    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    this.spinner.show()
    await this.http.get<Boolean>(API_URL+'/employees/change_status?id='+ employeeId, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.loadEmployees()
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
      }
    )
  }

  async loadEmployees(){
    this.employees = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IEmployee[]>(API_URL+'/employees', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        var sn = 1
        data?.forEach(element => {
          element.sn = sn
          this.employees.push(element)
          sn = sn + 1
        })
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, 'Could not load employees')
      }
    )
  }

  clear(){
    this.id = null
    this.no = ''
    this.firstName = ''
    this.middleName = ''
    this.lastName = ''
    this.nationalIdNo = ''
    this.phoneNo = ''
    this.email = ''
    this.gender = ''
    this.dateOfBirth!
    this.active = false
    this.payable = false
    this.physicalAddress = ''
    this.postalAddress = ''
    this.permanentResidence = ''
    this.temporaryResidence = ''
    this.kinName = ''
    this.kinPhoneNo = ''
    this.tinNo = ''
    this.basicSalary = 0
    this.joiningDate!
    this.employmentDate!
    this.terminationDate!
    this.bankAccountNo = ''
    this.bankAccountName = ''
    this.bankName = ''
    this.socialSecurityNo = ''
    this.socialSecurityName = ''
  }

  async getEmployee(key: string) {
    if(key == ''){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IEmployee>(API_URL+'/employees/get?id='+key, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        console.log(data)
        this.id = data?.id
          this.no = data!.no
          this.firstName = data!.firstName
          this.middleName = data!.middleName
          this.lastName = data!.lastName
          this.nationalIdNo = data!.nationalIdNo
          this.phoneNo = data!.phoneNo
          this.email = data!.email
          this.gender = data!.gender
          this.dateOfBirth = data!.dateOfBirth
          this.active = data!.active
          this.payable = data!.payable
          this.physicalAddress = data!.physicalAddress
          this.postalAddress = data!.postalAddress
          this.permanentResidence = data!.permanentResidence
          this.temporaryResidence = data!.temporaryResidence
          this.kinName = data!.kinName
          this.kinPhoneNo = data!.kinPhoneNo
          this.tinNo = data!.tinNo
          this.basicSalary = data!.basicSalary
          this.joiningDate = data!.joiningDate
          this.employmentDate = data!.employmentDate
          this.terminationDate = data!.terminationDate
          this.bankAccountNo = data!.bankAccountNo
          this.bankAccountName = data!.bankAccountName
          this.bankName = data!.bankName
          this.socialSecurityNo = data!.socialSecurityNo
          this.socialSecurityName = data!.socialSecurityName

        
      }
    )
    .catch(
      error=>{
        console.log(error)        
        this.msgBox.showErrorMessage(error, 'Could not find Doctor')
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
