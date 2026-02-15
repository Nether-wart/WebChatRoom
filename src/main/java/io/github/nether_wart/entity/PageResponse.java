package io.github.nether_wart.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PageResponse {
    public int total;
    public int current;
    @JsonProperty("page_size")
    public int pageSize;

    public PageResponse(int total, int current, int pageSize, List<?> list) {
        this.total = total;
        this.current = current;
        this.pageSize = pageSize;
        this.list = list;
    }

    public List<?> list;

}
