package by_Ramazanov.controllers;

import by_Ramazanov.DTO.LimitRequest;
import by_Ramazanov.services.LimitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Tag(name = "save_limit")
@RestController
@RequestMapping("/limits")
@Validated
@AllArgsConstructor
@Slf4j
public class LimitController {

    private final LimitService limitService;

    @PostMapping("/save")
    @Operation(summary = "Сохранение лимита", description = "Метод для сохранения лимита пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Лимит успешно сохранен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера при сохранении лимита")
    })
    public ResponseEntity<Map<String, String>> saveLimit(@Valid @RequestBody LimitRequest limitRequest) {
        log.info("Получен запрос на сохранение лимита: accountFrom={}, sumUsd={}, expenseCategory={}",
                limitRequest.getAccountFrom(), limitRequest.getSumUsd(), limitRequest.getExpenseCategory());

        try {
            limitService.saveLimit(limitRequest);
            log.info("Лимит успешно сохранен: accountFrom={}, sumUsd={}, expenseCategory={}",
                    limitRequest.getAccountFrom(), limitRequest.getSumUsd(), limitRequest.getExpenseCategory());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Collections.singletonMap("message", "Limit saved successfully"));
        } catch (IllegalArgumentException e) {
            log.warn("Некорректные данные запроса: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Некорректные данные запроса: " + e.getMessage()));
        } catch (Exception e) {
            log.error("Ошибка при сохранении лимита: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Ошибка при сохранении лимита: " + e.getMessage()));
        }
    }
}
