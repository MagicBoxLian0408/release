package kr.magicbox.release.application.dto.query;

public record GetAllReleasesQuery(Long cursorId, int size) {
    public static GetAllReleasesQuery of(Long cursorId, int size) {
        return new GetAllReleasesQuery(cursorId, size);
    }
}
