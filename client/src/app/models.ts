// You may use this file to create any models
export interface Item {
    id: string;
    name: string;
    price: number;
    description: string;
}

export interface Cart {
    id: string;
    name: string;
    unitPrice: number;
    price: number;
    quantity: number;
}

export interface Clean {
    id: string;
    price: number;
    quantity: number;
}

export interface User {
    username: String;
    password: String;
}