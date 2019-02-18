package com.digitalascent.logger;

import com.google.common.flogger.AbstractLogger;
import com.google.common.flogger.LogContext;
import com.google.common.flogger.LoggingApi;
import com.google.common.flogger.backend.LoggerBackend;
import com.google.common.flogger.backend.Platform;
import com.google.common.flogger.parser.DefaultPrintfMessageParser;
import com.google.common.flogger.parser.MessageParser;

import java.util.logging.Level;

final class FluentLoggerImpl extends AbstractLogger<FluentLoggingApi> implements FluentLogger {

    FluentLoggerImpl(LoggerBackend backend) {
        super(backend);
    }

    @Override
    public FluentLoggingApi at(Level level) {
        boolean isLoggable = isLoggable(level);
        boolean isForced = Platform.shouldForceLogging(getName(), level, isLoggable);
        return (isLoggable || isForced) ? new FluentLoggerImpl.Context(level, isForced) : NO_OP;
    }

    @Override
    public FluentLoggingApi atError() {
        return at(Level.SEVERE);
    }

    @Override
    public FluentLoggingApi atWarn() {
        return at(Level.WARNING);
    }

    @Override
    public FluentLoggingApi atDebug() {
        return at(Level.FINE);
    }

    @Override
    public FluentLoggingApi atTrace() {
        return at(Level.FINEST);
    }

    private static class NoOpFluentLoggingApiImpl extends LoggingApi.NoOp<FluentLoggingApi> implements FluentLoggingApi {
    }

    private static final NoOpFluentLoggingApiImpl NO_OP = new NoOpFluentLoggingApiImpl();

    private final class Context extends LogContext<FluentLoggerImpl, FluentLoggingApi> implements FluentLoggingApi {
        private Context(Level level, boolean isForced) {
            super(level, isForced);
        }

        @Override
        protected FluentLoggerImpl getLogger() {
            return FluentLoggerImpl.this;
        }

        @Override
        protected FluentLoggingApi api() {
            return this;
        }

        @Override
        protected FluentLoggingApi noOp() {
            return NO_OP;
        }

        @Override
        protected MessageParser getMessageParser() {
            return DefaultPrintfMessageParser.getInstance();
        }
    }
}
