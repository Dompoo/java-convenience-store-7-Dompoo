package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DecisionTypeTest {

    private static final LocalDate pastDate = LocalDate.now().minusDays(10);
    private static final LocalDate futureDate = LocalDate.now().plusDays(10);

    @ParameterizedTest
    @CsvSource({
            "1, 0, BUY_ONE_GET_ONE",
            "2, 0, BUY_ONE_GET_ONE",
            "3, 0, BUY_ONE_GET_ONE",
            "4, 0, BUY_ONE_GET_ONE",
    })
    void 프로모션_재고가_없으면_그냥_구매한다(int purchaseAmount, int promotionStock, String promotionName) {
        //given
        Promotion promotion = new Promotion("우도땅콩축제", PromotionType.valueOf(promotionName), pastDate, futureDate);
        Product product = new Product("땅콩", 1000, 100, promotionStock, promotion);

        //when
        DecisionType result = DecisionType.of(product, purchaseAmount);

        //then
        assertThat(result).isEqualTo(DecisionType.FULL_DEFAULT);
    }

    @ParameterizedTest
    @CsvSource({
            "3, 13, BUY_TWO_GET_ONE",
            "6, 13, BUY_TWO_GET_ONE",
            "9, 13, BUY_TWO_GET_ONE",
            "12, 13, BUY_TWO_GET_ONE",
            "2, 13, BUY_ONE_GET_ONE",
            "4, 13, BUY_ONE_GET_ONE",
            "6, 13, BUY_ONE_GET_ONE",
            "8, 13, BUY_ONE_GET_ONE",
            "10, 13, BUY_ONE_GET_ONE",
            "12, 13, BUY_ONE_GET_ONE",
    })
    void 프로모션_단위에_딱_맞게_구매한다(int purchaseAmount, int promotionStock, String promotionName) {
        //given
        Promotion promotion = new Promotion("우도땅콩축제", PromotionType.valueOf(promotionName), pastDate, futureDate);
        Product product = new Product("땅콩", 1000, 100, promotionStock, promotion);

        //when
        DecisionType result = DecisionType.of(product, purchaseAmount);

        //then
        assertThat(result).isEqualTo(DecisionType.FULL_PROMOTION);
    }

    @ParameterizedTest
    @CsvSource({
            "2, 13, BUY_TWO_GET_ONE",
            "5, 13, BUY_TWO_GET_ONE",
            "8, 13, BUY_TWO_GET_ONE",
            "11, 13, BUY_TWO_GET_ONE",
            "1, 13, BUY_ONE_GET_ONE",
            "3, 13, BUY_ONE_GET_ONE",
            "5, 13, BUY_ONE_GET_ONE",
            "7, 13, BUY_ONE_GET_ONE",
            "9, 13, BUY_ONE_GET_ONE",
            "11, 13, BUY_ONE_GET_ONE",
    })
    void 무료로_프로모션_가져갈_수_있게_구매한다(int purchaseAmount, int promotionStock, String promotionName) {
        //given
        Promotion promotion = new Promotion("우도땅콩축제", PromotionType.valueOf(promotionName), pastDate, futureDate);
        Product product = new Product("땅콩", 1000, 100, promotionStock, promotion);

        //when
        DecisionType result = DecisionType.of(product, purchaseAmount);

        //then
        assertThat(result).isEqualTo(DecisionType.CAN_GET_FREE_PRODUCT);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 13, BUY_TWO_GET_ONE",
            "4, 13, BUY_TWO_GET_ONE",
            "7, 13, BUY_TWO_GET_ONE",
            "10, 13, BUY_TWO_GET_ONE",
            "13, 13, BUY_TWO_GET_ONE",
            "14, 13, BUY_TWO_GET_ONE",
            "15, 13, BUY_TWO_GET_ONE",
            "16, 13, BUY_TWO_GET_ONE",
            "13, 13, BUY_ONE_GET_ONE",
            "14, 13, BUY_ONE_GET_ONE",
            "15, 13, BUY_ONE_GET_ONE",
            "16, 13, BUY_ONE_GET_ONE",
    })
    void 일부는_정가로_구매해야_하도록_구매한다(int purchaseAmount, int promotionStock, String promotionName) {
        //given
        Promotion promotion = new Promotion("우도땅콩축제", PromotionType.valueOf(promotionName), pastDate, futureDate);
        Product product = new Product("땅콩", 1000, 100, promotionStock, promotion);

        //when
        DecisionType result = DecisionType.of(product, purchaseAmount);

        //then
        assertThat(result).isEqualTo(DecisionType.PROMOTION_STOCK_LACK);
    }
}
