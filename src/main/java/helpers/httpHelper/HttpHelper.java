package helpers.httpHelper;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

public class HttpHelper {

    private final OkHttpClient client;

    public HttpHelper() {
        this.client = new OkHttpClient();
    }

    public Response sendGetRequest(String url, Map<String, Object> headersMap, Map<String, Object> queryParamsMap) throws IOException {
        // Build the URL with query parameters
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        if (queryParamsMap != null) {
            for (Map.Entry<String, Object> entry : queryParamsMap.entrySet()) {
                httpBuilder.addQueryParameter(entry.getKey(), entry.getValue().toString());
            }
        }

        // Build the request with headers
        Request.Builder requestBuilder = new Request.Builder().url(httpBuilder.build());
        if (headersMap != null) {
            for (Map.Entry<String, Object> entry : headersMap.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue().toString());
            }
        }

        Request request = requestBuilder.build();
        System.out.println("Request to execute: "+ request);

        return client.newCall(request).execute();
    }
}