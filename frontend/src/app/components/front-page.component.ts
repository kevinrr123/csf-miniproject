import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { Comment, commentss } from '../models';
import { ActivatedRoute, Router } from '@angular/router';
import { ReceipeService } from '../receipe.service';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-front-page',
  templateUrl: './front-page.component.html',
  styleUrls: ['./front-page.component.css']
})
export class FrontPageComponent implements OnInit {

  user!: string
  auth: boolean = false
  cList: Comment[] = []
  commentList = commentss
  cIDs: string[] = []
  not: boolean = false
  @ViewChild('htmlData') htmlData!: ElementRef;

  constructor(private router: Router, private rSvc: ReceipeService, private activatedRoute : ActivatedRoute,private alert: MatSnackBar) {}

  ngOnInit(): void {
    this.auth = !!this.rSvc.getToken();

    if (this.auth) {
      const user = this.rSvc.getUser()
      this.user = user.replace(/"/g, '')
    }else(
      this.router.navigate(['/login'])
    )
    this.getListHere()
  }

  getListHere() {
    this.rSvc.getTriedList(this.user)
      .then(results => {
        this.cList = results
        console.log("this.cList: ", this.cList)
        for(var d in commentss){
          delete commentss[d]
        }
        this.loaded()
      }).catch(res => {
        if(res.status === 500){
          this.not = true
          this.cIDs = []
        }
      })
  }

  loaded() {
    if (this.cList.length > 0) {
      for (var d of this.cList) {
        commentss[d.recipename] = d
      }
    }
    this.cIDs = []
    for (let k in commentss) {
      this.cIDs.push(k)
    }
  }

  edit(a: Comment){
    console.log("a", a)
    this.router.navigate(['/receipedets', a.id], { state: a })
  }

  downloadAsPDF() {
    let DATA: any = document.getElementById('htmlData');
    html2canvas(DATA).then((canvas) => {
      let fileWidth = 208;
      let fileHeight = (canvas.height * fileWidth) / canvas.width;
      const FILEURI = canvas.toDataURL('image/png');
      let PDF = new jsPDF('p', 'mm', 'a4');
      let position = 0;
      PDF.addImage(FILEURI, 'PNG', 0, position, fileWidth, fileHeight);
      //PDF.save('angular-demo.pdf');
      var blob = new Blob([PDF.output('blob')], { type: 'application/pdf' });
      this.rSvc.sendEmail(blob)
      .then((results) => {
        this.alert.open('Email Sent Successfully.', 'X', 
                     {duration: 3000,
                      verticalPosition: 'top',
                      panelClass: ['green-snackbar']})
      }).catch((res)=> {
        this.alert.open('Error.', 'Please try again.', 
                     {duration: 3000,
                      verticalPosition: 'top',
                      panelClass: ['red-snackbar']})
      })
      }).catch((results)=> {
        this.alert.open('Error.', 'Please try again.', 
                     {duration: 3000,
                      verticalPosition: 'top',
                      panelClass: ['red-snackbar']})
      })
  }

  removeOne(a: Comment){
    console.log("a", a)
    if(this.auth){
      this.rSvc.removeComment(this.user, a.recipename)
      .then(results => {
        if(results){
          window.location.reload();
        }else{
          this.alert.open('Erro Removing.', 'Please try again.', 
                     {duration: 3000,
                      verticalPosition: 'top',
                      panelClass: ['red-snackbar']})
        }})
    }
    else(
      this.router.navigate(['/login'])
    )
  }

}
