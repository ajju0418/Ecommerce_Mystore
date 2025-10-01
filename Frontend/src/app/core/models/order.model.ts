export interface Order {
  id: string;
  orderId: string;
  userId: number;
  userName?: string;
  userEmail?: string;
  userPhone?: string;
  totalAmount: number;
  status: string;
  createdAt?: string;
  updatedAt?: string;
  // Add other fields as needed
}
