package store.io.input;

import java.util.List;
import java.util.regex.Pattern;
import store.common.ParamsValidator;
import store.common.exception.StoreExceptions;

public class InputValidator {

    private static final Pattern DECISION_PATTERN = Pattern.compile("^\\s*[YN]\\s*$");
    private static final Pattern PURCHASE_PATTERN = Pattern.compile(
            "^\\s*\\[\\s*[a-zA-Z가-힣]+\\s*-\\s*[1-9]\\d*\\s*\\]\\s*$"
    );

    public void validateDecision(final String decisionInput) {
        ParamsValidator.validateParamsNotNull(decisionInput);
        if (!DECISION_PATTERN.matcher(decisionInput).matches()) {
            throw StoreExceptions.ILLEGAL_ARGUMENT.get();
        }
    }

    public void validatePurchases(final List<String> purchaseInputs) {
        ParamsValidator.validateParamsNotNull(purchaseInputs);
        validatePurchaseIsEmpty(purchaseInputs);
        purchaseInputs.forEach(this::validatePurchase);
    }

    private void validatePurchaseIsEmpty(final List<String> purchaseInputs) {
        if (purchaseInputs.isEmpty()) {
            throw StoreExceptions.INVALID_PURCHASE_FORMAT.get();
        }
    }

    private void validatePurchase(final String purchaseInput) {
        if (!PURCHASE_PATTERN.matcher(purchaseInput).matches()) {
            throw StoreExceptions.INVALID_PURCHASE_FORMAT.get();
        }
    }
}
