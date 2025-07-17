package com.company.membership.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class LoginHistory {
    private Long id;
    private String memberId;
    private LocalDateTime loginDate;
    private String channelId;
    private String loginType; //00: 일반 로그인, 1: 간편 로그인
    private String ipAddress;
    private String userAgent;
    private Boolean success;
    
    public LoginHistory() {
        this.loginDate = LocalDateTime.now();
        this.success = true;
    }
    
    public LoginHistory(String memberId, String loginType) {
        this();
        this.memberId = memberId;
        this.loginType = loginType;
    }
    
    // 비즈니스 메서드들
    public void markAsSuccess() {
        this.success = true;
    }
    
    public void markAsFailure() {
        this.success = false;
    }
    
    public boolean isSuccessful() {
        return Boolean.TRUE.equals(this.success);
    }
    
    public boolean isFailed() {
        return Boolean.FALSE.equals(this.success);
    }
    
    public boolean isGeneralLogin() {
        return "00".equals(this.loginType);
    }
    
    public boolean isSocialLogin() {
        return "1".equals(this.loginType);
    }
    
    public void setChannelInfo(String channelId) {
        this.channelId = channelId;
    }
    
    public void setClientInfo(String ipAddress, String userAgent) {
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }
    
    // 편의 메서드들
    public String getLoginDateAsString() {
        return this.loginDate != null ? 
            this.loginDate.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) : null;
    }
    
    public String getLoginTypeDescription() {
        if (isGeneralLogin()) {
            return "일반 로그인";
        } else if (isSocialLogin()) {
            return "간편 로그인";
        }
        return "알 수 없음";
    }
} 