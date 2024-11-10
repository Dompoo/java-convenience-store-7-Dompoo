package store.config.io.reader;

import store.io.reader.MissionUtilsReader;
import store.io.reader.Reader;

public class MissionUtilsReaderConfig implements ReaderConfig {

    private final Reader reader;

    public MissionUtilsReaderConfig() {
        this.reader = new MissionUtilsReader();
    }

    @Override
    public Reader getReader() {
        return this.reader;
    }
}
