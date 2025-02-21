package by_Ramazanov.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class ConversionResult {

    @Schema(description = "Сумма после конвертации", example = "100.00")
    private final BigDecimal convertedAmount;

    @Schema(description = "Курс конвертации", example = "1.0")
    private final BigDecimal rate;
}