package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DecisionTypeTest {

    @ParameterizedTest
    @CsvSource({
            "1, 0, 1, 1",
            "2, 0, 1, 1",
            "3, 0, 1, 1",
            "4, 0, 1, 1",
    })
    void 프로모션_재고가_없으면_그냥_구매한다(int purchaseAmount, int promotionStock, int buy, int get) {
        //given
        Promotion promotion = Promotion.of("우도땅콩축제", buy, get);
        Product product = Product.of("땅콩", 1000, 100, promotionStock, promotion);

        //when
        DecisionType result = DecisionType.of(product, purchaseAmount);

        //then
        assertThat(result).isEqualTo(DecisionType.FULL_DEFAULT);
    }

    @ParameterizedTest
    @CsvSource({
            "3, 13, 2, 1",
            "6, 13, 2, 1",
            "9, 13, 2, 1",
            "12, 13, 2, 1",
            "2, 13, 1, 1",
            "4, 13, 1, 1",
            "6, 13, 1, 1",
            "8, 13, 1, 1",
            "10, 13, 1, 1",
            "12, 13, 1, 1",
    })
    void 프로모션_단위에_딱_맞게_구매한다(int purchaseAmount, int promotionStock, int buy, int get) {
        //given
        Promotion promotion = Promotion.of("우도땅콩축제", buy, get);
        Product product = Product.of("땅콩", 1000, 100, promotionStock, promotion);

        //when
        DecisionType result = DecisionType.of(product, purchaseAmount);

        //then
        assertThat(result).isEqualTo(DecisionType.FULL_PROMOTION);
    }

    @ParameterizedTest
    @CsvSource({
            "2, 13, 2, 1",
            "5, 13, 2, 1",
            "8, 13, 2, 1",
            "11, 13, 2, 1",
            "1, 13, 1, 1",
            "3, 13, 1, 1",
            "5, 13, 1, 1",
            "7, 13, 1, 1",
            "9, 13, 1, 1",
            "11, 13, 1, 1",
    })
    void 무료로_프로모션_가져갈_수_있게_구매한다(int purchaseAmount, int promotionStock, int buy, int get) {
        //given
        Promotion promotion = Promotion.of("우도땅콩축제", buy, get);
        Product product = Product.of("땅콩", 1000, 100, promotionStock, promotion);

        //when
        DecisionType result = DecisionType.of(product, purchaseAmount);

        //then
        assertThat(result).isEqualTo(DecisionType.CAN_GET_FREE_PRODUCT);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 13, 2, 1",
            "4, 13, 2, 1",
            "7, 13, 2, 1",
            "10, 13, 2, 1",
            "13, 13, 2, 1",
            "14, 13, 2, 1",
            "15, 13, 2, 1",
            "16, 13, 2, 1",
            "13, 13, 1, 1",
            "14, 13, 1, 1",
            "15, 13, 1, 1",
            "16, 13, 1, 1",
    })
    void 일부는_정가로_구매해야_하도록_구매한다(int purchaseAmount, int promotionStock, int buy, int get) {
        //given
        Promotion promotion = Promotion.of("우도땅콩축제", buy, get);
        Product product = Product.of("땅콩", 1000, 100, promotionStock, promotion);

        //when
        DecisionType result = DecisionType.of(product, purchaseAmount);

        //then
        assertThat(result).isEqualTo(DecisionType.PROMOTION_STOCK_LACK);
    }
}
