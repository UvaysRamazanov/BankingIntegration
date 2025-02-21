package by_Ramazanov.DTO;

import by_Ramazanov.models.ExpenseCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class LimitRequest {

    @NotNull(message = "Поле 'accountFrom' не может быть null")
    @Schema(description = "ID счета, для которого устанавливается лимит", example = "12345")
    private Long accountFrom;

    @NotNull(message = "Поле 'sumUsd' не может быть null")
    @Positive(message = "Поле 'sumUsd' должно быть положительным")
    @Schema(description = "Сумма лимита в долларах США", example = "1000.00")
    private BigDecimal sumUsd;

    @NotNull(message = "Поле 'expenseCategory' не может быть null")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Категория расхода для лимита", example = "FOOD")
    private ExpenseCategory expenseCategory;
}
