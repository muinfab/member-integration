package com.company.membership.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
public class TermsAgreement {
    private String termInfoId;
    private Integer termItemInfoId;
    private Integer version;
    private String title;
    private String contents;
    private Boolean required;
    private Boolean agree;
    private List<String> receiveTypes;
    private LocalDateTime agreeDate;
    
    public TermsAgreement() {
        this.agreeDate = LocalDateTime.now();
    }
    
    // 비즈니스 메서드들
    public void agree() {
        this.agree = true;
        this.agreeDate = LocalDateTime.now();
    }
    
    public void disagree() {
        this.agree = false;
        this.agreeDate = null;
    }
    
    public boolean isAgreed() {
        return Boolean.TRUE.equals(this.agree);
    }
    
    public boolean isRequired() {
        return Boolean.TRUE.equals(this.required);
    }
    
    public boolean isOptional() {
        return !isRequired();
    }
    
    public void addReceiveType(String receiveType) {
        if (this.receiveTypes != null) {
            this.receiveTypes.add(receiveType);
        }
    }
    
    public boolean hasReceiveType(String receiveType) {
        return this.receiveTypes != null && this.receiveTypes.contains(receiveType);
    }
    
    // 편의 메서드들
    public String getAgreeDateAsString() {
        return this.agreeDate != null ? 
            this.agreeDate.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) : null;
    }
    
    public boolean isMarketingTerms() {
        return "MARKETING".equals(this.termInfoId);
    }
    
    public boolean isTosTerms() {
        return "TOS".equals(this.termInfoId);
    }
} 