package config;

import org.aeonbits.owner.Config;

public class ConfigFactory {
    private static final UserConfig CONFIG = org.aeonbits.owner.ConfigFactory.create(UserConfig.class, System.getProperties());

    public static final String
            BASEURL = CONFIG.baseURL();

    @Config.Sources({ "classpath:config/config.properties","system:properties"})

    private interface UserConfig extends Config {

        @Key("baseURL")
        String baseURL();
    }
}
