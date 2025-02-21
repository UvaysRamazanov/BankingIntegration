package by_Ramazanov.entity;

import by_Ramazanov.models.ExpenseCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="limit_transaction")
public class Limit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID лимита")
    private Long id;

    @Schema(description = "ID счета, для которого установлен лимит")
    @NotNull(message = "Account From cannot be null")
    private long accountFrom;

    @Schema(description = "Сумма лимита")
    @NotNull(message = "Limit Sum cannot be null")
    @Positive(message = "Limit Sum must be positive")
    private BigDecimal limitSum;

    @Schema(description = "Оставшийся лимит")
    @NotNull(message = "Remaining Limit cannot be null")
    private BigDecimal remainingLimit;

    @Schema(description = "Дата и время установки лимита")
    @NotNull(message = "Limit Date Time cannot be null")
    private ZonedDateTime limitDateTime;

    @Schema(description = "Категория расходов")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Expense Category cannot be null")
    private ExpenseCategory expenseCategory;

    @Schema(description = "Валюта лимита")
    @NotNull(message = "Limit Currency cannot be null")
    private String limitCurrency;

    @OneToMany(mappedBy = "limit")
    private List<Transaction> transactions;


}