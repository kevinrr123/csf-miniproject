import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom, Observable, Observer, Subject } from "rxjs";
import {User,Comment} from "./models"


const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';

@Injectable()

export class ReceipeService{

    httpSet = {
        headers: new HttpHeaders()
            .set('Content-Type', 'application/json')
            .set('Authorization', 'Bearer ' + this.getToken())
    };

    uploadHttp = {
        headers: new HttpHeaders()
            .set('Authorization', 'Bearer ' + this.getToken())
    };

    currentUser = new Subject<any>()

    constructor(private http: HttpClient) { }

    //for users
    public saveToken(token: string): void {
        window.sessionStorage.removeItem(TOKEN_KEY);
        window.sessionStorage.setItem(TOKEN_KEY, token);
    }

    public getToken(): any {
        return sessionStorage.getItem(TOKEN_KEY);
    }

    public saveUser(user: string): void {
        window.sessionStorage.removeItem(USER_KEY);
        window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
    }

    public getUser(): any {
        this.currentUser.next(sessionStorage.getItem(USER_KEY))
        return sessionStorage.getItem(USER_KEY);
    }

    register(user: User) {
        return firstValueFrom(
            this.http.post<any>('/api/register', { username: user.username, password: user.password, email: user.email, headers: this.httpSet })
        )
    }

    authenticate(user: User): Promise<any> {
        return firstValueFrom(
            this.http.post<any>('/authenticate', { username: user.username, password: user.password })
        )
    }

    signOut(): void {
        window.sessionStorage.clear();
    }

    getReceipeList(search: string){
        const params = new HttpParams()
            .set('search', search)
        return firstValueFrom(
            this.http.get<any>('/api/searchRecipe', { params })
        )
    }

    getRandomReceipe(){
        return firstValueFrom(
            this.http.get<any>('/api/randomReceipe')
        )
    }

    getDetails(id: string){
        const params = new HttpParams()
            .set('id', id)
        return firstValueFrom(
            this.http.get<any>('/api/getById', { params })
        )
    }

    addtofavourites(username: string, id: string): Promise<any> {
        return firstValueFrom(
            this.http.put<any>('/api/addtofavourites', { username: username, id: id}, this.httpSet)
        )
    }

    getFavList(username: string){
        const params = new HttpParams()
            .set('username', username)
        const httpOp = new HttpHeaders()
            .set('Authorization', 'Bearer ' + this.getToken())
        return firstValueFrom(
            this.http.get<any>('/api/favourites', { params:params,headers:httpOp })
        )
    }

    removefav(username: string, id: string): Promise<any>{
        return firstValueFrom(
            this.http.post<any>('/api/deletefavourites', { username: username, id: id}, this.httpSet)
        )
    }

    addtoTried(id:string,username: string,recipeName:string,comments:string): Promise<any> {
        //console.log(comments)
        return firstValueFrom(
            this.http.post<any>('/api/addtoTried', {recipeid:id, username: username, recipeName: recipeName,comments:comments }, this.httpSet)
        )
    }

    getTriedList(username: string){
        const params = new HttpParams()
            .set('username', username)
        const httpOp = new HttpHeaders()
            .set('Authorization', 'Bearer ' + this.getToken())
        return firstValueFrom(
            this.http.get<any>('/api/tried', { params:params, headers: httpOp })
        )
    }

    removeComment(username: string,recipeName:string): Promise<any>{        
        //console.log(recipeName)
        return firstValueFrom(
            this.http.post<any>('/api/delTried', {username: username, recipeName: recipeName}, this.httpSet)
        )
    }

    sendEmail(workbook: Blob) {
        const data = new FormData()
        data.set('file', workbook)
        return firstValueFrom(
            this.http.post<any>('/api/email/send', data, this.uploadHttp)
        )
    }
}