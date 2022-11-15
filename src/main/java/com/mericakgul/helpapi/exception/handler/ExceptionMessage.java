package com.mericakgul.helpapi.exception.handler;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class ExceptionMessage {

    private final Date timestamp;

    private final HttpStatus status;

    private final String message;
}
