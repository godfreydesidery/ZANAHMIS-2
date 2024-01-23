import { IConsultation } from "./consultation"
import { IDay } from "./day"
import { IUser } from "./user"

export interface IClinicalNote {
    id                      : any
    mainComplain            : string
    drugsAndAllergyHistory  : string
    familyAndSocialHistory  : string
    pastMedicalHistory      : string
    presentIllnessHistory   : string
    physicalExamination     : string
    reviewOfOtherSystems    : string
    managementPlan          : string

    consultation            :IConsultation
    
    created       : string
}