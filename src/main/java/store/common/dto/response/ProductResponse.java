package store.common.dto.response;

import java.util.ArrayList;
import java.util.List;
import store.domain.Product;

public record ProductResponse(
        String productName,
        int price,
        int stock,
        String promotionName
) implements Comparable<ProductResponse> {

    public static List<ProductResponse> fromList(final List<Product> products) {
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            String productName = product.getName();
            int price = product.getPrice();
            productResponses.add(new ProductResponse(productName, price, product.getDefaultStock(), ""));
            if (product.hasPromotion()) {
                int promotionStock = product.getPromotionStock();
                String promotionName = product.getPromotion().getName();
                productResponses.add(new ProductResponse(productName, price, promotionStock, promotionName));
            }
        }
        return productResponses;
    }

    @Override
    public int compareTo(final ProductResponse o) {
        int nameCompare = this.productName.compareTo(o.productName);
        if (nameCompare != 0) {
            return nameCompare;
        }
        return Integer.compare(o.promotionName.length(), this.promotionName.length());
    }
}
