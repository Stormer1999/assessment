package com.kbtg.bootcamp.posttest.constant;

import lombok.Getter;

@Getter
public enum Response {

    SUCCESS("success"),
    ERROR("error"),
    FAIL("fail"),
    NOT_FOUND("not found");

    private final String content;

    Response(String content) {
        this.content = content;
    }
}
