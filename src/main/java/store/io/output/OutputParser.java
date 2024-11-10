package store.io.output;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import store.common.dto.response.ProductResponse;
import store.common.dto.response.PromotionedProductResponse;
import store.common.dto.response.PurchaseCostResponse;
import store.common.dto.response.PurchasedProductResponse;

public class OutputParser {

    private static final String NEW_LINE = "\n";
    private static final String PRICE_FORMAT = "%,d";
    private static final String RECEIPT_OUTPUT_FORMAT = "%-15s\t%-5s\t  %s";

    public String parseProductResponses(final List<ProductResponse> productResponses) {
        return productResponses.stream()
                .sorted()
                .map(OutputParser::mapProductResponseToString)
                .collect(Collectors.joining(NEW_LINE));
    }

    private static String mapProductResponseToString(final ProductResponse productResponse) {
        return String.format("- %s %s원 %s %s",
                productResponse.productName(),
                PRICE_FORMAT.formatted(productResponse.price()),
                getStock(productResponse),
                productResponse.promotionName());
    }

    private static String getStock(final ProductResponse productResponse) {
        int stock = productResponse.stock();
        if (stock == 0) {
            return "재고 없음";
        }
        return stock + "개";
    }

    public String parsePurchasedProductsResponses(final List<PurchasedProductResponse> purchasedProductResponses) {
        return Stream.concat(
                Stream.of(RECEIPT_OUTPUT_FORMAT.formatted("상품명", "수량", "금액")),
                purchasedProductResponses.stream().map(OutputParser::mapPurchasedProductResponseToString)
        ).collect(Collectors.joining(NEW_LINE));
    }

    private static String mapPurchasedProductResponseToString(final PurchasedProductResponse response) {
        return RECEIPT_OUTPUT_FORMAT.formatted(
                response.productName(),
                getPurchasedProductName(response),
                getPurchasePrice(response));
    }

    private static String getPurchasedProductName(final PurchasedProductResponse response) {
        if (response.purchaseAmount() == 0) {
            return "";
        }
        return String.valueOf(response.purchaseAmount());
    }

    private static String getPurchasePrice(final PurchasedProductResponse response) {
        if (response.purchaseAmount() == 0) {
            return "취소";
        }
        return PRICE_FORMAT.formatted(response.originalPrice());
    }

    public String parsePromotionedProductsResponses(
            final List<PromotionedProductResponse> promotionedProductResponses
    ) {
        return promotionedProductResponses.stream()
                .map(OutputParser::mapPromotionedProductResponseToString)
                .collect(Collectors.joining(NEW_LINE));
    }

    private static String mapPromotionedProductResponseToString(final PromotionedProductResponse response) {
        return RECEIPT_OUTPUT_FORMAT.formatted(
                response.productName(),
                response.promotionedAmount(),
                "");
    }

    public String parsePurchaseCostResponse(final PurchaseCostResponse purchaseCostResponse) {
        StringJoiner stringJoiner = new StringJoiner(NEW_LINE);
        stringJoiner.add(mapOriginalPurchaseCost(purchaseCostResponse));
        stringJoiner.add(mapPromotionSaleCost(purchaseCostResponse));
        stringJoiner.add(mapMembershipSaleCost(purchaseCostResponse));
        stringJoiner.add(mapFinalPrice(purchaseCostResponse));
        return stringJoiner.toString();
    }

    private static String mapOriginalPurchaseCost(final PurchaseCostResponse purchaseCostResponse) {
        return RECEIPT_OUTPUT_FORMAT.formatted(
                "총구매액",
                purchaseCostResponse.purchaseAmount(),
                PRICE_FORMAT.formatted(purchaseCostResponse.originalPurchaseCost())
        );
    }

    private static String mapPromotionSaleCost(final PurchaseCostResponse purchaseCostResponse) {
        return RECEIPT_OUTPUT_FORMAT.formatted(
                "행사할인",
                "",
                "-" + PRICE_FORMAT.formatted(purchaseCostResponse.promotionSaleCost())
        );
    }

    private static String mapMembershipSaleCost(final PurchaseCostResponse purchaseCostResponse) {
        return RECEIPT_OUTPUT_FORMAT.formatted(
                "멤버십할인",
                "",
                "-" + PRICE_FORMAT.formatted(purchaseCostResponse.membershipSaleCost())
        );
    }

    private static String mapFinalPrice(final PurchaseCostResponse purchaseCostResponse) {
        return RECEIPT_OUTPUT_FORMAT.formatted(
                "내실돈",
                "",
                " " + PRICE_FORMAT.formatted(purchaseCostResponse.finalPrice())
        );
    }
}
