import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Component, NgModule, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterLink } from '@angular/router';
import { NgxSpinnerModule, NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { AuthService } from 'src/app/auth.service';
import { IRole } from 'src/app/domain/role';
import { IUser } from 'src/app/domain/user';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-user-profile',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {
  public usernameLocked     : boolean = true
  public passwordLocked     : boolean = true
  public passwordConfLocked : boolean = true
  public codeLocked       : boolean = true
  public firstNameLocked    : boolean = true
  public middleNameLocked   : boolean = true
  public lastNameLocked     : boolean = true
  public aliasLocked        : boolean = true
  
  public enableSearch : boolean = false
  public enableDelete : boolean = false
  public enableSave   : boolean = false

  public searchKey       : any
  public id              : any
  public username        : string
  public password        : string
  public confirmPassword : string
  public code          : string
  public firstName       : string
  public middleName      : string
  public lastName        : string
  public nickname           : string
  public active          : boolean

  public roles           : IRole[]

  public users           : IUser[]

  filterRecords : string = ''

  /**
   * 
   * @param http 
   * @param auth 
   */

 
  constructor(private auth : AuthService,
              private http :HttpClient,
              private spinner : NgxSpinnerService,
              private msgBox : MsgBoxService) {

    this.searchKey       = ''
    this.id              = null
    this.username        = ''
    this.password        = ''
    this.confirmPassword = ''
    this.code          = ''
    this.firstName       = ''
    this.middleName      = ''
    this.lastName        = ''
    this.nickname           = ''
    this.active          = true
    this.roles           = []
    this.users           = []
  }  
  getUserData(): any {
    var userRoles : IRole[] = []
    this.roles.forEach(role => { /**Get the roles */
      if(role.granted == true){
        userRoles.push(role)
      }
    })
    return {
      id          : this.id,
      username    : this.username,
      password    : this.password,
      code      : this.code,
      firstName   : this.firstName,
      middleName  : this.middleName,
      lastName    : this.lastName,
      nickname    : this.nickname,
      active      : this.active,
      roles       : userRoles
    }
  }
  
  ngOnInit(): void {
    this.getUsers()
    this.getRoles()
  }

  async saveUser(){
    /**
      * Create a single user:
      * First, validate inputs, then create user
      */
    if(this.validateInputs() == false){
      return
    }
    
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    
    if (this.id == null){
      /**Create a new user */
      this.spinner.show()  
      await this.http.post(API_URL+'/users/create', this.getUserData(), options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.showUser(data)
          this.msgBox.showSuccessMessage('User created successifuly')
          this.getUsers()
        }
      )
      .catch(
        error => {
          console.log(error)
          this.msgBox.showErrorMessage(error, 'Could not create user')
        }
      )   
    }else{
      /**Update an existing user */
      this.spinner.show()
      await this.http.put(API_URL+'/users/update', this.getUserData(), options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          console.log(data)
          this.msgBox.showSuccessMessage('User updated successifuly')
          this.getUsers()
        }
      )
      .catch(
        error => {
          console.log(error);
          this.msgBox.showErrorMessage(error, 'Could not update user')
        }
      )  
    }
  }
 
  async getRoles(){  
   /**Get all roles */
    let options = {
      headers : new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IRole[]>(API_URL+'/roles', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(
          element => {
            this.roles.push(element)
          }
        )
      }
    )
    .catch(error => {
      console.log(error)
    })
  }

  async getUsers(){
    this.users = []
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IUser[]>(API_URL+'/users', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        data?.forEach(
          element => {
            this.users.push(element)
          }
        )
      }
    )
    .catch(error => {
      this.msgBox.showErrorMessage(error, 'Could not load users')
    })
    return 
  }
  
  async getUser(key: string) {
    this.searchKey = key
    this.clearFields()
    this.username = this.searchKey

    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get(API_URL+'/users/get_user?username='+this.searchKey, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data=>{
        this.showUser(data)
        this.lockInputs()
      }
    )
    .catch(
      error=>{
        console.log(error)   
        this.msgBox.showErrorMessage(error, 'User not found')     
      }
    )
  }

  async deleteUser(){
    if(this.id == null){
      alert('No user selected, please select a user to delete')
      return
    }
    if(!confirm('Confirm delete the selected user. This action can not be undone')){
      return
    }
    let options = {
      headers : new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.delete(API_URL+'/users/delete?id='+this.id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      () => {
        this.clearFields()
        alert('User deleted succesifully')
        return true
      }
    )
    .catch(
      error => {
        console.log(error)
        this.msgBox.showErrorMessage(error, 'Could not delete user')
        return false
      }
    )
  }

  showUser(user : any){
    /**
     * Display user details, takes a json user object
     * Args: user object
     */
    this.id         = user['id']
    this.username   = user['username']
    this.code     = user['code']
    this.firstName  = user['firstName']
    this.middleName = user['middleName']
    this.lastName   = user['lastName']
    this.nickname      = user['nickname']
    this.active     = user['active']
    this.showUserRoles(this.roles, user['roles'])
  }

  showUserRoles(roles : IRole[], userRoles : IRole[]){
    /**
     * Display user roles, the roles for that particular user are checked
     * args: roles-global user roles, userRoles-roles for a specific user
     */
    /** First uncheck all roles */
    this.clearRoles()
    /** Now, check the respective  roles */
    userRoles.forEach(userRole => {
      roles.forEach(role => {        
        if(role.name === userRole.name){
          role.granted = true
        }
      })
    })
    this.roles = roles
  }

  clearRoles(){
    /**Uncheck all roles */
    this.roles.forEach(role => {
      role.granted = false
    })
  }

  validateInputs() : boolean{
    let valid : boolean = true
    /**Validate username */
    if(this.username == ''){
      alert('Empty username not allowed, please fill in the username field')
      return false
    }

    /**Validate Password */
    if(this.id == null){
      if(this.password == ''){
        alert('Empty password not allowed for new user')
        return false
      }
      if(this.password != this.confirmPassword){
        alert('Password and Password confirmation do not match')
        return false
      }
    }else{
      if(this.password != this.confirmPassword && (this.password != '' || this.confirmPassword != '')){
        alert('Password and Password confirmation do not match')
        return false
      }
    }
    if(this.firstName == '' || this.lastName == ''){
      alert('First name, last name and nickname are required fields')
      return false
    }
    return valid
  }

  clearFields(){
    /**Clear the specified fields */
    this.id               = null
    this.username         = ''
    this.password         = ''
    this.confirmPassword  = ''
    this.code           = ''
    this.firstName        = ''
    this.middleName       = ''
    this.lastName         = ''
    this.nickname         = ''
    this.active           = false
    this.clearRoles()
    this.enableSave = true
  }

  unlockInputs(){
    /**Unlock the specified fields */
    this.usernameLocked      = false
    this.passwordLocked      = false
    this.passwordConfLocked  = false
    this.codeLocked        = false
    this.firstNameLocked     = false
    this.middleNameLocked    = false
    this.lastNameLocked      = false
    this.aliasLocked         = false
  }

  lockInputs(){
    /**Lock the specified fields */
    this.usernameLocked      = true
    this.passwordLocked      = true
    this.passwordConfLocked  = true
    this.codeLocked        = true
    this.firstNameLocked     = true
    this.middleNameLocked    = true
    this.lastNameLocked      = true
    this.aliasLocked         = true
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