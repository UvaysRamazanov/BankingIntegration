package by_Ramazanov.services;

import by_Ramazanov.entity.Limit;
import by_Ramazanov.repositories.LimitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class UpdateLimitsService {
    private final LimitRepository limitRepository;

    @Scheduled(cron = "0 0 0 1 * ?") // Обновление лимитов 1-го числа нового месяца
    public void updateLimits() {
        log.info("Запущено обновление лимитов на {}", ZonedDateTime.now());

        List<Limit> limits = limitRepository.findAll();
        if (limits.isEmpty()) {
            log.warn("Нет лимитов для обновления.");
            return;
        }

        for (Limit limit : limits) {
            limit.setRemainingLimit(limit.getLimitSum());
            limit.setLimitDateTime(ZonedDateTime.now());
            limitRepository.save(limit);
            log.info("Лимит обновлен: ID={}, Новый остаток={}, Дата обновления={}",
                    limit.getId(), limit.getRemainingLimit(), limit.getLimitDateTime());
        }

        log.info("Обновление лимитов завершено.");
    }
}
