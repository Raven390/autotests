package business_objects.api.lark.chatHistory;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ByBitRestrictionBotMessage {

    @JsonProperty("title")
    private String title;

    @JsonProperty("elements")
    private List<List<Element>> elements;

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<List<Element>> getElements() {
        return elements;
    }

    public void setElements(List<List<Element>> elements) {
        this.elements = elements;
    }

    public static class Element {

        @JsonProperty("tag")
        private String tag;

        @JsonProperty("text")
        private String text;

        // Getters and setters
        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}

