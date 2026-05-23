package kr.magicbox.release.adapter.in.web;

import kr.magicbox.release.application.port.in.EndSaleUseCase;
import kr.magicbox.release.application.port.in.StartSaleUseCase;
import kr.magicbox.release.domain.vo.ReleaseId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/release")
@RequiredArgsConstructor
public class AdminReleaseCommandController {

    private final StartSaleUseCase startSaleUseCase;
    private final EndSaleUseCase endSaleUseCase;

    @PostMapping("/{releaseId}/start-sale")
    public ResponseEntity<Void> startSale(@PathVariable Long releaseId) {
        startSaleUseCase.startSale(ReleaseId.of(releaseId));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{releaseId}/end-sale")
    public ResponseEntity<Void> endSale(@PathVariable Long releaseId) {
        endSaleUseCase.endSale(ReleaseId.of(releaseId));
        return ResponseEntity.noContent().build();
    }
}
