package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.domain.vo.PurchaseInfo;
import store.domain.vo.PurchaseStatus;

class PurchaseTypeTest {

    private static final LocalDate pastDate = LocalDate.now().minusDays(10);
    private static final LocalDate futureDate = LocalDate.now().plusDays(10);

    @ParameterizedTest
    @CsvSource({
            "1, 0, BUY_ONE_GET_ONE, 1, 0, 0",
            "2, 0, BUY_ONE_GET_ONE, 2, 0, 0",
            "3, 0, BUY_ONE_GET_ONE, 3, 0, 0",
            "4, 0, BUY_ONE_GET_ONE, 4, 0, 0",
            "1, 0, BUY_TWO_GET_ONE, 1, 0, 0",
            "2, 0, BUY_TWO_GET_ONE, 2, 0, 0",
            "3, 0, BUY_TWO_GET_ONE, 3, 0, 0",
            "4, 0, BUY_TWO_GET_ONE, 4, 0, 0",
    })
    void 전부_기본_재고에서_구매한다(int purchaseAmount, int promotionStock, String promotionName,
                         int totalPurchase, int promotionGet, int decreasePromotionStock) {
        //given
        Promotion promotion = new Promotion("우도땅콩축제", PromotionType.valueOf(promotionName), pastDate, futureDate);
        Product product = Product.of("땅콩", 1000, 100, promotionStock, promotion);
        PurchaseInfo purchaseInfo = PurchaseInfo.of(product, purchaseAmount);

        //when
        PurchaseStatus result = PurchaseType.FULL_DEFAULT.proceed(purchaseInfo);

        //then
        assertThat(result).extracting(
                "finalPurchaseAmount", "promotionGetAmount", "decreasePromotionStock"
        ).containsExactly(
                totalPurchase, promotionGet, decreasePromotionStock
        );
    }

    @ParameterizedTest
    @CsvSource({
            "3, 13, BUY_TWO_GET_ONE, 3, 1, 3",
            "6, 13, BUY_TWO_GET_ONE, 6, 2, 6",
            "9, 13, BUY_TWO_GET_ONE, 9, 3, 9",
            "12, 13, BUY_TWO_GET_ONE, 12, 4, 12",
            "2, 13, BUY_ONE_GET_ONE, 2, 1, 2",
            "4, 13, BUY_ONE_GET_ONE, 4, 2, 4",
            "6, 13, BUY_ONE_GET_ONE, 6, 3, 6",
            "8, 13, BUY_ONE_GET_ONE, 8, 4, 8",
            "10, 13, BUY_ONE_GET_ONE, 10, 5, 10",
            "12, 13, BUY_ONE_GET_ONE, 12, 6, 12",
    })
    void 전부_프로모션에서_구매한다(int purchaseAmount, int promotionStock, String promotionName,
                        int totalPurchase, int promotionGet, int decreasePromotionStock) {
        //given
        Promotion promotion = new Promotion("우도땅콩축제", PromotionType.valueOf(promotionName), pastDate, futureDate);
        Product product = Product.of("땅콩", 1000, 100, promotionStock, promotion);
        PurchaseInfo purchaseInfo = PurchaseInfo.of(product, purchaseAmount);

        //when
        PurchaseStatus result = PurchaseType.FULL_PROMOTION.proceed(purchaseInfo);

        //then
        assertThat(result).extracting(
                "finalPurchaseAmount", "promotionGetAmount", "decreasePromotionStock"
        ).containsExactly(
                totalPurchase, promotionGet, decreasePromotionStock
        );
    }

    @ParameterizedTest
    @CsvSource({
            "2, 13, BUY_TWO_GET_ONE, 3, 1, 3",
            "5, 13, BUY_TWO_GET_ONE, 6, 2, 6",
            "8, 13, BUY_TWO_GET_ONE, 9, 3, 9",
            "11, 13, BUY_TWO_GET_ONE, 12, 4, 12",
            "1, 13, BUY_ONE_GET_ONE, 2, 1, 2",
            "3, 13, BUY_ONE_GET_ONE, 4, 2, 4",
            "5, 13, BUY_ONE_GET_ONE, 6, 3, 6",
            "7, 13, BUY_ONE_GET_ONE, 8, 4, 8",
            "9, 13, BUY_ONE_GET_ONE, 10, 5, 10",
            "11, 13, BUY_ONE_GET_ONE, 12, 6, 12",
    })
    void 전부_프로모션에서_구매한다_무료상품을_가져온다(int purchaseAmount, int promotionStock, String promotionName,
                                   int totalPurchase, int promotionGet, int decreasePromotionStock) {
        //given
        Promotion promotion = new Promotion("우도땅콩축제", PromotionType.valueOf(promotionName), pastDate, futureDate);
        Product product = Product.of("땅콩", 1000, 100, promotionStock, promotion);
        PurchaseInfo purchaseInfo = PurchaseInfo.of(product, purchaseAmount);

        //when
        PurchaseStatus result = PurchaseType.FULL_PROMOTION_BRING_FREE.proceed(purchaseInfo);

        //then
        assertThat(result).extracting(
                "finalPurchaseAmount", "promotionGetAmount", "decreasePromotionStock"
        ).containsExactly(
                totalPurchase, promotionGet, decreasePromotionStock
        );
    }

    @ParameterizedTest
    @CsvSource({
            "2, 13, BUY_TWO_GET_ONE, 2, 0, 2",
            "5, 13, BUY_TWO_GET_ONE, 5, 1, 5",
            "8, 13, BUY_TWO_GET_ONE, 8, 2, 8",
            "11, 13, BUY_TWO_GET_ONE, 11, 3, 11",
            "1, 13, BUY_ONE_GET_ONE, 1, 0, 1",
            "3, 13, BUY_ONE_GET_ONE, 3, 1, 3",
            "5, 13, BUY_ONE_GET_ONE, 5, 2, 5",
            "7, 13, BUY_ONE_GET_ONE, 7, 3, 7",
            "9, 13, BUY_ONE_GET_ONE, 9, 4, 9",
            "11, 13, BUY_ONE_GET_ONE, 11, 5, 11",
    })
    void 전부_프로모션에서_구매한다_무료상품을_가져오지_않는다(int purchaseAmount, int promotionStock, String promotionName,
                                       int totalPurchase, int promotionGet, int decreasePromotionStock) {
        //given
        Promotion promotion = new Promotion("우도땅콩축제", PromotionType.valueOf(promotionName), pastDate, futureDate);
        Product product = Product.of("땅콩", 1000, 100, promotionStock, promotion);
        PurchaseInfo purchaseInfo = PurchaseInfo.of(product, purchaseAmount);

        //when
        PurchaseStatus result = PurchaseType.FULL_PROMOTION_NOT_BRING_FREE.proceed(purchaseInfo);

        //then
        assertThat(result).extracting(
                "finalPurchaseAmount", "promotionGetAmount", "decreasePromotionStock"
        ).containsExactly(
                totalPurchase, promotionGet, decreasePromotionStock
        );
    }

    @ParameterizedTest
    @CsvSource({
            "1, 13, BUY_TWO_GET_ONE, 0, 0, 0",
            "4, 13, BUY_TWO_GET_ONE, 3, 1, 3",
            "7, 13, BUY_TWO_GET_ONE, 6, 2, 6",
            "10, 13, BUY_TWO_GET_ONE, 9, 3, 9",
            "13, 13, BUY_TWO_GET_ONE, 12, 4, 12",
            "14, 13, BUY_TWO_GET_ONE, 12, 4, 12",
            "15, 13, BUY_TWO_GET_ONE, 12, 4, 12",
            "16, 13, BUY_TWO_GET_ONE, 12, 4, 12",
            "13, 13, BUY_ONE_GET_ONE, 12, 6, 12",
            "14, 13, BUY_ONE_GET_ONE, 12, 6, 12",
            "15, 13, BUY_ONE_GET_ONE, 12, 6, 12",
            "16, 13, BUY_ONE_GET_ONE, 12, 6, 12",
    })
    void 일부만_프로모션에서_구매한다_프로모션_아닌_것은_가져다_놓는다(int purchaseAmount, int promotionStock, String promotionName,
                                            int totalPurchase, int promotionGet, int decreasePromotionStock) {
        //given
        Promotion promotion = new Promotion("우도땅콩축제", PromotionType.valueOf(promotionName), pastDate, futureDate);
        Product product = Product.of("땅콩", 1000, 100, promotionStock, promotion);
        PurchaseInfo purchaseInfo = PurchaseInfo.of(product, purchaseAmount);

        //when
        PurchaseStatus result = PurchaseType.PORTION_PROMOTION_BRING_BACK.proceed(purchaseInfo);

        //then
        assertThat(result).extracting(
                "finalPurchaseAmount", "promotionGetAmount", "decreasePromotionStock"
        ).containsExactly(
                totalPurchase, promotionGet, decreasePromotionStock
        );
    }

    @ParameterizedTest
    @CsvSource({
            "1, 13, BUY_TWO_GET_ONE, 1, 0, 1",
            "4, 13, BUY_TWO_GET_ONE, 4, 1, 4",
            "7, 13, BUY_TWO_GET_ONE, 7, 2, 7",
            "10, 13, BUY_TWO_GET_ONE, 10, 3, 10",
            "13, 13, BUY_TWO_GET_ONE, 13, 4, 13",
            "14, 13, BUY_TWO_GET_ONE, 14, 4, 13",
            "15, 13, BUY_TWO_GET_ONE, 15, 4, 13",
            "16, 13, BUY_TWO_GET_ONE, 16, 4, 13",
            "13, 13, BUY_ONE_GET_ONE, 13, 6, 13",
            "14, 13, BUY_ONE_GET_ONE, 14, 6, 13",
            "15, 13, BUY_ONE_GET_ONE, 15, 6, 13",
            "16, 13, BUY_ONE_GET_ONE, 16, 6, 13",
    })
    void 일부만_프로모션에서_구매한다_프로모션_아닌_것도_그대로_구매한다(int purchaseAmount, int promotionStock, String promotionName,
                                             int totalPurchase, int promotionGet, int decreasePromotionStock) {
        //given
        Promotion promotion = new Promotion("우도땅콩축제", PromotionType.valueOf(promotionName), pastDate, futureDate);
        Product product = Product.of("땅콩", 1000, 100, promotionStock, promotion);
        PurchaseInfo purchaseInfo = PurchaseInfo.of(product, purchaseAmount);

        //when
        PurchaseStatus result = PurchaseType.PORTION_PROMOTION_NOT_BRING_BACK.proceed(purchaseInfo);

        //then
        assertThat(result).extracting(
                "finalPurchaseAmount", "promotionGetAmount", "decreasePromotionStock"
        ).containsExactly(
                totalPurchase, promotionGet, decreasePromotionStock
        );
    }

}