package kr.magicbox.release.adapter.out.persistence.mapper;

import kr.magicbox.release.adapter.out.persistence.entity.ReleaseEntity;
import kr.magicbox.release.domain.aggregate.Release;
import kr.magicbox.release.domain.vo.CreatorId;
import kr.magicbox.release.domain.vo.ReleaseId;
import org.springframework.stereotype.Component;

@Component
public class ReleaseMapper {

    public ReleaseEntity toEntity(Release domain) {
        return ReleaseEntity.builder()
                .creatorId(domain.getCreatorId().value())
                .title(domain.getTitle())
                .description(domain.getDescription())
                .thumbnailUrl(domain.getThumbnailUrl())
                .level(domain.getLevel())
                .status(domain.getStatus())
                .price(domain.getPrice())
                .limitedQuantity(domain.getLimitedQuantity())
                .soldQuantity(domain.getSoldQuantity())
                .scheduledAt(domain.getScheduledAt())
                .build();
    }

    public Release toDomain(ReleaseEntity entity) {
        return Release.reconstructBuilder()
                .id(ReleaseId.of(entity.getId()))
                .creatorId(CreatorId.of(entity.getCreatorId()))
                .title(entity.getTitle())
                .description(entity.getDescription())
                .thumbnailUrl(entity.getThumbnailUrl())
                .level(entity.getLevel())
                .status(entity.getStatus())
                .price(entity.getPrice())
                .limitedQuantity(entity.getLimitedQuantity())
                .soldQuantity(entity.getSoldQuantity())
                .scheduledAt(entity.getScheduledAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
