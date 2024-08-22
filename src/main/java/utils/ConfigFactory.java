package utils;

import org.aeonbits.owner.Config;

public class ConfigFactory {
  private static final UserConfig CONFIG =
      org.aeonbits.owner.ConfigFactory.create(UserConfig.class, System.getProperties());

  public static final String BASEURL = CONFIG.baseURL(),
      BROWSER = CONFIG.browser(),
      BASEURLE2E = CONFIG.baseURLE2E(),
      BASE_API_URL = CONFIG.apiBaseUrl(),
      PATH_GET_USER = CONFIG.apiPathGetBaseUrl(),
      KAFKA_HOST = CONFIG.kafkaHost(),
      KAFKA_PORT = CONFIG.kafkaPort(),
      KAFKA_CORE_INCOMING_TOPIC = CONFIG.kafkaCoreIncomingTopic(),
      KAFKA_CORE_OUTCOMING_TOPIC = CONFIG.kafkaCoreOutcomingTopic(),
      POSTGRE_HOST = CONFIG.postgreLogin(),
      POSTGRE_PASSWORD = CONFIG.postgrePassword(),
      POSTGRE_LOGIN = CONFIG.postgreHost();
  public static final Boolean HEADLESS = CONFIG.headlessMode();

  @Config.Sources({"classpath:config/config.properties", "system:properties"})
  public interface UserConfig extends Config {

    @Key("baseURLE2E")
    String baseURLE2E();

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

    @Key("kafkaHost")
    String kafkaHost();

    @Key("kafkaPort")
    String kafkaPort();

    @Key("kafkaCoreIncomingTopic")
    String kafkaCoreIncomingTopic();

    @Key("kafkaCoreOutcomingTopic")
    String kafkaCoreOutcomingTopic();

    @Key("postgreHost")
    String postgreHost();

    @Key("postgreLogin")
    String postgreLogin();

    @Key("postgrePassword")
    String postgrePassword();
  }
}
