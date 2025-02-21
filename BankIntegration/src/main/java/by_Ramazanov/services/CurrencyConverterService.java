package by_Ramazanov.services;

import by_Ramazanov.DTO.ConversionResult;
import by_Ramazanov.entity.ExchangeRate;
import by_Ramazanov.repositories.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyConverterService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final RestTemplate restTemplate;

    @Value("${twelvedata.api.url}")
    private String apiUrl;

    @Value("${twelvedata.api.key}")
    private String apiKey;

    private static final String USD_CURRENCY_PAIR_SUFFIX = "/USD";

    public ConversionResult convertCurrency(BigDecimal amount, String currency) {
        String currencyPair = currency + USD_CURRENCY_PAIR_SUFFIX;
        ExchangeRate exchangeRate = isLocalCurrency(currency) ?
                getExchangeRateFromDatabase(currencyPair) :
                getExchangeRateFromExternalApi(currency);

        BigDecimal convertedAmount = amount.multiply(exchangeRate.getRate())
                .setScale(2, RoundingMode.HALF_UP);

        return ConversionResult.builder()
                .convertedAmount(convertedAmount)
                .rate(exchangeRate.getRate())
                .build();
    }

    private boolean isLocalCurrency(String currency) {
        return currency.equals("KZT") || currency.equals("RUB");
    }

    private ExchangeRate getExchangeRateFromDatabase(String currencyPair) {
        return exchangeRateRepository.findBySymbol(currencyPair)
                .orElseThrow(() -> {
                    log.error("Курс обмена не найден в базе данных для валютной пары: {}", currencyPair);
                    return new IllegalArgumentException("Курс обмена не найден в базе данных для " + currencyPair);
                });
    }

    private ExchangeRate getExchangeRateFromExternalApi(String currency) {
        String urlString = String.format("%s?symbol=%s&apikey=%s", apiUrl, currency, apiKey);

        try {
            ExchangeRate exchangeRate = restTemplate.getForObject(urlString, ExchangeRate.class);
            validateExchangeRate(exchangeRate, currency);
            return exchangeRate;
        } catch (Exception e) {
            log.error("Ошибка при получении курса обмена из внешнего API для валюты: {}", currency, e);
            throw new IllegalArgumentException("Ошибка при получении курса обмена для " + currency + USD_CURRENCY_PAIR_SUFFIX + ": " + e.getMessage());
        }
    }

    private void validateExchangeRate(ExchangeRate exchangeRate, String currency) {
        if (exchangeRate == null || exchangeRate.getRate() == null || exchangeRate.getRate().compareTo(BigDecimal.ZERO) == 0) {
            log.warn("Недопустимый или отсутствующий курс обмена из внешнего API для валюты: {}", currency);
            throw new IllegalArgumentException("Курс обмена не найден для " + currency + USD_CURRENCY_PAIR_SUFFIX);
        }
    }

}