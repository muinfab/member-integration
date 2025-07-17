package com.company.membership.api.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class EmailCertificationRequest {
    @NotBlank(message = "이메일은 필수입니다")
    private String email;
    
    @NotNull(message = "인증번호는 필수입니다")
    private Integer certificationKey;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCertificationKey() {
        return certificationKey;
    }

    public void setCertificationKey(Integer certificationKey) {
        this.certificationKey = certificationKey;
    }
} 