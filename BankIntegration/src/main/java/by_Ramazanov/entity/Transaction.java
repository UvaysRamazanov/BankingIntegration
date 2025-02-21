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

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID транзакции")
    private Long id;

    @NotNull(message = "Account From cannot be null")
    @Schema(description = "ID счета отправителя")
    private Long accountFrom;

    @NotNull(message = "Account To cannot be null")
    @Schema(description = "ID счета получателя")
    private Long accountTo;

    @NotNull(message = "Sum cannot be null")
    @Positive(message = "Sum must be positive")
    @Schema(description = "Сумма транзакции")
    private BigDecimal sum;

    @NotNull(message = "Currency Short Name cannot be null")
    @Schema(description = "Код валюты (например, USD)")
    private String currencyShortName;

    @NotNull(message = "Date Time cannot be null")
    @Schema(description = "Дата и время транзакции")
    private ZonedDateTime dateTime;

    @NotNull(message = "Rate cannot be null")
    @Positive(message = "Rate must be positive")
    @Schema(description = "Курс валюты на момент транзакции")
    private BigDecimal rate;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Категория расхода")
    private ExpenseCategory expenseCategory;

    @Schema(description = "Флаг превышения лимита")
    private boolean limitExceeded;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="limit_id")
    @Schema(description = "Лимит, связанный с транзакцией")
    private Limit limit;
}