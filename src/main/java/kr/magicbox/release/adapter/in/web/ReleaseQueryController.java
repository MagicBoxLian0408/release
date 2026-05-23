package kr.magicbox.release.adapter.in.web;

import kr.magicbox.release.adapter.in.web.dto.response.ReleaseResponse;
import kr.magicbox.release.application.dto.query.GetReleaseQuery;
import kr.magicbox.release.application.dto.result.ReleaseResult;
import kr.magicbox.release.application.port.in.GetReleaseUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/release")
@RequiredArgsConstructor
public class ReleaseQueryController {

    private final GetReleaseUseCase getReleaseUseCase;

    @GetMapping("/{releaseId}")
    public ResponseEntity<ReleaseResponse> getRelease(@PathVariable Long releaseId) {
        ReleaseResult result = getReleaseUseCase.getRelease(
                GetReleaseQuery.builder().releaseId(releaseId).build());
        return ResponseEntity.ok(ReleaseResponse.from(result));
    }
}
