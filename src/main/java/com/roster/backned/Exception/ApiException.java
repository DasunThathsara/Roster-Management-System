package com.roster.backned.Exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
        log.error(message);
    }

}