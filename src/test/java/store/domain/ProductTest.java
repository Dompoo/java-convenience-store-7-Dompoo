package store.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class ProductTest {

    private static final LocalDate futureDate = LocalDate.now().plusDays(10);
    private static final LocalDate pastDate = LocalDate.now().minusDays(10);

    @Nested
    class 상품_생성_테스트 {

        @Test
        void 정상적으로_상품을_생성한다() {
            //given
            String name = "우도땅콩";
            int price = 1000;
            int defaultStock = 10;
            int promotionStock = 10;
            Promotion promotion = new Promotion("우도땅콩축제", PromotionType.BUY_ONE_GET_ONE, pastDate, futureDate);

            //expected
            assertThatCode(() -> new Product(name, price, defaultStock, promotionStock, promotion))
                    .doesNotThrowAnyException();
        }

        @Test
        void 이름이_비어있으면_예외가_발생한다() {
            //given
            String name = "";
            int price = 1000;
            int defaultStock = 10;
            int promotionStock = 10;
            Promotion promotion = new Promotion("우도땅콩축제", PromotionType.BUY_ONE_GET_ONE, pastDate, futureDate);

            //expected
            assertThatThrownBy(() -> new Product(name, price, defaultStock, promotionStock, promotion))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("잘못된 입력입니다. 다시 입력해 주세요.");
        }

        @Test
        void 이름이_null이면_예외가_발생한다() {
            //given
            String name = null;
            int price = 1000;
            int defaultStock = 10;
            int promotionStock = 10;
            Promotion promotion = new Promotion("우도땅콩축제", PromotionType.BUY_ONE_GET_ONE, pastDate, futureDate);

            //expected
            assertThatThrownBy(() -> new Product(name, price, defaultStock, promotionStock, promotion))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("잘못된 입력입니다. 다시 입력해 주세요.");
        }

        @Test
        void 가격이_0보다_작으면_예외가_발생한다() {
            //given
            String name = "우도땅콩";
            int price = -1;
            int defaultStock = 10;
            int promotionStock = 10;
            Promotion promotion = new Promotion("우도땅콩축제", PromotionType.BUY_ONE_GET_ONE, pastDate, futureDate);

            //expected
            assertThatThrownBy(() -> new Product(name, price, defaultStock, promotionStock, promotion))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("잘못된 입력입니다. 다시 입력해 주세요.");
        }

        @Test
        void 기본_재고가_0보다_작으면_예외가_발생한다() {
            //given
            String name = "우도땅콩";
            int price = 1000;
            int defaultStock = -1;
            int promotionStock = 10;
            Promotion promotion = new Promotion("우도땅콩축제", PromotionType.BUY_ONE_GET_ONE, pastDate, futureDate);

            //expected
            assertThatThrownBy(() -> new Product(name, price, defaultStock, promotionStock, promotion))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("잘못된 입력입니다. 다시 입력해 주세요.");
        }

        @Test
        void 프로모션_재고가_0보다_작으면_예외가_발생한다() {
            //given
            String name = "우도땅콩";
            int price = 1000;
            int defaultStock = 10;
            int promotionStock = -1;
            Promotion promotion = new Promotion("우도땅콩축제", PromotionType.BUY_ONE_GET_ONE, pastDate, futureDate);

            //expected
            assertThatThrownBy(() -> new Product(name, price, defaultStock, promotionStock, promotion))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }

    @Nested
    class 상품_재고_차감_테스트 {

        @Test
        void 상품_재고를_차감하면_그대로_반영된다() {
            //given
            Promotion promotion = new Promotion("우도땅콩축제", PromotionType.BUY_ONE_GET_ONE, pastDate, futureDate);
            Product sut = new Product("우도땅콩", 1000, 10, 10, promotion);

            //when
            sut.reduceStock(10, 5);

            //then
            assertThat(sut).extracting(
                    "stock.defaultStock", "stock.promotionStock"
            ).containsExactly(5, 5);
        }
    }

    @Nested
    class 프로모션_단위_여부_체크_테스트 {

        @ParameterizedTest
        @ValueSource(ints = {
                3, 6, 9
        })
        void 정확한_프로모션단위로_요청한다(int purchaseAmount) {
            //given
            Promotion promotion = new Promotion("우도땅콩축제", PromotionType.BUY_TWO_GET_ONE, pastDate, futureDate);
            Product sut = new Product("우도땅콩", 1000, 10, 10, promotion);

            //when
            boolean result = sut.isJustRightPromotionUnit(purchaseAmount);

            //then
            assertThat(result).isTrue();
        }

        @ParameterizedTest
        @ValueSource(ints = {
                1, 2, 4, 5, 7, 8, 10, 11, 12, 13
        })
        void 정확한_프로모션단위로가_아니도록_요청한다(int purchaseAmount) {
            //given
            Promotion promotion = new Promotion("우도땅콩축제", PromotionType.BUY_TWO_GET_ONE, pastDate, futureDate);
            Product sut = new Product("우도땅콩", 1000, 10, 10, promotion);

            //when
            boolean result = sut.isJustRightPromotionUnit(purchaseAmount);

            //then
            assertThat(result).isFalse();
        }
    }

    @Nested
    class 무료_상품_여부_체크_테스트 {

        @ParameterizedTest
        @ValueSource(ints = {
                2, 5, 8
        })
        void 무료_상품을_받을_수_있는_수량으로_요청한다(int purchaseAmount) {
            //given
            Promotion promotion = new Promotion("우도땅콩축제", PromotionType.BUY_TWO_GET_ONE, pastDate, futureDate);
            Product sut = new Product("우도땅콩", 1000, 10, 10, promotion);

            //when
            boolean result = sut.canGetFreeProduct(purchaseAmount);

            //then
            assertThat(result).isTrue();
        }

        @ParameterizedTest
        @ValueSource(ints = {
                1, 3, 4, 6, 7, 9, 10, 11, 12, 13
        })
        void 무료_상품을_받을_수_없는_수량으로_요청한다(int purchaseAmount) {
            //given
            Promotion promotion = new Promotion("우도땅콩축제", PromotionType.BUY_TWO_GET_ONE, pastDate, futureDate);
            Product sut = new Product("우도땅콩", 1000, 10, 10, promotion);

            //when
            boolean result = sut.canGetFreeProduct(purchaseAmount);

            //then
            assertThat(result).isFalse();
        }

        @ParameterizedTest
        @ValueSource(ints = {
                1, 2, 3, 4, 5, 6, 7, 8, 9
        })
        void 프로모션이_없다면_항상_불가능하다(int purchaseAmount) {
            //given
            Promotion promotion = new Promotion("", PromotionType.NO_PROMOTION, pastDate, pastDate);
            Product sut = new Product("우도땅콩", 1000, 10, 10, promotion);

            //when
            boolean result = sut.canGetFreeProduct(purchaseAmount);

            //then
            assertThat(result).isFalse();
        }
    }

    @Nested
    class 프로모션으로_얻는_상품_개수_계산_테스트 {

        @ParameterizedTest
        @CsvSource({
                "1, 0",
                "2, 1",
                "3, 0",
                "4, 0",
                "5, 1",
                "6, 0",
                "7, 0",
                "8, 1",
                "9, 0",
                "10, 0",
                "11, 0",
                "12, 0",
                "13, 0",
        })
        void 프로모션으로_추가_무료로_얻을_수_있는_상품_개수를_계산한다(int purchaseAmount, int freeProducts) {
            //given
            Promotion promotion = new Promotion("우도땅콩축제", PromotionType.BUY_TWO_GET_ONE, pastDate, futureDate);
            Product sut = new Product("우도땅콩", 1000, 10, 10, promotion);

            //when
            int result = sut.calculateBringFreeProductCount(purchaseAmount);

            //then
            assertThat(result).isEqualTo(freeProducts);
        }
    }

    @Nested
    class 프로모션으로_구매하지_못하는_상품_개수_계산_테스트 {

        @ParameterizedTest
        @CsvSource({
                "1, 1",
                "2, 2",
                "3, 0",
                "4, 1",
                "5, 2",
                "6, 0",
                "7, 1",
                "8, 2",
                "9, 0",
                "10, 1",
                "11, 2",
                "12, 3",
                "13, 4",
        })
        void 프로모션으로_구매하지_못하는_상품_개수를_계산한다(int purchaseAmount, int noPromtions) {
            //given
            Promotion promotion = new Promotion("우도땅콩축제", PromotionType.BUY_TWO_GET_ONE, pastDate, futureDate);
            Product sut = new Product("우도땅콩", 1000, 10, 10, promotion);

            //when
            int result = sut.calculateNoPromotionsProductCount(purchaseAmount);

            //then
            assertThat(result).isEqualTo(noPromtions);
        }
    }
}
