package com.example.online.shopping.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND(404, "USER NOT FOUND"),
    ORDER_NOT_FOUND(404, "ORDER NOT FOUND"),

    ORDER_RESPONSEDTO_NOT_FOUND(404, "ORDER RESPONSEDTO_NOT_FOUND NOT FOUND"),
    ORDER_ITEM_NOT_FOUND(404, "ORDER ITEM NOT FOUND"),
    ITEM_NOT_FOUND(404, "ITEM NOT FOUND"),

    ADMIN_NOT_FOUND(404, "ADMIN NOT FOUND"),
    ADMIN_ITEM_NOT_FOUND(404, "ADMIN ITEM NOT FOUND"),
    EXCEEDING_STOCK(404,"상품의 제고가 부족합니다.");

    private int status;

    private String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
