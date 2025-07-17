package com.company.membership.api.dto;

import javax.validation.constraints.NotBlank;

public class FindIdRequest {
    @NotBlank(message = "회원 이름은 필수입니다")
    private String memberName;
    
    @NotBlank(message = "회원 이메일은 필수입니다")
    private String memberEmail;

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }
} 