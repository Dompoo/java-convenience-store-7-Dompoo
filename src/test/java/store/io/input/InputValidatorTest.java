package store.io.input;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class InputValidatorTest {

    private InputValidator sut;

    @BeforeEach
    void setUp() {
        this.sut = new InputValidator();
    }

    @Nested
    class YN응답_검증_테스트 {

        @Test
        void 올바른_Y응답을_검증한다() {
            //given
            String input = "Y";

            //expected
            assertThatCode(() -> sut.validateDecision(input))
                    .doesNotThrowAnyException();
        }

        @Test
        void 올바른_N응답을_검증한다() {
            //given
            String input = "Y";

            //expected
            assertThatCode(() -> sut.validateDecision(input))
                    .doesNotThrowAnyException();
        }

        @Test
        void 공백이_있어도_올바르게_검증된다() {
            //given
            String input = "  Y  ";

            //expected
            assertThatCode(() -> sut.validateDecision(input))
                    .doesNotThrowAnyException();
        }

        @Test
        void 다른_문자를_검증한다() {
            //given
            String input = "a";

            //expected
            assertThatThrownBy(() -> sut.validateDecision(input))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }

    @Nested
    class 구매_요청_검증_테스트 {

        @Test
        void 올바른_구매_요청을_검증한다() {
            //given
            List<String> inputs = List.of(
                    "[콜라-1]",
                    "[감자-5]"
            );

            //expected
            assertThatCode(() -> sut.validatePurchases(inputs))
                    .doesNotThrowAnyException();
        }

        @Test
        void 공백이_있어도_올바르게_검증된다() {
            //given
            List<String> inputs = List.of(
                    "  [ 콜라 - 1] ",
                    "  [ 감자- 5  ] "
            );

            //expected
            assertThatCode(() -> sut.validatePurchases(inputs))
                    .doesNotThrowAnyException();
        }

        @Test
        void 괄호를_열지_않은_요청을_검증한다() {
            //given
            List<String> inputs = List.of(
                    "콜라-1]"
            );

            //expected
            assertThatThrownBy(() -> sut.validatePurchases(inputs))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }

        @Test
        void 괄호를_닫지_않은_요청을_검증한다() {
            //given
            List<String> inputs = List.of(
                    "[콜라-1"
            );

            //expected
            assertThatThrownBy(() -> sut.validatePurchases(inputs))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }

        @Test
        void 대시가_없는_요청을_검증한다() {
            //given
            List<String> inputs = List.of(
                    "[콜라1]"
            );

            //expected
            assertThatThrownBy(() -> sut.validatePurchases(inputs))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }

        @Test
        void 여는_괄호가_여러개인_요청을_검증한다() {
            //given
            List<String> inputs = List.of(
                    "[[콜라-1]"
            );

            //expected
            assertThatThrownBy(() -> sut.validatePurchases(inputs))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }

        @Test
        void 닫는_괄호가_여러개인_요청을_검증한다() {
            //given
            List<String> inputs = List.of(
                    "[콜라-1]]"
            );

            //expected
            assertThatThrownBy(() -> sut.validatePurchases(inputs))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }

        @Test
        void 대시가_여러개인_요청을_검증한다() {
            //given
            List<String> inputs = List.of(
                    "[콜라--1]"
            );

            //expected
            assertThatThrownBy(() -> sut.validatePurchases(inputs))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }
}
