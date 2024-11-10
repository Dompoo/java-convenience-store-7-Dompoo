package store.config.io.outputHandler;

import store.config.io.writer.WriterConfig;
import store.io.output.OutputHandler;
import store.io.output.OutputParser;

public class OutputHandlerConfig {

    private final OutputHandler outputHandler;

    public OutputHandlerConfig(final WriterConfig writerConfig) {
        this.outputHandler = new OutputHandler(
                writerConfig.getWriter(),
                new OutputParser()
        );
    }

    public OutputHandler getOutputHandler() {
        return this.outputHandler;
    }
}
