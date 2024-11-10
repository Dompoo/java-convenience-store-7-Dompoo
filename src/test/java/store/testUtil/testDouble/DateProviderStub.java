package store.testUtil.testDouble;

import java.time.LocalDate;
import store.service.dateProvider.DateProvider;

public class DateProviderStub implements DateProvider {

    private LocalDate localDate;

    public void setDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    @Override
    public LocalDate getDate() {
        return localDate;
    }
}
