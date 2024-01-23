import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import * as pdfMake from 'pdfmake/build/pdfmake';
import { finalize } from 'rxjs';
import { AuthService } from 'src/app/auth.service';
import { IAdmission } from 'src/app/domain/admission';
import { IDeceasedNote } from 'src/app/domain/deceased-note';
import { IDischargePlan } from 'src/app/domain/discharge-plan';
import { IPatient } from 'src/app/domain/patient';
import { IPatientBill } from 'src/app/domain/patient-bill';
import { IPatientInvoice } from 'src/app/domain/patient-invoice';
import { ReceiptItem } from 'src/app/domain/receipt-item';
import { IReferralPlan } from 'src/app/domain/referral-plan';
import { AgePipe } from 'src/app/pipes/age.pipe';
import { ShowDateTimePipe } from 'src/app/pipes/date_time.pipe';
import { SearchFilterPipe } from 'src/app/pipes/search-filter-pipe';
import { ShowTimePipe } from 'src/app/pipes/show_time.pipe';
import { ShowUserPipe } from 'src/app/pipes/show_user.pipe';
import { DataService } from 'src/app/services/data.service';
import { MsgBoxService } from 'src/app/services/msg-box.service';
import { PosReceiptPrinterService } from 'src/app/services/pos-receipt-printer.service';
import { environment } from 'src/environments/environment';
var pdfFonts = require('pdfmake/build/vfs_fonts.js');

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-referral-list',
  templateUrl: './referral-list.component.html',
  styleUrls: ['./referral-list.component.scss'],
  standalone : true,
  imports : [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe,
    AgePipe,
    ShowUserPipe,
    ShowTimePipe,
    RouterLink
  ], 
})
export class ReferralListComponent {
  referralPlans : IReferralPlan[] = []

  filterRecords : string = ''

  constructor(private auth : AuthService,
    private http :HttpClient,
    private modalService: NgbModal,
    private spinner : NgxSpinnerService,
    private router : Router,
    private msgBox : MsgBoxService,
    private data : DataService) 
    {(window as any).pdfMake.vfs = pdfFonts.pdfMake.vfs;}

  async ngOnInit(): Promise<void> {
    this.loadReferralList()
  }

  async loadReferralList(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IReferralPlan[]>(API_URL+'/patients/load_referral_list', options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.referralPlans = data!
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

  referralPlan! : IReferralPlan
  
  async getReferralSummary(id : any){
    if(!window.confirm('Confirm get Referral summary. Confirm?')){
      return
    }
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    this.spinner.show()
    await this.http.get<IReferralPlan>(API_URL+'/patients/get_referral_summary?referral_plan_id='+id, options)
    .pipe(finalize(() => this.spinner.hide()))
    .toPromise()
    .then(
      data => {
        this.referralPlan = data!
        console.log(data)
        this.print(this.referralPlan)
      }
    )
    .catch(
      error => {
        this.msgBox.showErrorMessage(error, '')
        console.log(error)
      }
    )
  }

  logo!    : any
  documentHeader! : any
  print = async (plan : IReferralPlan) => {

    if(plan === null){
      this.msgBox.showErrorMessage3('No data to publish')
      return
    }

    var patient! : IPatient
    if(plan.admission != null){
      patient = plan.admission.patient
    }else if(plan.consultation != null){
      patient = plan.consultation.patient
    }else{
      this.msgBox.showErrorMessage3('Patient information missing')
      return
    }

    this.documentHeader = await this.data.getDocumentHeader()
    var header = ''
    var footer = ''
    var title  = 'Referral Note'
    var logo : any = ''
    var total : number = 0
    var discount : number = 0
    var tax : number = 0

    var sn : number = 1

    var total = 0

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
          {
            columns : 
            [
              {
                width : 200,
                layout : 'noBorders',
                table : {
                  widths : [200],
                  body : [
                    [
                      {text : 'Name: '+patient?.firstName.toString() + ' ' +patient?.middleName?.toString() + ' ' +patient?.lastName?.toString(), fontSize : 9},
                    ],
                    [
                      {text : 'File No: '+patient?.no.toString(), fontSize : 9}, 
                    ],
                    [
                      {text : 'Address: '+patient?.address.toString(), fontSize : 9},
                    ],
                    [
                      {text : 'Phone No: '+patient?.phoneNo.toString(), fontSize : 9},
                    ]
                  ]
                }
              },
              {
                width : 100,
                layout : 'noBorders',
                table : {
                  widths : [100],
                  body : [
                    [
                      {text : '', fontSize : 9}, 
                    ]
                  ]
                }
              },
              {
                width : 200,
                layout : 'noBorders',
                table : {
                  widths : [200],
                  body : [
                    [
                      {text : '', fontSize : 9}, 
                    ]
                  ]
                }
              }
            ]
          },
          ' ',
          ' ',
          {text : 'Refered to:', fontSize : 12, bold : true},
          {text : plan?.externalMedicalProvider.name, fontSize : 9, bold : false},
          ' ',
          {text : 'Referring Diagnosis', fontSize : 12, bold : true},
          {text : plan?.referringDiagnosis, fontSize : 9, bold : false},
          ' ',
          {text : 'Brief History & Patient Diagnosis:', fontSize : 12, bold : true},
          {text : plan?.history, fontSize : 9, bold : false},
          ' ',
          {text : 'Investigation Done:', fontSize : 12, bold : true},
          {text : plan?.investigation, fontSize : 9, bold : false},
          ' ',
          {text : 'Inpatient Management:', fontSize : 12, bold : true},
          {text : plan?.management, fontSize : 9, bold : false},
          ' ',
          {text : 'ICU Admission:', fontSize : 12, bold : true},
          {text : plan?.admission, fontSize : 9, bold : false},
          ' ',
          {text : 'Operation Done:', fontSize : 12, bold : true},
          {text : plan?.operationNote, fontSize : 9, bold : false},
          ' ',
          {text : 'General Recommendation:', fontSize : 12, bold : true},
          {text : plan?.generalRecommendation, fontSize : 9, bold : false},
          ' ',
      ]     
    };
    pdfMake.createPdf(docDefinition).print()
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