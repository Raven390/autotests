package uiTests.backoffice.tests.tests;

import org.junit.jupiter.api.Test;
import uiTests.backoffice.tests.apiHelpers.KeycloackAPI;

public class Experiment {
    @Test
    void attempt() {
        KeycloackAPI.getAUTHtoken();
    }
}
