package kr.magicbox.release.adapter.in.web.dto.response;

import kr.magicbox.release.application.dto.result.ReleaseMediaResult;

public record ReleaseMediaResponse(
        String mediaUrl,
        int sortOrder
) {
    public static ReleaseMediaResponse from(ReleaseMediaResult result) {
        return new ReleaseMediaResponse(result.mediaUrl(), result.sortOrder());
    }
}
