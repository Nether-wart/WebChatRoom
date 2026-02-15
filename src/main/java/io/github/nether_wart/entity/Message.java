package io.github.nether_wart.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue
    private Long mid;

    @Column(nullable = false)
    private long rid;

    @Column
    private long uid;

    @Column(name = "created_at")
    private long created = System.currentTimeMillis();

    @Column(columnDefinition = "text")
    private String content;

    private boolean isEnableHTML = false;
    private boolean isEnableMarkDown = false;

    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isEnableHTML() {
        return isEnableHTML;
    }

    public void setEnableHTML(boolean enableHTML) {
        isEnableHTML = enableHTML;
    }

    public boolean isEnableMarkDown() {
        return isEnableMarkDown;
    }

    public void setEnableMarkDown(boolean enableMarkDown) {
        isEnableMarkDown = enableMarkDown;
    }
}
