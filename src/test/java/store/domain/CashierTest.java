package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CashierTest {

    private static final LocalDate futureDate = LocalDate.now().plusDays(10);
    private static final LocalDate pastDate = LocalDate.now().minusDays(10);

    @ParameterizedTest
    @ValueSource(ints = {
            1, 2, 3, 4
    })
    void 프로모션이_없는_상품을_구매한다(int purchaseAmount) {
        //given
        Promotion promotion = new Promotion("감자1+2", PromotionType.NO_PROMOTION, pastDate, futureDate);
        Product product = new Product("감자", 1000, 10, 11, promotion);

        //when
        PurchaseType result = Cashier.decidePurchaseType(
                product,
                purchaseAmount,
                DecisionType.FULL_DEFAULT,
                (name, count) -> null,
                (name, count) -> null
        );

        //then
        assertThat(result).isEqualTo(PurchaseType.FULL_DEFAULT);
    }

    @ParameterizedTest
    @ValueSource(ints = {
            3, 6, 9
    })
    void 프로모션_단위와_딱_맞게_구매한다(int purchaseAmount) {
        //given
        Promotion promotion = new Promotion("감자1+2", PromotionType.BUY_TWO_GET_ONE, pastDate, futureDate);
        Product product = new Product("감자", 1000, 10, 11, promotion);

        //when
        PurchaseType result = Cashier.decidePurchaseType(
                product,
                purchaseAmount,
                DecisionType.FULL_PROMOTION,
                (name, count) -> null,
                (name, count) -> null
        );

        //then
        assertThat(result).isEqualTo(PurchaseType.FULL_PROMOTION);
    }

    @ParameterizedTest
    @ValueSource(ints = {
            2, 5, 8
    })
    void 무료로_프로모션_상품을_받을_수_있을_때_이를_거절한다(int purchaseAmount) {
        //given
        Promotion promotion = new Promotion("감자1+2", PromotionType.BUY_TWO_GET_ONE, pastDate, futureDate);
        Product product = new Product("감자", 1000, 10, 11, promotion);

        //when
        PurchaseType result = Cashier.decidePurchaseType(
                product,
                purchaseAmount,
                DecisionType.CAN_GET_FREE_PRODUCT,
                (name, count) -> false,
                (name, count) -> null
        );

        //then
        assertThat(result).isEqualTo(PurchaseType.FULL_PROMOTION_NOT_BRING_FREE);
    }

    @ParameterizedTest
    @ValueSource(ints = {
            2, 5, 8
    })
    void 무료로_프로모션_상품을_받을_수_있을_때_이를_승인한다(int purchaseAmount) {
        //given
        Promotion promotion = new Promotion("감자1+2", PromotionType.BUY_TWO_GET_ONE, pastDate, futureDate);
        Product product = new Product("감자", 1000, 10, 11, promotion);

        //when
        PurchaseType result = Cashier.decidePurchaseType(
                product,
                purchaseAmount,
                DecisionType.CAN_GET_FREE_PRODUCT,
                (name, count) -> true,
                (name, count) -> null
        );

        //then
        assertThat(result).isEqualTo(PurchaseType.FULL_PROMOTION_BRING_FREE);
    }

    @ParameterizedTest
    @ValueSource(ints = {
            1, 4, 7, 10, 11, 12, 13
    })
    void 프로모션이_불가능하게_가져왔을_때_돌려놓는다(int purchaseAmount) {
        //given
        Promotion promotion = new Promotion("감자1+2", PromotionType.BUY_TWO_GET_ONE, pastDate, futureDate);
        Product product = new Product("감자", 1000, 10, 11, promotion);

        //when
        PurchaseType result = Cashier.decidePurchaseType(
                product,
                purchaseAmount,
                DecisionType.PROMOTION_STOCK_LACK,
                (name, count) -> null,
                (name, count) -> false
        );

        //then
        assertThat(result).isEqualTo(PurchaseType.PORTION_PROMOTION_BRING_BACK);
    }

    @ParameterizedTest
    @ValueSource(ints = {
            1, 4, 7, 10, 11, 12, 13
    })
    void 프로모션이_불가능하게_가져왔을_때_그대로_구매한다(int purchaseAmount) {
        //given
        Promotion promotion = new Promotion("감자1+2", PromotionType.BUY_TWO_GET_ONE, pastDate, futureDate);
        Product product = new Product("감자", 1000, 10, 11, promotion);

        //when
        PurchaseType result = Cashier.decidePurchaseType(
                product,
                purchaseAmount,
                DecisionType.PROMOTION_STOCK_LACK,
                (name, count) -> null,
                (name, count) -> true
        );

        //then
        assertThat(result).isEqualTo(PurchaseType.PORTION_PROMOTION_NOT_BRING_BACK);
    }
}
