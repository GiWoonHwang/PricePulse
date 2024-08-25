package com.example.dustin.controller;

import com.example.dustin.vo.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice  // 이 클래스가 전역적으로 애플리케이션의 예외를 처리하는 클래스임을 나타냅니다.
@Slf4j  // Lombok의 로그 사용을 활성화하여 로깅 기능을 제공합니다.
public class ExController {

    // RuntimeException을 처리하는 메서드입니다.
    // 현재는 주석 처리되어 있어 동작하지 않습니다.

    // @ExceptionHandler 애너테이션은 특정 예외 유형을 처리하는 메서드를 지정합니다.
    // @ExceptionHandler({RuntimeException.class})
    // public ResponseEntity<Object> BadRequestException(final RuntimeException ex) {
    //     // HTTP 응답으로 400 Bad Request와 예외 메시지를 반환합니다.
    //     return ResponseEntity.badRequest().body(ex.getMessage());
    // }

    // 모든 종류의 예외를 처리하는 메서드입니다.
    // 현재는 주석 처리되어 있어 동작하지 않습니다.

    // @ExceptionHandler({ xxxx.class })
    // public ResponseEntity<Object> EveryException(final Exception ex) {
    //     // HTTP 응답으로 500 Internal Server Error와 예외 메시지를 반환합니다.
    //     return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    // }

    // NotFoundException을 처리하는 메서드입니다.
    @ExceptionHandler({ NotFoundException.class })
    public ResponseEntity<Object> NotFoundExceptionResponse(NotFoundException ex) {
        // NotFoundException이 발생하면 HTTP 상태 코드와 오류 메시지를 포함한 응답을 반환합니다.
        return new ResponseEntity<>(ex.getErrmsg(), ex.getHttpStatus());
    }
}
