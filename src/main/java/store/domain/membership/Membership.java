package store.domain.membership;

import java.util.List;
import store.domain.vo.PurchaseResult;

public interface Membership {

    int calculateMembershipSaleAmount(List<PurchaseResult> purchaseResults);
}
