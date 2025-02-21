package by_Ramazanov.services;

import by_Ramazanov.entity.ExchangeRate;
import by_Ramazanov.repositories.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final RestTemplate restTemplate;

    @Value("${twelvedata.api.url}")
    private String apiUrl;

    @Value("${twelvedata.api.key}")
    private String apiKey;

    private static final String RUB_USD_SYMBOL = "RUB/USD";
    private static final String KZT_USD_SYMBOL = "KZT/USD";

    @Scheduled(cron = "0 0 11 * * ?") // Обновление курса в 11:00
    @Scheduled(cron = "0 50 23 * * ?") // Закрытие торгов в 23:50
    public void updateExchangeRates() {
        updateExchangeRate(RUB_USD_SYMBOL);
        updateExchangeRate(KZT_USD_SYMBOL);
    }

    private void updateExchangeRate(String symbol) {
        log.debug("Начинаем обновление курса обмена для валютной пары: {}", symbol);
        try {
            ExchangeRate newExchangeRate = fetchExchangeRateFromApi(symbol);
            log.debug("Курс обмена для валютной пары {} успешно получен из API", symbol);

            exchangeRateRepository.findBySymbol(symbol)
                    .ifPresentOrElse(
                            existingRate -> updateExistingRate(existingRate, newExchangeRate),
                            () -> createNewRate(newExchangeRate)
                    );
            log.info("Курс обмена для валютной пары {} успешно обновлен.", symbol);
        } catch (Exception e) {
            log.error("Ошибка при обновлении курса обмена для валютной пары: {}", symbol, e);
        }
    }

    private ExchangeRate fetchExchangeRateFromApi(String symbol) {
        String urlString = String.format("%s?symbol=%s&apikey=%s", apiUrl, symbol, apiKey);
        log.debug("Выполняем запрос к API: {}", urlString);
        try {
            ExchangeRate exchangeRate = restTemplate.getForObject(urlString, ExchangeRate.class);
            log.debug("Успешно получили курс обмена из API для валютной пары: {}", symbol);
            return exchangeRate;
        } catch (Exception e) {
            log.warn("Не удалось получить курс обмена из API для валютной пары: {}", symbol, e);
            throw new IllegalStateException("Не удалось получить курс обмена из API для валютной пары: " + symbol + ". Ошибка: " + e.getMessage(), e);
        }
    }

    private void updateExistingRate(ExchangeRate existingRate, ExchangeRate newExchangeRate) {
        existingRate.setSymbol(newExchangeRate.getSymbol());
        existingRate.setRate(newExchangeRate.getRate());
        existingRate.setDate(LocalDateTime.now());
        exchangeRateRepository.save(existingRate);
        log.info("Обновлен существующий курс обмена для валютной пары: {}", existingRate.getSymbol());
    }

    private void createNewRate(ExchangeRate exchangeRate) {
        exchangeRate.setDate(LocalDateTime.now());
        exchangeRateRepository.save(exchangeRate);
        log.info("Создан новый курс обмена для валютной пары: {}", exchangeRate.getSymbol());
    }
}

