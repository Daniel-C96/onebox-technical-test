package com.danielcontador.onebox_technical_test.dto.response;

public record ErrorResponse(
        int status,
        String error,
        String message,
        String path
) {

}
