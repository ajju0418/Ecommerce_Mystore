# Ecommerce MyStore

A full-stack e-commerce application built with Spring Boot microservices backend and Angular frontend.

## Architecture

### Backend (Microservices)
- **API Gateway** - Routes requests and handles authentication
- **Eureka Server** - Service discovery
- **User Service** - User management and authentication
- **Product Service** - Product catalog management
- **Cart Service** - Shopping cart functionality
- **Order Service** - Order processing
- **Payment Service** - Payment processing
- **Admin Service** - Administrative functions

### Frontend
- **Angular** - Modern web application framework
- **TypeScript** - Type-safe JavaScript
- **Responsive Design** - Mobile-friendly interface

## Getting Started

### Prerequisites
- Java 17+
- Node.js 18+
- Maven 3.6+
- Angular CLI

### Running the Application

#### Backend Services
```bash
cd backend
# Start all services using the batch file
start-all-services.bat
```

#### Frontend
```bash
cd Frontend
npm install
npm start
```

The application will be available at:
- Frontend: http://localhost:4200
- API Gateway: http://localhost:8080
- Eureka Server: http://localhost:8761

## Features

- User registration and authentication
- Product browsing and search
- Shopping cart management
- Order placement and tracking
- Payment processing
- Admin panel for product and order management
- Responsive design for mobile and desktop

## Technology Stack

### Backend
- Spring Boot
- Spring Cloud Gateway
- Spring Security
- Spring Data JPA
- Netflix Eureka
- JWT Authentication
- Maven

### Frontend
- Angular 18
- TypeScript
- RxJS
- Angular Material (if used)
- Bootstrap (if used)

## API Documentation

The API documentation is available through Swagger UI when the services are running.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.