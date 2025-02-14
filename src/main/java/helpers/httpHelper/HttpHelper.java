package helpers.httpHelper;

import io.qameta.allure.Step;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

public class HttpHelper {

    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public HttpHelper() {
        // Regular client
        // this.client = new OkHttpClient();
        // Client with ssl errors ignore
        this.client = getUnsafeOkHttpClient();
        this.objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCertificates = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());

            // Create an SSL socket factory with our all-trusting manager
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            return new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCertificates[0]).hostnameVerifier((
                    hostname, session) -> true) // Disable hostname verification
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Send get request: {url}, {headersMap}, {queryParamsMap}")
    public Response sendGetRequest(String url, Map<String, Object> headersMap, Map<String, Object> queryParamsMap)
            throws IOException {
        HttpUrl httpUrl = buildUrlWithQueryParams(url, queryParamsMap);
        Request request = buildRequestWithHeaders(httpUrl, headersMap).get().build();

        System.out.println("Request to execute: " + request);
        Response response = client.newCall(request).execute();
        System.out.println("Response : " + response);
        System.out.println("Response body: " + response.peekBody(Long.MAX_VALUE).string());
        return response;
    }

    @Step("Send post request: {url}, {headersMap}, {queryParamsMap}")
    public Response sendPostRequest(String url, Map<String, Object> headersMap, Map<String, Object> queryParamsMap,
            Object requestBody) throws IOException {
        HttpUrl httpUrl = buildUrlWithQueryParams(url, queryParamsMap);
        String jsonBody = convertObjectToJson(requestBody);
        System.out.println("Body: " + jsonBody);
        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));

        Request request = buildRequestWithHeaders(httpUrl, headersMap).post(body).build();

        System.out.println("Request to execute: " + request);
        Response response = client.newCall(request).execute();
        System.out.println("Response : " + response);
        System.out.println("Response body: " + response.peekBody(Long.MAX_VALUE).string());

        return response;
    }

    @Step("Send put request: {url}, {headersMap}, {queryParamsMap}")
    public Response sendPutRequest(String url, Map<String, Object> headersMap, Map<String, Object> queryParamsMap,
            Object requestBody) throws IOException {
        HttpUrl httpUrl = buildUrlWithQueryParams(url, queryParamsMap);
        String jsonBody = convertObjectToJson(requestBody);
        System.out.println("Body: " + jsonBody);
        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));

        Request request = buildRequestWithHeaders(httpUrl, headersMap).put(body).build();

        System.out.println("Request to execute: " + request);
        Response response = client.newCall(request).execute();
        System.out.println("Response : " + response);
        System.out.println("Response body: " + response.peekBody(Long.MAX_VALUE).string());

        return response;
    }

    private HttpUrl buildUrlWithQueryParams(String url, Map<String, Object> queryParamsMap) {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        if (queryParamsMap != null) {
            for (Map.Entry<String, Object> entry : queryParamsMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value instanceof List) {
                    // If the value is a List, add each item in the List as a query parameter
                    for (Object item : (List<?>) value) {
                        httpBuilder.addQueryParameter(key, item.toString());
                    }
                } else {
                    // Otherwise, add the value directly
                    httpBuilder.addQueryParameter(key, value.toString());
                }
            }
        }
        return httpBuilder.build();
    }

    private Request.Builder buildRequestWithHeaders(HttpUrl httpUrl, Map<String, Object> headersMap) {
        Request.Builder requestBuilder = new Request.Builder().url(httpUrl);
        if (headersMap != null) {
            for (Map.Entry<String, Object> entry : headersMap.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue().toString());
            }
        }
        return requestBuilder;
    }

    private String convertObjectToJson(Object requestBody) throws IOException {
        try {
            return objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            throw new IOException("Failed to serialize request body to JSON", e);
        }
    }
}