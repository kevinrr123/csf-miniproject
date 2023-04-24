import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { MaterialModule } from './material.module'; 
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HomeComponent } from './components/home.component';
import { ReceipeService } from './receipe.service';
import { DetailsComponent } from './components/details.component';
import { LoginComponent } from './components/login.component';
import { RandomComponent } from './components/random.component';
import { FavouritesComponent } from './components/favourites.component';
import { FrontPageComponent } from './components/front-page.component';
import { ServiceWorkerModule } from '@angular/service-worker';
import { environment } from '../environments/environment';
import { ContactComponent } from './components/contact.component';
import { GoogleMapsModule } from '@angular/google-maps'

const appRoutes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'frontpage', component: FrontPageComponent},
  { path: 'find/:keyword', component: HomeComponent },
  { path:'receipedets/:id', component: DetailsComponent},
  { path: 'random', component:RandomComponent},
  { path: 'favourites', component:FavouritesComponent},
  { path: 'contact', component:ContactComponent},
  { path: '**', redirectTo: '/', pathMatch: 'full' }
]


@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    FrontPageComponent,
    DetailsComponent,
    RandomComponent,
    FavouritesComponent,
    ContactComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MaterialModule,
    NgbModule,
    RouterModule.forRoot(appRoutes , { useHash : true }),
    BrowserAnimationsModule,
    ServiceWorkerModule.register('ngsw-worker.js', {
      enabled: environment.production,
      // Register the ServiceWorker as soon as the app is stable
      // or after 30 seconds (whichever comes first).
      //registrationStrategy: 'registerWhenStable:30000'
    }),
    // AgmCoreModule.forRoot({
    //   apiKey: ''
    // })
    GoogleMapsModule
    
  ],
  providers: [ReceipeService],
  bootstrap: [AppComponent]
})
export class AppModule { }
