import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ReceipeService } from './receipe.service';
import { Subscription } from 'rxjs';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Receipe, receipes } from './models';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'frontend';
  
  form!: FormGroup
  subs$!: Subscription
  user!: string
  authenticate: boolean = false
  reList: Receipe[] = []
  receipeList = receipes
  reIDs: string[] = []
  keyword!: string
  not: boolean = false

  constructor(private rSvc: ReceipeService, private router: Router, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.checkUser()
    this.form = this.createForm()
  }

  createForm() {
    return this.fb.group({
      search: this.fb.control<string>('')
    })
  }


  checkUser() {
    this.authenticate = !!this.rSvc.getToken();
    if (this.authenticate) {
      const user = this.rSvc.getUser();
      this.user = user.replace(/"/g, '');
    }
  }

  searchbykey() {
    this.keyword = this.form.value['search']
    this.router.navigate(['/find', this.keyword]).then(() => window.location.reload())
  }

  details(a: Receipe){
    this.router.navigate(['/receipedets', a.id], { state: a })
  }
  

  signout() {
    this.rSvc.signOut()
    this.router.navigate(['/'])
    .then(() => window.location.reload())
  }

  random(){
    this.router.navigate(['/random']).then(() => window.location.reload())
  }

}

