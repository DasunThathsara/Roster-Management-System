package com.roster.backned.Exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SQLException extends RuntimeException {
    public SQLException(String message) {
        super(message);
        log.error(message);
    }
}
