package store.infra.repository.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.PromotionType;
import store.infra.entity.PromotionEntity;

class PromotionEntityConverterTest {

    private final LocalDate pastDate = LocalDate.of(2000, 5, 17);
    private final LocalDate futureDate = LocalDate.of(2003, 8, 22);

    private PromotionEntityConverter sut;

    @BeforeEach
    void setUp() {
        sut = new PromotionEntityConverter();
    }

    @Test
    void 상품을_프로모션엔티티로_변환한다() {
        //given
        Promotion promotion1 = new Promotion("우도땅콩축제", PromotionType.BUY_ONE_GET_ONE, pastDate, futureDate);
        Promotion promotion2 = new Promotion("빼빼로1+1", PromotionType.BUY_TWO_GET_ONE, pastDate, futureDate);
        Promotion noPromotion = new Promotion("", PromotionType.NO_PROMOTION, pastDate, pastDate);
        List<Product> products = List.of(
                new Product("우도땅콩", 1000, 10, 7, promotion1),
                new Product("빼빼로", 1500, 15, 0, promotion2),
                new Product("감자", 2000, 5, 0, noPromotion)
        );

        //when
        List<PromotionEntity> result = sut.convert(products);

        //then
        assertThat(result).extracting(
                PromotionEntity::name, PromotionEntity::buy, PromotionEntity::get,
                r -> r.startDate().toString(), r -> r.endDate().toString()
        ).containsExactlyInAnyOrder(
                Tuple.tuple("우도땅콩축제", 1, 1, "2000-05-17", "2003-08-22"),
                Tuple.tuple("빼빼로1+1", 2, 1, "2000-05-17", "2003-08-22")
        );
    }
}
