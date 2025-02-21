package by_Ramazanov.services;

import by_Ramazanov.DTO.LimitRequest;
import by_Ramazanov.DTO.LimitResponse;
import by_Ramazanov.entity.Limit;
import by_Ramazanov.entity.Transaction;
import by_Ramazanov.models.ExpenseCategory;
import by_Ramazanov.repositories.LimitRepository;
import by_Ramazanov.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LimitService {
    private final LimitRepository limitRepository;
    private final TransactionRepository transactionRepository;

    public void saveLimit(LimitRequest limitRequest) {
        log.info("Получен запрос на сохранение лимита: {}", limitRequest);

        Limit limit;
        if (limitRepository.findTopByAccountFromAndExpenseCategoryOrderByLimitDateTimeDesc(limitRequest.getAccountFrom(), limitRequest.getExpenseCategory()).isPresent()) {
            log.info("Найден существующий лимит для счета {} и категории {}", limitRequest.getAccountFrom(), limitRequest.getExpenseCategory());

            BigDecimal totalSpent = sumAmountsByCurrency(limitRequest.getAccountFrom(), limitRequest.getExpenseCategory());
            BigDecimal remainingLimit = limitRequest.getSumUsd().subtract(totalSpent);

            limit = Limit.builder()
                    .accountFrom(limitRequest.getAccountFrom())
                    .limitSum(limitRequest.getSumUsd())
                    .remainingLimit(remainingLimit)
                    .expenseCategory(limitRequest.getExpenseCategory())
                    .limitDateTime(ZonedDateTime.now())
                    .limitCurrency("USD")
                    .build();
        } else {
            log.info("Новый лимит для счета {} и категории {}", limitRequest.getAccountFrom(), limitRequest.getExpenseCategory());
            limit = Limit.builder()
                    .accountFrom(limitRequest.getAccountFrom())
                    .limitSum(limitRequest.getSumUsd())
                    .remainingLimit(limitRequest.getSumUsd())
                    .expenseCategory(limitRequest.getExpenseCategory())
                    .limitDateTime(ZonedDateTime.now())
                    .limitCurrency("USD")
                    .build();
        }

        limitRepository.save(limit);
        log.info("Лимит успешно сохранен: {}", limit);

        LimitResponse response = new LimitResponse("success",
                "Лимит успешно сохранен", limit.getId());

        log.info("Возвращаем ответ: {}", response);
    }

    private BigDecimal sumAmountsByCurrency(Long accountFrom, ExpenseCategory expenseCategory) {
        log.info("Суммируем транзакции для счета {} и категории {}", accountFrom, expenseCategory);
        List<Transaction> transactions = transactionRepository
                .findAllByAccountFromAndExpenseCategory(accountFrom, expenseCategory);

        BigDecimal totalAmount = transactions.stream()
                .map(transaction -> {
                    BigDecimal sum = transaction.getSum();
                    BigDecimal rate = transaction.getRate();
                    BigDecimal transactionAmount = sum.multiply(rate).setScale(2, RoundingMode.HALF_UP);
                    log.info("Транзакция: сумма={}, курс={}, итоговая сумма={}", sum, rate, transactionAmount);
                    return transactionAmount;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info("Общая сумма транзакций: {}", totalAmount);
        return totalAmount;
    }
}
