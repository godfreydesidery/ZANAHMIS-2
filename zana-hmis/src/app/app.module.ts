import { FormsModule } from '@angular/forms';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/compiler';
import { NgModule, NO_ERRORS_SCHEMA } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { DashboardComponent } from './dashboard/dashboard.component';
import { LoginComponent } from './login/login.component';
import { HttpClientModule } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { JwtHelperService } from '@auth0/angular-jwt';
import { PatientRegisterComponent } from './pages/registration/patient-register/patient-register.component';
import { RouterModule } from '@angular/router';
import { PatientListComponent } from './pages/registration/patient-list/patient-list.component';
import { PatientPaymentComponent } from './pages/payments/patient-payment/patient-payment.component';
import { RegistrationPaymentComponent } from './pages/payments/registration-payment/registration-payment.component';
import { ClinicComponent } from './pages/admin/medical-units/clinic/clinic.component';
import { ClinicianComponent } from './pages/admin/personnel/clinician/clinician.component';
import { MyConsultationComponent } from './pages/doctor/my-consultation/my-consultation.component';
import { DoctorCrackingComponent } from './pages/doctor/doctor-cracking/doctor-cracking.component';
import { ListFromReceptionComponent } from './pages/doctor/list-from-reception/list-from-reception.component';
import { DiagnosisTypeComponent } from './pages/admin/medical-operations/diagnosis-type/diagnosis-type.component';
import { LabTestTypeComponent } from './pages/admin/medical-operations/lab-test-type/lab-test-type.component';
import { ProcedureTypeComponent } from './pages/admin/medical-operations/procedure-type/procedure-type.component';
import { RadiologyTypeComponent } from './pages/admin/medical-operations/radiology-type/radiology-type.component';
import { InsuranceProviderComponent } from './pages/admin/insurance-management/insurance-provider/insurance-provider.component';
import { InsurancePlanComponent } from './pages/admin/insurance-management/insurance-plan/insurance-plan.component';
import { RegistrationPlanComponent } from './pages/admin/insurance-management/insurance-plan-pricing/registration-plan/registration-plan.component';
import { ConsultationPlanComponent } from './pages/admin/insurance-management/insurance-plan-pricing/consultation-plan/consultation-plan.component';
import { LabTestPlanComponent } from './pages/admin/insurance-management/insurance-plan-pricing/lab-test-plan/lab-test-plan.component';
import { ProcedurePlanComponent } from './pages/admin/insurance-management/insurance-plan-pricing/procedure-plan/procedure-plan.component';
import { RadiologyPlanComponent } from './pages/admin/insurance-management/insurance-plan-pricing/radiology-plan/radiology-plan.component';
import { MedicinePlanComponent } from './pages/admin/insurance-management/insurance-plan-pricing/medicine-plan/medicine-plan.component';
import { UserProfileComponent } from './pages/admin/user-and-access/user-profile/user-profile.component';
import { RoleComponent } from './pages/admin/user-and-access/role/role.component';
import { AccessManagementComponent } from './pages/admin/user-and-access/access-management/access-management.component';
import { MedicineComponent } from './pages/admin/medical-operations/medicine/medicine.component';
import { MainPageComponent } from './main-page/main-page.component';
import { NgxSpinnerModule } from 'ngx-spinner';
import { InvestigationPaymentComponent } from './pages/payments/investigation-payment/investigation-payment.component';
import { LabTestComponent } from './pages/laboratory/lab-test/lab-test.component';
import { LabPatientListComponent } from './pages/laboratory/lab-patient-list/lab-patient-list.component';
import { LabOutpatientListComponent } from './pages/laboratory/lab-outpatient-list/lab-outpatient-list.component';
import { LabInpatientListComponent } from './pages/laboratory/lab-inpatient-list/lab-inpatient-list.component';
import { LabOutsiderListComponent } from './pages/laboratory/lab-outsider-list/lab-outsider-list.component';
import { LabTestTypeRangeComponent } from './pages/admin/medical-operations/lab-test-type-range/lab-test-type-range.component';
import { LabTestPaymentComponent } from './pages/payments/lab-test-payment/lab-test-payment.component';
import { RadiologyPaymentComponent } from './pages/payments/radiology-payment/radiology-payment.component';
import { MedicationPaymentComponent } from './pages/payments/medication-payment/medication-payment.component';
import { ProcedurePaymentComponent } from './pages/payments/procedure-payment/procedure-payment.component';
import { RegistrationPricesComponent } from './pages/price-view/registration-price/registration-prices.component';
import { ConsultationPricesComponent } from './pages/price-view/consultation-price/consultation-prices.component';
import { LabTestPriceComponent } from './pages/price-view/lab-test-price/lab-test-price.component';
import { RadiologyPriceComponent } from './pages/price-view/radiology-price/radiology-price.component';
import { ProcedurePriceComponent } from './pages/price-view/procedure-price/procedure-price.component';
import { MedicinePricesComponent } from './pages/price-view/medicine-price/medicine-price.component';
import { RadiologyInpatientListComponent } from './pages/radiology/radiology-inpatient-list/radiology-inpatient-list.component';
import { RadiologyOutpatientListComponent } from './pages/radiology/radiology-outpatient-list/radiology-outpatient-list.component';
import { RadiologyOutsiderListComponent } from './pages/radiology/radiology-outsider-list/radiology-outsider-list.component';
import { ProcedureInpatientListComponent } from './pages/procedure/procedure-inpatient-list/procedure-inpatient-list.component';
import { ProcedureOutpatientListComponent } from './pages/procedure/procedure-outpatient-list/procedure-outpatient-list.component';
import { ProcedureOutsiderListComponent } from './pages/procedure/procedure-outsider-list/procedure-outsider-list.component';
import { RadiologyComponent } from './pages/radiology/radiology/radiology.component';
import { PharmacyComponent } from './pages/admin/medical-units/pharmacy/pharmacy.component';
import { PharmacyOutpatientListComponent } from './pages/pharmacy/pharmacy-outpatient-list/pharmacy-outpatient-list.component';
import { PharmacyInpatientListComponent } from './pages/pharmacy/pharmacy-inpatient-list/pharmacy-inpatient-list.component';
import { PharmacyOutsiderListComponent } from './pages/pharmacy/pharmacy-outsider-list/pharmacy-outsider-list.component';
import { SelectPharmacyComponent } from './pages/pharmacy/select-pharmacy/select-pharmacy.component';
import { PharmacistComponent } from './pages/admin/personnel/pharmacist/pharmacist.component';
import { PatientPharmacyComponent } from './pages/pharmacy/patient-pharmacy/patient-pharmacy.component';
import { PatientProcedureComponent } from './pages/procedure/patient-procedure/patient-procedure.component';
import { AgePipe } from './pipes/age.pipe';
import { TheatreComponent } from './pages/admin/medical-units/theatre/theatre.component';
import { ItemRegisterComponent } from './pages/admin/inventory/item-register/item-register.component';
import { SupplierRegisterComponent } from './pages/admin/stakeholders/supplier-register/supplier-register.component';
import { ItemInquiryComponent } from './pages/store/item-inquiry/item-inquiry.component';
import { PharmacyToStoreROComponent } from './pages/pharmacy/pharmacy-to-store-r-o/pharmacy-to-store-r-o.component';
import { PharmacyToStoreROListComponent } from './pages/store/pharmacy-to-store-r-o-list/pharmacy-to-store-r-o-list.component';
import { StoreToPharmacyTOComponent } from './pages/store/store-to-pharmacy-t-o/store-to-pharmacy-t-o.component';
import { ItemMedicineConversionCoefficientComponent } from './pages/store/conversion-coefficients/item-medicine-conversion-coefficient/item-medicine-conversion-coefficient.component';
import { StoreToPharmacyRNComponent } from './pages/pharmacy/store-to-pharmacy-r-n/store-to-pharmacy-r-n.component';
import { CompanyProfileComponent } from './pages/admin/company/company-profile/company-profile.component';
import { ReportTemplateComponent } from './pages/reports/report-template/report-template.component';
import { PatientHistoryMenuComponent } from './pages/doctor/patient-history-menu/patient-history-menu.component';
import { ClinicalNoteHistoryComponent } from './pages/doctor/clinical-note-history/clinical-note-history.component';
import { GeneralExaminationHistoryComponent } from './pages/doctor/general-examination-history/general-examination-history.component';
import { LabTestHistoryComponent } from './pages/doctor/lab-test-history/lab-test-history.component';
import { RadiologyHistoryComponent } from './pages/doctor/radiology-history/radiology-history.component';
import { ProcedureHistoryComponent } from './pages/doctor/procedure-history/procedure-history.component';
import { PrescriptionHistoryComponent } from './pages/doctor/prescription-history/prescription-history.component';
import { WorkingDiagnosisHistoryComponent } from './pages/doctor/working-diagnosis-history/working-diagnosis-history.component';
import { FinalDiagnosisHistoryComponent } from './pages/doctor/final-diagnosis-history/final-diagnosis-history.component';
import { PharmacyMedicineStockStatusComponent } from './pages/pharmacy/pharmacy-medicine-stock-status/pharmacy-medicine-stock-status.component';
import { LabTestReportComponent } from './pages/laboratory/reports/lab-test-report/lab-test-report.component';
import { LabTestStatisticsReportComponent } from './pages/laboratory/reports/lab-test-statistics-report/lab-test-statistics-report.component';
import { LabSampleCollectionReportComponent } from './pages/laboratory/reports/lab-sample-collection-report/lab-sample-collection-report.component';
import { ShowTimePipe } from './pipes/show_time.pipe';
import { ShowUserPipe } from './pipes/show_user.pipe';
import { LabTestTypePriceComponent } from './pages/admin/insurance-management/prices/lab-test-type-price/lab-test-type-price.component';
import { ShowDateTimePipe } from './pipes/date_time.pipe';
import { RadiologyTypePriceComponent } from './pages/admin/insurance-management/prices/radiology-type-price/radiology-type-price.component';
import { ProcedureTypePriceComponent } from './pages/admin/insurance-management/prices/procedure-type-price/procedure-type-price.component';
import { MedicinePriceComponent } from './pages/admin/insurance-management/prices/medicine-price/medicine-price.component';
import { ConsultationPriceComponent } from './pages/admin/insurance-management/prices/consultation-price/consultation-price.component';
import { RegistrationPriceComponent } from './pages/admin/insurance-management/prices/registration-price/registration-price.component';
import { SearchFilterPipe } from './pipes/search-filter-pipe';
import { WardCategoryComponent } from './pages/admin/medical-units/ward-management/ward-category/ward-category.component';
import { WardTypeComponent } from './pages/admin/medical-units/ward-management/ward-type/ward-type.component';
import { WardComponent } from './pages/admin/medical-units/ward-management/ward/ward.component';
import { WardTypePlanComponent } from './pages/admin/insurance-management/insurance-plan-pricing/ward-type-plan/ward-type-plan.component';
import { WardPlanComponent } from './pages/admin/insurance-management/insurance-plan-pricing/ward-plan/ward-plan.component';
import { WardTypePriceComponent } from './pages/admin/insurance-management/prices/ward-type-price/ward-type-price.component';
import { WardConfigurationComponent } from './pages/admin/medical-units/ward-management/ward-configuration/ward-configuration.component';
import { InpatientPaymentComponent } from './pages/payments/inpatient-payment/inpatient-payment.component';
import { DoctorInpatientListComponent } from './pages/doctor/doctor-inpatient-list/doctor-inpatient-list.component';
import { NurseOutpatientListComponent } from './pages/nurse/nurse-outpatient-list/nurse-outpatient-list.component';
import { NurseInpatientListComponent } from './pages/nurse/nurse-inpatient-list/nurse-inpatient-list.component';
import { NurseOutsiderListComponent } from './pages/nurse/nurse-outsider-list/nurse-outsider-list.component';
import { DoctorInpatientComponent } from './pages/doctor/doctor-inpatient/doctor-inpatient.component';
import { ConsultationReportComponent } from './pages/admin/reports/consultation-report/consultation-report.component';
import { ShowDateOnlyPipe } from './pipes/date.pipe';
import { ProcedureReportComponent } from './pages/admin/reports/procedure-report/procedure-report.component';
import { DoctorToRadiologyReportComponent } from './pages/admin/reports/doctor-to-radiology-report/doctor-to-radiology-report.component';
import { DoctorToLaboratoryReportComponent } from './pages/admin/reports/doctor-to-laboratory-report/doctor-to-laboratory-report.component';
import { NurseComponent } from './pages/admin/personnel/nurse/nurse.component';
import { NurseInpatientChartComponent } from './pages/nurse/nurse-inpatient-chart/nurse-inpatient-chart.component';
import { DressingComponent } from './pages/admin/medical-operations/dressing/dressing.component';
import { ConsumableComponent } from './pages/admin/medical-operations/consumable/consumable.component';
import { PatientDirectInvoicesComponent } from './pages/payments/patient-direct-invoices/patient-direct-invoices.component';
import { PatientInvoiceComponent } from './pages/payments/patient-invoice/patient-invoice.component';
import { PatientInsuranceInvoicesComponent } from './pages/payments/patient-insurance-invoices/patient-insurance-invoices.component';
import { DischargeListComponent } from './pages/payments/discharge-list/discharge-list.component';
import { DeceasedListComponent } from './pages/payments/deceased-list/deceased-list.component';
import { ExternalMedicalProviderComponent } from './pages/admin/medical-units/external-medical-provider/external-medical-provider.component';
import { ReferralListComponent } from './pages/payments/referral-list/referral-list.component';
import { StoreComponent } from './pages/admin/medical-units/store/store.component';
import { SelectStoreComponent } from './pages/store/select-store/select-store.component';
import { LocalPurchaseOrderComponent } from './pages/procurement/local-purchase-order/local-purchase-order.component';
import { SupplierItemPriceListComponent } from './pages/procurement/supplier-item-price-list/supplier-item-price-list.component';
import { GoodsReceivedNoteComponent } from './pages/store/goods-received-note/goods-received-note.component';
import { StorePersonComponent } from './pages/admin/personnel/store-person/store-person.component';
import { StoreItemStockStatusComponent } from './pages/store/store-item-stock-status/store-item-stock-status.component';
import { GoodsReceivedNoteReportComponent } from './pages/store/reports/goods-received-note-report/goods-received-note-report.component';
import { StoreStockCardReportComponent } from './pages/store/reports/store-stock-card-report/store-stock-card-report.component';
import { CashierComponent } from './pages/admin/personnel/cashier/cashier.component';
import { ManagementDashboardComponent } from './pages/management/management-dashboard/management-dashboard.component';
import { ManagementComponent } from './pages/admin/personnel/management/management.component';
import { EmployeeRegisterComponent } from './pages/human-resource/employee-register/employee-register.component';
import { PayrollComponent } from './pages/human-resource/payroll/payroll.component';

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    LoginComponent,
    
    MainPageComponent,
    
    LabPatientListComponent,
   
   
  ],
  imports: [
    HttpClientModule,
    BrowserModule,
    NgxSpinnerModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    SearchFilterPipe
],
  schemas: [ 
    NO_ERRORS_SCHEMA
   ],
  providers: [
    DatePipe,
    ShowTimePipe,
    JwtHelperService,
    SearchFilterPipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
