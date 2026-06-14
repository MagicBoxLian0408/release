package kr.magicbox.release.adapter.in.web;

import jakarta.validation.Valid;
import kr.magicbox.release.adapter.in.web.dto.request.RegisterReleaseRequest;
import kr.magicbox.release.adapter.in.web.dto.request.UpdateReleaseRequest;
import kr.magicbox.release.application.port.in.RegisterReleaseUseCase;
import kr.magicbox.release.application.port.in.UpdateReleaseUseCase;
import kr.magicbox.release.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
public class ReleaseCommandController {

    private final RegisterReleaseUseCase registerReleaseUseCase;
    private final UpdateReleaseUseCase updateReleaseUseCase;

    @PostMapping
    public ResponseEntity<Void> registerRelease(
            @AuthenticationPrincipal UserId userId,
            @Valid @RequestBody RegisterReleaseRequest request
    ) {
        registerReleaseUseCase.registerRelease(request.toCommand(userId));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{releaseId}")
    public ResponseEntity<Void> updateRelease(
            @AuthenticationPrincipal UserId userId,
            @PathVariable Long releaseId,
            @Valid @RequestBody UpdateReleaseRequest request
    ) {
        updateReleaseUseCase.updateRelease(request.toCommand(releaseId, userId));
        return ResponseEntity.noContent().build();
    }
}
