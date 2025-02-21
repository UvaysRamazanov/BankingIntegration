package by_Ramazanov.repositories;

import by_Ramazanov.entity.ExchangeRate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    @Cacheable("exchangeRates")
    Optional<ExchangeRate> findBySymbol(String symbol);
}