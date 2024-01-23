import { Pipe, PipeTransform } from '@angular/core';
@Pipe({
    standalone : true,
    name: 'age'
})
export class AgePipe implements PipeTransform {

        /**
     * Format given date/dob to the age.
     * @param date - Given date to be converted to the dob and age
     * @returns - age string
     */
    transform(dob: Date): string {

        const today: Date = new Date();
        const birthDate: Date = new Date(dob);

        let years = 0
        let months = 0
        let days = 0

        let age : string = ''

        let todayYears : number = today.getFullYear()
        let todayMonth : number = today.getMonth()
        let todayDay : number = today.getDate()

        let agoYears : number = birthDate.getFullYear()
        let agoMonth : number = birthDate.getMonth()
        let agoyDay : number = birthDate.getDate()

        if(todayDay < agoyDay){
            todayMonth = todayMonth - 1
            days = 30 + todayDay - agoyDay
        }else{
            days = todayDay - agoyDay
        }
        if(todayMonth < agoMonth){
            todayYears = todayYears - 1
            months = 12 + todayMonth - agoMonth
        }else{
            months = todayMonth - agoMonth
        }
        years = todayYears - agoYears

        age = years.toString() + ' Years ' + months.toString() + ' Months ' + days.toString() + 'days'
        
        return age
    }
}