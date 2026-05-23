package kr.magicbox.release.adapter.in.grpc;

import io.grpc.*;
import kr.magicbox.release.global.exception.BusinessException;
import kr.magicbox.release.global.exception.SystemError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.GlobalServerInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@GlobalServerInterceptor
public class GrpcExceptionInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        ServerCall.Listener<ReqT> delegate = next.startCall(call, headers);
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(delegate) {
            @Override
            public void onHalfClose() {
                try {
                    super.onHalfClose();
                } catch (BusinessException e) {
                    log.warn("gRPC 비즈니스 예외: {}", e.getMessage());
                    call.close(toGrpcStatus(e).withDescription(e.getMessage()), new Metadata());
                } catch (SystemError e) {
                    log.error("gRPC 시스템 오류: {}", e.getMessage(), e);
                    call.close(Status.INTERNAL.withDescription("서버 내부 오류가 발생했습니다."), new Metadata());
                } catch (Exception e) {
                    log.error("gRPC 예상치 못한 예외: {}", e.getMessage(), e);
                    call.close(Status.UNKNOWN.withDescription("예상치 못한 오류가 발생했습니다."), new Metadata());
                }
            }
        };
    }

    private Status toGrpcStatus(BusinessException e) {
        return switch (e.getStatus()) {
            case NOT_FOUND -> Status.NOT_FOUND;
            case CONFLICT -> Status.ALREADY_EXISTS;
            case FORBIDDEN -> Status.PERMISSION_DENIED;
            case UNAUTHORIZED -> Status.UNAUTHENTICATED;
            default -> Status.INVALID_ARGUMENT;
        };
    }
}
