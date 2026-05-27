package kr.magicbox.release.domain.exception;

import kr.magicbox.release.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class ReleaseUnauthorizedException extends BusinessException {

    public ReleaseUnauthorizedException() {
        super("해당 릴리즈에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }
}
