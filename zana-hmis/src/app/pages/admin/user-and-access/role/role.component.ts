import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterLink } from '@angular/router';
import { NgxSpinnerModule, NgxSpinnerService } from 'ngx-spinner';
import { finalize } from 'rxjs';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { AuthService } from 'src/app/auth.service';
import { IRole } from 'src/app/domain/role';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { environment } from 'src/environments/environment';

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-role',
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    RouterLink
  ],
  templateUrl: './role.component.html',
  styleUrls: ['./role.component.scss']
})
export class RoleComponent implements OnInit {

  public nameLocked : boolean = true

  enableSearch : boolean = false
  enableSave   : boolean = false
  enableDelete : boolean = false

  searchKey : any
  id        : any
  name      : string
  granted   : boolean
  active    : boolean

  public roles : IRole[]

  filterRecords : string = ''

  constructor(private auth : AuthService,
              private http :HttpClient,
              private spinner : NgxSpinnerService,
              private msgBox : MsgBoxService) 
              {
    this.id      = null
    this.name    = ''
    this.granted = false
    this.active  = false
    this.roles   = []
  }

  ngOnInit(): void {
    this.getRoles()
  }
  
  async saveRole(): Promise<void> {
    if(this.validateInputs() == false){//validate inputs
      return
    }
    
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    if (this.id == null){
      /**Create a new role */
      this.spinner.show()
      await this.http.post<IRole>(API_URL+'/roles/create', this.getRoleData(), options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          this.nameLocked = true

          this.id = data?.id
          this.name = data!.name
          this.enableDelete = true

          console.log(data)
          this.roles = []
          this.getRoles()
          alert('Role created successifully')
        }
      )
      .catch(
        error => {
          console.log(error)
          this.msgBox.showErrorMessage(error, 'Could not create role')
        }
      )   
    }else{
      /**Update an existing role */
      this.spinner.show()
      await this.http.put(API_URL+'/roles/update', this.getRoleData(), options)
      .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(
        data => {
          console.log(data)
          this.roles = []
          this.getRoles()
          alert('Role updated successifully')
        }
      )
      .catch(
        error => {
          console.log(error)
          this.msgBox.showErrorMessage(error, 'Could not update role')
        }
      )   
    }
  }

  async getRoles() {
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
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
  
  async getRole(key: string): Promise<any> {
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }

    this.searchKey = key
    this.clearFields()
    this.name = this.searchKey
    this.spinner.show()
    await this.http.get<IRole>(API_URL+'/roles/get_role?name='+this.searchKey, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.nameLocked = true

        this.id = data?.id
        this.name = data!.name
        this.enableDelete = true 
      }
    )
    .catch(
      error=>{
        console.log(error) 
        this.name = ''   
        this.msgBox.showErrorMessage(error, 'No matching role')    
      }
    )
  }

  async deleteRole(id: string): Promise<any> {
    if (!window.confirm('Confirm deletion of the selected role')) {
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer ' + this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.delete<IRole[]>(API_URL + '/roles/delete?id=' + id, options)
    .pipe(finalize(() => this.spinner.hide()))
      .toPromise()
      .then(data => {
        this.id    = null
        this.name  = ''
        this.roles = []
        this.nameLocked = true
        this.getRoles()
      })
      .catch(
        error => {
          console.log(error)
          this.msgBox.showErrorMessage(error, 'Could not delete role')
          return false
        }
      )
    return true
  }

  validateInputs() : boolean{
    let valid : boolean = true
    /**Validate role */
    if(this.name == ''){
      alert('Empty name not allowed, please fill in the name field')
      return false
    }
    return valid
  }

  getRoleData(): any {
    return {
      id   : this.id,
      name : this.name
    }
  }

  showRole(role : IRole){
    /**
     * Display role details, takes a json user object
     * Args: role object
     */
    this.id   = role['id']
    this.name = role['name'] 
    this.enableDelete = true   
  }

  clearFields(){
    /**Clear all the fields */
    this.id   = null
    this.name = ''
    this.enableSave = true
    this.enableDelete = false
    this.nameLocked = false
  }

  edit(){
    this.nameLocked = false
  }

  public grant(privilege : string[]) : boolean{  
    /**Allows a user to perform an action if the user has that privilege */
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