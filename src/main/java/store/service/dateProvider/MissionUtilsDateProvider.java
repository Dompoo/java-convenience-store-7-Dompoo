package store.service.dateProvider;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;

public class MissionUtilsDateProvider implements DateProvider {

    @Override
    public LocalDate getDate() {
        return DateTimes.now().toLocalDate();
    }
}
