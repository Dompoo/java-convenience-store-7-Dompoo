package store.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PromotionTest {

    private static final LocalDate futureDate = LocalDate.now().plusDays(10);
    private static final LocalDate pastDate = LocalDate.now().minusDays(10);

    @Nested
    class 프로모션_생성_테스트 {

        @Test
        void 정상적으로_생성한다() {
            //given
            String name = "프로모션이름";
            PromotionType promotionType = PromotionType.BUY_ONE_GET_ONE;

            //expected
            assertThatCode(() -> new Promotion(name, promotionType, pastDate, futureDate))
                    .doesNotThrowAnyException();
        }

        @Test
        void 이름이_null이면_예외가_발생한다() {
            //given
            String name = null;
            PromotionType promotionType = PromotionType.BUY_ONE_GET_ONE;

            //expected
            assertThatThrownBy(() -> new Promotion(name, promotionType, pastDate, futureDate))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("잘못된 입력입니다. 다시 입력해 주세요.");
        }

        @Test
        void 타입이_null이면_예외가_발생한다() {
            //given
            String name = "프로모션이름";
            PromotionType promotionType = null;

            //expected
            assertThatThrownBy(() -> new Promotion(name, promotionType, pastDate, futureDate))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("잘못된 입력입니다. 다시 입력해 주세요.");
        }

        @Test
        void 시작날짜가_null이면_예외가_발생한다() {
            //given
            String name = "프로모션이름";
            PromotionType promotionType = PromotionType.BUY_ONE_GET_ONE;

            //expected
            assertThatThrownBy(() -> new Promotion(name, promotionType, null, futureDate))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("잘못된 입력입니다. 다시 입력해 주세요.");
        }

        @Test
        void 종료날짜가_null이면_예외가_발생한다() {
            //given
            String name = "프로모션이름";
            PromotionType promotionType = PromotionType.BUY_ONE_GET_ONE;

            //expected
            assertThatThrownBy(() -> new Promotion(name, promotionType, pastDate, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("잘못된 입력입니다. 다시 입력해 주세요.");
        }

        @Test
        void 종료날짜가_시작날짜보다_미래면_예외가_발생한다() {
            //given
            String name = "프로모션이름";
            PromotionType promotionType = PromotionType.BUY_ONE_GET_ONE;

            //expected
            assertThatThrownBy(() -> new Promotion(name, promotionType, futureDate, pastDate))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }

    @Nested
    class 프로모션_가능_여부_테스트 {

        @Test
        void 프로모션이_존재하지_않는_타입이면_불가능하다() {
            //given
            Promotion sut = new Promotion("프로모션이름", PromotionType.NO_PROMOTION, pastDate, futureDate);

            //when
            boolean result = sut.hasPromotion();

            //then
            assertThat(result).isFalse();
        }

        @ParameterizedTest
        @CsvSource({
                "BUY_ONE_GET_ONE",
                "BUY_TWO_GET_ONE",
        })
        void 프로모션이_존재하지_않는_타입이_아니면_가능하다(String promotionTypeName) {
            //given
            PromotionType promotionType = PromotionType.valueOf(promotionTypeName);
            Promotion sut = new Promotion("프로모션이름", promotionType, pastDate, futureDate);

            //when
            boolean result = sut.hasPromotion();

            //then
            assertThat(result).isTrue();
        }
    }
}
