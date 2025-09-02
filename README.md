# International Currency Converter

A professional foreign exchange currency converter API. This Spring Boot application provides real-time currency conversion between major international currencies.


## Features
- **Real-time Conversion:** Convert between ETB, USD, EUR, GBP, and other major currencies.
- **Secure Architecture:** Backend handles all API calls, keeping sensitive keys safe.
- **Mobile-Responsive:** Perfectly responsive on all devices - desktop, tablet, and mobile.
- **Professional UI:** Styled with DBE's official brand colors and logo.
- **Error Handling:** Comprehensive error handling with user-friendly messages.

## Architecture

This project follows a client-server architecture:

### Frontend (Client)
- **React.js** - Modern UI library for building user interfaces
- **Axios** - For making HTTP requests to the backend API
- **CSS3** - Responsive styling with Flexbox and Media Queries

### Backend (Server)
- **Spring Boot** - Robust Java-based backend framework
- **Spring Security** - Basic security configuration
- **RESTful API** - Clean API endpoint for currency conversion

## Screenshot

!\[Screenshot](image.png)

## Getting Started

### Prerequisites

- Java 17 or higher
- Node.js 14 or higher
- Maven
- ExchangeRate-API account ([Get free API key](https://www.exchangerate-api.com/))

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/dbe-currency-converter.git
   cd dbe-currency-converter

2. **Backend Setup (Spring Boot)**
   cd backend
   mvn spring-boot:run

   Server runs on http://localhost:8080

3. **Frontend Setup (React)**
   cd frontend
   npm install
   npm start

   Client runs on http://localhost:3000

## Usage

Open your browser to http://localhost:3000
Enter the amount to convert
Select source currency (e.g., USD)
Select target currency (e.g., ETB)
Click "Convert" to see results
View formatted conversion result

## UI Features
Brand Compliance: Uses DBE's official green (#00a651) and gold (#e5b833) colors
Responsive Design: Adapts to desktop, tablet, and mobile screens
User-Friendly: Intuitive form with dropdown selectors
Loading States: Visual feedback during conversion
Error Handling: Clear error messages for failed requests

## Security
API keys stored securely on backend server
CORS configured for controlled cross-origin requests
Input validation on both client and server side
No sensitive data exposed to client

## License
This project is developed for educational purposes as part of the Development Bank of Ethiopia internship program.