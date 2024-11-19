package businessObjects.api.clickhouseApiService.getAbuseTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetAbuseTypesResponse {

    @JsonProperty
    public List<responseList> list;

    public static class responseList{
        @JsonProperty("clientId")
        public Integer clientId;

        @JsonProperty("fraudType")
        public String[] fraudType;
    }
}
