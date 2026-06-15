package kr.magicbox.release.domain.aggregate;

import kr.magicbox.release.domain.enums.ReleaseLevel;
import kr.magicbox.release.domain.enums.ReleaseStatus;
import kr.magicbox.release.domain.exception.InvalidFieldException;
import kr.magicbox.release.domain.exception.ReleaseStatusConflictException;
import kr.magicbox.release.domain.vo.CreatorId;
import kr.magicbox.release.domain.vo.ReleaseId;
import kr.magicbox.release.domain.vo.ReleaseMedia;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class Release {

    private final ReleaseId id;
    private final CreatorId creatorId;
    private String title;
    private String description;
    private List<ReleaseMedia> mediaList;
    private final ReleaseLevel level;
    private ReleaseStatus status;
    private Long price;
    private Integer limitedQuantity;
    private Integer soldQuantity;
    private final Instant scheduledAt;
    private final Instant createdAt;
    private Instant updatedAt;

    @Builder(builderMethodName = "createBuilder", builderClassName = "CreateBuilder")
    public Release(CreatorId creatorId, String title, String description,
                   List<ReleaseMedia> mediaList, ReleaseLevel level, Long price,
                   Integer limitedQuantity, Instant scheduledAt) {
        validateCreate(creatorId, title, price, limitedQuantity, scheduledAt, mediaList);
        this.id = null;
        this.creatorId = creatorId;
        this.title = title;
        this.description = description;
        this.mediaList = List.copyOf(mediaList);
        this.level = level != null ? level : ReleaseLevel.BEGINNER;
        this.status = ReleaseStatus.SCHEDULED;
        this.price = price;
        this.limitedQuantity = limitedQuantity;
        this.soldQuantity = 0;
        this.scheduledAt = scheduledAt;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @Builder(builderMethodName = "reconstructBuilder", builderClassName = "ReconstructBuilder")
    public Release(ReleaseId id, CreatorId creatorId, String title, String description,
                   List<ReleaseMedia> mediaList, ReleaseLevel level, ReleaseStatus status,
                   Long price, Integer limitedQuantity, Integer soldQuantity,
                   Instant scheduledAt, Instant createdAt, Instant updatedAt) {
        validateReconstruct(id, creatorId, title, level, status, price, limitedQuantity, soldQuantity,
                scheduledAt, createdAt, updatedAt);
        this.id = id;
        this.creatorId = creatorId;
        this.title = title;
        this.description = description;
        this.mediaList = mediaList != null ? List.copyOf(mediaList) : List.of();
        this.level = level;
        this.status = status;
        this.price = price;
        this.limitedQuantity = limitedQuantity;
        this.soldQuantity = soldQuantity;
        this.scheduledAt = scheduledAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private void validateCreate(CreatorId creatorId, String title, Long price,
                                Integer limitedQuantity, Instant scheduledAt,
                                List<ReleaseMedia> mediaList) {
        if (creatorId == null) throw new InvalidFieldException("크리에이터 ID는 필수입니다.");
        if (title == null || title.isBlank()) throw new InvalidFieldException("제목은 필수입니다.");
        if (price == null || price <= 0) throw new InvalidFieldException("가격은 양수여야 합니다.");
        if (limitedQuantity == null || limitedQuantity <= 0) throw new InvalidFieldException("한정 수량은 양수여야 합니다.");
        if (scheduledAt == null) throw new InvalidFieldException("판매 예정 시각은 필수입니다.");
        if (mediaList == null || mediaList.isEmpty()) throw new InvalidFieldException("미디어는 하나 이상 필수입니다.");
        validateMediaSortOrder(mediaList);
    }

    private void validateReconstruct(ReleaseId id, CreatorId creatorId, String title,
                                     ReleaseLevel level, ReleaseStatus status,
                                     Long price, Integer limitedQuantity, Integer soldQuantity,
                                     Instant scheduledAt, Instant createdAt, Instant updatedAt) {
        if (id == null) throw new InvalidFieldException("릴리즈 ID는 필수입니다.");
        if (creatorId == null) throw new InvalidFieldException("크리에이터 ID는 필수입니다.");
        if (title == null || title.isBlank()) throw new InvalidFieldException("제목은 필수입니다.");
        if (level == null) throw new InvalidFieldException("레벨은 필수입니다.");
        if (status == null) throw new InvalidFieldException("상태는 필수입니다.");
        if (price == null || price <= 0) throw new InvalidFieldException("가격은 양수여야 합니다.");
        if (limitedQuantity == null || limitedQuantity <= 0) throw new InvalidFieldException("한정 수량은 양수여야 합니다.");
        if (soldQuantity == null || soldQuantity < 0) throw new InvalidFieldException("판매 수량은 0 이상이어야 합니다.");
        if (scheduledAt == null) throw new InvalidFieldException("판매 예정 시각은 필수입니다.");
        if (createdAt == null) throw new InvalidFieldException("생성 시각은 필수입니다.");
        if (updatedAt == null) throw new InvalidFieldException("수정 시각은 필수입니다.");
    }

    private void validateMediaSortOrder(List<ReleaseMedia> mediaList) {
        Set<Integer> sortOrders = mediaList.stream()
                .map(ReleaseMedia::getSortOrder)
                .collect(Collectors.toSet());
        if (sortOrders.size() != mediaList.size()) {
            throw new InvalidFieldException("미디어 정렬 순서는 중복될 수 없습니다.");
        }
    }

    public void startSale() {
        if (this.status != ReleaseStatus.SCHEDULED) {
            throw new ReleaseStatusConflictException("판매 예정 상태에서만 판매를 시작할 수 있습니다. 현재: " + this.status);
        }
        this.status = ReleaseStatus.ON_SALE;
        this.updatedAt = Instant.now();
    }

    public boolean increaseSoldQuantity() {
        if (this.status != ReleaseStatus.ON_SALE) {
            throw new ReleaseStatusConflictException("판매 중 상태에서만 판매 수량을 증가할 수 있습니다. 현재: " + this.status);
        }
        this.soldQuantity++;
        this.updatedAt = Instant.now();
        if (this.soldQuantity.equals(this.limitedQuantity)) {
            this.status = ReleaseStatus.SOLD_OUT;
        }
        return this.status == ReleaseStatus.SOLD_OUT;
    }

    public void soldOut() {
        if (this.status != ReleaseStatus.ON_SALE) {
            throw new ReleaseStatusConflictException("판매 중 상태에서만 매진 처리할 수 있습니다. 현재: " + this.status);
        }
        this.status = ReleaseStatus.SOLD_OUT;
        this.updatedAt = Instant.now();
    }

    public void endSale() {
        if (this.status != ReleaseStatus.ON_SALE && this.status != ReleaseStatus.SOLD_OUT) {
            throw new ReleaseStatusConflictException("판매 중 또는 매진 상태에서만 판매를 종료할 수 있습니다. 현재: " + this.status);
        }
        this.status = ReleaseStatus.ENDED;
        this.updatedAt = Instant.now();
    }

    public void update(String title, String description, Long price, Integer limitedQuantity, List<ReleaseMedia> mediaList) {
        if (title != null && !title.isBlank()) this.title = title;
        if (description != null) this.description = description;
        if (price != null) this.price = price;
        if (limitedQuantity != null) {
            this.limitedQuantity = limitedQuantity;
            if (this.status == ReleaseStatus.SOLD_OUT && this.soldQuantity < this.limitedQuantity) {
                this.status = ReleaseStatus.ON_SALE;
            }
        }
        if (mediaList != null && !mediaList.isEmpty()) {
            validateMediaSortOrder(mediaList);
            this.mediaList = List.copyOf(mediaList);
        }
        this.updatedAt = Instant.now();
    }

    public boolean isOnSale() {
        return this.status == ReleaseStatus.ON_SALE;
    }
}
