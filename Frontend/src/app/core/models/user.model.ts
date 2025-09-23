export interface User {
    id?: number; // Optional for registration
  username: string;
  email: string;
  phone: string;
  gender: string;
  password: string;
  token?: string;

}
