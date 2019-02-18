
Fluent logger based on [Google Flogger](https://github.com/google/flogger) that provides conventional error, warn, info, debug, and trace levels,
avoiding use of java.util.logging (JUL) levels in code that doesn't otherwise use JUL.

### 1. Add the dependencies

### 2. Add import for `FluentLogger`
```java
import com.digitalascent.logger.FluentLogger;
```

### 3. Create a `private static final` instance
```java
private static final FluentLogger logger = FluentLogger.forEnclosingClass();
```

### 4. Start Logging:
```java
logger.atError().withCause(exception).log("Log message with: %s", argument);
```

This implementation differs from Google's FluentLogger in these ways:
* provides a different set of level selectors: `atError()`, `atWarn()`, `atInfo()`, `atDebug()`, `atTrace()`
* returns an interface from `FluentLogger.forEnclosingClass()` instead of a concrete implementation
* uses a static default method on the `FluentLogger` interface instead of on the concrete logger instance, allowing the implementation class to change w/o changing calling code
* JDK 8+ is required 


