package kr.magicbox.release.domain.exception;

import kr.magicbox.release.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class ReleaseStatusConflictException extends BusinessException {

    public ReleaseStatusConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
