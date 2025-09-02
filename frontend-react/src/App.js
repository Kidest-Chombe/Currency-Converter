import React, { useState } from 'react';
import axios from 'axios';
import './App.css';

function App() {
  const [formData, setFormData] = useState({ from: 'USD', to: 'ETB', amount: 100 });
  const [result, setResult] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setResult('');
    setError('');

    try {
      const response = await axios.get(`http://localhost:8080/api/convert`, {
        params: formData
      });
      setResult(`${formData.amount} ${formData.from} = ${response.data.toLocaleString()} ${formData.to}`);
    } catch (error) {
      console.error("Conversion error:", error);
      setError("Failed to convert. Please ensure the backend is running on port 8080 and check the console for details.");
    } finally {
      setIsLoading(false);
    }
  };

  const currencies = ['ETB', 'USD', 'EUR', 'GBP', 'CNY', 'JPY', 'CAD'];

  return (
    <div className="App">
      
      <header className="main-header">
        <div className="header-content">
             <img src="https://tse1.mm.bing.net/th/id/OIP.4IejVhnSMEcsFRO1N9vp9QHaHa?r=0&rs=1&pid=ImgDetMain&o=7&rm=3" alt="DBE Logo" class="logo"/>

          <div className="header-titles">
            <h1>Development Bank of Ethiopia</h1>
            <p className="slogan">Your Development Partner!</p> 
            
          </div>
        </div>
      </header>

      <div className="converter-container">
        <h2>International Currency Converter</h2> 
        <form onSubmit={handleSubmit} className="converter-form">
          
          <div className="input-group">
            <label>Amount:</label>
            <input 
              type="number" 
              name="amount" 
              value={formData.amount} 
              onChange={handleChange} 
              step="any"
              required 
            />
          </div>

          <div className="input-group">
            <label>From:</label>
            <select name="from" value={formData.from} onChange={handleChange}>
              {currencies.map(currency => <option key={currency} value={currency}>{currency}</option>)}
            </select>
          </div>

          <div className="input-group">
            <label>To:</label>
            <select name="to" value={formData.to} onChange={handleChange}>
              {currencies.map(currency => <option key={currency} value={currency}>{currency}</option>)}
            </select>
          </div>

          <button type="submit" disabled={isLoading}>
            {isLoading ? 'Converting...' : 'Convert'}
          </button>
        </form>

        {error && <div className="error-message">{error}</div>}
        {result && <div className="result-display">{result}</div>}
        
      </div>

      <footer class="footer">
  <div class="container">
    <div class="footer-content">
      <div class="footer-logo">
        <p>Development Bank of Ethiopia</p>
      </div>
      <div class="footer-links">
        <a href="https://dbe.com.et/?page_id=6885">Privacy Policy</a>
        <a href="https://dbe.com.et/?stm_works=terms-and-tariffs#">Terms of Service</a>
        <a href="https://dbe.com.et/?page_id=6598">Contact Us</a>
      </div>
    </div>
    <p class="copyright">© 2025 Development Bank of Ethiopia. All rights reserved.</p>
  </div>
</footer>
    </div>
  );
}

export default App;