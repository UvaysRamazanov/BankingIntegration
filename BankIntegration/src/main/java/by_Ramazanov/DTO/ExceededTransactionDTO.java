package by_Ramazanov.DTO;

import by_Ramazanov.models.ExpenseCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ExceededTransactionDTO {

    @NotNull(message = "Поле 'accountFrom' не может быть null")
    @Positive(message = "Поле 'accountFrom' должно быть положительным")
    @Schema(description = "ID счета отправителя", example = "12345")
    private final Long accountFrom;

    @NotNull(message = "Поле 'accountTo' не может быть null")
    @Positive(message = "Поле 'accountTo' должно быть положительным")
    @Schema(description = "ID счета получателя", example = "67890")
    private final Long accountTo;

    @NotNull(message = "Поле 'currencyShortName' не может быть null")
    @Schema(description = "Код валюты (например, USD)", example = "USD")
    private final String currencyShortName;

    @NotNull(message = "Поле 'sum' не может быть null")
    @Positive(message = "Поле 'sum' должно быть положительным")
    @Schema(description = "Сумма транзакции", example = "100.00")
    private final BigDecimal sum;

    @NotNull(message = "Поле 'dateTime' не может быть null")
    @Schema(description = "Дата и время транзакции", example = "2023-02-20T12:00:00Z")
    private final ZonedDateTime dateTime;

    @NotNull(message = "Поле 'expenseCategory' не может быть null")
    @Schema(description = "Категория расхода", example = "FOOD")
    private final ExpenseCategory expenseCategory;

    @NotNull(message = "Поле 'limitSum' не может быть null")
    @Positive(message = "Поле 'limitSum' должно быть положительным")
    @Schema(description = "Сумма лимита", example = "1000.00")
    private final BigDecimal limitSum;

    @NotNull(message = "Поле 'limitDateTime' не может быть null")
    @Schema(description = "Дата и время лимита", example = "2023-02-20T12:00:00Z")
    private final ZonedDateTime limitDateTime;

    @NotNull(message = "Поле 'limitCurrency' не может быть null")
    @Schema(description = "Валюта лимита", example = "USD")
    private final String limitCurrency;
}
