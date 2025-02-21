package by_Ramazanov.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountNumberValidator implements ConstraintValidator<ValidAccountNumber, Long> {

    @Override
    public void initialize(ValidAccountNumber constraintAnnotation) {}

    @Override
    public boolean isValid(Long accountFrom, ConstraintValidatorContext context) {
        if (accountFrom == null) {
            log.warn("Входящий номер счета равен null. Пропускаем валидацию.");
            return true;
        }

        String accountStr = Long.toString(accountFrom);
        boolean isValid = accountStr.length() == 10;

        if (isValid) {
            log.info("Номер счета {} валиден.", accountStr);
        } else {
            log.error("Номер счета {} не валиден. Должен содержать ровно 10 цифр.", accountStr);
        }

        return isValid;
    }
}
