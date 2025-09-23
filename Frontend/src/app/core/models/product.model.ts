export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  originalPrice?: number;
  imageUrl: string;
  images?: string[];
  category: string;
  brand: string;
  gender: string;
  size?: string[];
  colors?: string[];
  stockQuantity: number;
  rating: number;
  reviewCount: number;
  isFavorite?: boolean;
  isNew?: boolean;
  onSale?: boolean;
}