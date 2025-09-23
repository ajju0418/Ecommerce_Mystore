@echo off
echo ========================================
echo    E-Store Microservices Startup
echo ========================================
echo.

echo [1/8] Starting Eureka Server...
start "Eureka Server" cmd /k "cd eureka-server && mvn spring-boot:run"
echo Waiting for Eureka Server to start...
timeout /t 45 /nobreak >nul

echo [2/8] Starting API Gateway...
start "API Gateway" cmd /k "cd api-gateway && mvn spring-boot:run"
echo Waiting for API Gateway to register...
timeout /t 20 /nobreak >nul

echo [3/8] Starting User Service...
start "User Service" cmd /k "cd user-service && mvn spring-boot:run"
timeout /t 5 /nobreak >nul

echo [4/8] Starting Product Service...
start "Product Service" cmd /k "cd product-service && mvn spring-boot:run"
timeout /t 5 /nobreak >nul

echo [5/8] Starting Cart Service...
start "Cart Service" cmd /k "cd cart-service && mvn spring-boot:run"
timeout /t 5 /nobreak >nul

echo [6/8] Starting Order Service...
start "Order Service" cmd /k "cd order-service && mvn spring-boot:run"
timeout /t 5 /nobreak >nul

echo [7/8] Starting Payment Service...
start "Payment Service" cmd /k "cd payment-service && mvn spring-boot:run"
timeout /t 5 /nobreak >nul

echo [8/8] Starting Admin Service...
start "Admin Service" cmd /k "cd admin-service && mvn spring-boot:run"

echo.
echo ========================================
echo    All services are starting...
echo ========================================
echo.
echo Service URLs:
echo   Eureka Dashboard: http://localhost:8761
echo   API Gateway:      http://localhost:9091
echo.
echo Frontend URLs:
echo   Angular App:      http://localhost:4200
echo.
echo Health Check URLs:
echo   API Gateway:      http://localhost:9091/actuator/health
echo   User Service:     http://localhost:9091/api/users/health
echo   Product Service:  http://localhost:9091/api/products/health
echo   Cart Service:     http://localhost:9091/api/cart/health
echo   Order Service:    http://localhost:9091/api/orders/health
echo   Payment Service:  http://localhost:9091/api/payments/health
echo.
echo Note: Wait 2-3 minutes for all services to fully start
echo       Check Eureka dashboard to verify all services are UP
echo.
pause