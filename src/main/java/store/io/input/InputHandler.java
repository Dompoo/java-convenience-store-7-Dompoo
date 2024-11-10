package store.io.input;

import java.util.List;
import store.common.dto.request.PurchaseRequest;
import store.io.reader.Reader;
import store.io.writer.Writer;

public class InputHandler {

    private static final String PURCHASE_SEPARATOR = ",";
    private static final String PURCHASE_AMOUNT_SEPARATOR = "-";

    private final Reader reader;
    private final Writer writer;
    private final InputParser inputParser;
    private final InputValidator inputValidator;

    public InputHandler(
            final Reader reader,
            final Writer writer,
            final InputParser inputParser,
            final InputValidator inputValidator
    ) {
        this.reader = reader;
        this.writer = writer;
        this.inputParser = inputParser;
        this.inputValidator = inputValidator;
    }

    public List<PurchaseRequest> handlePurchases() {
        writer.writeLine("\n구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        List<String> purchases = reader.readLineAsStrings(PURCHASE_SEPARATOR);
        inputValidator.validatePurchases(purchases);
        return inputParser.parsePurchases(purchases, PURCHASE_AMOUNT_SEPARATOR);
    }

    public boolean handleFreeProductDecision(final String productName, final int freeCount) {
        writer.writeLine("\n현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"
                .formatted(productName, freeCount));
        String freeProductDecision = reader.readLineAsString();
        inputValidator.validateDecision(freeProductDecision);
        return inputParser.parseDecision(freeProductDecision);
    }

    public boolean handleBringDefaultProductBackDecision(final String productName, final int noPromotionCount) {
        writer.writeLine("\n현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"
                .formatted(productName, noPromotionCount));
        String bringDefaultProductBackDecision = reader.readLineAsString();
        inputValidator.validateDecision(bringDefaultProductBackDecision);
        return inputParser.parseDecision(bringDefaultProductBackDecision);
    }

    public boolean handleMembershipDecision() {
        writer.writeLine("\n멤버십 할인을 받으시겠습니까? (Y/N)");
        String membershipDecision = reader.readLineAsString();
        inputValidator.validateDecision(membershipDecision);
        return inputParser.parseDecision(membershipDecision);
    }

    public boolean handleRePuchase() {
        writer.writeLine("\n감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        String rePurchaseDecision = reader.readLineAsString();
        inputValidator.validateDecision(rePurchaseDecision);
        return inputParser.parseDecision(rePurchaseDecision);
    }
}
