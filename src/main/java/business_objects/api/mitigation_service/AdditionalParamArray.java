package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * AdditionalParamArray
 */
public class AdditionalParamArray extends AdditionalParamEntry {
    @JsonProperty("value")
    private List<String> value = new ArrayList<>();

    public AdditionalParamArray value(List<String> value) {
        this.value = value;
        return this;
    }

    public AdditionalParamArray addValueItem(String valueItem) {
        this.value.add(valueItem);
        return this;
    }

    /**
     * Get value
     *
     * @return value
     **/
    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdditionalParamArray additionalParamArray = (AdditionalParamArray) o;
        return Objects.equals(this.value, additionalParamArray.value) && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AdditionalParamArray {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
