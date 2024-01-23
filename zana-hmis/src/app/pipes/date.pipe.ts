import { Pipe, PipeTransform } from '@angular/core';
@Pipe({
    standalone : true,
    name: 'dateOnly'
})
export class ShowDateOnlyPipe implements PipeTransform {

    transform(value: Date): string {

        var str = value.toString()
        var str1 = str.substring(0, str.length - 9)
        var str2 = str.substring(str.length - 9, str.length)

        str1 = str1.replaceAll(',','-')
        str2 = str2.replaceAll(',',':')

        //return str1 + str2
        str = str.replace(',','-').replace(',','-').replace(',',':').replace(',',':').replace(',',':')

        var i = str.indexOf(':')

        str = str.substring(0, i)

        return str
        
    }
}