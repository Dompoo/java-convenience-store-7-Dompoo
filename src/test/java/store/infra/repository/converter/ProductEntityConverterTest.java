package store.infra.repository.converter;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.PromotionType;
import store.infra.entity.ProductEntity;

class ProductEntityConverterTest {

    private final LocalDate pastDate = LocalDate.now().minusDays(10);
    private final LocalDate futureDate = LocalDate.now().plusDays(10);

    private ProductEntityConverter sut;

    @BeforeEach
    void setUp() {
        sut = new ProductEntityConverter();
    }

    @Test
    void 상품을_상품엔티티로_변환한다() {
        //given
        Promotion promotion1 = new Promotion("우도땅콩축제", PromotionType.BUY_ONE_GET_ONE, pastDate, futureDate);
        Promotion promotion2 = new Promotion("빼빼로1+1", PromotionType.BUY_ONE_GET_ONE, pastDate, futureDate);
        Promotion noPromotion = new Promotion("", PromotionType.NO_PROMOTION, pastDate, pastDate);
        List<Product> products = List.of(
                new Product("우도땅콩", 1000, 10, 7, promotion1),
                new Product("빼빼로", 1500, 15, 0, promotion2),
                new Product("감자", 2000, 5, 0, noPromotion)
        );

        //when
        List<ProductEntity> result = sut.convert(products);

        //then
        assertThat(result).extracting(
                "name", "price", "quantity", "promotionName"
        ).containsExactlyInAnyOrder(
                Tuple.tuple("우도땅콩", 1000, 10, "null"),
                Tuple.tuple("우도땅콩", 1000, 7, "우도땅콩축제"),
                Tuple.tuple("빼빼로", 1500, 15, "null"),
                Tuple.tuple("빼빼로", 1500, 0, "빼빼로1+1"),
                Tuple.tuple("감자", 2000, 5, "null")
        );
    }
}
