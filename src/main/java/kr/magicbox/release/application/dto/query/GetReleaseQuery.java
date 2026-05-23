package kr.magicbox.release.application.dto.query;

import lombok.Builder;

@Builder
public record GetReleaseQuery(Long releaseId) {}
