package store;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static camp.nextstep.edu.missionutils.test.Assertions.assertNowTest;
import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest extends NsTest {

    @Test
    void 파일에_있는_상품_목록을_출력한다() {
        assertSimpleTest(() -> {
            run("[물-1]", "N", "N");
            assertThat(output()).contains(
                    "- 콜라 1,000원 10개 탄산2+1",
                    "- 콜라 1,000원 10개",
                    "- 사이다 1,000원 8개 탄산2+1",
                    "- 사이다 1,000원 7개",
                    "- 오렌지주스 1,800원 9개 MD추천상품",
                    "- 오렌지주스 1,800원 재고 없음",
                    "- 탄산수 1,200원 5개 탄산2+1",
                    "- 탄산수 1,200원 재고 없음",
                    "- 물 500원 10개",
                    "- 비타민워터 1,500원 6개",
                    "- 감자칩 1,500원 5개 반짝할인",
                    "- 감자칩 1,500원 5개",
                    "- 초코바 1,200원 5개 MD추천상품",
                    "- 초코바 1,200원 5개",
                    "- 에너지바 2,000원 5개",
                    "- 정식도시락 6,400원 8개",
                    "- 컵라면 1,700원 1개 MD추천상품",
                    "- 컵라면 1,700원 10개"
            );
        });
    }

    @Test
    void 프로모션이_되지_않는_수량을_그대로_포함하여_구매한다() {
        assertSimpleTest(() -> {
            run("[콜라-10]", "Y", "N", "N");
            assertThat(outputWithNoBlank()).contains(
                    withNoBlank("현재 콜라 1개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"),
                    withNoBlank("총구매액 10 10,000"),
                    withNoBlank("행사할인 -3,000"),
                    withNoBlank("멤버십할인 -0"),
                    withNoBlank("내실돈 7,000")
            );
        });
    }

    @Test
    void 프로모션이_되지_않는_수량을_그대로_포함하여_구매하면_멤버십_할인을_받을_수_있다() {
        assertSimpleTest(() -> {
            run("[콜라-10]", "Y", "Y", "N");
            assertThat(outputWithNoBlank()).contains(
                    withNoBlank("현재 콜라 1개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"),
                    withNoBlank("총구매액 10 10,000"),
                    withNoBlank("행사할인 -3,000"),
                    withNoBlank("멤버십할인 -300"),
                    withNoBlank("내실돈 6,700")
            );
        });
    }

    @Test
    void 프로모션이_되지_않는_수량을_빼고_구매한다() {
        assertSimpleTest(() -> {
            run("[콜라-10]", "N", "N", "N");
            assertThat(outputWithNoBlank()).contains(
                    withNoBlank("현재 콜라 1개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"),
                    withNoBlank("총구매액 9 9,000"),
                    withNoBlank("행사할인 -3,000"),
                    withNoBlank("멤버십할인 -0"),
                    withNoBlank("내실돈 6,000")
            );
        });
    }

    @Test
    void 프로모션이_되지_않는_수량을_빼고_구매하면_멤버십_할인을_받을_수_없다() {
        assertSimpleTest(() -> {
            run("[콜라-10]", "N", "Y", "N");
            assertThat(outputWithNoBlank()).contains(
                    withNoBlank("현재 콜라 1개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"),
                    withNoBlank("총구매액 9 9,000"),
                    withNoBlank("행사할인 -3,000"),
                    withNoBlank("멤버십할인 -0"),
                    withNoBlank("내실돈 6,000")
            );
        });
    }

    @Test
    void 프로모션에_의하여_무료로_받을_수_있는_수량을_포함하여_계산한다() {
        assertSimpleTest(() -> {
            run("[콜라-2]", "Y", "N", "N");
            assertThat(outputWithNoBlank()).contains(
                    withNoBlank("현재 콜라은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"),
                    withNoBlank("총구매액 3 3,000"),
                    withNoBlank("행사할인 -1,000"),
                    withNoBlank("멤버십할인 -0"),
                    withNoBlank("내실돈 2,000")
            );
        });
    }

    @Test
    void 프로모션에_의하여_무료로_받을_수_있는_수량을_포함하여_계산하면_멤버십을_적용받을_수_없다() {
        assertSimpleTest(() -> {
            run("[콜라-2]", "Y", "Y", "N");
            assertThat(outputWithNoBlank()).contains(
                    withNoBlank("현재 콜라은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"),
                    withNoBlank("총구매액 3 3,000"),
                    withNoBlank("행사할인 -1,000"),
                    withNoBlank("멤버십할인 -0"),
                    withNoBlank("내실돈 2,000")
            );
        });
    }

    @Test
    void 프로모션에_의하여_무료로_받을_수_있는_수량을_빼고_계산한다() {
        assertSimpleTest(() -> {
            run("[콜라-2]", "N", "N", "N");
            assertThat(outputWithNoBlank()).contains(
                    withNoBlank("현재 콜라은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"),
                    withNoBlank("총구매액 2 2,000"),
                    withNoBlank("행사할인 -0"),
                    withNoBlank("멤버십할인 -0"),
                    withNoBlank("내실돈 2,000")
            );
        });
    }

    @Test
    void 프로모션에_의하여_무료로_받을_수_있는_수량을_빼고_계산하면_멤버십을_적용받을_수_있다() {
        assertSimpleTest(() -> {
            run("[콜라-2]", "N", "Y", "N");
            assertThat(outputWithNoBlank()).contains(
                    withNoBlank("현재 콜라은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"),
                    withNoBlank("총구매액 2 2,000"),
                    withNoBlank("행사할인 -0"),
                    withNoBlank("멤버십할인 -600"),
                    withNoBlank("내실돈 1,400")
            );
        });
    }

    @Test
    void 멤버십_할인을_받아서_구매한다() {
        assertSimpleTest(() -> {
            run("[콜라-3],[에너지바-5]", "Y", "N");
            assertThat(outputWithNoBlank()).contains(
                    withNoBlank("멤버십 할인을 받으시겠습니까? (Y/N)"),
                    withNoBlank("총구매액 8 13,000"),
                    withNoBlank("행사할인 -1,000"),
                    withNoBlank("멤버십할인 -3,000"),
                    withNoBlank("내실돈 9,000")
            );
        });
    }

    @Test
    void 멤버십_할인을_받지_않고_구매한다() {
        assertSimpleTest(() -> {
            run("[콜라-3],[에너지바-5]", "N", "N");
            assertThat(outputWithNoBlank()).contains(
                    withNoBlank("멤버십 할인을 받으시겠습니까? (Y/N)"),
                    withNoBlank("총구매액 8 13,000"),
                    withNoBlank("행사할인 -1,000"),
                    withNoBlank("멤버십할인 -0"),
                    withNoBlank("내실돈 12,000")
            );
        });
    }

    @Test
    void 여러_개의_일반_상품_구매() {
        assertSimpleTest(() -> {
            run("[비타민워터-3],[물-2],[정식도시락-2]", "N", "N");
            assertThat(outputWithNoBlank()).contains("내실돈18,300");
        });
    }

    @Test
    void 재구매시_업데이트된_파일목록을_보여준다() {
        assertSimpleTest(() -> {
            run("[물-10]", "N", "Y", "[감자칩-2]","N", "N");
            assertThat(output()).contains(
                    "- 물 500원 재고 없음"
            );
        });
    }

    @Test
    void 같은_상품을_여러번_구매한다() {
        assertSimpleTest(() -> {
            run("[비타민워터-3],[비타민워터-2]", "N", "N");
            assertThat(outputWithNoBlank()).contains("내실돈7,500");
        });
    }

    @Test
    void 기간에_해당하지_않는_프로모션을_적용한다() {
        assertNowTest(() -> {
            run("[감자칩-2]", "N", "N");
            assertThat(outputWithNoBlank()).contains("내실돈3,000");
        }, LocalDate.of(2024, 2, 1).atStartOfDay());
    }

    @Test
    void 재고를_초과하여_구매하면_에러메시지가_출력된다() {
        assertSimpleTest(() -> {
            runException("[컵라면-12]", "N", "N");
            assertThat(output()).contains("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        });
    }

    @Test
    void 존재하지_않는_상품을_구매하면_에러메시지가_출력된다() {
        assertSimpleTest(() -> {
            runException("[없는상품이지롱-12]");
            assertThat(output()).contains("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
        });
    }

    @Test
    void 잘못된_형식으로_구매하면_에러메시지가_출력된다() {
        assertSimpleTest(() -> {
            runException("[오류!");
            assertThat(output()).contains("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        });
    }

    @Test
    void 잘못된_형식으로_의사결정하면_에러메시지가_출력된다() {
        assertSimpleTest(() -> {
            runException("[콜라-5]", "오류!");
            assertThat(output()).contains("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
        });
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }

    private String outputWithNoBlank() {
        return output().replaceAll("\\s", "");
    }

    private String withNoBlank(String value) {
        return value.replaceAll("\\s", "");
    }
}
