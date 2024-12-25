package businessObjects.db.backofficeDb.userActionAudit;

import java.util.Objects;

public class UserActionAudit {
    public Long id;
    public String userId;
    public String createdAt;
    public String action;
    public String entity;
    public String attributes;

    public UserActionAudit() {
    }

    public UserActionAudit(Long id, String userId, String createdAt, String action, String entity, String attributes) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.action = action;
        this.entity = entity;
        this.attributes = attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserActionAudit that = (UserActionAudit) o;
        return Objects.equals(userId, that.userId) && Objects.equals(action, that.action) && Objects.equals(entity, that.entity) && Objects.equals(attributes, that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, action, entity, attributes);
    }

    @Override
    public String toString() {
        return "UserActionAudit{" + "id=" + id + ", userId='" + userId + '\'' + ", createdAt='" + createdAt + '\'' + ", action='" + action + '\'' + ", entity='" + entity + '\'' + ", attributes='" + attributes + '\'' + '}';
    }
}
