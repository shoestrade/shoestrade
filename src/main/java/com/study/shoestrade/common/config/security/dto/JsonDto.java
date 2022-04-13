package com.study.shoestrade.common.config.security.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsonDto {
    private boolean success;
    private int code;
    private String msg;
}
