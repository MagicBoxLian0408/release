package kr.magicbox.release.adapter.in.web;

import kr.magicbox.release.adapter.in.web.constants.CursorConstants;
import kr.magicbox.release.adapter.in.web.dto.response.CursorResponse;
import kr.magicbox.release.adapter.in.web.dto.response.ReleaseResponse;
import kr.magicbox.release.adapter.in.web.validation.CursorSize;
import kr.magicbox.release.application.dto.query.GetAllReleasesQuery;
import kr.magicbox.release.application.dto.query.GetReleaseQuery;
import kr.magicbox.release.application.dto.result.ReleaseResult;
import kr.magicbox.release.application.port.in.GetAllReleasesUseCase;
import kr.magicbox.release.application.port.in.GetReleaseUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class ReleaseQueryController {

    private final GetReleaseUseCase getReleaseUseCase;
    private final GetAllReleasesUseCase getAllReleasesUseCase;

    @GetMapping("/{releaseId}")
    public ResponseEntity<ReleaseResponse> getRelease(@PathVariable Long releaseId) {
        ReleaseResult result = getReleaseUseCase.getRelease(
                GetReleaseQuery.builder().releaseId(releaseId).build());
        return ResponseEntity.ok(ReleaseResponse.from(result));
    }

    @GetMapping
    public ResponseEntity<CursorResponse<ReleaseResponse>> getAllReleases(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = CursorConstants.DEFAULT_SIZE) @CursorSize Integer size) {
        List<ReleaseResponse> contents = getAllReleasesUseCase.getAllReleases(GetAllReleasesQuery.of(cursor, size))
                .stream()
                .map(ReleaseResponse::from)
                .toList();
        return ResponseEntity.ok(CursorResponse.of(contents, size, ReleaseResponse::releaseId));
    }
}
