package store.service.decisionService;

import java.util.function.Supplier;
import store.aop.RetryHandler;
import store.common.dto.request.PurchaseRequest;
import store.domain.Cashier.DecisionSupplier;
import store.domain.DecisionType;
import store.domain.PurchaseType;
import store.domain.membership.Membership;

public class DecisionServiceRetryProxy implements DecisionService {

    private final DecisionService decisionServiceTarget;
    private final RetryHandler retryHandler;

    public DecisionServiceRetryProxy(
            final DecisionService decisionServiceTarget,
            final RetryHandler retryHandler
    ) {
        this.decisionServiceTarget = decisionServiceTarget;
        this.retryHandler = retryHandler;
    }

    @Override
    public DecisionType getDecisionType(final PurchaseRequest purchaseRequest) {
        return retryHandler.tryUntilSuccess(() -> decisionServiceTarget.getDecisionType(purchaseRequest));
    }

    @Override
    public PurchaseType decidePurchaseType(
            final PurchaseRequest purchaseRequest,
            final DecisionType decisionType,
            final DecisionSupplier<Boolean> bringFreeProductDecisionSupplier,
            final DecisionSupplier<Boolean> bringDefaultProductBackDecisionSupplier
    ) {
        return retryHandler.tryUntilSuccess(() -> decisionServiceTarget.decidePurchaseType(
                purchaseRequest,
                decisionType,
                bringFreeProductDecisionSupplier,
                bringDefaultProductBackDecisionSupplier
        ));
    }

    @Override
    public Membership decideMembership(final Supplier<Boolean> membershipDecisionSupplier) {
        return retryHandler.tryUntilSuccess(() -> decisionServiceTarget.decideMembership(membershipDecisionSupplier));
    }
}
