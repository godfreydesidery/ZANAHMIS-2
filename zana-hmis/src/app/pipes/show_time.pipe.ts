
import { Pipe, PipeTransform } from '@angular/core';
@Pipe({
    name: 'showTime',
    standalone : true
})
export class ShowTimePipe implements PipeTransform {

    transform(value: String | undefined): string {

        return value!.substring(0, value!.indexOf('|'))
    }
}