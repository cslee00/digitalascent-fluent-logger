package com.digitalascent.logger;


import com.google.common.flogger.backend.LogData;
import com.google.common.flogger.backend.LoggerBackend;
import com.google.common.flogger.backend.system.BackendFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static org.assertj.core.api.Assertions.assertThat;

class FluentLoggerImplTest {

    private static TestLoggerBackend testLoggerBackend;
    private static FluentLogger logger;

    public static class TestLoggerBackendFactory extends BackendFactory {

        private static final BackendFactory INSTANCE = new TestLoggerBackendFactory();

        @SuppressWarnings("unused")
        public static BackendFactory getInstance() {
            return INSTANCE;
        }

        @Override
        public LoggerBackend create(String loggingClassName) {
            testLoggerBackend = new TestLoggerBackend(loggingClassName);
            return testLoggerBackend;
        }
    }

    private static class TestLoggerBackend extends LoggerBackend {

        private final String name;

        List<LogData> logMessages() {
            return logMessages;
        }

        private final List<LogData> logMessages = new ArrayList<>();

        private TestLoggerBackend(String name) {
            this.name = name;
        }

        @Override
        public String getLoggerName() {
            return name;
        }

        @Override
        public boolean isLoggable(Level lvl) {
            return true;
        }

        @Override
        public void log(LogData data) {
            logMessages.add(data);
        }

        @Override
        public void handleError(RuntimeException error, LogData badData) {

        }
    }

    @BeforeEach
    void setup() {
        System.setProperty("flogger.backend_factory", TestLoggerBackendFactory.class.getName() + "#getInstance");
        logger = FluentLogger.forEnclosingClass();
    }

    @Test
    void testLoggerLogs() {
        logger.atWarn().log("Hello");
        logger.atError().log("Hello");
        logger.atInfo().log("Hello");
        logger.atDebug().log("Hello");
        logger.atTrace().log("Hello");

        assertThat(testLoggerBackend.logMessages()).hasSize(5);
    }

    @Test
    void testLoggerName() {
        assertThat(testLoggerBackend.getLoggerName()).isEqualTo(getClass().getName());
    }

}