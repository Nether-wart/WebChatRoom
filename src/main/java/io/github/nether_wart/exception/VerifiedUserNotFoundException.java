package io.github.nether_wart.exception;

public class VerifiedUserNotFoundException extends RuntimeException {
    //这个异常不应该出现
    //用于 用户token验证通过，但数据库里找不到该用户
}
