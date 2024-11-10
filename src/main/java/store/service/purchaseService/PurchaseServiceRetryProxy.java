package store.service.purchaseService;

import java.util.List;
import java.util.function.Supplier;
import store.aop.RetryHandler;
import store.common.dto.request.PurchaseRequest;
import store.domain.PurchaseType;
import store.domain.vo.PurchaseResult;

public class PurchaseServiceRetryProxy implements PurchaseService {

    private final PurchaseService purchaseServiceTarget;
    private final RetryHandler retryHandler;

    public PurchaseServiceRetryProxy(final PurchaseService purchaseServiceTarget, final RetryHandler retryHandler) {
        this.purchaseServiceTarget = purchaseServiceTarget;
        this.retryHandler = retryHandler;
    }

    @Override
    public List<PurchaseRequest> getPurchases(final Supplier<List<PurchaseRequest>> purchaseRequestsSupplier) {
        return retryHandler.tryUntilSuccess(() -> purchaseServiceTarget.getPurchases(purchaseRequestsSupplier));
    }

    @Override
    public PurchaseResult purchaseProduct(final PurchaseRequest purchaseRequest, final PurchaseType purchaseType) {
        return retryHandler.tryUntilSuccess(() -> purchaseServiceTarget.purchaseProduct(purchaseRequest, purchaseType)
        );
    }
}
