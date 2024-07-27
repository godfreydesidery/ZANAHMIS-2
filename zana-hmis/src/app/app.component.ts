import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { environment } from 'src/environments/environment';
import { AuthService } from './auth.service';
import { DataService } from './services/data.service';
import { PosReceiptPrinterService } from './services/pos-receipt-printer.service';

import * as pdfMake from 'pdfmake/build/pdfmake';
import { ReceiptItem } from './domain/receipt-item';
import { AuthGuard } from './auth-guard';
import { ClinicComponent } from './pages/admin/medical-units/clinic/clinic.component';
import { PharmacyComponent } from './pages/admin/medical-units/pharmacy/pharmacy.component';
import { ConsultationPlanComponent } from './pages/admin/insurance-management/insurance-plan-pricing/consultation-plan/consultation-plan.component';
import { LabTestPlanComponent } from './pages/admin/insurance-management/insurance-plan-pricing/lab-test-plan/lab-test-plan.component';
import { MedicinePlanComponent } from './pages/admin/insurance-management/insurance-plan-pricing/medicine-plan/medicine-plan.component';
import { ProcedurePlanComponent } from './pages/admin/insurance-management/insurance-plan-pricing/procedure-plan/procedure-plan.component';
import { RadiologyPlanComponent } from './pages/admin/insurance-management/insurance-plan-pricing/radiology-plan/radiology-plan.component';
import { RegistrationPlanComponent } from './pages/admin/insurance-management/insurance-plan-pricing/registration-plan/registration-plan.component';
import { InsurancePlanComponent } from './pages/admin/insurance-management/insurance-plan/insurance-plan.component';
import { InsuranceProviderComponent } from './pages/admin/insurance-management/insurance-provider/insurance-provider.component';
import { DiagnosisTypeComponent } from './pages/admin/medical-operations/diagnosis-type/diagnosis-type.component';
import { LabTestTypeRangeComponent } from './pages/admin/medical-operations/lab-test-type-range/lab-test-type-range.component';
import { LabTestTypeComponent } from './pages/admin/medical-operations/lab-test-type/lab-test-type.component';
import { MedicineComponent } from './pages/admin/medical-operations/medicine/medicine.component';
import { ProcedureTypeComponent } from './pages/admin/medical-operations/procedure-type/procedure-type.component';
import { RadiologyTypeComponent } from './pages/admin/medical-operations/radiology-type/radiology-type.component';
import { TheatreComponent } from './pages/admin/medical-units/theatre/theatre.component';
import { ClinicianComponent } from './pages/admin/personnel/clinician/clinician.component';
import { PharmacistComponent } from './pages/admin/personnel/pharmacist/pharmacist.component';
import { AccessManagementComponent } from './pages/admin/user-and-access/access-management/access-management.component';
import { RoleComponent } from './pages/admin/user-and-access/role/role.component';
import { UserProfileComponent } from './pages/admin/user-and-access/user-profile/user-profile.component';
import { CompanyProfileComponent } from './pages/admin/company/company-profile/company-profile.component';
import { PatientListComponent } from './pages/registration/patient-list/patient-list.component';
import { PatientRegisterComponent } from './pages/registration/patient-register/patient-register.component';
import { LabTestPaymentComponent } from './pages/payments/lab-test-payment/lab-test-payment.component';
import { MedicationPaymentComponent } from './pages/payments/medication-payment/medication-payment.component';
import { PatientPaymentComponent } from './pages/payments/patient-payment/patient-payment.component';
import { ProcedurePaymentComponent } from './pages/payments/procedure-payment/procedure-payment.component';
import { RadiologyPaymentComponent } from './pages/payments/radiology-payment/radiology-payment.component';
import { RegistrationPaymentComponent } from './pages/payments/registration-payment/registration-payment.component';
import { LabInpatientListComponent } from './pages/laboratory/lab-inpatient-list/lab-inpatient-list.component';
import { LabOutpatientListComponent } from './pages/laboratory/lab-outpatient-list/lab-outpatient-list.component';
import { LabOutsiderListComponent } from './pages/laboratory/lab-outsider-list/lab-outsider-list.component';
import { LabTestComponent } from './pages/laboratory/lab-test/lab-test.component';
import { LabSampleCollectionReportComponent } from './pages/laboratory/reports/lab-sample-collection-report/lab-sample-collection-report.component';
import { LabTestReportComponent } from './pages/laboratory/reports/lab-test-report/lab-test-report.component';
import { LabTestStatisticsReportComponent } from './pages/laboratory/reports/lab-test-statistics-report/lab-test-statistics-report.component';
import { RadiologyComponent } from './pages/radiology/radiology/radiology.component';
import { RadiologyInpatientListComponent } from './pages/radiology/radiology-inpatient-list/radiology-inpatient-list.component';
import { RadiologyOutpatientListComponent } from './pages/radiology/radiology-outpatient-list/radiology-outpatient-list.component';
import { RadiologyOutsiderListComponent } from './pages/radiology/radiology-outsider-list/radiology-outsider-list.component';
import { ConsultationPricesComponent } from './pages/price-view/consultation-price/consultation-prices.component';
import { LabTestPriceComponent } from './pages/price-view/lab-test-price/lab-test-price.component';
import { MedicinePricesComponent } from './pages/price-view/medicine-price/medicine-price.component';
import { ProcedurePriceComponent } from './pages/price-view/procedure-price/procedure-price.component';
import { RadiologyPriceComponent } from './pages/price-view/radiology-price/radiology-price.component';
import { RegistrationPricesComponent } from './pages/price-view/registration-price/registration-prices.component';
import { SelectPharmacyComponent } from './pages/pharmacy/select-pharmacy/select-pharmacy.component';
import { PatientPharmacyComponent } from './pages/pharmacy/patient-pharmacy/patient-pharmacy.component';
import { PharmacyInpatientListComponent } from './pages/pharmacy/pharmacy-inpatient-list/pharmacy-inpatient-list.component';
import { PharmacyOutpatientListComponent } from './pages/pharmacy/pharmacy-outpatient-list/pharmacy-outpatient-list.component';
import { PharmacyOutsiderListComponent } from './pages/pharmacy/pharmacy-outsider-list/pharmacy-outsider-list.component';
import { ItemRegisterComponent } from './pages/admin/inventory/item-register/item-register.component';
import { PharmacyToStoreROComponent } from './pages/pharmacy/pharmacy-to-store-r-o/pharmacy-to-store-r-o.component';
import { PharmacyToStoreROListComponent } from './pages/store/pharmacy-to-store-r-o-list/pharmacy-to-store-r-o-list.component';
import { ClinicalNoteHistoryComponent } from './pages/doctor/clinical-note-history/clinical-note-history.component';
import { FinalDiagnosisHistoryComponent } from './pages/doctor/final-diagnosis-history/final-diagnosis-history.component';
import { GeneralExaminationHistoryComponent } from './pages/doctor/general-examination-history/general-examination-history.component';
import { LabTestHistoryComponent } from './pages/doctor/lab-test-history/lab-test-history.component';
import { PatientHistoryMenuComponent } from './pages/doctor/patient-history-menu/patient-history-menu.component';
import { PrescriptionHistoryComponent } from './pages/doctor/prescription-history/prescription-history.component';
import { ProcedureHistoryComponent } from './pages/doctor/procedure-history/procedure-history.component';
import { RadiologyHistoryComponent } from './pages/doctor/radiology-history/radiology-history.component';
import { WorkingDiagnosisHistoryComponent } from './pages/doctor/working-diagnosis-history/working-diagnosis-history.component';
import { NurseComponent } from './pages/admin/personnel/nurse/nurse.component';
import { WardPlanComponent } from './pages/admin/insurance-management/insurance-plan-pricing/ward-plan/ward-plan.component';
import { ConsultationPriceComponent } from './pages/admin/insurance-management/prices/consultation-price/consultation-price.component';
import { LabTestTypePriceComponent } from './pages/admin/insurance-management/prices/lab-test-type-price/lab-test-type-price.component';
import { MedicinePriceComponent } from './pages/admin/insurance-management/prices/medicine-price/medicine-price.component';
import { ProcedureTypePriceComponent } from './pages/admin/insurance-management/prices/procedure-type-price/procedure-type-price.component';
import { RadiologyTypePriceComponent } from './pages/admin/insurance-management/prices/radiology-type-price/radiology-type-price.component';
import { RegistrationPriceComponent } from './pages/admin/insurance-management/prices/registration-price/registration-price.component';
import { WardTypePriceComponent } from './pages/admin/insurance-management/prices/ward-type-price/ward-type-price.component';
import { WardCategoryComponent } from './pages/admin/medical-units/ward-management/ward-category/ward-category.component';
import { WardConfigurationComponent } from './pages/admin/medical-units/ward-management/ward-configuration/ward-configuration.component';
import { WardTypeComponent } from './pages/admin/medical-units/ward-management/ward-type/ward-type.component';
import { WardComponent } from './pages/admin/medical-units/ward-management/ward/ward.component';
import { DoctorInpatientListComponent } from './pages/doctor/doctor-inpatient-list/doctor-inpatient-list.component';
import { NurseInpatientListComponent } from './pages/nurse/nurse-inpatient-list/nurse-inpatient-list.component';
import { NurseOutpatientListComponent } from './pages/nurse/nurse-outpatient-list/nurse-outpatient-list.component';
import { NurseOutsiderListComponent } from './pages/nurse/nurse-outsider-list/nurse-outsider-list.component';
import { DoctorInpatientComponent } from './pages/doctor/doctor-inpatient/doctor-inpatient.component';
import { ConsultationReportComponent } from './pages/admin/reports/consultation-report/consultation-report.component';
import { DoctorToLaboratoryReportComponent } from './pages/admin/reports/doctor-to-laboratory-report/doctor-to-laboratory-report.component';
import { DoctorToRadiologyReportComponent } from './pages/admin/reports/doctor-to-radiology-report/doctor-to-radiology-report.component';
import { ProcedureReportComponent } from './pages/admin/reports/procedure-report/procedure-report.component';
import { StoreToPharmacyRNComponent } from './pages/pharmacy/store-to-pharmacy-r-n/store-to-pharmacy-r-n.component';
import { StoreToPharmacyTOComponent } from './pages/store/store-to-pharmacy-t-o/store-to-pharmacy-t-o.component';
import { InpatientPaymentComponent } from './pages/payments/inpatient-payment/inpatient-payment.component';
import { PharmacyMedicineStockStatusComponent } from './pages/pharmacy/pharmacy-medicine-stock-status/pharmacy-medicine-stock-status.component';
import { ItemMedicineConversionCoefficientComponent } from './pages/store/conversion-coefficients/item-medicine-conversion-coefficient/item-medicine-conversion-coefficient.component';
import { MyConsultationComponent } from './pages/doctor/my-consultation/my-consultation.component';
import { DoctorCrackingComponent } from './pages/doctor/doctor-cracking/doctor-cracking.component';
import { ListFromReceptionComponent } from './pages/doctor/list-from-reception/list-from-reception.component';
import { SupplierRegisterComponent } from './pages/admin/stakeholders/supplier-register/supplier-register.component';
import { ItemInquiryComponent } from './pages/store/item-inquiry/item-inquiry.component';
import { NurseInpatientChartComponent } from './pages/nurse/nurse-inpatient-chart/nurse-inpatient-chart.component';
import { ReportTemplateComponent } from './pages/reports/report-template/report-template.component';
import { CommonModule } from '@angular/common';
var pdfFonts = require('pdfmake/build/vfs_fonts.js'); 

const API_URL = environment.apiUrl;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'zana-hmis';
  isLoggedIn : boolean = false;

  userName : string = ''

  constructor(private router : Router,
    private http  : HttpClient,
    private auth : AuthService) { (window as any).pdfMake.vfs = pdfFonts.pdfMake.vfs; }

  async ngOnInit(): Promise<void> {

    var currentUser = null
    if(localStorage.getItem('user-name') != null){
      this.userName = localStorage.getItem('user-name')!
    }else{
      this.userName = ''
    }
    if(localStorage.getItem('current-user') != null){
      currentUser = localStorage.getItem('current-user')
    }
    if(currentUser != null){
      this.isLoggedIn = true
    }else{
      this.isLoggedIn = false
    }

    await this.router.navigate(['/dashboard'])//Navigates to home if url is entered on address bar
    
    //this.ping()
    this.loadCompanyName()
    this.getLogo()
  }

  async ping() {   
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.get(API_URL+'/ping', options)
    .toPromise()
    .then(
      ()=>{}
    )
    .catch(
      ()=>{}
    )
  }

  async loadDay(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.get<IDayData>(API_URL+'/days/get_bussiness_date', options)
    .toPromise()
    .then(
      data => {
        localStorage.setItem('system-date', data?.bussinessDate!+'')        
      }
    )
    .catch(() => {})
  }

  async loadCompanyName(){
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer '+this.auth.user.access_token)
    }
    await this.http.get<ICompanyData>(API_URL+'/company_profile/get', options)
    .toPromise()
    .then(
      data => {
        localStorage.setItem('company-name', data?.companyName!+'')  
        localStorage.setItem('public-path', data?.publicPath!+'')      
      }
    )
    .catch(() => {})
  }

  public async logout() : Promise<any>{
    localStorage.removeItem('current-user')
    alert('You have logged out!')
    //await this.router.navigate([''])
    window.location.reload()
  }

  selectedFile!: File;
  retrievedImage!: any;
  base64Data: any;
  retrieveResponse: any;
  message!: string;
  imageName: any;
  async getLogo() {
    //Make a call to Sprinf Boot to get the Image Bytes.
    //this.spinner.show()
    await this.http.get(API_URL+'/company_profile/get_logo')
    //.pipe(finalize(() => this.spinner.hide()))
    .toPromise()
      .then(
        res => {
          this.retrieveResponse = res
          this.base64Data = this.retrieveResponse.logo
          this.retrievedImage = 'data:image/png;base64,'+this.base64Data
          console.log(this.retrievedImage)
        }
      )
      .catch(error => {
        console.log(error)
      })  
      
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

    loadRegistrationModule(){
      this.router.config.push(
        {path : 'patient-register', loadComponent : () => import('./pages/registration/patient-register/patient-register.component').then(m => m.PatientRegisterComponent), canActivate: [AuthGuard]},
        {path : 'patient-list', loadComponent : () => import('./pages/registration/patient-list/patient-list.component').then(m => m.PatientListComponent), canActivate: [AuthGuard]},
      )
    }
    loadClinicModule(){
      this.router.config.push(
        {path : 'patient-history-menu', loadComponent : () => import('./pages/doctor/patient-history-menu/patient-history-menu.component').then(m => m.PatientHistoryMenuComponent), canActivate: [AuthGuard]},
        {path : 'clinical-note-history', loadComponent : () => import('./pages/doctor/clinical-note-history/clinical-note-history.component').then(m => m.ClinicalNoteHistoryComponent), canActivate: [AuthGuard]},
        {path : 'general-examination-history', loadComponent : () => import('./pages/doctor/general-examination-history/general-examination-history.component').then(m => m.GeneralExaminationHistoryComponent), canActivate: [AuthGuard]},
        {path : 'lab-test-history', loadComponent : () => import('./pages/doctor/lab-test-history/lab-test-history.component').then(m => m.LabTestHistoryComponent), canActivate: [AuthGuard]},
        {path : 'radiology-history', loadComponent : () => import('./pages/doctor/radiology-history/radiology-history.component').then(m => m.RadiologyHistoryComponent), canActivate: [AuthGuard]},
        {path : 'procedure-history', loadComponent : () => import('./pages/doctor/procedure-history/procedure-history.component').then(m => m.ProcedureHistoryComponent), canActivate: [AuthGuard]},
        {path : 'prescription-history', loadComponent : () => import('./pages/doctor/prescription-history/prescription-history.component').then(m => m.PrescriptionHistoryComponent), canActivate: [AuthGuard]},
        {path : 'working-diagnosis-history', loadComponent : () => import('./pages/doctor/working-diagnosis-history/working-diagnosis-history.component').then(m => m.WorkingDiagnosisHistoryComponent), canActivate: [AuthGuard]},
        {path : 'final-diagnosis-history', loadComponent : () => import('./pages/doctor/final-diagnosis-history/final-diagnosis-history.component').then(m => m.FinalDiagnosisHistoryComponent), canActivate: [AuthGuard]},
        {path : 'doctor-inpatient-list', loadComponent : () => import('./pages/doctor/doctor-inpatient-list/doctor-inpatient-list.component').then(m => m.DoctorInpatientListComponent)},
        {path : 'doctor-inpatient' , loadComponent : () => import('./pages/doctor/doctor-inpatient/doctor-inpatient.component').then(m => m.DoctorInpatientComponent)},
        {path : 'my-consultation', loadComponent : () => import('./pages/doctor/my-consultation/my-consultation.component').then(m => m.MyConsultationComponent), canActivate: [AuthGuard]}, 
        {path : 'doctor-cracking', loadComponent : () => import('./pages/doctor/doctor-cracking/doctor-cracking.component').then(m => m.DoctorCrackingComponent), canActivate: [AuthGuard]},
        {path : 'doctor-follow-up', loadComponent : () => import('./pages/doctor/doctor-follow-up/doctor-follow-up.component').then(m => m.DoctorFollowUpComponent), canActivate: [AuthGuard]},
        {path : 'list-from-reception', loadComponent : () => import('./pages/doctor/list-from-reception/list-from-reception.component').then(m => m.ListFromReceptionComponent), canActivate: [AuthGuard]},
        {path : 'follow-up-list', loadComponent : () => import('./pages/doctor/follow-up-list/follow-up-list.component').then(m => m.FollowUpListComponent), canActivate: [AuthGuard]},
        {path : 'discharge-plan', loadComponent : () => import('./pages/doctor/discharge-plan/discharge-plan.component').then(m => m.DischargePlanComponent), canActivate: [AuthGuard]},
        {path : 'deceased-note', loadComponent : () => import('./pages/doctor/deceased-note/deceased-note.component').then(m => m.DeceasedNoteComponent), canActivate: [AuthGuard]},
        {path : 'referral-plan', loadComponent : () => import('./pages/doctor/referral-plan/referral-plan.component').then(m => m.ReferralPlanComponent), canActivate: [AuthGuard]}
        
      )
    }
    loadNursingModule(){
      this.router.config.push(
        {path : 'nurse-inpatient-list', loadComponent : () => import('./pages/nurse/nurse-inpatient-list/nurse-inpatient-list.component').then(m => m.NurseInpatientListComponent)},
        {path : 'nurse-outpatient-list', loadComponent : () => import('./pages/nurse/nurse-outpatient-list/nurse-outpatient-list.component').then(m => m.NurseOutpatientListComponent)},
        {path : 'nurse-outpatient-chart', loadComponent : () => import('./pages/nurse/nurse-outpatient-chart/nurse-outpatient-chart.component').then(m => m.NurseOutpatientChartComponent)},
        {path : 'nurse-outsider-list', loadComponent : () => import('./pages/nurse/nurse-outsider-list/nurse-outsider-list.component').then(m => m.NurseOutsiderListComponent)},
        {path : 'nurse-inpatient-chart', loadComponent : () => import('./pages/nurse/nurse-inpatient-chart/nurse-inpatient-chart.component').then(m => m.NurseInpatientChartComponent)},
        {path : 'nurse-outsider-chart', loadComponent : () => import('./pages/nurse/nurse-outsider-chart/nurse-outsider-chart.component').then(m => m.NurseOutsiderChartComponent)},
        {path : 'nursing-history-menu', loadComponent : () => import('./pages/nurse/nursing-history-menu/nursing-history-menu.component').then(m => m.NursingHistoryMenuComponent)},



        {path : 'nurse-patient-history-menu', loadComponent : () => import('./pages/nurse/nurse-patient-history-menu/nurse-patient-history-menu.component').then(m => m.NursePatientHistoryMenuComponent), canActivate: [AuthGuard]},
        {path : 'nurse-clinical-note-history', loadComponent : () => import('./pages/nurse/nurse-clinical-note-history/nurse-clinical-note-history.component').then(m => m.NurseClinicalNoteHistoryComponent), canActivate: [AuthGuard]},
        {path : 'nurse-general-examination-history', loadComponent : () => import('./pages/nurse/nurse-general-examination-history/nurse-general-examination-history.component').then(m => m.NurseGeneralExaminationHistoryComponent), canActivate: [AuthGuard]},
        {path : 'nurse-lab-test-history', loadComponent : () => import('./pages/nurse/nurse-lab-test-history/nurse-lab-test-history.component').then(m => m.NurseLabTestHistoryComponent), canActivate: [AuthGuard]},
        {path : 'nurse-radiology-history', loadComponent : () => import('./pages/nurse/nurse-radiology-history/nurse-radiology-history.component').then(m => m.NurseRadiologyHistoryComponent), canActivate: [AuthGuard]},
        {path : 'nurse-procedure-history', loadComponent : () => import('./pages/nurse/nurse-procedure-history/nurse-procedure-history.component').then(m => m.NurseProcedureHistoryComponent), canActivate: [AuthGuard]},
        {path : 'nurse-prescription-history', loadComponent : () => import('./pages/nurse/nurse-prescription-history/nurse-prescription-history.component').then(m => m.NursePrescriptionHistoryComponent), canActivate: [AuthGuard]},
        {path : 'nurse-working-diagnosis-history', loadComponent : () => import('./pages/nurse/nurse-working-diagnosis-history/nurse-working-diagnosis-history.component').then(m => m.NurseWorkingDiagnosisHistoryComponent), canActivate: [AuthGuard]},
        {path : 'nurse-final-diagnosis-history', loadComponent : () => import('./pages/nurse/nurse-final-diagnosis-history/nurse-final-diagnosis-history.component').then(m => m.NurseFinalDiagnosisHistoryComponent), canActivate: [AuthGuard]},



        {path : 'patient-history-menu', loadComponent : () => import('./pages/doctor/patient-history-menu/patient-history-menu.component').then(m => m.PatientHistoryMenuComponent), canActivate: [AuthGuard]},
        {path : 'clinical-note-history', loadComponent : () => import('./pages/doctor/clinical-note-history/clinical-note-history.component').then(m => m.ClinicalNoteHistoryComponent), canActivate: [AuthGuard]},
        {path : 'general-examination-history', loadComponent : () => import('./pages/doctor/general-examination-history/general-examination-history.component').then(m => m.GeneralExaminationHistoryComponent), canActivate: [AuthGuard]},
        {path : 'lab-test-history', loadComponent : () => import('./pages/doctor/lab-test-history/lab-test-history.component').then(m => m.LabTestHistoryComponent), canActivate: [AuthGuard]},
        {path : 'radiology-history', loadComponent : () => import('./pages/doctor/radiology-history/radiology-history.component').then(m => m.RadiologyHistoryComponent), canActivate: [AuthGuard]},
        {path : 'procedure-history', loadComponent : () => import('./pages/doctor/procedure-history/procedure-history.component').then(m => m.ProcedureHistoryComponent), canActivate: [AuthGuard]},
        {path : 'prescription-history', loadComponent : () => import('./pages/doctor/prescription-history/prescription-history.component').then(m => m.PrescriptionHistoryComponent), canActivate: [AuthGuard]},
        {path : 'working-diagnosis-history', loadComponent : () => import('./pages/doctor/working-diagnosis-history/working-diagnosis-history.component').then(m => m.WorkingDiagnosisHistoryComponent), canActivate: [AuthGuard]},
        {path : 'final-diagnosis-history', loadComponent : () => import('./pages/doctor/final-diagnosis-history/final-diagnosis-history.component').then(m => m.FinalDiagnosisHistoryComponent), canActivate: [AuthGuard]},
      )
    }
    loadLaboratoryModule(){
      this.router.config.push(
        {path : 'lab-test', loadComponent : () => import('./pages/laboratory/lab-test/lab-test.component').then(m => m.LabTestComponent), canActivate: [AuthGuard]},
        {path : 'lab-outpatient-list', loadComponent : () => import('./pages/laboratory/lab-outpatient-list/lab-outpatient-list.component').then(m => m.LabOutpatientListComponent), canActivate: [AuthGuard]},
        {path : 'lab-inpatient-list', loadComponent : () => import('./pages/laboratory/lab-inpatient-list/lab-inpatient-list.component').then(m => m.LabInpatientListComponent), canActivate: [AuthGuard]},
        {path : 'lab-outsider-list', loadComponent : () => import('./pages/laboratory/lab-outsider-list/lab-outsider-list.component').then(m => m.LabOutsiderListComponent), canActivate: [AuthGuard]},
        {path : 'lab-test-report', loadComponent : () => import('./pages/laboratory/reports/lab-test-report/lab-test-report.component').then(m => m.LabTestReportComponent), canActivate: [AuthGuard]},
        {path : 'lab-test-statistics-report', loadComponent : () => import('./pages/laboratory/reports/lab-test-statistics-report/lab-test-statistics-report.component').then(m => m.LabTestStatisticsReportComponent), canActivate: [AuthGuard]},
        {path : 'lab-sample-collection-report', loadComponent : () => import('./pages/laboratory/reports/lab-sample-collection-report/lab-sample-collection-report.component').then(m => m.LabSampleCollectionReportComponent), canActivate: [AuthGuard]},
        {path : 'patient-results', loadComponent : () => import('./pages/laboratory/patient-results/patient-results.component').then(m => m.PatientResultsComponent), canActivate: [AuthGuard]},
      )
    }
    loadRadiologyModule(){
      this.router.config.push(
        {path : 'radiology', loadComponent : () => import('./pages/radiology/radiology/radiology.component').then(m => m.RadiologyComponent), canActivate: [AuthGuard]},
        {path : 'radiology-outpatient-list', loadComponent : () => import('./pages/radiology/radiology-outpatient-list/radiology-outpatient-list.component').then(m => m.RadiologyOutpatientListComponent), canActivate: [AuthGuard]},
        {path : 'radiology-inpatient-list', loadComponent : () => import('./pages/radiology/radiology-inpatient-list/radiology-inpatient-list.component').then(m => m.RadiologyInpatientListComponent), canActivate: [AuthGuard]},
        {path : 'radiology-outsider-list', loadComponent : () => import('./pages/radiology/radiology-outsider-list/radiology-outsider-list.component').then(m => m.RadiologyOutsiderListComponent), canActivate: [AuthGuard]},

      )
    }
    loadPharmacyModule(){
      this.router.config.push(
        {path : 'select-pharmacy', loadComponent : () => import('./pages/pharmacy/select-pharmacy/select-pharmacy.component').then(m => m.SelectPharmacyComponent), canActivate: [AuthGuard]},
        {path : 'pharmacy-outpatient-list', loadComponent : () => import('./pages/pharmacy/pharmacy-outpatient-list/pharmacy-outpatient-list.component').then(m => m.PharmacyOutpatientListComponent), canActivate: [AuthGuard]},
        {path : 'pharmacy-inpatient-list', loadComponent : () => import('./pages/pharmacy/pharmacy-inpatient-list/pharmacy-inpatient-list.component').then(m => m.PharmacyInpatientListComponent), canActivate: [AuthGuard]},
        {path : 'pharmacy-outsider-list', loadComponent : () => import('./pages/pharmacy/pharmacy-outsider-list/pharmacy-outsider-list.component').then(m => m.PharmacyOutsiderListComponent), canActivate: [AuthGuard]},
        {path : 'patient-pharmacy', loadComponent : () => import('./pages/pharmacy/patient-pharmacy/patient-pharmacy.component').then(m => m.PatientPharmacyComponent), canActivate: [AuthGuard]},
        {path : 'pharmacy-to-store-r-o', loadComponent : () => import('./pages/pharmacy/pharmacy-to-store-r-o/pharmacy-to-store-r-o.component').then(m => m.PharmacyToStoreROComponent), canActivate: [AuthGuard]},
        {path : 'pharmacy-to-pharmacy-r-o', loadComponent : () => import('./pages/pharmacy/pharmacy-to-pharmacy-r-o/pharmacy-to-pharmacy-r-o.component').then(m => m.PharmacyToPharmacyROComponent), canActivate: [AuthGuard]},
        {path : 'pharmacy-to-store-r-o-list', loadComponent : () => import('./pages/store/pharmacy-to-store-r-o-list/pharmacy-to-store-r-o-list.component').then(m => m.PharmacyToStoreROListComponent), canActivate: [AuthGuard]},
        {path : 'store-to-pharmacy-r-n', loadComponent : () => import('./pages/pharmacy/store-to-pharmacy-r-n/store-to-pharmacy-r-n.component').then(m => m.StoreToPharmacyRNComponent), canActivate: [AuthGuard]},
        {path : 'pharmacy-to-pharmacy-r-n', loadComponent : () => import('./pages/pharmacy/pharmacy-to-pharmacy-r-n/pharmacy-to-pharmacy-r-n.component').then(m => m.PharmacyToPharmacyRNComponent), canActivate: [AuthGuard]},
        {path : 'pharmacy-medicine-stock-status', loadComponent : () => import('./pages/pharmacy/pharmacy-medicine-stock-status/pharmacy-medicine-stock-status.component').then(m => m.PharmacyMedicineStockStatusComponent), canActivate: [AuthGuard]},
        {path : 'pharmacy-dispensing-report', loadComponent : () => import('./pages/pharmacy/reports/pharmacy-dispensing-report/pharmacy-dispensing-report.component').then(m => m.PharmacyDispensingReportComponent), canActivate: [AuthGuard]},
        {path : 'fast-moving-drugs-report', loadComponent : () => import('./pages/pharmacy/reports/fast-moving-drugs/fast-moving-drugs.component').then(m => m.FastMovingDrugsComponent), canActivate: [AuthGuard]},
        {path : 'slow-moving-drugs-report', loadComponent : () => import('./pages/pharmacy/reports/slow-moving-drugs/slow-moving-drugs.component').then(m => m.SlowMovingDrugsComponent), canActivate: [AuthGuard]},
        {path : 'pharmacy-stock-card-report', loadComponent : () => import('./pages/pharmacy/reports/pharmacy-stock-card-report/pharmacy-stock-card-report.component').then(m => m.PharmacyStockCardReportComponent), canActivate: [AuthGuard]},
        {path : 'pharmacy-to-pharmacy-r-o-list', loadComponent : () => import('./pages/pharmacy/pharmacy-to-pharmacy-r-o-list/pharmacy-to-pharmacy-r-o-list.component').then(m => m.PharmacyToPharmacyROListComponent), canActivate: [AuthGuard]},
        {path : 'pharmacy-to-pharmacy-t-o', loadComponent : () => import('./pages/pharmacy/pharmacy-to-pharmacy-t-o/pharmacy-to-pharmacy-t-o.component').then(m => m.PharmacyToPharmacyTOComponent), canActivate: [AuthGuard]},
      )
    }
    loadCashierModule(){
      this.router.config.push(
        {path : 'patient-payment', loadComponent : () => import('./pages/payments/patient-payment/patient-payment.component').then(m => m.PatientPaymentComponent), canActivate: [AuthGuard]},
        {path : 'registration-payment', loadComponent : () => import('./pages/payments/registration-payment/registration-payment.component').then(m => m.RegistrationPaymentComponent), canActivate: [AuthGuard]},
        {path : 'lab-test-payment', loadComponent : () => import('./pages/payments/lab-test-payment/lab-test-payment.component').then(m => m.LabTestPaymentComponent), canActivate: [AuthGuard]},
        {path : 'radiology-payment', loadComponent : () => import('./pages/payments/radiology-payment/radiology-payment.component').then(m => m.RadiologyPaymentComponent), canActivate: [AuthGuard]},
        {path : 'medication-payment', loadComponent : () => import('./pages/payments/medication-payment/medication-payment.component').then(m => m.MedicationPaymentComponent), canActivate: [AuthGuard]},
        {path : 'procedure-payment', loadComponent : () => import('./pages/payments/procedure-payment/procedure-payment.component').then(m => m.ProcedurePaymentComponent), canActivate: [AuthGuard]},
        {path : 'patient-direct-pending-invoices', loadComponent : () => import('./pages/payments/patient-direct-invoices/patient-direct-invoices.component').then(m => m.PatientDirectInvoicesComponent), canActivate: [AuthGuard]},
        {path : 'patient-insurance-pending-invoices', loadComponent : () => import('./pages/payments/patient-insurance-invoices/patient-insurance-invoices.component').then(m => m.PatientInsuranceInvoicesComponent), canActivate: [AuthGuard]},
        {path : 'patient-invoice', loadComponent : () => import('./pages/payments/patient-invoice/patient-invoice.component').then(m => m.PatientInvoiceComponent), canActivate: [AuthGuard]},
        {path : 'discharge-list', loadComponent : () => import('./pages/payments/discharge-list/discharge-list.component').then(m => m.DischargeListComponent), canActivate: [AuthGuard]},
        {path : 'deceased-list', loadComponent : () => import('./pages/payments/deceased-list/deceased-list.component').then(m => m.DeceasedListComponent), canActivate: [AuthGuard]},
        {path : 'referral-list', loadComponent : () => import('./pages/payments/referral-list/referral-list.component').then(m => m.ReferralListComponent), canActivate: [AuthGuard]},

        /*{path : 'registration-prices', loadComponent : () => import('./pages/payments/pri').then(m => m), canActivate: [AuthGuard]},
        {path : 'consultation-prices', loadComponent : () => import('./pages').then(m => m), canActivate: [AuthGuard]},
        {path : 'lab-test-price', loadComponent : () => import('./pages').then(m => m), canActivate: [AuthGuard]},
        {path : 'procedure-price', loadComponent : () => import('./pages').then(m => m), canActivate: [AuthGuard]},
        {path : 'radiology-price', loadComponent : () => import('./pages').then(m => m), canActivate: [AuthGuard]},
        {path : 'medicine-prices', loadComponent : () => import('./pages').then(m => m), canActivate: [AuthGuard]},*/

        {path : 'inpatient-payment', loadComponent : () => import('./pages/payments/inpatient-payment/inpatient-payment.component').then(m => m.InpatientPaymentComponent), canActivate: [AuthGuard]},
        {path : 'patient-report', loadComponent : () => import('./pages/reports/patient-report/patient-report.component').then(m => m.PatientReportComponent), canActivate: [AuthGuard]},
      )
    }
    loadStoreModule(){
      this.router.config.push(
        {path : 'select-store', loadComponent : () => import('./pages/store/select-store/select-store.component').then(m => m.SelectStoreComponent), canActivate: [AuthGuard]},
        {path : 'store-to-pharmacy-t-o', loadComponent : () => import('./pages/store/store-to-pharmacy-t-o/store-to-pharmacy-t-o.component').then(m => m.StoreToPharmacyTOComponent), canActivate: [AuthGuard]},
        {path : 'item-medicine-conversion-coefficient', loadComponent : () => import('./pages/store/conversion-coefficients/item-medicine-conversion-coefficient/item-medicine-conversion-coefficient.component').then(m => m.ItemMedicineConversionCoefficientComponent), canActivate: [AuthGuard]},
        {path : 'item-inquiry', loadComponent : () => import('./pages/store/item-inquiry/item-inquiry.component').then(m => m.ItemInquiryComponent), canActivate: [AuthGuard]},
        {path : 'pharmacy-to-store-r-o-list', loadComponent : () => import('./pages/store/pharmacy-to-store-r-o-list/pharmacy-to-store-r-o-list.component').then(m => m.PharmacyToStoreROListComponent), canActivate: [AuthGuard]},
        {path : 'goods-received-note', loadComponent : () => import('./pages/store/goods-received-note/goods-received-note.component').then(m => m.GoodsReceivedNoteComponent), canActivate: [AuthGuard]},
        {path : 'store-item-stock-status', loadComponent : () => import('./pages/store/store-item-stock-status/store-item-stock-status.component').then(m => m.StoreItemStockStatusComponent), canActivate: [AuthGuard]},
        {path : 'goods-received-note-report', loadComponent : () => import('./pages/store/reports/goods-received-note-report/goods-received-note-report.component').then(m => m.GoodsReceivedNoteReportComponent), canActivate: [AuthGuard]},
        {path : 'store-stock-card-report', loadComponent : () => import('./pages/store/reports/store-stock-card-report/store-stock-card-report.component').then(m => m.StoreStockCardReportComponent), canActivate: [AuthGuard]},
        {path : 'local-purchase-order-report', loadComponent : () => import('./pages/procurement/reports/local-purchase-order-report/local-purchase-order-report.component').then(m => m.LocalPurchaseOrderReportComponent), canActivate: [AuthGuard]},
      )
    }

    loadProcurementModule(){
      this.router.config.push(
        {path : 'local-purchase-order', loadComponent : () => import('./pages/procurement/local-purchase-order/local-purchase-order.component').then(m => m.LocalPurchaseOrderComponent), canActivate: [AuthGuard]},
        {path : 'supplier-item-price-list', loadComponent : () => import('./pages/procurement/supplier-item-price-list/supplier-item-price-list.component').then(m => m.SupplierItemPriceListComponent), canActivate: [AuthGuard]},
        {path : 'supplier-register', loadComponent : () => import('./pages/admin/stakeholders/supplier-register/supplier-register.component').then(m => m.SupplierRegisterComponent), canActivate: [AuthGuard]},
        {path : 'item-register', loadComponent : () => import('./pages/admin/inventory/item-register/item-register.component').then(m => m.ItemRegisterComponent), canActivate: [AuthGuard]},
        {path : 'local-purchase-order-report', loadComponent : () => import('./pages/procurement/reports/local-purchase-order-report/local-purchase-order-report.component').then(m => m.LocalPurchaseOrderReportComponent), canActivate: [AuthGuard]},
        {path : 'goods-received-note-report', loadComponent : () => import('./pages/store/reports/goods-received-note-report/goods-received-note-report.component').then(m => m.GoodsReceivedNoteReportComponent), canActivate: [AuthGuard]},
      )
    }
    loadAdminModule(){
      this.router.config.push(
        {path : 'clinic', loadComponent : () => import('./pages/admin/medical-units/clinic/clinic.component').then(m => m.ClinicComponent), canActivate: [AuthGuard]},
        {path : 'pharmacy', loadComponent : () => import('./pages/admin/medical-units/pharmacy/pharmacy.component').then(m => m.PharmacyComponent), canActivate: [AuthGuard]},
        {path : 'store', loadComponent : () => import('./pages/admin/medical-units/store/store.component').then(m => m.StoreComponent), canActivate: [AuthGuard]},
        {path : 'theatre', loadComponent : () => import('./pages/admin/medical-units/theatre/theatre.component').then(m => m.TheatreComponent), canActivate: [AuthGuard]},
        {path : 'clinician', loadComponent : () => import('./pages/admin/personnel/clinician/clinician.component').then(m => m.ClinicianComponent), canActivate: [AuthGuard]},
        {path : 'management', loadComponent : () => import('./pages/admin/personnel/management/management.component').then(m => m.ManagementComponent), canActivate: [AuthGuard]},
        {path : 'store-person', loadComponent : () => import('./pages/admin/personnel/store-person/store-person.component').then(m => m.StorePersonComponent), canActivate: [AuthGuard]},
        {path : 'pharmacist', loadComponent : () => import('./pages/admin/personnel/pharmacist/pharmacist.component').then(m => m.PharmacistComponent), canActivate: [AuthGuard]},
        {path : 'cashier', loadComponent : () => import('./pages/admin/personnel/cashier/cashier.component').then(m => m.CashierComponent), canActivate: [AuthGuard]},
        {path : 'diagnosis-type', loadComponent : () => import('./pages/admin/medical-operations/diagnosis-type/diagnosis-type.component').then(m => m.DiagnosisTypeComponent), canActivate: [AuthGuard]},
        {path : 'lab-test-type', loadComponent : () => import('./pages/admin/medical-operations/lab-test-type/lab-test-type.component').then(m => m.LabTestTypeComponent), canActivate: [AuthGuard]},
        {path : 'lab-test-type-range', loadComponent : () => import('./pages/admin/medical-operations/lab-test-type-range/lab-test-type-range.component').then(m => m.LabTestTypeRangeComponent), canActivate: [AuthGuard]},
        {path : 'procedure-type', loadComponent : () => import('./pages/admin/medical-operations/procedure-type/procedure-type.component').then(m => m.ProcedureTypeComponent), canActivate: [AuthGuard]},
        {path : 'radiology-type', loadComponent : () => import('./pages/admin/medical-operations/radiology-type/radiology-type.component').then(m => m.RadiologyTypeComponent), canActivate: [AuthGuard]},
        {path : 'dressing', loadComponent : () => import('./pages/admin/medical-operations/dressing/dressing.component').then(m => m.DressingComponent), canActivate: [AuthGuard]},
        {path : 'consumable', loadComponent : () => import('./pages/admin/medical-operations/consumable/consumable.component').then(m => m.ConsumableComponent), canActivate: [AuthGuard]},
        {path : 'insurance-provider', loadComponent : () => import('./pages/admin/insurance-management/insurance-provider/insurance-provider.component').then(m => m.InsuranceProviderComponent), canActivate: [AuthGuard]},
        {path : 'insurance-plan', loadComponent : () => import('./pages/admin/insurance-management/insurance-plan/insurance-plan.component').then(m => m.InsurancePlanComponent), canActivate: [AuthGuard]},
        {path : 'registration-plan', loadComponent : () => import('./pages/admin/insurance-management/insurance-plan-pricing/registration-plan/registration-plan.component').then(m => m.RegistrationPlanComponent), canActivate: [AuthGuard]},
        {path : 'consultation-plan', loadComponent : () => import('./pages/admin/insurance-management/insurance-plan-pricing/consultation-plan/consultation-plan.component').then(m => m.ConsultationPlanComponent), canActivate: [AuthGuard]},
        {path : 'lab-test-plan', loadComponent : () => import('./pages/admin/insurance-management/insurance-plan-pricing/lab-test-plan/lab-test-plan.component').then(m => m.LabTestPlanComponent), canActivate: [AuthGuard]},
        {path : 'procedure-plan', loadComponent : () => import('./pages/admin/insurance-management/insurance-plan-pricing/procedure-plan/procedure-plan.component').then(m => m.ProcedurePlanComponent), canActivate: [AuthGuard]},
        {path : 'radiology-plan', loadComponent : () => import('./pages/admin/insurance-management/insurance-plan-pricing/radiology-plan/radiology-plan.component').then(m => m.RadiologyPlanComponent), canActivate: [AuthGuard]},
        {path : 'medicine-plan', loadComponent : () => import('./pages/admin/insurance-management/insurance-plan-pricing/medicine-plan/medicine-plan.component').then(m => m.MedicinePlanComponent), canActivate: [AuthGuard]},
        {path : 'user-profile', loadComponent : () => import('./pages/admin/user-and-access/user-profile/user-profile.component').then(m => m.UserProfileComponent), canActivate: [AuthGuard]},
        {path : 'role', loadComponent : () => import('./pages/admin/user-and-access/role/role.component').then(m => m.RoleComponent), canActivate: [AuthGuard]},
        {path : 'access-management', loadComponent : () => import('./pages/admin/user-and-access/access-management/access-management.component').then(m => m.AccessManagementComponent), canActivate: [AuthGuard]},
        {path : 'medicine', loadComponent : () => import('./pages/admin/medical-operations/medicine/medicine.component').then(m => m.MedicineComponent), canActivate: [AuthGuard]},
        {path : 'company-profile', loadComponent : () => import('./pages/admin/company/company-profile/company-profile.component').then(m => m.CompanyProfileComponent), canActivate: [AuthGuard]},
        {path : 'item-register', loadComponent : () => import('./pages/admin/inventory/item-register/item-register.component').then(m => m.ItemRegisterComponent), canActivate: [AuthGuard]},

        {path : 'nurse', loadComponent : () => import('./pages/admin/personnel/nurse/nurse.component').then(m => m.NurseComponent), canActivate: [AuthGuard]}, 

        {path : 'ward-plan', loadComponent : () => import('./pages/admin/insurance-management/insurance-plan-pricing/ward-plan/ward-plan.component').then(m => m.WardPlanComponent), canActivate: [AuthGuard]},   
        {path : 'lab-test-type-price', loadComponent : () => import('./pages/admin/insurance-management/prices/lab-test-type-price/lab-test-type-price.component').then(m => m.LabTestTypePriceComponent), canActivate: [AuthGuard]},
        {path : 'radiology-type-price', loadComponent : () => import('./pages/admin/insurance-management/prices/radiology-type-price/radiology-type-price.component').then(m => m.RadiologyTypePriceComponent), canActivate: [AuthGuard]},
        {path : 'procedure-type-price', loadComponent : () => import('./pages/admin/insurance-management/prices/procedure-type-price/procedure-type-price.component').then(m => m.ProcedureTypePriceComponent), canActivate: [AuthGuard]},
        {path : 'medicine-price', loadComponent : () => import('./pages/admin/insurance-management/prices/medicine-price/medicine-price.component').then(m => m.MedicinePriceComponent), canActivate: [AuthGuard]},
        {path : 'consultation-price', loadComponent : () => import('./pages/admin/insurance-management/prices/consultation-price/consultation-price.component').then(m => m.ConsultationPriceComponent), canActivate: [AuthGuard]},
        {path : 'registration-price', loadComponent : () => import('./pages/admin/insurance-management/prices/registration-price/registration-price.component').then(m => m.RegistrationPriceComponent), canActivate: [AuthGuard]},
        {path : 'ward-type-price', loadComponent : () => import('./pages/admin/insurance-management/prices/ward-type-price/ward-type-price.component').then(m => m.WardTypePriceComponent), canActivate: [AuthGuard]},
        {path : 'ward', loadComponent : () => import('./pages/admin/medical-units/ward-management/ward/ward.component').then(m => m.WardComponent), canActivate: [AuthGuard]},
        {path : 'ward-configuration', loadComponent : () => import('./pages/admin/medical-units/ward-management/ward-configuration/ward-configuration.component').then(m => m.WardConfigurationComponent), canActivate: [AuthGuard]},
        {path : 'ward-category', loadComponent : () => import('./pages/admin/medical-units/ward-management/ward-category/ward-category.component').then(m => m.WardCategoryComponent), canActivate: [AuthGuard]},
        {path : 'ward-type', loadComponent : () => import('./pages/admin/medical-units/ward-management/ward-type/ward-type.component').then(m => m.WardTypeComponent), canActivate: [AuthGuard]},

        {path : 'consultation-report', loadComponent : () => import('./pages/admin/reports/consultation-report/consultation-report.component').then(m => m.ConsultationReportComponent), canActivate: [AuthGuard]},
        {path : 'procedure-report', loadComponent : () => import('./pages/admin/reports/procedure-report/procedure-report.component').then(m => m.ProcedureReportComponent), canActivate: [AuthGuard]},
        {path : 'doctor-to-radiology-report', loadComponent : () => import('./pages/admin/reports/doctor-to-radiology-report/doctor-to-radiology-report.component').then(m => m.DoctorToRadiologyReportComponent), canActivate: [AuthGuard]},
        {path : 'doctor-to-laboratory-report', loadComponent : () => import('./pages/admin/reports/doctor-to-laboratory-report/doctor-to-laboratory-report.component').then(m => m.DoctorToLaboratoryReportComponent), canActivate: [AuthGuard]},

        {path : 'supplier-register', loadComponent : () => import('./pages/admin/stakeholders/supplier-register/supplier-register.component').then(m => m.SupplierRegisterComponent), canActivate: [AuthGuard]},
        {path : 'external-medical-provider', loadComponent : () => import('./pages/admin/medical-units/external-medical-provider/external-medical-provider.component').then(m => m.ExternalMedicalProviderComponent), canActivate: [AuthGuard]},
      )      
    }
    loadReportModule(){
      this.router.config.push(
        {path : 'report-template', loadComponent: () => import('./pages/reports/report-template/report-template.component').then(m => m.ReportTemplateComponent), canActivate: [AuthGuard]},
        {path : 'doctors-reports', loadComponent: () => import('./pages/reports/doctors-reports/doctors-reports.component').then(m => m.DoctorsReportsComponent), canActivate: [AuthGuard]},
        {path : 'collections-report', loadComponent: () => import('./pages/reports/collections-report/collections-report.component').then(m => m.CollectionsReportComponent), canActivate: [AuthGuard]},
        {path : 'revenue-report', loadComponent: () => import('./pages/reports/revenue-report/revenue-report.component').then(m => m.RevenueReportComponent), canActivate: [AuthGuard]},
        {path : 'ipd-report', loadComponent: () => import('./pages/reports/ipd-report/ipd-report.component').then(m => m.IpdReportComponent), canActivate: [AuthGuard]},
      )
    }

    loadHRModule(){
      this.router.config.push(
        {path : 'employee-register', loadComponent: () => import('./pages/human-resource/employee-register/employee-register.component').then(m => m.EmployeeRegisterComponent), canActivate: [AuthGuard]},
        {path : 'payroll', loadComponent: () => import('./pages/human-resource/payroll/payroll.component').then(m => m.PayrollComponent), canActivate: [AuthGuard]},
        {path : 'asset-register', loadComponent: () => import('./pages/human-resource/asset-register/asset-register.component').then(m => m.AssetRegisterComponent), canActivate: [AuthGuard]},
      )
    }

    loadManagementModule(){
      this.router.config.push(
        //{path : 'report-template', loadComponent: () => import('./pages/reports/report-template/report-template.component').then(m => m.ReportTemplateComponent), canActivate: [AuthGuard]},
        //{path : 'doctors-reports', loadComponent: () => import('./pages/reports/doctors-reports/doctors-reports.component').then(m => m.DoctorsReportsComponent), canActivate: [AuthGuard]},
        {path : 'management-dashboard', loadComponent: () => import('./pages/management/management-dashboard/management-dashboard.component').then(m => m.ManagementDashboardComponent), canActivate: [AuthGuard]},
        {path : 'payroll', loadComponent: () => import('./pages/human-resource/payroll/payroll.component').then(m => m.PayrollComponent), canActivate: [AuthGuard]},

        //{path : 'patient-direct-pending-invoices', loadComponent : () => import('./pages/payments/patient-direct-invoices/patient-direct-invoices.component').then(m => m.PatientDirectInvoicesComponent), canActivate: [AuthGuard]},
        //{path : 'patient-insurance-pending-invoices', loadComponent : () => import('./pages/payments/patient-insurance-invoices/patient-insurance-invoices.component').then(m => m.PatientInsuranceInvoicesComponent), canActivate: [AuthGuard]},
        //{path : 'patient-invoice', loadComponent : () => import('./pages/payments/patient-invoice/patient-invoice.component').then(m => m.PatientInvoiceComponent), canActivate: [AuthGuard]},

        {path : 'patient-report', loadComponent : () => import('./pages/reports/patient-report/patient-report.component').then(m => m.PatientReportComponent), canActivate: [AuthGuard]},

        {path : 'consultation-report', loadComponent : () => import('./pages/admin/reports/consultation-report/consultation-report.component').then(m => m.ConsultationReportComponent), canActivate: [AuthGuard]},
        {path : 'procedure-report', loadComponent : () => import('./pages/admin/reports/procedure-report/procedure-report.component').then(m => m.ProcedureReportComponent), canActivate: [AuthGuard]},
        {path : 'doctor-to-radiology-report', loadComponent : () => import('./pages/admin/reports/doctor-to-radiology-report/doctor-to-radiology-report.component').then(m => m.DoctorToRadiologyReportComponent), canActivate: [AuthGuard]},
        {path : 'doctor-to-laboratory-report', loadComponent : () => import('./pages/admin/reports/doctor-to-laboratory-report/doctor-to-laboratory-report.component').then(m => m.DoctorToLaboratoryReportComponent), canActivate: [AuthGuard]},

        {path : 'lab-test-report', loadComponent : () => import('./pages/laboratory/reports/lab-test-report/lab-test-report.component').then(m => m.LabTestReportComponent), canActivate: [AuthGuard]},
        {path : 'lab-test-statistics-report', loadComponent : () => import('./pages/laboratory/reports/lab-test-statistics-report/lab-test-statistics-report.component').then(m => m.LabTestStatisticsReportComponent), canActivate: [AuthGuard]},
        {path : 'lab-sample-collection-report', loadComponent : () => import('./pages/laboratory/reports/lab-sample-collection-report/lab-sample-collection-report.component').then(m => m.LabSampleCollectionReportComponent), canActivate: [AuthGuard]},
        
        {path : 'pharmacy-medicine-stock-status', loadComponent : () => import('./pages/pharmacy/pharmacy-medicine-stock-status/pharmacy-medicine-stock-status.component').then(m => m.PharmacyMedicineStockStatusComponent), canActivate: [AuthGuard]},
        {path : 'pharmacy-dispensing-report', loadComponent : () => import('./pages/pharmacy/reports/pharmacy-dispensing-report/pharmacy-dispensing-report.component').then(m => m.PharmacyDispensingReportComponent), canActivate: [AuthGuard]},
        {path : 'fast-moving-drugs-report', loadComponent : () => import('./pages/pharmacy/reports/fast-moving-drugs/fast-moving-drugs.component').then(m => m.FastMovingDrugsComponent), canActivate: [AuthGuard]},
        {path : 'slow-moving-drugs-report', loadComponent : () => import('./pages/pharmacy/reports/slow-moving-drugs/slow-moving-drugs.component').then(m => m.SlowMovingDrugsComponent), canActivate: [AuthGuard]},
        {path : 'pharmacy-stock-card-report', loadComponent : () => import('./pages/pharmacy/reports/pharmacy-stock-card-report/pharmacy-stock-card-report.component').then(m => m.PharmacyStockCardReportComponent), canActivate: [AuthGuard]},
        
        {path : 'goods-received-note-report', loadComponent : () => import('./pages/store/reports/goods-received-note-report/goods-received-note-report.component').then(m => m.GoodsReceivedNoteReportComponent), canActivate: [AuthGuard]},
        {path : 'store-stock-card-report', loadComponent : () => import('./pages/store/reports/store-stock-card-report/store-stock-card-report.component').then(m => m.StoreStockCardReportComponent), canActivate: [AuthGuard]},
        {path : 'local-purchase-order-report', loadComponent : () => import('./pages/procurement/reports/local-purchase-order-report/local-purchase-order-report.component').then(m => m.LocalPurchaseOrderReportComponent), canActivate: [AuthGuard]},
      
        {path : 'local-purchase-order-report', loadComponent : () => import('./pages/procurement/reports/local-purchase-order-report/local-purchase-order-report.component').then(m => m.LocalPurchaseOrderReportComponent), canActivate: [AuthGuard]},
        {path : 'goods-received-note-report', loadComponent : () => import('./pages/store/reports/goods-received-note-report/goods-received-note-report.component').then(m => m.GoodsReceivedNoteReportComponent), canActivate: [AuthGuard]},
      
        {path : 'consultation-report', loadComponent : () => import('./pages/admin/reports/consultation-report/consultation-report.component').then(m => m.ConsultationReportComponent), canActivate: [AuthGuard]},
        {path : 'procedure-report', loadComponent : () => import('./pages/admin/reports/procedure-report/procedure-report.component').then(m => m.ProcedureReportComponent), canActivate: [AuthGuard]},
        {path : 'doctor-to-radiology-report', loadComponent : () => import('./pages/admin/reports/doctor-to-radiology-report/doctor-to-radiology-report.component').then(m => m.DoctorToRadiologyReportComponent), canActivate: [AuthGuard]},
        {path : 'doctor-to-laboratory-report', loadComponent : () => import('./pages/admin/reports/doctor-to-laboratory-report/doctor-to-laboratory-report.component').then(m => m.DoctorToLaboratoryReportComponent), canActivate: [AuthGuard]},

      )
    }

/*

    {path : 'procedure-outpatient-list', component : ProcedureOutpatientListComponent, canActivate: [AuthGuard]},
    {path : 'procedure-inpatient-list', component : ProcedureInpatientListComponent, canActivate: [AuthGuard]},
    {path : 'procedure-outsider-list', component : ProcedureOutsiderListComponent, canActivate: [AuthGuard]},

    
  
    {path : 'patient-procedure', component : PatientProcedureComponent, canActivate: [AuthGuard]},
    
    */
  
}

interface IDayData{
  bussinessDate : String
}

interface ICompanyData{
  companyName : String
  publicPath : string
}


