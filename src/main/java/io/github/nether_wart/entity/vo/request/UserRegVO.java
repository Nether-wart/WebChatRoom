package io.github.nether_wart.entity.vo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegVO {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 40, message = "用户名在3~40字符之间")
    private String name;

    @Size(min = 6, max = 128, message = "密码在6~128字符之间")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
