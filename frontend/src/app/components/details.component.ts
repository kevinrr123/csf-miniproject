import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ReceipeService } from '../receipe.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Comment, Receipe, receipes } from '../models';
import { CommaExpr } from '@angular/compiler';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css']
})
export class DetailsComponent implements OnInit {
  
  form!: FormGroup
  data!: any;
  routeState!: any;
  auth: boolean = false
  user!: string
  boolfav: boolean = false
  booladd: boolean = false
  id!: string
  name!: string
  username!: string
  recipeName!: string
  comments!: string
  recipeid!: string
  reList: Receipe[] = []
  receipeList = receipes
  reIDs: string[] = []

  constructor(private router: Router, private rSvc: ReceipeService, private activatedRoute : ActivatedRoute, private fb: FormBuilder) {
  //   if (this.router.getCurrentNavigation()?.extras.state) {
  //     this.data = this.router.getCurrentNavigation()?.extras.state;
  //     console.log(this.data)
  // }
}
  
  ngOnInit(): void {
    this.id = this.activatedRoute.snapshot.params['id']
    this.auth = !!this.rSvc.getToken();

    if (this.auth) {
      const user = this.rSvc.getUser()
      this.user = user.replace(/"/g, '')
    }else(
      this.router.navigate(['/login'])
    )
    this.getById()
    this.form = this.createForm()
  }

  getById(){
    console.log("a", this.id)
    this.rSvc.getDetails(this.id)
    .then(results => {
      this.reList = results
      //console.log("this.reList: ", this.reList)
      for(var d in receipes){
        delete receipes[d]
      }
      this.load()
    }).catch(res => {
      if(res.status === 500){
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

  createForm() {
    return this.fb.group({
      id: this.id,
      username: this.user,
      comments: this.fb.control<string>('')
    })
  }

  addtofav(a : any){
    if(this.auth){
      this.rSvc.addtofavourites(this.user, this.id)
      .then(results => {
        if(results){
        this.boolfav = true
        }else{
          this.boolfav= false
        }})
    }
    else(
      this.router.navigate(['/login'])
    )
  }

  addtoT(a : any){
    const comments = this.form.value as Comment
    this.recipeid = comments.id
    this.username = comments.username
    this.recipeName = a
    this.comments = comments.comments
    console.log(this.username)
    if(this.auth){
      this.rSvc.addtoTried(this.id,this.username,this.recipeName,this.comments)
      .then(results => {
        if(results){
          this.router.navigate(['/frontpage'])
        }else{
          this.booladd= false
        }})
    }
    else(
      this.router.navigate(['/login'])
    )
  }

}
