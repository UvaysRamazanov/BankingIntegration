package by_Ramazanov.controllers;

import by_Ramazanov.DTO.ExceededTransactionDTO;
import by_Ramazanov.repositories.TransactionRepository;
import by_Ramazanov.services.TransactionService;
import by_Ramazanov.DTO.TransactionRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Tag(name = "transactions")
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

    @Operation(summary = "Сохранение транзакции", description = "Метод для сохранения новой транзакции.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Транзакция успешно сохранена"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера при сохранении транзакции")
    })
    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> saveTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        log.info("Запрос на сохранение транзакции: {}", transactionRequest);
        try {
            transactionService.saveTransaction(transactionRequest);
            log.info("Транзакция успешно сохранена: {}", transactionRequest);
            return ResponseEntity.ok(Collections.singletonMap("message", "Транзакция успешно сохранена"));
        } catch (IllegalArgumentException e) {
            log.warn("Некорректные данные запроса: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Некорректные данные запроса: " + e.getMessage()));
        } catch (Exception e) {
            log.error("Ошибка при сохранении транзакции: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Ошибка сервера при сохранении транзакции"));
        }
    }

    @Operation(summary = "Получение превышенных транзакций", description = "Метод для получения списка превышенных транзакций по идентификатору счета.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список превышенных транзакций успешно получен"),
            @ApiResponse(responseCode = "404", description = "Счет не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера при получении транзакций")
    })
    @GetMapping("/getExceeded")
    public ResponseEntity<List<ExceededTransactionDTO>> getExceededTransactions(@RequestParam Long accountFrom) {
        log.info("Запрос на получение превышенных транзакций для счета: {}", accountFrom);
        List<ExceededTransactionDTO> exceededTransactions = transactionRepository.findExceededTransactionDetails(accountFrom);

        if (exceededTransactions.isEmpty()) {
            log.warn("Не найдено превышенных транзакций для счета: {}", accountFrom);
        } else {
            log.info("Найдено {} превышенных транзакций для счета: {}", exceededTransactions.size(), accountFrom);
        }

        return ResponseEntity.ok(exceededTransactions);
    }
}
