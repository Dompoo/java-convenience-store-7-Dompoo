package store.domain.receipt;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import store.common.dto.response.PurchasedProductResponse;
import store.domain.vo.PurchaseResult;

class PromotionReceiptTest {

    @Test
    void 구매한_상품_내역을_받는다() {
        //given
        ProductReceipt sut = new ProductReceipt();
        sut.addPurchase(new PurchaseResult("땅콩", 20, 10, 1000, 5));
        sut.addPurchase(new PurchaseResult("빼빼로", 25, 15, 500, 5));

        //when
        List<PurchasedProductResponse> result = sut.buildPurchasedProductResponses();

        //then
        assertThat(result).extracting(
                "productName", "purchaseAmount", "originalPrice"
        ).containsExactlyInAnyOrder(
                Tuple.tuple("땅콩", 20, 20000),
                Tuple.tuple("빼빼로", 25, 12500)
        );
    }
}
