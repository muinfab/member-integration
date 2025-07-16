package com.company.membership.api.dto;

import com.company.membership.api.dto.validation.ValidLoginRequest;

@ValidLoginRequest
public class LoginRequest {
    private Integer loginType; // 00: 일반 로그인, 1: 간편 로그인
    private String loginId;
    private String loginPw;
    private String channelId;
    private String memberCi;
    // getter/setter 생략
} 