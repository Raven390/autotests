package business_objects.api.lark.chatHistory;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

public class ChatHistoryResponse {
    @JsonProperty("code")
    Integer code;

    @JsonProperty("msg")
    String msg;

    @JsonProperty("data")
    LarkApiData data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LarkApiData getData() {
        return data;
    }

    public void setData(LarkApiData data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChatHistoryResponse that = (ChatHistoryResponse) o;
        return Objects.equals(code, that.code) && Objects.equals(msg, that.msg) && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, msg, data);
    }

    @Override
    public String toString() {
        return "ChatHistoryResponse{" + "code=" + code + ", msg='" + msg + '\'' + ", data=" + data + '}';
    }

    public static class LarkApiData {
        @JsonProperty("has_more")
        Boolean hasMore;

        @JsonProperty("page_token")
        String pageToken;

        @JsonProperty("items")
        List<LarkApiDataItem> items;

        public Boolean getHasMore() {
            return hasMore;
        }

        public void setHasMore(Boolean hasMore) {
            this.hasMore = hasMore;
        }

        public String getPageToken() {
            return pageToken;
        }

        public void setPageToken(String pageToken) {
            this.pageToken = pageToken;
        }

        public List<LarkApiDataItem> getItems() {
            return items;
        }

        public void setItems(List<LarkApiDataItem> items) {
            this.items = items;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            LarkApiData that = (LarkApiData) o;
            return Objects.equals(hasMore, that.hasMore)
                    && Objects.equals(pageToken, that.pageToken)
                    && Objects.equals(items, that.items);
        }

        @Override
        public int hashCode() {
            return Objects.hash(hasMore, pageToken, items);
        }

        @Override
        public String toString() {
            return "LarkApiData{" + "hasMore=" + hasMore + ", pageToken='" + pageToken + '\'' + ", items=" + items
                    + '}';
        }
    }

    public static class LarkApiDataItem {
        @JsonProperty("message_id")
        String messageId;

        @JsonProperty("root_id")
        String rootId;

        @JsonProperty("parent_id")
        String parentId;

        @JsonProperty("thread_id")
        String threadId;

        @JsonProperty("msg_type")
        String msgType;

        @JsonProperty("create_time") // Message generation timestamp (in ms)
        Long createTime;

        @JsonProperty("update_time") // Message update timestamp (in ms)
        Long updateTime;

        @JsonProperty("deleted")
        Boolean deleted;

        @JsonProperty("updated")
        Boolean updated;

        @JsonProperty("chat_id")
        String chatId;

        @JsonProperty("sender")
        LarkApiDataItemSender sender;

        @JsonProperty("body")
        LarkApiDataItemBody body;

        @JsonProperty("mentions")
        List<LarkApiDataItemMention> mentions;

        @JsonProperty("upper_message_id")
        String upperMessageId;

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getRootId() {
            return rootId;
        }

        public void setRootId(String rootId) {
            this.rootId = rootId;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getThreadId() {
            return threadId;
        }

        public void setThreadId(String threadId) {
            this.threadId = threadId;
        }

        public String getMsgType() {
            return msgType;
        }

        public void setMsgType(String msgType) {
            this.msgType = msgType;
        }

        public Long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Long createTime) {
            this.createTime = createTime;
        }

        public Long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Long updateTime) {
            this.updateTime = updateTime;
        }

        public Boolean getDeleted() {
            return deleted;
        }

        public void setDeleted(Boolean deleted) {
            this.deleted = deleted;
        }

        public Boolean getUpdated() {
            return updated;
        }

        public void setUpdated(Boolean updated) {
            this.updated = updated;
        }

        public String getChatId() {
            return chatId;
        }

        public void setChatId(String chatId) {
            this.chatId = chatId;
        }

        public LarkApiDataItemSender getSender() {
            return sender;
        }

        public void setSender(LarkApiDataItemSender sender) {
            this.sender = sender;
        }

        public LarkApiDataItemBody getBody() {
            return body;
        }

        public void setBody(LarkApiDataItemBody body) {
            this.body = body;
        }

        public List<LarkApiDataItemMention> getMentions() {
            return mentions;
        }

        public void setMentions(List<LarkApiDataItemMention> mentions) {
            this.mentions = mentions;
        }

        public String getUpperMessageId() {
            return upperMessageId;
        }

        public void setUpperMessageId(String upperMessageId) {
            this.upperMessageId = upperMessageId;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            LarkApiDataItem that = (LarkApiDataItem) o;
            return Objects.equals(messageId, that.messageId)
                    && Objects.equals(rootId, that.rootId)
                    && Objects.equals(parentId, that.parentId)
                    && Objects.equals(threadId, that.threadId)
                    && Objects.equals(msgType, that.msgType)
                    && Objects.equals(createTime, that.createTime)
                    && Objects.equals(updateTime, that.updateTime)
                    && Objects.equals(deleted, that.deleted)
                    && Objects.equals(updated, that.updated)
                    && Objects.equals(chatId, that.chatId)
                    && Objects.equals(sender, that.sender)
                    && Objects.equals(body, that.body)
                    && Objects.equals(mentions, that.mentions)
                    && Objects.equals(upperMessageId, that.upperMessageId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(
                    messageId,
                    rootId,
                    parentId,
                    threadId,
                    msgType,
                    createTime,
                    updateTime,
                    deleted,
                    updated,
                    chatId,
                    sender,
                    body,
                    mentions,
                    upperMessageId);
        }

        @Override
        public String toString() {
            return "LarkApiDataItem{" + "messageId='" + messageId + '\'' + ", rootId='" + rootId + '\'' + ", parentId='"
                    + parentId + '\'' + ", threadId='" + threadId + '\'' + ", msgType='" + msgType + '\''
                    + ", createTime=" + createTime + ", updateTime=" + updateTime + ", deleted=" + deleted
                    + ", updated=" + updated + ", chatId='" + chatId + '\'' + ", sender=" + sender + ", body=" + body
                    + ", mentions=" + mentions + ", upperMessageId='" + upperMessageId + '\'' + '}';
        }
    }

    public static class LarkApiDataItemSender {
        @JsonProperty("id")
        String id;

        @JsonProperty("id_type")
        String idType;

        @JsonProperty("sender_type")
        String senderType;

        @JsonProperty("tenant_key")
        String tenantKey;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIdType() {
            return idType;
        }

        public void setIdType(String idType) {
            this.idType = idType;
        }

        public String getSenderType() {
            return senderType;
        }

        public void setSenderType(String senderType) {
            this.senderType = senderType;
        }

        public String getTenantKey() {
            return tenantKey;
        }

        public void setTenantKey(String tenantKey) {
            this.tenantKey = tenantKey;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            LarkApiDataItemSender that = (LarkApiDataItemSender) o;
            return Objects.equals(id, that.id)
                    && Objects.equals(idType, that.idType)
                    && Objects.equals(senderType, that.senderType)
                    && Objects.equals(tenantKey, that.tenantKey);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, idType, senderType, tenantKey);
        }

        @Override
        public String toString() {
            return "LarkApiDataItemSender{" + "id='" + id + '\'' + ", idType='" + idType + '\'' + ", senderType='"
                    + senderType + '\'' + ", tenantKey='" + tenantKey + '\'' + '}';
        }
    }

    public static class LarkApiDataItemBody {
        @JsonProperty("content")
        String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            LarkApiDataItemBody that = (LarkApiDataItemBody) o;
            return Objects.equals(content, that.content);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(content);
        }

        @Override
        public String toString() {
            return "LarkApiDataItemBody{" + "content='" + content + '\'' + '}';
        }
    }

    public static class LarkApiDataItemMention {
        @JsonProperty("key")
        String key;

        @JsonProperty("id")
        String id;

        @JsonProperty("id_type")
        String idType;

        @JsonProperty("name")
        String name;

        @JsonProperty("tenant_key")
        String tenantKey;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIdType() {
            return idType;
        }

        public void setIdType(String idType) {
            this.idType = idType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTenantKey() {
            return tenantKey;
        }

        public void setTenantKey(String tenantKey) {
            this.tenantKey = tenantKey;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            LarkApiDataItemMention that = (LarkApiDataItemMention) o;
            return Objects.equals(key, that.key)
                    && Objects.equals(id, that.id)
                    && Objects.equals(idType, that.idType)
                    && Objects.equals(name, that.name)
                    && Objects.equals(tenantKey, that.tenantKey);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, id, idType, name, tenantKey);
        }

        @Override
        public String toString() {
            return "LarkApiDataItemMention{" + "key='" + key + '\'' + ", id='" + id + '\'' + ", idType='" + idType
                    + '\'' + ", name='" + name + '\'' + ", tenantKey='" + tenantKey + '\'' + '}';
        }
    }
}
