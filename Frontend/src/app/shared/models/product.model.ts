// Check Product interface

export interface Product {
  id?: number;
  name: string;
  description: string;
  price: number;
  category: string;
  imageUrl?: string;
  brand?: string;
  stock?: number;
  image?: string;
  status?: string;
  createdAt?: Date;
  updatedAt?: Date;
}