package store.service.productService;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.common.dto.response.ProductResponse;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.PromotionType;
import store.testUtil.testDouble.ProductRepositoryFake;

class DefaultProductServiceTest {

    private final LocalDate pastDate = LocalDate.now().minusDays(10);
    private final LocalDate futureDate = LocalDate.now().plusDays(10);

    private ProductRepositoryFake productRepositoryFake;
    private DefaultProductService sut;

    @BeforeEach
    void setUp() {
        productRepositoryFake = new ProductRepositoryFake();
        sut = new DefaultProductService(productRepositoryFake);
    }

    @Nested
    class 상품_전체_읽기_테스트 {

        @Test
        void 전체_상품을_읽는다() {
            //given
            Promotion promotion1 = new Promotion("우도땅콩축제", PromotionType.BUY_ONE_GET_ONE, pastDate, futureDate);
            Promotion promotion2 = new Promotion("빼빼로데이", PromotionType.BUY_ONE_GET_ONE, pastDate, futureDate);
            Promotion promotion3 = new Promotion("", PromotionType.NO_PROMOTION, pastDate, pastDate);
            Product product1 = new Product("우도땅콩", 1000, 10, 9, promotion1);
            Product product2 = new Product("빼빼로", 500, 5, 4, promotion2);
            Product product3 = new Product("아이스크림", 1500, 15, 0, promotion3);
            productRepositoryFake.setProducts(product1, product2, product3);

            //when
            List<ProductResponse> result = sut.readAllProducts();

            //then
            assertThat(result).extracting(
                    "productName", "price", "stock", "promotionName"
            ).containsExactlyInAnyOrder(
                    Tuple.tuple("우도땅콩", 1000, 9, "우도땅콩축제"),
                    Tuple.tuple("우도땅콩", 1000, 10, ""),
                    Tuple.tuple("빼빼로", 500, 4, "빼빼로데이"),
                    Tuple.tuple("빼빼로", 500, 5, ""),
                    Tuple.tuple("아이스크림", 1500, 15, "")
            );
        }

        @Test
        void 프로모션_중이나_프로모션_재고가_없는_상품을_읽는다() {
            //given
            Promotion promotion = new Promotion("우도땅콩축제", PromotionType.BUY_ONE_GET_ONE, pastDate, futureDate);
            Product product = new Product("우도땅콩", 1000, 10, 0, promotion);
            productRepositoryFake.setProducts(product);

            //when
            List<ProductResponse> result = sut.readAllProducts();

            //then
            assertThat(result).extracting(
                    "productName", "price", "stock", "promotionName"
            ).containsExactlyInAnyOrder(
                    Tuple.tuple("우도땅콩", 1000, 0, "우도땅콩축제"),
                    Tuple.tuple("우도땅콩", 1000, 10, "")
            );
        }

        @Test
        void 프로모션_중이나_기본_재고가_없는_상품을_읽는다() {
            //given
            Promotion promotion = new Promotion("우도땅콩축제", PromotionType.BUY_ONE_GET_ONE, pastDate, futureDate);
            Product product = new Product("우도땅콩", 1000, 0, 9, promotion);
            productRepositoryFake.setProducts(product);

            //when
            List<ProductResponse> result = sut.readAllProducts();

            //then
            assertThat(result).extracting(
                    "productName", "price", "stock", "promotionName"
            ).containsExactlyInAnyOrder(
                    Tuple.tuple("우도땅콩", 1000, 9, "우도땅콩축제"),
                    Tuple.tuple("우도땅콩", 1000, 0, "")
            );
        }

        @Test
        void 프로모션_중이나_모든_재고가_없는_상품을_읽는다() {
            //given
            Promotion promotion = new Promotion("우도땅콩축제", PromotionType.BUY_ONE_GET_ONE, pastDate, futureDate);
            Product product = new Product("우도땅콩", 1000, 0, 0, promotion);
            productRepositoryFake.setProducts(product);

            //when
            List<ProductResponse> result = sut.readAllProducts();

            //then
            assertThat(result).extracting(
                    "productName", "price", "stock", "promotionName"
            ).containsExactlyInAnyOrder(
                    Tuple.tuple("우도땅콩", 1000, 0, "우도땅콩축제"),
                    Tuple.tuple("우도땅콩", 1000, 0, "")
            );
        }

        @Test
        void 프로모션_중이지_않으며_기본_재고가_없는_상품을_읽는다() {
            //given
            Promotion promotion = new Promotion("", PromotionType.NO_PROMOTION, pastDate, futureDate);
            Product product = new Product("우도땅콩", 1000, 0, 0, promotion);
            productRepositoryFake.setProducts(product);

            //when
            List<ProductResponse> result = sut.readAllProducts();

            //then
            assertThat(result).extracting(
                    "productName", "price", "stock", "promotionName"
            ).containsExactlyInAnyOrder(
                    Tuple.tuple("우도땅콩", 1000, 0, "")
            );
        }
    }
}
