export interface User {
    username: string,
    password: string,
    email: string
}

export interface Comment {
    id: string,
    username: string,
    recipename: string,
    comments: string,
    date: String        
}

export type commentList = {
    [key: string]: Comment
}

export const commentss: commentList = {}

export interface Receipe{
    id: string,
    name: string,
    category: string,
    area: string,
    instructions: string,
    imageUrl: string,
    ingredients: string,
    measures: string
    sourceUrl: string
}

export type ReceipeList = {
    [key: string]: Receipe
}

export const receipes: ReceipeList = {}