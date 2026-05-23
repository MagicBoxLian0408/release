package kr.magicbox.release.adapter.in.web.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ScheduledAtMultipleOfTenMinutesValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ScheduledAtMultipleOfTenMinutes {
    String message() default "판매 시작 시각은 10분 단위여야 합니다. (예: 14:00, 14:10, 14:20)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
