package by_Ramazanov.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Symbol cannot be blank")
    @Schema(description = "Символ валюты (например, USD)", example = "USD")
    private String symbol;

    @NotNull(message = "Rate cannot be null")
    @Schema(description = "Курс валюты", example = "74.50")
    @Column(precision = 19, scale = 4)
    private BigDecimal rate;

    @Schema(description = "Дата и время обновления курса")
    private LocalDateTime date;
}
