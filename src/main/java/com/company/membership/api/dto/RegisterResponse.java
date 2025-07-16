package com.company.membership.api.dto;

public class RegisterResponse {
    private String code;
    private String message;
    private RegisterData data;
    // getter/setter 생략

    public static class RegisterData {
        private String memberType;
        private String rewardsMembershipNo;
        private String loginId;
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