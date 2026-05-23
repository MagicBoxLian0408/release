package kr.magicbox.release.adapter.out.communication.grpc;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import kr.magicbox.release.adapter.out.communication.grpc.exception.CreatorNotFoundException;
import kr.magicbox.release.adapter.out.communication.grpc.exception.CreatorServiceUnavailableException;
import kr.magicbox.release.application.port.out.CreatorIdQueryPort;
import kr.magicbox.release.domain.vo.CreatorId;
import kr.magicbox.release.domain.vo.UserId;
import kr.magicbox.release.grpc.creator.CreatorServiceGrpc;
import kr.magicbox.release.grpc.creator.GetCreatorIdByUserIdRequest;
import kr.magicbox.release.grpc.creator.GetCreatorIdByUserIdResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreatorGrpcAdapter implements CreatorIdQueryPort {

    private final ManagedChannel creatorManagedChannel;

    @Override
    @CircuitBreaker(name = "creatorService", fallbackMethod = "getCreatorIdFallback")
    public CreatorId getCreatorId(UserId userId) {
        GetCreatorIdByUserIdRequest request = GetCreatorIdByUserIdRequest.newBuilder()
                .setUserId(userId.value())
                .build();

        CreatorServiceGrpc.CreatorServiceBlockingStub stub = CreatorServiceGrpc.newBlockingStub(creatorManagedChannel)
                .withDeadlineAfter(2, TimeUnit.SECONDS);
        GetCreatorIdByUserIdResponse response = stub.getCreatorIdByUserId(request);

        return new CreatorId(response.getCreatorId());
    }

    @SuppressWarnings("unused")
    private CreatorId getCreatorIdFallback(UserId userId, Throwable throwable) {
        if (throwable instanceof StatusRuntimeException statusException
                && statusException.getStatus().getCode() == Status.Code.NOT_FOUND) {
            throw new CreatorNotFoundException();
        }
        log.warn("크리에이터 서비스 연결 실패");
        throw new CreatorServiceUnavailableException(throwable);
    }
}
