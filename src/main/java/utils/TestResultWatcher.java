package utils;

import static utils.Utils.writeLog;

import java.util.Optional;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public class TestResultWatcher implements TestWatcher, AfterTestExecutionCallback, AfterEachCallback {

    private static final ThreadLocal<Boolean> FAILED = ThreadLocal.withInitial(() -> false);

    public static boolean isFailed() {
        return Boolean.TRUE.equals(FAILED.get());
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        FAILED.set(context.getExecutionException().isPresent());
    }

    @Override
    public void afterEach(ExtensionContext context) {
        FAILED.remove();
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        writeLog("Test passed: " + context.getDisplayName());
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        writeLog("Test failed: " + context.getDisplayName());
        writeLog("Failure reason: " + (cause == null ? "unknown" : cause.getMessage()));
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        writeLog("Test aborted: " + context.getDisplayName());
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        writeLog("Test disabled: " + context.getDisplayName());
    }
}
