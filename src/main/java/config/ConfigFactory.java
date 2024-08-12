package config;

import org.aeonbits.owner.Config;

public class ConfigFactory {
    private static final UserConfig CONFIG = org.aeonbits.owner.ConfigFactory.create(UserConfig.class, System.getProperties());

    public static final String
            BASEURL = CONFIG.baseURL();

    @Config.Sources({ "classpath:config/config.properties","system:properties"})
    public interface UserConfig extends Config {

        @Key("baseURL")
        String baseURL();

        @Key("api.base.url")
        String apiBaseUrl();

        @Key("api.path.get.user")
        String apiPathGetBaseUrl();

        String BASE_API_URL = CONFIG.apiBaseUrl();
        String PATH_GET_USER = CONFIG.apiPathGetBaseUrl();
    }
}
