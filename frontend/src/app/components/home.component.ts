import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Receipe, receipes } from '../models';
import { ReceipeService } from '../receipe.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  form!: FormGroup
  user!: string
  authenticate: boolean = false
  reList: Receipe[] = []
  receipeList = receipes
  reIDs: string[] = []
  keyword!: string
  not: boolean = false
  search!: string

  constructor(private rSvc: ReceipeService, private router: Router,private activatedRoute : ActivatedRoute) {}

  ngOnInit(): void {
    this.search = this.activatedRoute.snapshot.params['keyword']
    this.searchbykey()
    
  }
  searchbykey() {
    this.keyword = this.search
    this.rSvc.getReceipeList(this.keyword)
      .then(results => {
        this.reList = results
        //console.log("this.reList: ", this.reList)
        for(var d in receipes){
          delete receipes[d]
        }
        this.load()
      }).catch(res => {
        if(res.status === 500){
          this.not = true
          this.reIDs = []
        }
      })
  }

  load() {
    if (this.reList.length > 0) {
      for (var d of this.reList) {
        receipes[d.id] = d
      }
    }
    this.reIDs = []
    for (let k in receipes) {
      this.reIDs.push(k)
    }
  }

  details(a: Receipe){
    this.router.navigate(['/receipedets', a.id], { state: a })
  }
  
}
