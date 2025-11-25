package com.kicc.app.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ExceptionDto {
    private String message;
    private LocalDate timestamp;
    private String code;
}
