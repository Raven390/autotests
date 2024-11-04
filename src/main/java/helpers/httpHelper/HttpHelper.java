package helpers.httpHelper;

import io.qameta.allure.Step;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class HttpHelper {

    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public HttpHelper() {
        this.client = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Step("Send get request: {url}, {headersMap}, {queryParamsMap}")
    public Response sendGetRequest(String url, Map<String, Object> headersMap, Map<String, Object> queryParamsMap) throws IOException {
        HttpUrl httpUrl = buildUrlWithQueryParams(url, queryParamsMap);
        Request request = buildRequestWithHeaders(httpUrl, headersMap).get().build();

        System.out.println("Request to execute: " + request);
        Response response = client.newCall(request).execute();
        System.out.println("Response : " + response);
        System.out.println("Response body: " + response.peekBody(Long.MAX_VALUE).string());
        return response;
    }

    @Step("Send post request: {url}, {headersMap}, {queryParamsMap}")
    public Response sendPostRequest(String url, Map<String, Object> headersMap, Map<String, Object> queryParamsMap, Object requestBody) throws IOException {
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

    private HttpUrl buildUrlWithQueryParams(String url, Map<String, Object> queryParamsMap) {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        if (queryParamsMap != null) {
            for (Map.Entry<String, Object> entry : queryParamsMap.entrySet()) {
                httpBuilder.addQueryParameter(entry.getKey(), entry.getValue().toString());
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