package store.config.io.writer;

import store.io.writer.SystemWriter;
import store.io.writer.Writer;

public class SystemWriterConfig implements WriterConfig {

    private final Writer writer;

    public SystemWriterConfig() {
        this.writer = new SystemWriter();
    }

    @Override
    public Writer getWriter() {
        return this.writer;
    }
}
