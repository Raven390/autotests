package api.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;

public class BaseApiOkHttp {
  public final OkHttpClient httpClient = new OkHttpClient();
  public final ObjectMapper objectMapper = new ObjectMapper();
}
