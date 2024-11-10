package store.domain.membership;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.domain.vo.PurchaseResult;

class RatioMembershipTest {

    @ParameterizedTest
    @CsvSource({
            "20, 10, 3000",
            "30, 20, 3000",
            "25, 10, 4500",
            "17, 10, 2100",
    })
    void 멤버십을_적용한다(
            int purchaseAmount,
            int promotionedProductAmount,
            int membershipSaleAmount
    ) {
        //given
        RatioMembership sut = new RatioMembership();
        PurchaseResult purchaseResult = new PurchaseResult(
                "땅콩",
                purchaseAmount,
                promotionedProductAmount,
                1000,
                5
        );

        //when
        int result = sut.calculateMembershipSaleAmount(List.of(purchaseResult));

        //then
        Assertions.assertThat(result).isEqualTo(membershipSaleAmount);
    }

    @ParameterizedTest
    @CsvSource({
            "20, 10, 20, 10, 6000",
            "30, 20, 30, 20, 6000",
            "25, 10, 20, 10, 7500",
    })
    void 여러_결과를_통해_한번에_계산한다(
            int purchaseAmount1,
            int promotionedProductAmount1,
            int purchaseAmoun2,
            int promotionedProductAmount2,
            int membershipSaleAmount
    ) {
        //given
        RatioMembership sut = new RatioMembership();
        List<PurchaseResult> purchaseResults = List.of(
                new PurchaseResult("땅콩", purchaseAmount1, promotionedProductAmount1, 1000, 5),
                new PurchaseResult("빼빼로", purchaseAmoun2, promotionedProductAmount2, 1000, 5)
        );

        //when
        int result = sut.calculateMembershipSaleAmount(purchaseResults);

        //then
        Assertions.assertThat(result).isEqualTo(membershipSaleAmount);
    }

    @ParameterizedTest
    @CsvSource({
            "200, 0, 8000",
            "50, 10, 8000",
            "27, 0, 8000",
    })
    void 한도를_초과하게_구매하면_한도만큼만_할인된다(
            int purchaseAmount,
            int promotionedProductAmount,
            int membershipSaleAmount
    ) {
        //given
        RatioMembership sut = new RatioMembership();
        PurchaseResult purchaseResult = new PurchaseResult(
                "땅콩",
                purchaseAmount,
                promotionedProductAmount,
                1000,
                5
        );

        //when
        int result = sut.calculateMembershipSaleAmount(List.of(purchaseResult));

        //then
        Assertions.assertThat(result).isEqualTo(membershipSaleAmount);
    }
}
