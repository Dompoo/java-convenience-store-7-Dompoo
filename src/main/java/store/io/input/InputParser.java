package store.io.input;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import store.common.dto.request.PurchaseRequest;
import store.common.exception.StoreExceptions;

public class InputParser {

    public boolean parseDecision(final String decisionInput) {
        try {
            return decisionInput.replaceAll("\\s+", "").equals("Y");
        } catch (IllegalArgumentException e) {
            throw StoreExceptions.ILLEGAL_ARGUMENT.get();
        }
    }

    public List<PurchaseRequest> parsePurchases(
            final List<String> purchaseInputs,
            final String purchaseAmountSeparator
    ) {
        AtomicInteger requestOrder = new AtomicInteger();
        List<PurchaseRequest> purchaseRequests = purchaseInputs.stream()
                .map(input -> parseToParchaseRequest(input, purchaseAmountSeparator, requestOrder.getAndIncrement()))
                .toList();

        return mergeSameProducts(purchaseRequests);
    }

    private static PurchaseRequest parseToParchaseRequest(
            final String purchaseInputs,
            final String purchaseAmountSeparator,
            final int requestOrder
    ) {
        try {
            String[] inputs = purchaseInputs
                    .replaceAll("\\s+", "")
                    .replaceAll("[\\[\\]]", "")
                    .split(purchaseAmountSeparator);
            return new PurchaseRequest(inputs[0], Integer.parseInt(inputs[1]), requestOrder);
        } catch (IllegalArgumentException e) {
            throw StoreExceptions.INVALID_PURCHASE_FORMAT.get();
        }
    }

    private List<PurchaseRequest> mergeSameProducts(final List<PurchaseRequest> purchaseRequests) {
        return purchaseRequests.stream()
                .collect(Collectors.groupingBy(
                        PurchaseRequest::productName,
                        Collectors.reducing(PurchaseRequest.getDefaultPurchaseRequest(), PurchaseRequest::merge)))
                .values().stream()
                .sorted()
                .toList();
    }
}
