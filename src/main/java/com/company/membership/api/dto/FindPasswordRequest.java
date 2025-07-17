package com.company.membership.api.dto;

import javax.validation.constraints.NotBlank;

public class FindPasswordRequest {
    @NotBlank(message = "회원 아이디는 필수입니다")
    private String loginId;
    
    @NotBlank(message = "회원 이메일은 필수입니다")
    private String memberEmail;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }
} 