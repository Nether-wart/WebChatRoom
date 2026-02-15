package io.github.nether_wart.entity;

public class ApiResponse<T> {
    public int code;
    public String msg;
    public T data;

    public ApiResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ApiResponse<?> success() {
        return new ApiResponse<>(200, "success", null);
    }

    public static ApiResponse<?> success(Object data) {
        return new ApiResponse<>(200, "success", data);
    }

    public static ApiResponse<?> forbidden() {
        return new ApiResponse<>(403, "forbidden", null);
    }

    public static ApiResponse<?> forbidden(String msg) {
        return new ApiResponse<>(403, msg, null);
    }

    public static ApiResponse<?> notFound() {
        return new ApiResponse<>(404, "not found", null);
    }

    public static ApiResponse<?> error(String msg) {
        return new ApiResponse<>(500, msg, null);
    }
}
