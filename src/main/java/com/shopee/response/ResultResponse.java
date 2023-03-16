package com.shopee.response;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
public class ResultResponse {

    public static final int STATUS_VALIDATE_FAILED = 509;

    public static final int STATUS_UNKNOWN_FAILED = 500;

    public static final int STATUS_SUCCESS = 200;

    public static final int STATUS_UNAUTHORIZED = 401;

    public static final int STATUS_FORBIDDEN = 403;

    public static final int STATUS_NOT_SUPPORT = 300;

    public static final int STATUS_TOO_MANY_REQUEST = 309;

    public static final int STATUS_RESOURCE_NOT_FOUND = 404;

    public static final ResultResponse SUCCESS = new ResultResponse(STATUS_SUCCESS, "Api.success");
    public static final ResultResponse FAILED = new ResultResponse(STATUS_UNKNOWN_FAILED, "Api.unknown.error");
    public static final ResultResponse UNAUTHORIZED = new ResultResponse(STATUS_UNAUTHORIZED, "unauthorized");
    public static final ResultResponse FORBIDDEN = new ResultResponse(STATUS_FORBIDDEN, "forbidden");
    public static final ResultResponse VALIDATE_FAILED = new ResultResponse(STATUS_VALIDATE_FAILED, "validate.failure");
    public static final ResultResponse RESOURCE_NOT_FOUND = new ResultResponse(STATUS_RESOURCE_NOT_FOUND, "resource.notfound");
    public static final ResultResponse TOO_MANY_REQUEST = new ResultResponse(STATUS_TOO_MANY_REQUEST, "too.many.request");

    public ResultResponse(@NotNull Integer status, @NotNull String messageCode, Optional<String> message, Object result) {
        this.status = status;
        this.messageCode = messageCode;
        this.message = message;
        this.result = result;
    }

    public ResultResponse withResult(Object result) {
        return new ResultResponse(status, messageCode, Optional.empty(), result);
    }

    public ResultResponse withMessage(String message) {
        return new ResultResponse(status, messageCode, Optional.ofNullable(message), null);
    }

    @NotNull
    private final Integer status;
    @NotNull
    private final String messageCode;

    private final Instant serverTime = Instant.now();

    private final Optional<String> message;

    private final Object result;

    @JsonIgnore
    private final Map<String, Object> messageParams = new HashMap<>();

    public ResultResponse(Integer status, String messageCode) {
        this.status = status;
        this.messageCode = messageCode;
        this.message = Optional.empty();
        this.result = null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("status", status)
                .append("messageCode", messageCode)
                .append("message", message)
                .append("result", result)
                .append("messageParams", messageParams)
                .toString();
    }
}
