package com.company.membership.api.dto;

public class JoinResponse {
    private String code;
    private String message;
    private JoinData data;
    // getter/setter 생략

    public static class JoinData {
        private String memberType;
        private Integer membershipUserInfoId;
        private String membershipNo;
        private String membershipId;
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