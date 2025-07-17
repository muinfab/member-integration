package com.company.membership.domain;

import com.company.membership.domain.value.Email;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Getter
@Setter
public class EmailCertification {
    private Email email;
    private Integer certificationKey;
    private LocalDateTime sentAt;
    private Boolean certified;
    private LocalDateTime certifiedAt;
    
    public EmailCertification() {
        this.certified = false;
    }
    
    public EmailCertification(Email email) {
        this.email = email;
        this.certified = false;
        this.sentAt = LocalDateTime.now();
        this.certificationKey = generateCertificationKey();
    }
    
    // 비즈니스 메서드들
    public void sendCertification() {
        this.sentAt = LocalDateTime.now();
        this.certificationKey = generateCertificationKey();
        this.certified = false;
        this.certifiedAt = null;
    }
    
    public boolean verifyCertification(Integer inputKey) {
        if (this.certificationKey != null && this.certificationKey.equals(inputKey)) {
            this.certified = true;
            this.certifiedAt = LocalDateTime.now();
            return true;
        }
        return false;
    }
    
    public boolean isCertified() {
        return Boolean.TRUE.equals(this.certified);
    }
    
    public boolean isExpired() {
        if (this.sentAt == null) {
            return true;
        }
        // 10분 후 만료
        return LocalDateTime.now().isAfter(this.sentAt.plusMinutes(10));
    }
    
    public boolean canResend() {
        if (this.sentAt == null) {
            return true;
        }
        // 1분 후 재발송 가능
        return LocalDateTime.now().isAfter(this.sentAt.plusMinutes(1));
    }
    
    private Integer generateCertificationKey() {
        Random random = new Random();
        return 100000 + random.nextInt(900000); // 6 숫자
    }
    
    // 편의 메서드들
    public String getSentAtAsString() {
        return this.sentAt != null ? 
            this.sentAt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) : null;
    }
    
    public String getCertifiedAtAsString() {
        return this.certifiedAt != null ? 
            this.certifiedAt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) : null;
    }
    
    public String getEmailAsString() {
        return this.email != null ? this.email.toString() : null;
    }
} 