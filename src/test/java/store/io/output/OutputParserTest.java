package store.io.output;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.common.dto.response.ProductResponse;
import store.common.dto.response.PromotionedProductResponse;
import store.common.dto.response.PurchaseCostResponse;
import store.common.dto.response.PurchasedProductResponse;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.PromotionType;

class OutputParserTest {

    private static final LocalDate pastDate = LocalDate.now().minusDays(10);
    private static final LocalDate futureDate = LocalDate.now().plusDays(10);

    private OutputParser sut;

    @BeforeEach
    void setUp() {
        this.sut = new OutputParser();
    }

    @Nested
    class 상품_목록_파싱_테스트 {

        @Test
        void 상품_목록을_파싱한다() {
            //given
            Promotion promotion1 = new Promotion("콜라1+1", PromotionType.BUY_ONE_GET_ONE, pastDate, futureDate);
            Promotion promotion2 = new Promotion("초코바1+1", PromotionType.BUY_ONE_GET_ONE, pastDate, futureDate);
            Promotion noPromotion = new Promotion("", PromotionType.NO_PROMOTION, pastDate, pastDate);
            List<ProductResponse> productResponses = ProductResponse.fromList(List.of(
                    new Product("콜라", 1500, 0, 10, promotion1),
                    new Product("초코바", 2000, 50, 15, promotion2),
                    new Product("감자", 500, 75, 0, noPromotion)
            ));

            //when
            String result = sut.parseProductResponses(productResponses);

            //then
            Assertions.assertThat(result).contains(
                    "- 감자 500원 75개",
                    "- 초코바 2,000원 15개 초코바1+1",
                    "- 초코바 2,000원 50개",
                    "- 콜라 1,500원 재고 없음",
                    "- 콜라 1,500원 10개 콜라1+1"
            );
        }

        @Test
        void 기본_재고만_있는_상품이_재고가_바닥나면_재고없음으로_처리된다() {
            //given
            Promotion noPromotion = new Promotion("", PromotionType.NO_PROMOTION, pastDate, pastDate);
            List<ProductResponse> productResponses = ProductResponse.fromList(List.of(
                    new Product("감자", 500, 0, 0, noPromotion))
            );

            //when
            String result = sut.parseProductResponses(productResponses);

            //then
            assertThat(result).contains(
                    "- 감자 500원 재고 없음"
            );
        }

        @Test
        void 프로모션_재고가_있는_상품이_모든_재고가_바닥나면_재고없음으로_처리된다() {
            //given
            Promotion promotion = new Promotion("콜라1+1", PromotionType.BUY_ONE_GET_ONE, pastDate, futureDate);

            List<ProductResponse> productResponses = ProductResponse.fromList(List.of(
                    new Product("콜라", 1500, 0, 0, promotion)
            ));

            //when
            String result = sut.parseProductResponses(productResponses);

            //then
            assertThat(result).contains(
                    "- 콜라 1,500원 재고 없음",
                    "- 콜라 1,500원 재고 없음 콜라1+1"
            );
        }

        @Test
        void 두_재고가_모두_있는_상품의_기본_재고가_바닥나면_재고없음으로_처리된다() {
            //given
            Promotion promotion = new Promotion("초코바1+1", PromotionType.BUY_ONE_GET_ONE, pastDate, futureDate);
            List<ProductResponse> productResponses = ProductResponse.fromList(List.of(
                    new Product("초코바", 2000, 0, 15, promotion)
            ));

            //when
            String result = sut.parseProductResponses(productResponses);

            //then
            assertThat(result).contains(
                    "- 초코바 2,000원 재고 없음",
                    "- 초코바 2,000원 15개 초코바1+1"
            );
        }

        @Test
        void 두_재고가_모두_있는_상품의_프로모션_재고가_바닥나면_재고없음으로_처리된다() {
            //given
            Promotion promotion = new Promotion("초코바1+1", PromotionType.BUY_ONE_GET_ONE, pastDate, futureDate);
            List<ProductResponse> productResponses = ProductResponse.fromList(List.of(
                    new Product("초코바", 2000, 50, 0, promotion)
            ));

            //when
            String result = sut.parseProductResponses(productResponses);

            //then
            assertThat(result).contains(
                    "- 초코바 2,000원 재고 없음 초코바1+1",
                    "- 초코바 2,000원 50개"
            );
        }
    }

    @Nested
    class 구매한_상품_목록_파싱_테스트 {

        @Test
        void 구매한_상품_목록을_파싱한다() {
            //given
            List<PurchasedProductResponse> purchasedProducts = List.of(
                    new PurchasedProductResponse("콜라", 10, 10000),
                    new PurchasedProductResponse("초코바", 3, 4500),
                    new PurchasedProductResponse("감자", 4, 2000)
            );

            //when
            String result = sut.parsePurchasedProductsResponses(purchasedProducts);

            //then
            assertThat(getResultWithOneSpace(result)).contains(
                    "상품명 수량 금액",
                    "콜라 10 10,000",
                    "초코바 3 4,500",
                    "감자 4 2,000"
            );
        }

        @Test
        void 구매량이_0이면_취소로_파싱된다() {
            //given
            List<PurchasedProductResponse> purchasedProducts = List.of(
                    new PurchasedProductResponse("콜라", 0, 1000)
            );

            //when
            String result = sut.parsePurchasedProductsResponses(purchasedProducts);

            //then
            assertThat(getResultWithOneSpace(result)).contains(
                    "상품명 수량 금액",
                    "콜라 취소"
            );
        }
    }

    @Nested
    class 프로모션된_상품_목록_파싱_테스트 {

        @Test
        void 프로모션된_상품_목록을_파싱한다() {
            //given
            List<PromotionedProductResponse> promotionedProductResponses = List.of(
                    new PromotionedProductResponse("콜라", 1),
                    new PromotionedProductResponse("감자", 5)
            );

            //when
            String result = sut.parsePromotionedProductsResponses(promotionedProductResponses);

            //then
            assertThat(getResultWithOneSpace(result)).contains(
                    "콜라 1",
                    "감자 5"
            );
        }
    }

    @Nested
    class 최종_계산_파싱_테스트 {

        @Test
        void 최종_계산을_파싱한다() {
            //given
            PurchaseCostResponse purchaseCostResponse = new PurchaseCostResponse(10000, 5, 1000, 300, 8700);

            //when
            String result = sut.parsePurchaseCostResponse(purchaseCostResponse);

            //then
            assertThat(getResultWithOneSpace(result)).contains(
                    "총구매액 5 10,000",
                    "행사할인 -1,000",
                    "멤버십할인 -300",
                    "내실돈 8,700"
            );
        }
    }

    private String getResultWithOneSpace(String result) {
        return result.replaceAll("\\s+", " ");
    }
}