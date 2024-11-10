package store.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PromotionTest {

    @Nested
    class 프로모션_생성_테스트 {

        @Test
        void 정상적으로_생성한다() {
            //given
            String name = "프로모션이름";
            int buy = 1;
            int get = 1;

            //expected
            assertThatCode(() -> Promotion.of(name, buy, get))
                    .doesNotThrowAnyException();
        }

        @Test
        void 이름이_null이면_예외가_발생한다() {
            //given
            String name = null;
            int buy = 1;
            int get = 1;

            //expected
            assertThatThrownBy(() -> Promotion.of(name, buy, get))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }

    @Nested
    class 프로모션_가능_여부_테스트 {

        @Test
        void 프로모션이_존재하지_않는_타입이면_불가능하다() {
            //given
            Promotion sut = Promotion.getNoPromotion();

            //when
            boolean result = sut.hasPromotion();

            //then
            assertThat(result).isFalse();
        }

        @ParameterizedTest
        @CsvSource({
                "1, 1",
                "2, 1",
        })
        void 프로모션이_존재하지_않는_타입이_아니면_가능하다(int buy, int get) {
            //given
            Promotion sut = Promotion.of("프로모션이름", buy, get);

            //when
            boolean result = sut.hasPromotion();

            //then
            assertThat(result).isTrue();
        }
    }
}
