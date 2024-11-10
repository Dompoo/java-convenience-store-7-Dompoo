package store.service.decisionService;

import java.util.function.Supplier;
import store.common.dto.request.PurchaseRequest;
import store.common.exception.StoreExceptions;
import store.domain.Cashier;
import store.domain.Cashier.DecisionSupplier;
import store.domain.DecisionType;
import store.domain.Product;
import store.domain.PurchaseType;
import store.domain.membership.Membership;
import store.domain.membership.NoMembership;
import store.domain.membership.RatioMembership;
import store.infra.repository.Repository;

public class DefaultDecisionService implements DecisionService {

    private final Repository<Product> productRepository;

    public DefaultDecisionService(
            final Repository<Product> productRepository
    ) {
        this.productRepository = productRepository;
    }

    @Override
    public DecisionType getDecisionType(final PurchaseRequest purchaseRequest) {
        Product product = productRepository.findByName(purchaseRequest.productName())
                .orElseThrow(StoreExceptions.PRODUCT_NOT_FOUND::get);
        return DecisionType.of(product, purchaseRequest.purchaseAmount());
    }

    @Override
    public PurchaseType decidePurchaseType(
            final PurchaseRequest purchaseRequest,
            final DecisionType decisionType,
            final DecisionSupplier<Boolean> bringFreeProductDecisionSupplier,
            final DecisionSupplier<Boolean> bringDefaultProductBackDecisionSupplier
    ) {
        Product product = productRepository.findByName(purchaseRequest.productName())
                .orElseThrow(StoreExceptions.PRODUCT_NOT_FOUND::get);
        return Cashier.decidePurchaseType(
                product,
                purchaseRequest.purchaseAmount(),
                decisionType,
                bringFreeProductDecisionSupplier,
                bringDefaultProductBackDecisionSupplier
        );
    }

    @Override
    public Membership decideMembership(final Supplier<Boolean> membershipDecisionSupplier) {
        if (membershipDecisionSupplier.get()) {
            return new RatioMembership();
        }
        return new NoMembership();
    }
}
