package store.infra.repository.converter;

import store.domain.Product;
import store.domain.Promotion;
import store.infra.entity.PromotionEntity;

public class ProductBuilder {

    private String name;
    private int price;
    private int defaultStock;
    private int promotionStock;
    private Promotion promotion = Promotion.getNoPromotion();

    public ProductBuilder setName(final String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder setPrice(final int price) {
        this.price = price;
        return this;
    }

    public ProductBuilder setDefaultStock(final int defaultStock) {
        this.defaultStock = defaultStock;
        return this;
    }

    public ProductBuilder setPromotionStock(final int promotionStock) {
        this.promotionStock = promotionStock;
        return this;
    }

    public ProductBuilder setPromotion(final PromotionEntity promotionEntity) {
        this.promotion = Promotion.of(promotionEntity.name(), promotionEntity.buy(), promotionEntity.get());
        return this;
    }

    public Product build() {
        return Product.of(name, price, defaultStock, promotionStock, promotion);
    }
}
