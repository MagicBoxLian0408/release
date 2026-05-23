package kr.magicbox.release.application.port.in;

import kr.magicbox.release.domain.vo.ReleaseId;

public interface EndSaleUseCase {
    void endSale(ReleaseId releaseId);
}
