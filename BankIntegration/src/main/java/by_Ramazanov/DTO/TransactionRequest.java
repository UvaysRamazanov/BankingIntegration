package by_Ramazanov.DTO;

import by_Ramazanov.models.ExpenseCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class TransactionRequest {

    @Schema(description = "ID счета отправителя", example = "12345")
    @NotNull(message = "Account From cannot be null")
    @Positive(message = "Account From must be a positive number")
    private Long accountFrom;

    @Schema(description = "ID счета получателя", example = "67890")
    @NotNull(message = "Account To cannot be null")
    @Positive(message = "Account To must be a positive number")
    private Long accountTo;

    @Schema(description = "Сумма транзакции", example = "100.00")
    @NotNull(message = "Sum cannot be null")
    @DecimalMin(value="0.01", message="Sum must be greater than 0.01")
    private BigDecimal sum;

    @Schema(description="Код валюты (например, USD)", example="USD")
    @NotBlank(message="Currency Short Name cannot be blank")
    @Size(min=3, max=3, message="Currency Short Name must be 3 characters long")
    private String currencyShortName;

    @Schema(description="Категория расхода", example="FOOD")
    @NotNull(message="Expense Category cannot be null")
    private ExpenseCategory expenseCategory;
}
