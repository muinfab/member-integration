package com.company.membership.api.dto;

import java.util.List;

public class UserListResponse {
    private String code;
    private String message;
    private UserListData data;
    // getter/setter 생략

    public static class UserListData {
        private List<UserSummary> users;
        // getter/setter 생략
    }

    public static class UserSummary {
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