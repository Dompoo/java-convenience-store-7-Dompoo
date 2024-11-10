package store.infra.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.domain.Product;
import store.domain.Stock;
import store.infra.entity.ProductEntity;
import store.infra.entity.PromotionEntity;
import store.infra.repository.converter.ProductConverter;

class ProductConverterTest {

    private final LocalDate pastDate = LocalDate.now().minusDays(10);
    private final LocalDate futureDate = LocalDate.now().plusDays(10);
    private ProductConverter sut;

    @BeforeEach
    void setUp() {
        this.sut = new ProductConverter();
    }

    @Nested
    class 상품_변환_테스트 {

        @Test
        void 상품엔티티와_프로모션으로_상품을_만든다() {
            //given
            List<ProductEntity> productEntities = List.of(
                    new ProductEntity("콜라", 1000, 5, ""),
                    new ProductEntity("콜라", 1000, 5, "콜라1+1"),
                    new ProductEntity("감자", 1500, 10, ""),
                    new ProductEntity("땅콩", 1000, 3, ""),
                    new ProductEntity("땅콩", 1000, 3, "과거프로모션"),
                    new ProductEntity("땅콩버터", 2000, 5, "과거프로모션")
            );

            List<PromotionEntity> promotions = List.of(
                    new PromotionEntity("콜라1+1", 1, 1, pastDate, futureDate),
                    new PromotionEntity("초코바2+1", 2, 1, pastDate, futureDate),
                    new PromotionEntity("과거프로모션", 1, 1, pastDate, pastDate)
            );

            //when
            List<Product> result = sut.convert(productEntities, promotions);

            //then
            assertThat(result).extracting(
                    "name", "price", "stock"
            ).containsExactlyInAnyOrder(
                    Tuple.tuple("콜라", 1000, new Stock(5, 5)),
                    Tuple.tuple("감자", 1500, new Stock(10, 0)),
                    Tuple.tuple("땅콩", 1000, new Stock(3, 3)),
                    Tuple.tuple("땅콩버터", 2000, new Stock(0, 5))
            );
        }
    }
}