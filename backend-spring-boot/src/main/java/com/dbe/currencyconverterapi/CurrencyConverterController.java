package com.dbe.currencyconverterapi;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/convert")
@CrossOrigin(origins = "http://localhost:3000")
public class CurrencyConverterController {

    private static final String EXCHANGE_API_BASE_URL = "https://v6.exchangerate-api.com/v6/";
    private static final String SUCCESS_STATUS = "success";
    private static final String RESULT_FIELD = "result";
    private static final String CONVERSION_RESULT_FIELD = "conversion_result";
    private static final String ERROR_TYPE_FIELD = "error-type";

    @Value("${exchange.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;


    public CurrencyConverterController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public double convertCurrency(@RequestParam String from,
                                  @RequestParam String to,
                                  @RequestParam double amount) {

        validateInputParameters(from, to, amount);
        
        String apiUrl = buildApiUrl(from, to, amount);
        Map<String, Object> response = callExchangeRateApi(apiUrl);
        
        return extractConversionResult(response);
    }

    private void validateInputParameters(String from, String to, double amount) {
        if (from == null || from.trim().isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Source currency (from) parameter is required"
            );
        }
        
        if (to == null || to.trim().isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Target currency (to) parameter is required"
            );
        }
        
        if (amount <= 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Amount must be greater than zero"
            );
        }
    }

    private String buildApiUrl(String from, String to, double amount) {
        return EXCHANGE_API_BASE_URL + apiKey + "/pair/" + 
               from.toUpperCase() + "/" + to.toUpperCase() + "/" + amount;
    }

@SuppressWarnings("unchecked")
private Map<String, Object> callExchangeRateApi(String url) {
    try {
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        
        if (response == null) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Received empty response from currency exchange service"
            );
        }
        
        return response;
        
    } catch (ResourceAccessException | HttpClientErrorException e) {
        throw new ResponseStatusException(
            HttpStatus.SERVICE_UNAVAILABLE,
            "Currency exchange service is temporarily unavailable. Please try again later.",
            e
        );
    } catch (HttpServerErrorException e) {
        throw new ResponseStatusException(
            HttpStatus.BAD_GATEWAY,
            "Currency exchange service is experiencing technical difficulties. Please try again later.",
            e
        );
    } catch (RestClientException e) {
        throw new ResponseStatusException(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Failed to communicate with currency exchange service.",
            e
        );
    }
}
    private double extractConversionResult(Map<String, Object> response) {
        Object resultStatus = response.get(RESULT_FIELD);
        
        if (!SUCCESS_STATUS.equals(resultStatus)) {
            handleApiError(response);
        }
        
        Object conversionResult = response.get(CONVERSION_RESULT_FIELD);
        return validateAndExtractNumber(conversionResult);
    }

    private void handleApiError(Map<String, Object> response) {
        Object errorType = response.get(ERROR_TYPE_FIELD);
        String errorMessage = "Currency conversion failed";
        
        if (errorType != null) {
            errorMessage += ". Error: " + errorType.toString();
            System.err.println("ExchangeRate-API Error: " + errorType);
        }
        
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
    }

    private double validateAndExtractNumber(Object conversionResult) {
        if (conversionResult == null) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Currency service returned null conversion result"
            );
        }
        
        if (conversionResult instanceof Number numberResult) {
            double result = numberResult.doubleValue();
            
            if (result < 0) {
                throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Currency service returned invalid negative conversion result"
                );
            }
            
            return result;
        }
        
        throw new ResponseStatusException(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Invalid conversion result format. Expected number, got: " + 
            conversionResult.getClass().getSimpleName()
        );
    }
}