package io.github.nether_wart.entity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageVO {
    @JsonProperty("is_enable_html")
    public boolean isEnableHTML = false;
    @JsonProperty("is_enable_markdown")
    public boolean isEnableMarkDown = false;
    @Size(min = 1, max = 102400)
    public String content;
    @Nullable
    public List<String> files;
}