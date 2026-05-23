package kr.magicbox.release.adapter.out.communication.grpc.exception;

import kr.magicbox.release.global.exception.SystemError;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class CreatorServiceUnavailableException extends SystemError {

    public CreatorServiceUnavailableException(Throwable cause) {
        super("크리에이터 서비스에 연결할 수 없습니다.", HttpStatus.SERVICE_UNAVAILABLE, cause);
    }
}
