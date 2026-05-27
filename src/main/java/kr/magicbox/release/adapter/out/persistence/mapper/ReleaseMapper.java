package kr.magicbox.release.adapter.out.persistence.mapper;

import kr.magicbox.release.adapter.out.persistence.entity.ReleaseEntity;
import kr.magicbox.release.adapter.out.persistence.entity.ReleaseMediaEntity;
import kr.magicbox.release.domain.aggregate.Release;
import kr.magicbox.release.domain.vo.CreatorId;
import kr.magicbox.release.domain.vo.ReleaseId;
import kr.magicbox.release.domain.vo.ReleaseMedia;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReleaseMapper {

    public ReleaseEntity toEntity(Release domain) {
        ReleaseEntity entity = ReleaseEntity.builder()
                .creatorId(domain.getCreatorId().value())
                .title(domain.getTitle())
                .description(domain.getDescription())
                .level(domain.getLevel())
                .status(domain.getStatus())
                .price(domain.getPrice())
                .limitedQuantity(domain.getLimitedQuantity())
                .soldQuantity(domain.getSoldQuantity())
                .scheduledAt(domain.getScheduledAt())
                .build();

        domain.getMediaList().forEach(media -> entity.addMedia(toMediaEntity(media)));

        return entity;
    }

    public ReleaseMediaEntity toMediaEntity(ReleaseMedia media) {
        return ReleaseMediaEntity.builder()
                .mediaUrl(media.getMediaUrl())
                .sortOrder(media.getSortOrder())
                .build();
    }

    public Release toDomain(ReleaseEntity entity) {
        List<ReleaseMedia> mediaList = entity.getReleaseMediaList().stream()
                .map(m -> ReleaseMedia.builder()
                        .mediaUrl(m.getMediaUrl())
                        .sortOrder(m.getSortOrder())
                        .build())
                .toList();

        return Release.reconstructBuilder()
                .id(ReleaseId.of(entity.getId()))
                .creatorId(CreatorId.of(entity.getCreatorId()))
                .title(entity.getTitle())
                .description(entity.getDescription())
                .mediaList(mediaList)
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
