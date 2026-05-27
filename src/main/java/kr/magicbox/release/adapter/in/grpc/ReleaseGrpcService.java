package kr.magicbox.release.adapter.in.grpc;

import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import kr.magicbox.release.application.dto.query.GetReleaseQuery;
import kr.magicbox.release.application.dto.result.ReleaseResult;
import kr.magicbox.release.application.port.in.GetReleaseCountByCreatorUseCase;
import kr.magicbox.release.application.port.in.GetReleaseListByCreatorUseCase;
import kr.magicbox.release.application.port.in.GetReleaseUseCase;
import kr.magicbox.release.domain.vo.CreatorId;
import kr.magicbox.release.domain.vo.ReleaseId;
import kr.magicbox.release.grpc.release.GetReleaseCountRequest;
import kr.magicbox.release.grpc.release.GetReleaseCountResponse;
import kr.magicbox.release.grpc.release.GetReleasesByCreatorIdRequest;
import kr.magicbox.release.grpc.release.GetReleasesByCreatorIdResponse;
import kr.magicbox.release.grpc.release.GetRemainingQuantityRequest;
import kr.magicbox.release.grpc.release.GetRemainingQuantityResponse;
import kr.magicbox.release.grpc.release.IsReleaseOnSaleRequest;
import kr.magicbox.release.grpc.release.IsReleaseOnSaleResponse;
import kr.magicbox.release.grpc.release.Release;
import kr.magicbox.release.grpc.release.ReleaseLevel;
import kr.magicbox.release.grpc.release.ReleaseServiceGrpc;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class ReleaseGrpcService extends ReleaseServiceGrpc.ReleaseServiceImplBase {

    private final GetReleaseCountByCreatorUseCase getReleaseCountByCreatorUseCase;
    private final GetReleaseListByCreatorUseCase getReleaseListByCreatorUseCase;
    private final GetReleaseUseCase getReleaseUseCase;

    @Override
    public void getReleaseCount(GetReleaseCountRequest request,
                                StreamObserver<GetReleaseCountResponse> responseObserver) {
        long count = getReleaseCountByCreatorUseCase.getReleaseCount(CreatorId.of(request.getCreatorId()));
        responseObserver.onNext(GetReleaseCountResponse.newBuilder()
                .setReleaseCount(count)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getReleasesByCreatorId(GetReleasesByCreatorIdRequest request,
                                       StreamObserver<GetReleasesByCreatorIdResponse> responseObserver) {
        List<ReleaseResult> results = getReleaseListByCreatorUseCase.getReleaseListByCreator(
                CreatorId.of(request.getCreatorId()));

        List<Release> releases = results.stream()
                .map(this::toProtoRelease)
                .toList();

        responseObserver.onNext(GetReleasesByCreatorIdResponse.newBuilder()
                .addAllReleases(releases)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void isReleaseOnSale(IsReleaseOnSaleRequest request,
                                StreamObserver<IsReleaseOnSaleResponse> responseObserver) {
        ReleaseResult result = getReleaseUseCase.getRelease(
                GetReleaseQuery.builder().releaseId(request.getReleaseId()).build());
        boolean onSale = result.isOnSale();
        responseObserver.onNext(IsReleaseOnSaleResponse.newBuilder()
                .setOnSale(onSale)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getRemainingQuantity(GetRemainingQuantityRequest request,
                                     StreamObserver<GetRemainingQuantityResponse> responseObserver) {
        ReleaseResult result = getReleaseUseCase.getRelease(
                GetReleaseQuery.builder().releaseId(request.getReleaseId()).build());
        int remaining = result.limitedQuantity() - result.soldQuantity();
        responseObserver.onNext(GetRemainingQuantityResponse.newBuilder()
                .setRemainingQuantity(remaining)
                .build());
        responseObserver.onCompleted();
    }

    private Release toProtoRelease(ReleaseResult result) {
        return Release.newBuilder()
                .setReleaseId(result.releaseId())
                .setTitle(result.title())
                .addAllMediaUrls(result.mediaList().stream().map(m -> m.mediaUrl()).toList())
                .setLevel(toProtoLevel(result.level()))
                .setPrice(result.price())
                .setCreatedAt(Timestamp.newBuilder()
                        .setSeconds(result.createdAt().getEpochSecond())
                        .setNanos(result.createdAt().getNano())
                        .build())
                .setScheduledAt(Timestamp.newBuilder()
                        .setSeconds(result.scheduledAt().getEpochSecond())
                        .setNanos(result.scheduledAt().getNano())
                        .build())
                .setStatus(toProtoStatus(result.status()))
                .setLimitedQuantity(result.limitedQuantity())
                .setSoldQuantity(result.soldQuantity())
                .build();
    }

    private ReleaseLevel toProtoLevel(kr.magicbox.release.domain.enums.ReleaseLevel level) {
        return switch (level) {
            case BEGINNER -> ReleaseLevel.BEGINNER;
            case INTERMEDIATE -> ReleaseLevel.INTERMEDIATE;
            case ADVANCED -> ReleaseLevel.ADVANCED;
        };
    }

    private kr.magicbox.release.grpc.release.ReleaseStatus toProtoStatus(kr.magicbox.release.domain.enums.ReleaseStatus status) {
        return switch (status) {
            case SCHEDULED -> kr.magicbox.release.grpc.release.ReleaseStatus.SCHEDULED;
            case ON_SALE -> kr.magicbox.release.grpc.release.ReleaseStatus.ON_SALE;
            case SOLD_OUT -> kr.magicbox.release.grpc.release.ReleaseStatus.SOLD_OUT;
            case ENDED -> kr.magicbox.release.grpc.release.ReleaseStatus.ENDED;
        };
    }
}
