// Check Order interface

export interface Order {
  id?: number;
  userId: number;
  items: OrderItem[];
  status: 'pending' | 'processing' | 'completed' | 'cancelled' | 
          'PENDING' | 'PROCESSING' | 'COMPLETED' | 'CANCELLED' |
          'paid' | 'PAID' | 'confirmed' | 'CONFIRMED' |
          'shipped' | 'SHIPPED' | 'delivered' | 'DELIVERED' |
          'failed' | 'FAILED';
  total?: number;
  amount?: number;
  totalPrice?: number;
  orderDate?: string;
  createdAt?: Date;
  updatedAt?: Date;
  
  // Helper method to get total
  getTotal?(): number;
}

export interface OrderItem {
  id?: number;
  productId: number;
  quantity: number;
  price: number;
}