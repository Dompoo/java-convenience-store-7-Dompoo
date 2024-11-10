package store.service.purchaseService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.common.dto.request.PurchaseRequest;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.PromotionType;
import store.domain.PurchaseType;
import store.domain.vo.PurchaseResult;
import store.testUtil.testDouble.ProductRepositoryFake;

class DefaultPurchaseInfoServiceTest {

    private final LocalDate pastDate = LocalDate.now().minusDays(10);
    private final LocalDate futureDate = LocalDate.now().plusDays(10);

    private ProductRepositoryFake productRepositoryFake;
    private DefaultPurchaseService sut;

    @BeforeEach
    void setUp() {
        productRepositoryFake = new ProductRepositoryFake();
        sut = new DefaultPurchaseService(productRepositoryFake);
    }

    @Nested
    class 구매_요청_가져오기_테스트 {

        @Test
        void 재고가_충분한_요청을_가져온다() {
            //given
            Promotion promotion1 = new Promotion("우도땅콩축제", PromotionType.BUY_ONE_GET_ONE, pastDate, futureDate);
            Promotion promotion2 = new Promotion("", PromotionType.NO_PROMOTION, pastDate, pastDate);
            Product product1 = new Product("우도땅콩", 1000, 10, 9, promotion1);
            Product product2 = new Product("빼빼로", 500, 10, 0, promotion2);
            productRepositoryFake.setProducts(product1, product2);
            List<PurchaseRequest> purchaseRequests = List.of(
                    new PurchaseRequest("우도땅콩", 10),
                    new PurchaseRequest("빼빼로", 10)
            );

            //when
            List<PurchaseRequest> result = sut.getPurchases(() -> purchaseRequests);

            //then
            assertThat(result).extracting(
                    "productName", "purchaseAmount"
            ).containsExactlyInAnyOrder(
                    Tuple.tuple("우도땅콩", 10),
                    Tuple.tuple("빼빼로", 10)
            );
        }

        @Test
        void 재고가_부족한_요청을_가져온다() {
            //given
            Promotion promotion1 = new Promotion("우도땅콩축제", PromotionType.BUY_ONE_GET_ONE, pastDate, futureDate);
            Promotion promotion2 = new Promotion("", PromotionType.NO_PROMOTION, pastDate, pastDate);
            Product product1 = new Product("우도땅콩", 1000, 9, 9, promotion1);
            Product product2 = new Product("빼빼로", 500, 9, 0, promotion2);
            productRepositoryFake.setProducts(product1, product2);
            List<PurchaseRequest> purchaseRequests = List.of(
                    new PurchaseRequest("우도땅콩", 10),
                    new PurchaseRequest("빼빼로", 10)
            );

            //expected
            assertThatThrownBy(() -> sut.getPurchases(() -> purchaseRequests))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }

    @Nested
    class 구매_수행_테스트 {

        @ParameterizedTest
        @CsvSource({
                "6, 6, 0, 0",
                "7, 7, 0, 0",
                "8, 8, 0, 0",
                "9, 9, 0, 0",
        })
        void 전부_기본_재고로_구매한다(
                int purchaseAmount,
                int resultPurchaseAmount,
                int promtionedProductAmount,
                int promotionGetAmount
        ) {
            //given
            Promotion promotion = new Promotion("우도땅콩축제", PromotionType.NO_PROMOTION, pastDate, futureDate);
            Product product = new Product("우도땅콩", 1000, 9, 9, promotion);
            productRepositoryFake.setProducts(product);

            //when
            PurchaseResult result = sut.purchaseProduct(
                    new PurchaseRequest("우도땅콩", purchaseAmount),
                    PurchaseType.FULL_DEFAULT
            );

            //then
            assertThat(result).extracting(
                    "productName", "purchaseAmount", "promotionedProductAmount", "price", "promotionGetAmount"
            ).containsExactly(
                    "우도땅콩", resultPurchaseAmount, promtionedProductAmount, 1000, promotionGetAmount
            );
        }

        @ParameterizedTest
        @CsvSource({
                "3, 3, 3, 1",
                "6, 6, 6, 2",
                "9, 9, 9, 3",
        })
        void 전부_프로모션으로_구매한다(
                int purchaseAmount,
                int resultPurchaseAmount,
                int promtionedProductAmount,
                int promotionGetAmount
        ) {
            //given
            Promotion promotion = new Promotion("우도땅콩축제", PromotionType.BUY_TWO_GET_ONE, pastDate, futureDate);
            Product product = new Product("우도땅콩", 1000, 9, 9, promotion);
            productRepositoryFake.setProducts(product);

            //when
            PurchaseResult result = sut.purchaseProduct(
                    new PurchaseRequest("우도땅콩", purchaseAmount),
                    PurchaseType.FULL_PROMOTION
            );

            //then
            assertThat(result).extracting(
                    "productName", "purchaseAmount", "promotionedProductAmount", "price", "promotionGetAmount"
            ).containsExactly(
                    "우도땅콩", resultPurchaseAmount, promtionedProductAmount, 1000, promotionGetAmount
            );
        }

        @ParameterizedTest
        @CsvSource({
                "2, 3, 3, 1",
                "5, 6, 6, 2",
                "8, 9, 9, 3",
        })
        void 무료를_가져와서_프로모션으로_구매한다(
                int purchaseAmount,
                int resultPurchaseAmount,
                int promtionedProductAmount,
                int promotionGetAmount
        ) {
            //given
            Promotion promotion = new Promotion("우도땅콩축제", PromotionType.BUY_TWO_GET_ONE, pastDate, futureDate);
            Product product = new Product("우도땅콩", 1000, 9, 9, promotion);
            productRepositoryFake.setProducts(product);

            //when
            PurchaseResult result = sut.purchaseProduct(
                    new PurchaseRequest("우도땅콩", purchaseAmount),
                    PurchaseType.FULL_PROMOTION_BRING_FREE
            );

            //then
            assertThat(result).extracting(
                    "productName", "purchaseAmount", "promotionedProductAmount", "price", "promotionGetAmount"
            ).containsExactly(
                    "우도땅콩", resultPurchaseAmount, promtionedProductAmount, 1000, promotionGetAmount
            );
        }

        @ParameterizedTest
        @CsvSource({
                "2, 2, 0, 0",
                "5, 5, 3, 1",
                "8, 8, 6, 2",
        })
        void 무료를_가져오지_않고_그대로_결제한다(
                int purchaseAmount,
                int resultPurchaseAmount,
                int promtionedProductAmount,
                int promotionGetAmount
        ) {
            //given
            Promotion promotion = new Promotion("우도땅콩축제", PromotionType.BUY_TWO_GET_ONE, pastDate, futureDate);
            Product product = new Product("우도땅콩", 1000, 9, 9, promotion);
            productRepositoryFake.setProducts(product);

            //when
            PurchaseResult result = sut.purchaseProduct(
                    new PurchaseRequest("우도땅콩", purchaseAmount),
                    PurchaseType.FULL_PROMOTION_NOT_BRING_FREE
            );

            //then
            assertThat(result).extracting(
                    "productName", "purchaseAmount", "promotionedProductAmount", "price", "promotionGetAmount"
            ).containsExactly(
                    "우도땅콩", resultPurchaseAmount, promtionedProductAmount, 1000, promotionGetAmount
            );
        }

        @ParameterizedTest
        @CsvSource({
                "1, 0, 0, 0",
                "4, 3, 3, 1",
                "7, 6, 6, 2",
                "10, 9, 9, 3",
                "11, 9, 9, 3",
                "12, 9, 9, 3",
        })
        void 프로모션에_해당되지_않는_만큼_돌려놓고_결제한다(
                int purchaseAmount,
                int resultPurchaseAmount,
                int promtionedProductAmount,
                int promotionGetAmount
        ) {
            //given
            Promotion promotion = new Promotion("우도땅콩축제", PromotionType.BUY_TWO_GET_ONE, pastDate, futureDate);
            Product product = new Product("우도땅콩", 1000, 9, 9, promotion);
            productRepositoryFake.setProducts(product);

            //when
            PurchaseResult result = sut.purchaseProduct(
                    new PurchaseRequest("우도땅콩", purchaseAmount),
                    PurchaseType.PORTION_PROMOTION_BRING_BACK
            );

            //then
            assertThat(result).extracting(
                    "productName", "purchaseAmount", "promotionedProductAmount", "price", "promotionGetAmount"
            ).containsExactly(
                    "우도땅콩", resultPurchaseAmount, promtionedProductAmount, 1000, promotionGetAmount
            );
        }

        @ParameterizedTest
        @CsvSource({
                "1, 1, 0, 0",
                "4, 4, 3, 1",
                "7, 7, 6, 2",
                "10, 10, 9, 3",
                "11, 11, 9, 3",
                "12, 12, 9, 3",
        })
        void 프로모션에_해당되지_않는_것도_그대로_결제한다(
                int purchaseAmount,
                int resultPurchaseAmount,
                int promtionedProductAmount,
                int promotionGetAmount
        ) {
            //given
            Promotion promotion = new Promotion("우도땅콩축제", PromotionType.BUY_TWO_GET_ONE, pastDate, futureDate);
            Product product = new Product("우도땅콩", 1000, 9, 9, promotion);
            productRepositoryFake.setProducts(product);

            //when
            PurchaseResult result = sut.purchaseProduct(
                    new PurchaseRequest("우도땅콩", purchaseAmount),
                    PurchaseType.PORTION_PROMOTION_NOT_BRING_BACK
            );

            //then
            assertThat(result).extracting(
                    "productName", "purchaseAmount", "promotionedProductAmount", "price", "promotionGetAmount"
            ).containsExactly(
                    "우도땅콩", resultPurchaseAmount, promtionedProductAmount, 1000, promotionGetAmount
            );
        }
    }
}
