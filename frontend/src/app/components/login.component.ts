import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from '../models';
import { ReceipeService } from '../receipe.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  user!: string
  auth: boolean = false
  loginForm! : FormGroup
  registerForm! : FormGroup
  displayregAccount : boolean = false
  dialogLoginFail: Boolean = false
  dialogRegFail: Boolean = false
  doneReg: Boolean = false

  constructor(private fb: FormBuilder, private rSvc: ReceipeService, private router: Router,private alert: MatSnackBar) { }

  ngOnInit(): void {
    this.auth = !!this.rSvc.getToken();
    if (this.auth) {
      this.router.navigate(['/frontpage'])
    }
    this.loginForm = this.fb.group({
      username: this.fb.control<string>('', [Validators.required, Validators.minLength(5)]),
      password: this.fb.control<string>('', [Validators.required, Validators.minLength(5)])
    })
    this.registerForm = this.fb.group({
      username: this.fb.control<string>('', [Validators.required, Validators.minLength(5)]),
      password: this.fb.control<string>('', [Validators.required, Validators.minLength(5)]),
      email: this.fb.control<string>('', [Validators.required, Validators.email])
    })
  }

  flipPage() : void{
    this.displayregAccount = !this.displayregAccount
  }

  login() {
    const login = this.loginForm.value as User
    this.rSvc.authenticate(login)
    .then(results => {
      //console.log("details", results)
      this.rSvc.saveToken(results.token);
      this.rSvc.saveUser(login.username);
      this.alert.open('Signed In!', 'X', {duration: 1000})
      this.router.navigate(['/frontpage'])
      .then(()=> {
        window.location.reload();
      })
    }).catch(results=> {
      console.log(results)
      this.alert.open('Login Failed. Check your username and password', 'X', 
                     {duration: 3000,
                      verticalPosition: 'top',
                      panelClass: ['red-snackbar']})
    })
  }
  register() {
    const register = this.registerForm.value as User
    this.rSvc.register(register)
    .then(results => {
      //console.log("registered", results)
      this.router.navigate(['/login'])
      this.alert.open("Account created! Please login.", 'X', {duration: 3000,
                                                              verticalPosition: 'top',
                                                              panelClass: ['green-snackbar']})
    }).catch(results=> {
      console.log(results)
      this.alert.open('User Credentials already exist.', 'Please try using Different Credentials.', 
                     {duration: 3000,
                      verticalPosition: 'top',
                      panelClass: ['red-snackbar']})
    })
  }


}
