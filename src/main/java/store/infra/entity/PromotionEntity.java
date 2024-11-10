package store.infra.entity;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

public record PromotionEntity(
        String name,
        int buy,
        int get,
        LocalDate startDate,
        LocalDate endDate
) implements DatabaseEntity {

    private static final String NAME_COLUMN = "name";
    private static final String BUY_COLUMN = "buy";
    private static final String GET_COLUMN = "get";
    private static final String START_DATE_COLUMN = "start_date";
    private static final String END_DATE_COLUMN = "end_date";

    public static PromotionEntity from(final Map<String, String> dataMap) {
        return new PromotionEntity(
                dataMap.get(NAME_COLUMN),
                Integer.parseInt(dataMap.get(BUY_COLUMN)),
                Integer.parseInt(dataMap.get(GET_COLUMN)),
                LocalDate.parse(dataMap.get(START_DATE_COLUMN)),
                LocalDate.parse(dataMap.get(END_DATE_COLUMN))
        );
    }

    public boolean isAvailable(final LocalDate now) {
        return startDate.isBefore(now) && now.isBefore(endDate);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PromotionEntity that)) {
            return false;
        }
        return buy == that.buy && get == that.get && Objects.equals(name, that.name) && Objects.equals(
                endDate, that.endDate) && Objects.equals(startDate, that.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, buy, get, startDate, endDate);
    }
}
