import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Receipe, receipes } from '../models';
import { ReceipeService } from '../receipe.service';

@Component({
  selector: 'app-random',
  templateUrl: './random.component.html',
  styleUrls: ['./random.component.css']
})
export class RandomComponent implements OnInit {

  user!: string
  auth: boolean = false
  reList: Receipe[] = []
  receipeList = receipes
  reIDs: string[] = []
  not: boolean = false
  id!: string

  constructor(private router: Router, private rSvc: ReceipeService, private activatedRoute : ActivatedRoute) {}

  ngOnInit(): void {
    this.auth = !!this.rSvc.getToken();
    
    if (this.auth) {
      const user = this.rSvc.getUser()
      this.user = user.replace(/"/g, '')
    }else(
      this.router.navigate(['/login'])
    )
    this.getRandom()
  }

  getRandom(){
    this.rSvc.getRandomReceipe()
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

  addtofav(username: string,a : any){
    console.log(username, a)
    if(this.auth){
      this.rSvc.addtofavourites(username, a)
    }
    else(
      this.router.navigate(['/login'])
    )
  }

}
