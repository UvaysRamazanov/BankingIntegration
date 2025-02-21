package by_Ramazanov.repositories;

import by_Ramazanov.entity.Limit;
import by_Ramazanov.models.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {
    Optional<Limit> findTopByAccountFromAndExpenseCategoryOrderByLimitDateTimeDesc(
            @NonNull Long accountFrom,
            @NonNull ExpenseCategory expenseCategory
    );
}