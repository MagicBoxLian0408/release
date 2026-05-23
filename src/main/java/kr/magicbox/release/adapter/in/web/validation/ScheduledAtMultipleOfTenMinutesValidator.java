package kr.magicbox.release.adapter.in.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;

public class ScheduledAtMultipleOfTenMinutesValidator
        implements ConstraintValidator<ScheduledAtMultipleOfTenMinutes, Instant> {

    private static final long TEN_MINUTES_SECONDS = 600L;

    @Override
    public boolean isValid(Instant value, ConstraintValidatorContext context) {
        if (value == null) return true; // null 체크는 @NotNull이 담당
        boolean isMultipleOfTen = value.getEpochSecond() % TEN_MINUTES_SECONDS == 0;
        boolean isFarEnough = value.isAfter(Instant.now().plusSeconds(TEN_MINUTES_SECONDS));
        return isMultipleOfTen && isFarEnough;
    }
}
