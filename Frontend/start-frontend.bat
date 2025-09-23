@echo off
echo ========================================
echo    E-Store Frontend Startup
echo ========================================
echo.

echo Starting Angular Development Server...
echo.

echo Make sure the backend services are running first!
echo Backend startup: cd backend && start-all-services.bat
echo.

echo Starting Angular on http://localhost:4200
echo.

npm start

pause
