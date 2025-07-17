package com.company.membership.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class Membership {
    private String membershipNo;
    private String membershipId;
    private String type;
    private String extraInfo;
    private LocalDateTime paymentDate;
    private Integer paymentAmount;
    private String paymentBillNo;
    private String status; // ACTIVE, INACTIVE, EXPIRED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public Membership() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = "ACTIVE";
    }
    
    // 비즈니스 메서드들
    public void activate() {
        this.status = "ACTIVE";
        this.updatedAt = LocalDateTime.now();
    }
    
    public void deactivate() {
        this.status = "INACTIVE";
        this.updatedAt = LocalDateTime.now();
    }
    
    public void expire() {
        this.status = "EXPIRED";
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }
    
    public boolean isInactive() {
        return "INACTIVE".equals(this.status);
    }
    
    public boolean isExpired() {
        return "EXPIRED".equals(this.status);
    }
    
    public void updatePaymentInfo(Integer amount, String billNo) {
        this.paymentAmount = amount;
        this.paymentBillNo = billNo;
        this.paymentDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean hasPaymentInfo() {
        return this.paymentAmount != null && this.paymentBillNo != null;
    }
    
    // 편의 메서드들
    public String getPaymentDateAsString() {
        return this.paymentDate != null ? 
            this.paymentDate.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) : null;
    }
    
    public String getCreatedAtAsString() {
        return this.createdAt != null ? 
            this.createdAt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) : null;
    }
    
    public String getUpdatedAtAsString() {
        return this.updatedAt != null ? 
            this.updatedAt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) : null;
    }
} 