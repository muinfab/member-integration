package com.company.membership.api.dto;

import javax.validation.constraints.NotBlank;

public class WithdrawalRequest {
    @NotBlank(message = "회원 유형은 필수입니다")
    private String memberType;
    
    @NotBlank(message = "회원 아이디는 필수입니다")
    private String loginId;
    
    @NotBlank(message = "회원 패스워드는 필수입니다")
    private String loginPw;
    
    @NotBlank(message = "탈퇴 사유는 필수입니다")
    private String withdrawalReason;

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginPw() {
        return loginPw;
    }

    public void setLoginPw(String loginPw) {
        this.loginPw = loginPw;
    }

    public String getWithdrawalReason() {
        return withdrawalReason;
    }

    public void setWithdrawalReason(String withdrawalReason) {
        this.withdrawalReason = withdrawalReason;
    }
} 