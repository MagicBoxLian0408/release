package kr.magicbox.release.domain.exception;

import kr.magicbox.release.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class ReleaseNotFoundException extends BusinessException {

    public ReleaseNotFoundException() {
        super("릴리즈를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
