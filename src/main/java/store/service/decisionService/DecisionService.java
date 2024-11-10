package store.service.decisionService;

import java.util.function.Supplier;
import store.common.dto.request.PurchaseRequest;
import store.domain.Cashier.DecisionSupplier;
import store.domain.DecisionType;
import store.domain.PurchaseType;
import store.domain.membership.Membership;

public interface DecisionService {

    DecisionType getDecisionType(PurchaseRequest purchaseRequest);

    PurchaseType decidePurchaseType(
            PurchaseRequest purchaseRequest,
            DecisionType decisionType,
            DecisionSupplier<Boolean> bringFreeProductDecisionSupplier,
            DecisionSupplier<Boolean> bringDefaultProductBackDecisionSupplier
    );

    Membership decideMembership(
            Supplier<Boolean> membershipDecisionSupplier
    );
}
