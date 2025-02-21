package by_Ramazanov.DTO;

import lombok.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class LimitResponse {

    @NonNull
    @Schema(description = "Статус операции", example = "success")
    private final String status;

    @NonNull
    @Schema(description = "Сообщение о результате операции", example = "Лимит успешно сохранен.")
    private final String message;

    @Schema(description = "ID лимита, если применимо", example = "1")
    private Long limitId;
}
