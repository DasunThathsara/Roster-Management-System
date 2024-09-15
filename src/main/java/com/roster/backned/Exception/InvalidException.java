package com.roster.backned.Exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidException extends RuntimeException {
    public InvalidException(String message) {
        super(message);
        log.error(message);
    }
}
