package utils;

import java.util.Optional;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import static utils.Utils.writeLog;

public class TestResultWatcher implements TestWatcher {

    @Override
    public void testSuccessful(ExtensionContext context) {
        writeLog("Test passed: " + context.getDisplayName());
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        writeLog("Test failed: " + context.getDisplayName());
        writeLog("Failure reason: " + cause.getMessage());
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
