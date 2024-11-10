package store.infra.entity;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import store.domain.Product;
import store.domain.Promotion;

public record PromotionEntity(
        String name,
        int buy,
        int get,
        LocalDate startDate,
        LocalDate endDate
) implements DatabaseEntity {

    public static PromotionEntity from(final Map<String, String> dataMap) {
        String name = dataMap.get("name");
        int buy = Integer.parseInt(dataMap.get("buy"));
        int get = Integer.parseInt(dataMap.get("get"));
        LocalDate startDate = LocalDate.parse(dataMap.get("start_date"));
        LocalDate endDate = LocalDate.parse(dataMap.get("end_date"));

        return new PromotionEntity(name, buy, get, startDate, endDate);
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
