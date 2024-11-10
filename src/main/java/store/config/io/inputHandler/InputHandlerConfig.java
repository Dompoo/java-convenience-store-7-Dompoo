package store.config.io.inputHandler;

import store.config.io.reader.ReaderConfig;
import store.config.io.writer.WriterConfig;
import store.io.input.InputHandler;
import store.io.input.InputParser;
import store.io.input.InputValidator;

public class InputHandlerConfig {

    private final InputHandler inputHandler;

    public InputHandlerConfig(final ReaderConfig readerConfig, final WriterConfig writerConfig) {
        this.inputHandler = new InputHandler(
                readerConfig.getReader(),
                writerConfig.getWriter(),
                new InputParser(),
                new InputValidator()
        );
    }

    public InputHandler getInputHandler() {
        return this.inputHandler;
    }
}
