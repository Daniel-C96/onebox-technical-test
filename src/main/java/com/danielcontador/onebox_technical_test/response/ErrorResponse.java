package com.danielcontador.onebox_technical_test.response;

public record ErrorResponse(
        int status,
        String error,
        String message,
        String path
) {

}
