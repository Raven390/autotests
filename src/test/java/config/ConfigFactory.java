package config;

import org.aeonbits.owner.Config;

public class ConfigFactory {
    private static final UserConfig CONFIG = org.aeonbits.owner.ConfigFactory.create(UserConfig.class, System.getProperties());

    public static final String
            BASEURL = CONFIG.baseURL(),
            BROWSER = CONFIG.browser();
    public static final Boolean HEADLESS = CONFIG.headlessMode();

    @Config.Sources({ "classpath:config/config.properties","system:properties"})
    public interface UserConfig extends Config {

        @Key("baseURL")
        String baseURL();

        @Key("browser")
        String browser();

        @Key("headlessMode")
        Boolean headlessMode();

        @Key("apiBaseUrl")
        String apiBaseUrl();

        @Key("apiPathGetUser")
        String apiPathGetBaseUrl();

        String BASE_API_URL = CONFIG.apiBaseUrl();
        String PATH_GET_USER = CONFIG.apiPathGetBaseUrl();
    }
}
