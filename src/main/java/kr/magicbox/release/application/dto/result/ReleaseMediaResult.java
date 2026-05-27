package kr.magicbox.release.application.dto.result;

import kr.magicbox.release.domain.vo.ReleaseMedia;

public record ReleaseMediaResult(
        String mediaUrl,
        int sortOrder
) {
    public static ReleaseMediaResult from(ReleaseMedia media) {
        return new ReleaseMediaResult(media.getMediaUrl(), media.getSortOrder());
    }
}
