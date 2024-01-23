import { ILabTest } from "./lab-test"

export interface ILabTestAttachment{
    id : any

    name : string
    fileName : string

    labTest : ILabTest
}