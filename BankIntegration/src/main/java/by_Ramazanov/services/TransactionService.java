package by_Ramazanov.services;

import by_Ramazanov.DTO.ConversionResult;
import by_Ramazanov.DTO.TransactionRequest;
import by_Ramazanov.entity.Limit;
import by_Ramazanov.entity.Transaction;
import by_Ramazanov.repositories.LimitRepository;
import by_Ramazanov.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final LimitRepository limitRepository;
    private final CurrencyConverterService currencyConverterService;

    public void saveTransaction(TransactionRequest transactionRequest) {
        log.info("Получен запрос на сохранение транзакции: {}", transactionRequest);

        Limit limit = limitRepository.findTopByAccountFromAndExpenseCategoryOrderByLimitDateTimeDesc(transactionRequest.getAccountFrom(), transactionRequest.getExpenseCategory())
                .orElse(Limit.builder()
                        .accountFrom(transactionRequest.getAccountFrom())
                        .expenseCategory(transactionRequest.getExpenseCategory())
                        .limitDateTime(ZonedDateTime.now())
                        .limitCurrency("USD")
                        .limitSum(BigDecimal.valueOf(1000))
                        .remainingLimit(BigDecimal.valueOf(1000))
                        .build());

        log.info("Используем лимит: {}", limit);

        ConversionResult conversionResult = currencyConverterService.convertCurrency(transactionRequest.getSum(), transactionRequest.getCurrencyShortName());
        log.info("Результат конвертации: {}", conversionResult);

        limit.setRemainingLimit(limit.getRemainingLimit().subtract(conversionResult.getConvertedAmount()));
        log.info("Остаток лимита после транзакции: {}", limit.getRemainingLimit());

        Transaction transaction = Transaction.builder()
                .accountFrom(transactionRequest.getAccountFrom())
                .accountTo(transactionRequest.getAccountTo())
                .sum(transactionRequest.getSum())
                .currencyShortName(transactionRequest.getCurrencyShortName())
                .dateTime(ZonedDateTime.now())
                .rate(conversionResult.getRate()) // Используем BigDecimal
                .expenseCategory(transactionRequest.getExpenseCategory())
                .limit(limit)
                .limitExceeded(limit.getRemainingLimit().compareTo(BigDecimal.ZERO) < 0)
                .build();

        if (limit.getRemainingLimit().compareTo(BigDecimal.ZERO) >= 0) {
            log.info("Транзакция успешно сохранена: {}", transaction);
            transactionRepository.save(transaction);
        } else {
            log.warn("Превышен лимит для транзакции: {}", transaction);
            transaction.setLimitExceeded(true);
            transactionRepository.save(transaction);
        }
    }
}