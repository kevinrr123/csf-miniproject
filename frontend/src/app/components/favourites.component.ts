import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Receipe, receipes } from '../models';
import { ReceipeService } from '../receipe.service';

@Component({
  selector: 'app-favourites',
  templateUrl: './favourites.component.html',
  styleUrls: ['./favourites.component.css']
})
export class FavouritesComponent implements OnInit {

  //subs$!: Subscription
  user!: string
  auth: boolean = false
  reList: Receipe[] = []
  receipeList = receipes
  reIDs: string[] = []
  not: boolean = false

  constructor(private router: Router, private rSvc: ReceipeService, private activatedRoute : ActivatedRoute) {}

  ngOnInit(): void {
    this.auth = !!this.rSvc.getToken();
    if (this.auth) {
      const user = this.rSvc.getUser()
      this.user = user.replace(/"/g, '')
    }else(
      this.router.navigate(['/login'])
    )
    this.getFavListHere()
  }

  getFavListHere() {
    this.rSvc.getFavList(this.user)
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

  remove(a: Receipe){
    
    if(this.auth){
      this.rSvc.removefav(this.user, a.id)
      .then(results => {
        if(results){
          window.location.reload();
        }else{
          alert("Error deleting")
        }})
    }
    else(
      this.router.navigate(['/login'])
    )
  }

}
