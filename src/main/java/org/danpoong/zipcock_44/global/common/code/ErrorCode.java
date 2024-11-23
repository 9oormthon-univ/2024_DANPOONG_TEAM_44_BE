package org.danpoong.zipcock_44.global.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Test Error
    TEST_ERROR(10000, HttpStatus.BAD_REQUEST, "테스트 에러입니다."),
    // 404 Not Found
    NOT_FOUND_END_POINT(40400, HttpStatus.NOT_FOUND, "존재하지 않는 API입니다."),
    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(50000, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),

    REQUEST_NOT_VALID(400, HttpStatus.BAD_REQUEST, "요청에 해당하는 정보가 없습니다."),

    ERROR_WHILE_PARSING_JSON(500, HttpStatus.INTERNAL_SERVER_ERROR, "JSON 파싱 중 오류가 발생했습니다.")
    ;

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}
