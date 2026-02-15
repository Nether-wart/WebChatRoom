package io.github.nether_wart.entity.vo.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class UserLoginVO {
    @Nullable
    private Long uid;
    @Nullable
    private String name;
    @NotNull
    private String password;

    @Nullable
    public Long getUid() {
        return uid;
    }

    public void setUid(@Nullable Long uid) {
        this.uid = uid;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
