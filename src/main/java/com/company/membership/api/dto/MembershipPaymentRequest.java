package com.company.membership.api.dto;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class MembershipPaymentRequest {
    @NotBlank(message = "회원 유형은 필수입니다")
    private String memberType;
    
    @NotBlank(message = "회원 아이디는 필수입니다")
    private String loginId;
    
    private ExtraMembershipInfo extraMembershipInfo;
    private List<TermInfo> termInfos;

    public static class ExtraMembershipInfo {
        private String extraMembershipType;
        private String paymentBillNo;
        private Integer paymentAmount;
        private String paymentDate;

        public String getExtraMembershipType() {
            return extraMembershipType;
        }

        public void setExtraMembershipType(String extraMembershipType) {
            this.extraMembershipType = extraMembershipType;
        }

        public String getPaymentBillNo() {
            return paymentBillNo;
        }

        public void setPaymentBillNo(String paymentBillNo) {
            this.paymentBillNo = paymentBillNo;
        }

        public Integer getPaymentAmount() {
            return paymentAmount;
        }

        public void setPaymentAmount(Integer paymentAmount) {
            this.paymentAmount = paymentAmount;
        }

        public String getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(String paymentDate) {
            this.paymentDate = paymentDate;
        }
    }

    public static class TermInfo {
        @NotBlank(message = "약관 유형 아이디는 필수입니다")
        private String termInfoId;
        
        @NotBlank(message = "동의 여부는 필수입니다")
        private String agreeYn;
        
        private Integer version;
        private String title;
        private String agreeDate;

        public String getTermInfoId() {
            return termInfoId;
        }

        public void setTermInfoId(String termInfoId) {
            this.termInfoId = termInfoId;
        }

        public String getAgreeYn() {
            return agreeYn;
        }

        public void setAgreeYn(String agreeYn) {
            this.agreeYn = agreeYn;
        }

        public Integer getVersion() {
            return version;
        }

        public void setVersion(Integer version) {
            this.version = version;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAgreeDate() {
            return agreeDate;
        }

        public void setAgreeDate(String agreeDate) {
            this.agreeDate = agreeDate;
        }
    }

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

    public ExtraMembershipInfo getExtraMembershipInfo() {
        return extraMembershipInfo;
    }

    public void setExtraMembershipInfo(ExtraMembershipInfo extraMembershipInfo) {
        this.extraMembershipInfo = extraMembershipInfo;
    }

    public List<TermInfo> getTermInfos() {
        return termInfos;
    }

    public void setTermInfos(List<TermInfo> termInfos) {
        this.termInfos = termInfos;
    }
} 