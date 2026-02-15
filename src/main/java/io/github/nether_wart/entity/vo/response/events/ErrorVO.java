package io.github.nether_wart.entity.vo.response.events;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorVO {
    public ErrorVO(String message, boolean isFatal, boolean isEnableWrite) {
        this.message = message;
        this.isFatal = isFatal;
        this.isEnableWrite = isEnableWrite;
    }

    public ErrorVO(String message, boolean isFatal) {
        this.message = message;
        this.isFatal = isFatal;
    }

    public ErrorVO(String message) {
        this.message = message;
    }

    public String message;
    @JsonProperty("is_fatal")
    public boolean isFatal = true;
    @JsonProperty("is_enable_write")
    public boolean isEnableWrite = false;
}
