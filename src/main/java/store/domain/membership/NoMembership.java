package store.domain.membership;

import java.util.List;
import store.domain.vo.PurchaseResult;

final public class NoMembership implements Membership {

    @Override
    public int calculateMembershipSaleAmount(final List<PurchaseResult> purchaseResults) {
        return 0;
    }
}
