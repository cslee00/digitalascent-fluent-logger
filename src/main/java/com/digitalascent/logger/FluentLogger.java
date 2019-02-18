package com.digitalascent.logger;

import com.google.common.flogger.backend.Platform;
import com.google.common.flogger.util.CallerFinder;

public interface FluentLogger {
    FluentLoggingApi atError();

    FluentLoggingApi atWarn();

    FluentLoggingApi atInfo();

    FluentLoggingApi atDebug();

    FluentLoggingApi atTrace();

    @SuppressWarnings("CatchAndPrintStackTrace")
    static FluentLogger forEnclosingClass() {
        StackTraceElement caller = CallerFinder.findCallerOf(FluentLogger.class, new Throwable(), 0);
        if (caller == null) {
            throw new IllegalStateException("no caller found on the stack for: " + FluentLogger.class);
        }

        // This might contain '$' for inner/nested classes
        String loggingClass = caller.getClassName();
        return new FluentLoggerImpl(Platform.getBackend(loggingClass));
    }
}
