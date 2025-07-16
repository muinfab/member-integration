package com.company.membership.api.dto;

public class UserResponse {
    private String code;
    private String message;
    private UserData data;
    // getter/setter 생략

    public static class UserData {
        private String memberType;
        private String integrationType;
        private Integer webInfoId;
        private String membershipNo;
        private String membershipId;
        private String webMemberId;
        private Integer cmsProfileId;
        private String memberName;
        private String memberFirstName;
        private String memberMiddleName;
        private String memberLastName;
        private String memberMobile;
        private String memberEmail;
        private Integer memberGender;
        private String memberBirth;
        private String memberJoinDate;
        private String employeeStatus;
        // getter/setter 생략
    }
} 