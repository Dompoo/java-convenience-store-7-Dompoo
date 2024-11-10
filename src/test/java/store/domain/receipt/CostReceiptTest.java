package store.domain.receipt;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import store.common.dto.response.PurchaseCostResponse;
import store.domain.membership.RatioMembership;
import store.domain.vo.PurchaseResult;

class CostReceiptTest {

    @Test
    void 멤버십_없이_결제정보를_받는다() {
        //given
        CostReceipt sut = new CostReceipt();
        sut.addPurchase(new PurchaseResult("땅콩", 20, 10, 1000, 5));
        sut.addPurchase(new PurchaseResult("빼빼로", 25, 15, 500, 5));

        //when
        PurchaseCostResponse result = sut.buildPurchaseCostResponse();

        //then
        assertThat(result).extracting(
                "originalPurchaseCost", "purchaseAmount", "promotionSaleCost", "membershipSaleCost", "finalPrice"
        ).containsExactly(
                32500, 45, 7500, 0, 25000
        );
    }

    @Test
    void 멤버십을_포함하여_결제정보를_받는다() {
        //given
        CostReceipt sut = new CostReceipt();
        sut.addPurchase(new PurchaseResult("땅콩", 20, 10, 1000, 5));
        sut.addPurchase(new PurchaseResult("빼빼로", 25, 15, 500, 5));
        sut.applyMembership(new RatioMembership());

        //when
        PurchaseCostResponse result = sut.buildPurchaseCostResponse();

        //then
        assertThat(result).extracting(
                "originalPurchaseCost", "purchaseAmount", "promotionSaleCost", "membershipSaleCost", "finalPrice"
        ).containsExactly(
                32500, 45, 7500, 4500, 20500
        );
    }
}
