# Deployment Guide

## Free Deployment Options

### Option 1: Render + Vercel (Recommended)

#### Backend Deployment (Render)
1. **Sign up at [render.com](https://render.com)**
2. **Connect GitHub repository**
3. **Deploy each service separately:**

   **Eureka Server:**
   - Service Name: `estore-eureka-server`
   - Build Command: `cd backend/eureka-server && mvn clean package`
   - Start Command: `java -jar target/*.jar`
   - Port: 8761

   **API Gateway:**
   - Service Name: `estore-api-gateway`
   - Build Command: `cd backend/api-gateway && mvn clean package`
   - Start Command: `java -jar target/*.jar`
   - Port: 8080

   **User Service:**
   - Service Name: `estore-user-service`
   - Build Command: `cd backend/user-service && mvn clean package`
   - Start Command: `java -jar target/*.jar`
   - Port: 8081

   **Product Service:**
   - Service Name: `estore-product-service`
   - Build Command: `cd backend/product-service && mvn clean package`
   - Start Command: `java -jar target/*.jar`
   - Port: 8082

   **Cart Service:**
   - Service Name: `estore-cart-service`
   - Build Command: `cd backend/cart-service && mvn clean package`
   - Start Command: `java -jar target/*.jar`
   - Port: 8083

   **Order Service:**
   - Service Name: `estore-order-service`
   - Build Command: `cd backend/order-service && mvn clean package`
   - Start Command: `java -jar target/*.jar`
   - Port: 8084

   **Payment Service:**
   - Service Name: `estore-payment-service`
   - Build Command: `cd backend/payment-service && mvn clean package`
   - Start Command: `java -jar target/*.jar`
   - Port: 8085

   **Admin Service:**
   - Service Name: `estore-admin-service`
   - Build Command: `cd backend/admin-service && mvn clean package`
   - Start Command: `java -jar target/*.jar`
   - Port: 8086

4. **Create PostgreSQL Database on Render**
   - Database Name: `estore-db`
   - Add connection URL to each service's environment variables

#### Frontend Deployment (Vercel)
1. **Sign up at [vercel.com](https://vercel.com)**
2. **Connect GitHub repository**
3. **Configure build settings:**
   - Framework: Angular
   - Root Directory: `Frontend`
   - Build Command: `npm run build`
   - Output Directory: `dist/estore`

### Option 2: Fly.io + Netlify

#### Backend Deployment (Fly.io)
1. **Install Fly CLI**
2. **Deploy each service:**
   ```bash
   cd backend/eureka-server
   fly launch
   fly deploy
   ```

#### Frontend Deployment (Netlify)
1. **Sign up at [netlify.com](https://netlify.com)**
2. **Connect GitHub repository**
3. **Configure build settings:**
   - Base directory: `Frontend`
   - Build command: `npm run build`
   - Publish directory: `dist/estore`

## Local Development

### Using Docker Compose
```bash
# Build and start all services
docker-compose up --build

# Stop all services
docker-compose down
```

### Manual Setup
```bash
# Start backend services
cd backend
./start-all-services.bat

# Start frontend
cd Frontend
npm install
npm start
```

## Environment Variables

### Backend Services
- `SPRING_DATASOURCE_URL`: PostgreSQL connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE`: Eureka server URL
- `JWT_SECRET`: JWT signing secret

### Frontend
- `API_BASE_URL`: Backend API gateway URL

## Database Setup

### PostgreSQL Schema
The application will automatically create tables on first run using JPA/Hibernate.

### Sample Data
Initial data will be loaded through DataInitializer classes in each service.

## Monitoring

- **Eureka Dashboard**: `http://eureka-server-url:8761`
- **API Gateway**: `http://api-gateway-url:8080`
- **Swagger UI**: Available on each service at `/swagger-ui.html`

## Troubleshooting

### Common Issues
1. **Services not registering with Eureka**: Check network connectivity and Eureka URL
2. **Database connection issues**: Verify PostgreSQL connection string and credentials
3. **CORS errors**: Ensure API Gateway CORS configuration is correct
4. **JWT token issues**: Verify JWT secret is consistent across all services

### Logs
Check service logs in your deployment platform's dashboard for detailed error information.