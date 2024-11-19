package org.danpoong.zipcock_44.global.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.danpoong.zipcock_44.global.common.code.ErrorCode;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;

    public String getMessage() {
        return errorCode.getMessage();
    }
}