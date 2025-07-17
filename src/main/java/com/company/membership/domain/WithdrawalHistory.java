package com.company.membership.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class WithdrawalHistory {
    private Long id;
    private String memberId;
    private String reason;
    private LocalDateTime withdrawalDate;
    private String reasonCode;
    private String reasonDetail;
    private String adminId; // 관리자 처리 시
    
    public WithdrawalHistory() {
        this.withdrawalDate = LocalDateTime.now();
    }
    
    public WithdrawalHistory(String memberId, String reason) {
        this();
        this.memberId = memberId;
        this.reason = reason;
    }
    
    public WithdrawalHistory(String memberId, String reasonCode, String reasonDetail) {
        this();
        this.memberId = memberId;
        this.reasonCode = reasonCode;
        this.reasonDetail = reasonDetail;
        this.reason = reasonDetail != null ? reasonDetail : reasonCode;
    }
    
    // 비즈니스 메서드들
    public void setReason(String reasonCode, String reasonDetail) {
        this.reasonCode = reasonCode;
        this.reasonDetail = reasonDetail;
        this.reason = reasonDetail != null ? reasonDetail : reasonCode;
    }
    
    public boolean hasReasonCode() {
        return this.reasonCode != null && !this.reasonCode.trim().isEmpty();
    }
    
    public boolean hasReasonDetail() {
        return this.reasonDetail != null && !this.reasonDetail.trim().isEmpty();
    }
    
    public boolean isAdminProcessed() {
        return this.adminId != null && !this.adminId.trim().isEmpty();
    }
    
    public void setAdminProcess(String adminId) {
        this.adminId = adminId;
    }
    
    // 편의 메서드들
    public String getWithdrawalDateAsString() {
        return this.withdrawalDate != null ? 
            this.withdrawalDate.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) : null;
    }
    
    public String getWithdrawalDateAsDateString() {
        return this.withdrawalDate != null ? 
            this.withdrawalDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) : null;
    }
} 