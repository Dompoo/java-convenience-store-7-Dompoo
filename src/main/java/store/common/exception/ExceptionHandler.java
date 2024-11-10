package store.common.exception;

import store.io.writer.Writer;

public class ExceptionHandler {

    private static final String EXCEPTION_MESSAGE_PREFIX = "\n[ERROR] ";

    private final Writer writer;

    public ExceptionHandler(final Writer writer) {
        this.writer = writer;
    }

    public void handleException(final Exception exception) {
        writer.writeLine(EXCEPTION_MESSAGE_PREFIX + exception.getMessage());
    }
}
