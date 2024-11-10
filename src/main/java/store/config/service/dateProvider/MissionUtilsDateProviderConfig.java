package store.config.service.dateProvider;

import store.service.dateProvider.DateProvider;
import store.service.dateProvider.MissionUtilsDateProvider;

public class MissionUtilsDateProviderConfig implements DateProviderConfig {

    private final DateProvider dateProvider;

    public MissionUtilsDateProviderConfig() {
        this.dateProvider = new MissionUtilsDateProvider();
    }

    @Override
    public DateProvider getDateProvider() {
        return this.dateProvider;
    }
}
