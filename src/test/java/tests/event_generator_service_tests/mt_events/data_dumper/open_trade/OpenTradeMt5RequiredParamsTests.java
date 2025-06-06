package tests.event_generator_service_tests.mt_events.data_dumper.open_trade;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import tests.TestBaseKafka;

import static utils.Constants.*;
import static utils.Constants.LAYER_API;
import static utils.Constants.SUITE_EVENT_GENERATOR_SERVICE;

@Disabled
@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_DATA_DUMPER_OPEN_TRADE_EVENT)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class OpenTradeMt5RequiredParamsTests extends TestBaseKafka {
}
