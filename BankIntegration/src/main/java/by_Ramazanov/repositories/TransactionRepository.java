package by_Ramazanov.repositories;

import by_Ramazanov.DTO.ExceededTransactionDTO;
import by_Ramazanov.entity.Transaction;
import by_Ramazanov.models.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByAccountFromAndExpenseCategory(Long accountFrom, ExpenseCategory expenseCategory);

    @Query("SELECT new by_Ramazanov.DTO.ExceededTransactionDTO(t.accountFrom, t.accountTo, t.currencyShortName, t.sum, " +
            "t.dateTime, t.expenseCategory, l.limitSum, l.limitDateTime, l.limitCurrency) " +
            "FROM Transaction t JOIN t.limit l WHERE t.limitExceeded = true AND t.accountFrom = :accountFrom")
    List<ExceededTransactionDTO> findExceededTransactionDetails(@Param("accountFrom") Long accountFrom);
}