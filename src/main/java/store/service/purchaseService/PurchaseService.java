package store.service.purchaseService;

import java.util.List;
import java.util.function.Supplier;
import store.common.dto.request.PurchaseRequest;
import store.domain.PurchaseType;
import store.domain.vo.PurchaseResult;

public interface PurchaseService {

    List<PurchaseRequest> getPurchases(Supplier<List<PurchaseRequest>> purchaseRequestsSupplier);

    PurchaseResult purchaseProduct(PurchaseRequest purchaseRequest, PurchaseType purchaseType);
}
