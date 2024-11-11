package store.io.input;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.common.dto.request.PurchaseRequest;
import store.testUtil.testDouble.ReaderFake;
import store.testUtil.testDouble.WriterFake;

class InputHandlerTest {

    private ReaderFake readerFake;
    private WriterFake writerFake;
    private InputHandler sut;

    @BeforeEach
    void setUp() {
        this.readerFake = new ReaderFake();
        this.writerFake = new WriterFake();
        this.sut = new InputHandler(readerFake, writerFake, new InputParser(), new InputValidator());
    }

    @Nested
    class 구매_상품_입력_테스트 {

        @Test
        void 구매할_상품들을_입력받는다() {
            //given
            readerFake.setInputStrings("[콜라-1]", "[초코바-3]", "[햄버거-100]");

            //when
            List<PurchaseRequest> result = sut.handlePurchases();

            //then
            assertThat(result).extracting("productName", "purchaseAmount").containsExactlyInAnyOrder(
                    Tuple.tuple("콜라", 1),
                    Tuple.tuple("초코바", 3),
                    Tuple.tuple("햄버거", 100)
            );
            assertThat(writerFake.getOutput())
                    .contains("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        }

        @Test
        void 같은_상품을_여러개_입력하면_합쳐서_구매된다() {
            //given
            readerFake.setInputStrings("[콜라-1]", "[초코바-3]", "[콜라-5]");

            //when
            List<PurchaseRequest> result = sut.handlePurchases();

            //then
            assertThat(result).extracting("productName", "purchaseAmount").containsExactlyInAnyOrder(
                    Tuple.tuple("콜라", 6),
                    Tuple.tuple("초코바", 3)
            );
        }

        @Test
        void 중간에_공백이_있어도_입력받는다() {
            //given
            readerFake.setInputStrings("[콜라  - 1 ]", "  [ 초코바- 3 ]  ");

            //when
            List<PurchaseRequest> result = sut.handlePurchases();

            //then
            assertThat(result).extracting("productName", "purchaseAmount").containsExactlyInAnyOrder(
                    Tuple.tuple("콜라", 1),
                    Tuple.tuple("초코바", 3)
            );
            assertThat(writerFake.getOutput())
                    .contains("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        }

        @Test
        void 대괄호가_시작되지_않으면_예외가_발생한다() {
            //given
            readerFake.setInputStrings("콜라-1]");

            //expected
            assertThatThrownBy(() -> sut.handlePurchases())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }

        @Test
        void 대괄호가_끝나지_않으면_예외가_발생한다() {
            //given
            readerFake.setInputStrings("[콜라-1");

            //expected
            assertThatThrownBy(() -> sut.handlePurchases())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }

        @Test
        void 대시가_없으면_예외가_발생한다() {
            //given
            readerFake.setInputStrings("[콜라1]");

            //expected
            assertThatThrownBy(() -> sut.handlePurchases())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }

        @Test
        void 개수가_숫자형식이_아니면_예외가_발생한다() {
            //given
            readerFake.setInputStrings("[콜라-a]");

            //expected
            assertThatThrownBy(() -> sut.handlePurchases())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }

    @Nested
    class 공짜_제품_가져오기_여부_입력_테스트 {

        @Test
        void 공짜_제품을_가져오는_입력을_받는다() {
            //given
            String productName = "초코바";
            int freeCount = 3;
            readerFake.setInputs("Y");

            //when
            boolean result = sut.handleFreeProductDecision(productName, freeCount);

            //then
            assertThat(result).isTrue();
            assertThat(writerFake.getOutput())
                    .contains("현재 초코바은(는) 3개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
        }

        @Test
        void 공짜_제품을_가져오지_않는_입력을_받는다() {
            //given
            String productName = "초코바";
            int freeCount = 3;
            readerFake.setInputs("N");

            //when
            boolean result = sut.handleFreeProductDecision(productName, freeCount);

            //then
            assertThat(result).isFalse();
            assertThat(writerFake.getOutput())
                    .contains("현재 초코바은(는) 3개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
        }

        @Test
        void 다른_문자가_입력되면_예외가_발생한다() {
            //given
            String productName = "초코바";
            int freeCount = 3;
            readerFake.setInputs("a");

            //expected
            assertThatThrownBy(() -> sut.handleFreeProductDecision(productName, freeCount))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("잘못된 입력입니다. 다시 입력해 주세요.");
        }

        @Test
        void 여러_자리_문자가_입력되면_예외가_발생한다() {
            //given
            String productName = "초코바";
            int freeCount = 3;
            readerFake.setInputs("aa");

            //expected
            assertThatThrownBy(() -> sut.handleFreeProductDecision(productName, freeCount))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }

    @Nested
    class 비_프로모션_재고_돌려놓기_여부_입력_테스트 {

        @Test
        void 비_프로모션_재고를_돌려놓는다() {
            //given
            String productName = "초코바";
            int freeCount = 3;
            readerFake.setInputs("Y");

            //when
            boolean result = sut.handleBringDefaultProductBackDecision(productName, freeCount);

            //then
            assertThat(result).isTrue();
            assertThat(writerFake.getOutput())
                    .contains("현재 초코바 3개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
        }

        @Test
        void 비_프로모션_재고를_그대로_계산한다() {
            //given
            String productName = "초코바";
            int freeCount = 3;
            readerFake.setInputs("N");

            //when
            boolean result = sut.handleBringDefaultProductBackDecision(productName, freeCount);

            //then
            assertThat(result).isFalse();
            assertThat(writerFake.getOutput())
                    .contains("현재 초코바 3개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
        }

        @Test
        void 다른_문자가_입력되면_예외가_발생한다() {
            //given
            String productName = "초코바";
            int freeCount = 3;
            readerFake.setInputs("a");

            //expected
            assertThatThrownBy(() -> sut.handleBringDefaultProductBackDecision(productName, freeCount))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("잘못된 입력입니다. 다시 입력해 주세요.");
        }

        @Test
        void 여러_자리_문자가_입력되면_예외가_발생한다() {
            //given
            String productName = "초코바";
            int freeCount = 3;
            readerFake.setInputs("aa");

            //expected
            assertThatThrownBy(() -> sut.handleBringDefaultProductBackDecision(productName, freeCount))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }

    @Nested
    class 멤버십_여부_입력_테스트 {

        @Test
        void 멤버십을_추가한다() {
            //given
            readerFake.setInputs("Y");

            //when
            boolean result = sut.handleMembershipDecision();

            //then
            assertThat(result).isTrue();
        }

        @Test
        void 멤버십을_추가하지_않는다() {
            //given
            readerFake.setInputs("Y");

            //when
            boolean result = sut.handleMembershipDecision();

            //then
            assertThat(result).isTrue();
        }

        @Test
        void 다른_문자가_입력되면_예외가_발생한다() {
            //given
            readerFake.setInputs("a");

            //expected
            assertThatThrownBy(() -> sut.handleMembershipDecision())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("잘못된 입력입니다. 다시 입력해 주세요.");
        }

        @Test
        void 여러_자리_문자가_입력되면_예외가_발생한다() {
            //given
            readerFake.setInputs("aa");

            //expected
            assertThatThrownBy(() -> sut.handleMembershipDecision())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }

    @Nested
    class 재구매_여부_입력_테스트 {

        @Test
        void 재구매_한다() {
            //given
            readerFake.setInputs("Y");

            //when
            boolean result = sut.handleRePuchaseDecision();

            //then
            assertThat(result).isTrue();
        }

        @Test
        void 재구매하지_않는다() {
            //given
            readerFake.setInputs("N");

            //when
            boolean result = sut.handleRePuchaseDecision();

            //then
            assertThat(result).isFalse();
        }

        @Test
        void 다른_문자가_입력되면_예외가_발생한다() {
            //given
            readerFake.setInputs("a");

            //expected
            assertThatThrownBy(() -> sut.handleRePuchaseDecision())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("잘못된 입력입니다. 다시 입력해 주세요.");
        }

        @Test
        void 여러_자리_문자가_입력되면_예외가_발생한다() {
            //given
            readerFake.setInputs("aa");

            //expected
            assertThatThrownBy(() -> sut.handleRePuchaseDecision())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }
}
