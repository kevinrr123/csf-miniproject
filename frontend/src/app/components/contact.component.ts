import { Component,ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ContactComponent {

  mapOptions: google.maps.MapOptions = {
    //disableDefaultUI: true,
    center: { lat: 1.3728468167772283, lng: 103.89374395361179 },
    zoom : 14
 }
 marker = {
    position: { lat: 1.3728468167772283, lng: 103.89374395361179 },
 }
 
}
