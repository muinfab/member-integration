package com.company.membership.api.dto;

public class LoginResponse {
    private String code;
    private String message;
    private LoginData data;
    // getter/setter 생략

    public static class LoginData {
        private String memberType;
        private String integrationType;
        private Integer webInfoId;
        private String rewardsMembershipNo;
        private String rewardsMembershipId;
        private String loginId;
        private String webMemberId;
        private String cmsProfileId;
        private String memberName;
        private String memberFirstName;
        private String memberMiddleName;
        private String memberLastName;
        private String memberMobile;
        private String memberEmail;
        private Integer memberGender;
        private String memberBirth;
        private String employeeStatus;
        // getter/setter 생략
    }
} 