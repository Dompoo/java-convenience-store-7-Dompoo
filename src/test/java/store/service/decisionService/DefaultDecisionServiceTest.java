package store.service.decisionService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.common.dto.request.PurchaseRequest;
import store.domain.DecisionType;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.PromotionType;
import store.domain.PurchaseType;
import store.domain.membership.Membership;
import store.domain.membership.NoMembership;
import store.domain.membership.RatioMembership;
import store.testUtil.testDouble.ProductRepositoryFake;

class DefaultDecisionServiceTest {

    private final LocalDate pastDate = LocalDate.now().minusDays(10);
    private final LocalDate futureDate = LocalDate.now().plusDays(10);

    private ProductRepositoryFake productRepositoryFake;
    private DefaultDecisionService sut;

    @BeforeEach
    void setUp() {
        productRepositoryFake = new ProductRepositoryFake();
        sut = new DefaultDecisionService(productRepositoryFake);
    }

    @Nested
    class 결정_유형_결정_테스트 {

        @ParameterizedTest
        @ValueSource(ints = {
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        })
        void 프로모션이_없는_상품을_구매하는_유형(int purchaseAmount) {
            //given
            Promotion promotion = new Promotion("", PromotionType.NO_PROMOTION, pastDate, pastDate);
            Product product = new Product("감자", 1000, 10, 13, promotion);
            productRepositoryFake.setProducts(product);
            PurchaseRequest purchaseRequest = new PurchaseRequest("감자", purchaseAmount);

            //when
            DecisionType result = sut.getDecisionType(purchaseRequest);

            //then
            assertThat(result).isEqualTo(DecisionType.FULL_DEFAULT);
        }

        @ParameterizedTest
        @ValueSource(ints = {
                3, 6, 9, 12
        })
        void 프로모션_단위에_딱_맞게_가져오는_유형(int purchaseAmount) {
            //given
            Promotion promotion = new Promotion("감자1+2", PromotionType.BUY_TWO_GET_ONE, pastDate, futureDate);
            Product product = new Product("감자", 1000, 10, 13, promotion);
            productRepositoryFake.setProducts(product);
            PurchaseRequest purchaseRequest = new PurchaseRequest("감자", purchaseAmount);

            //when
            DecisionType result = sut.getDecisionType(purchaseRequest);

            //then
            assertThat(result).isEqualTo(DecisionType.FULL_PROMOTION);
        }

        @ParameterizedTest
        @ValueSource(ints = {
                2, 5, 8, 11
        })
        void 프로모션_상품을_무료로_받을_수_있는_유형(int purchaseAmount) {
            //given
            Promotion promotion = new Promotion("감자1+2", PromotionType.BUY_TWO_GET_ONE, pastDate, futureDate);
            Product product = new Product("감자", 1000, 10, 13, promotion);
            productRepositoryFake.setProducts(product);
            PurchaseRequest purchaseRequest = new PurchaseRequest("감자", purchaseAmount);

            //when
            DecisionType result = sut.getDecisionType(purchaseRequest);

            //then
            assertThat(result).isEqualTo(DecisionType.CAN_GET_FREE_PRODUCT);
        }

        @ParameterizedTest
        @ValueSource(ints = {
                1, 4, 7, 10, 11, 12, 13
        })
        void 프로모션이_있지만_불가능하게_상품을_가져온_경우(int purchaseAmount) {
            //given
            Promotion promotion = new Promotion("감자1+2", PromotionType.BUY_TWO_GET_ONE, pastDate, futureDate);
            Product product = new Product("감자", 1000, 10, 11, promotion);
            productRepositoryFake.setProducts(product);
            PurchaseRequest purchaseRequest = new PurchaseRequest("감자", purchaseAmount);

            //when
            DecisionType result = sut.getDecisionType(purchaseRequest);

            //then
            assertThat(result).isEqualTo(DecisionType.PROMOTION_STOCK_LACK);
        }

        @ParameterizedTest
        @ValueSource(ints = {
                22, 23, 24, 25
        })
        void 전체_재고보다_많이_구매하는_유형(int purchaseAmount) {
            //given
            Promotion promotion = new Promotion("감자1+2", PromotionType.BUY_TWO_GET_ONE, pastDate, futureDate);
            Product product = new Product("감자", 1000, 10, 11, promotion);
            productRepositoryFake.setProducts(product);
            PurchaseRequest purchaseRequest = new PurchaseRequest("감자", purchaseAmount);

            //expected
            assertThatThrownBy(() -> sut.getDecisionType(purchaseRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }

    @Nested
    class 구매_타입_결정_테스트 {

        @ParameterizedTest
        @ValueSource(ints = {
                1, 2, 3, 4
        })
        void 프로모션이_없는_상품을_구매한다(int purchaseAmount) {
            //given
            Promotion promotion = new Promotion("감자1+2", PromotionType.NO_PROMOTION, pastDate, futureDate);
            Product product = new Product("감자", 1000, 10, 11, promotion);
            productRepositoryFake.setProducts(product);

            //when
            PurchaseType result = sut.decidePurchaseType(
                    new PurchaseRequest("감자", purchaseAmount),
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
            productRepositoryFake.setProducts(product);

            //when
            PurchaseType result = sut.decidePurchaseType(
                    new PurchaseRequest("감자", purchaseAmount),
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
            productRepositoryFake.setProducts(product);

            //when
            PurchaseType result = sut.decidePurchaseType(
                    new PurchaseRequest("감자", purchaseAmount),
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
            productRepositoryFake.setProducts(product);

            //when
            PurchaseType result = sut.decidePurchaseType(
                    new PurchaseRequest("감자", purchaseAmount),
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
            productRepositoryFake.setProducts(product);

            //when
            PurchaseType result = sut.decidePurchaseType(
                    new PurchaseRequest("감자", purchaseAmount),
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
            productRepositoryFake.setProducts(product);

            //when
            PurchaseType result = sut.decidePurchaseType(
                    new PurchaseRequest("감자", purchaseAmount),
                    DecisionType.PROMOTION_STOCK_LACK,
                    (name, count) -> null,
                    (name, count) -> true
            );

            //then
            assertThat(result).isEqualTo(PurchaseType.PORTION_PROMOTION_NOT_BRING_BACK);
        }
    }

    @Nested
    class 멤버십_결정_테스트 {

        @Test
        void 멤버십을_추가한다() {
            //given

            //when
            Membership membership = sut.decideMembership(() -> true);

            //then
            assertThat(membership).isExactlyInstanceOf(RatioMembership.class);
        }

        @Test
        void 멤버십을_추가하지_않는다() {
            //given

            //when
            Membership membership = sut.decideMembership(() -> false);

            //then
            assertThat(membership).isExactlyInstanceOf(NoMembership.class);
        }
    }
}
