package store.io.input;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.common.dto.request.PurchaseRequest;

class InputParserTest {

    private InputParser sut;

    @BeforeEach
    void setUp() {
        this.sut = new InputParser();
    }

    @Nested
    class YN응답_파싱_테스트 {

        @Test
        void Y응답을_파싱한다() {
            //given
            String input = "Y";

            //when
            boolean result = sut.parseDecision(input);

            //then
            assertThat(result).isTrue();
        }

        @Test
        void N응답을_파싱한다() {
            //given
            String input = "N";

            //when
            boolean result = sut.parseDecision(input);

            //then
            assertThat(result).isFalse();
        }

        @Test
        void 공백이_있어도_파싱한다() {
            //given
            String input = "   N  ";

            //when
            boolean result = sut.parseDecision(input);

            //then
            assertThat(result).isFalse();
        }
    }

    @Nested
    class 구매_상품_목록_파싱_테스트 {

        @Test
        void 구매_상품_목록을_파싱한다() {
            //given
            List<String> inputs = List.of(
                    "[콜라-1]",
                    "[감자-10]"
            );

            //when
            List<PurchaseRequest> result = sut.parsePurchases(inputs, "-");

            //then
            assertThat(result).extracting(
                    "productName", "purchaseAmount"
            ).containsExactlyInAnyOrder(
                    Tuple.tuple("콜라", 1),
                    Tuple.tuple("감자", 10)
            );
        }

        @Test
        void 공백이_있어도_파싱한다() {
            //given
            List<String> inputs = List.of(
                    "   [ 콜 라 - 1 ]   ",
                    "  [ 감  자 -1  0    ]"
            );

            //when
            List<PurchaseRequest> result = sut.parsePurchases(inputs, "-");

            //then
            assertThat(result).extracting(
                    "productName", "purchaseAmount"
            ).containsExactlyInAnyOrder(
                    Tuple.tuple("콜라", 1),
                    Tuple.tuple("감자", 10)
            );
        }
    }
}
