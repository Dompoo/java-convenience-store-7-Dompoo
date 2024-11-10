package store.config.aop;

import store.aop.RetryHandler;
import store.common.exception.ExceptionHandler;
import store.config.io.writer.WriterConfig;

public class RetryHandlerConfig {

    private final RetryHandler retryHandler;

    public RetryHandlerConfig(final WriterConfig writerConfig) {
        this.retryHandler = new RetryHandler(new ExceptionHandler(writerConfig.getWriter()));
    }

    public RetryHandler getRetryHandler() {
        return this.retryHandler;
    }
}
