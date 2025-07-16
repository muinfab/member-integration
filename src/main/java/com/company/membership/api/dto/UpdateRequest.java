package com.company.membership.api.dto;

import java.util.List;

public class UpdateRequest {
    private String memberType;
    private Integer membershipUserInfoId;
    private String membershipNo;
    private String membershipId;
    private String webMemberId;
    private String memberName;
    private String memberFirstName;
    private String memberMiddleName;
    private String memberLastName;
    private String memberMobile;
    private String memberEmail;
    private String memberBirth;
    private String zipCode;
    private String address1;
    private String address2;
    private List<TermInfoDto> termInfos;
    // getter/setter 생략

    public static class TermInfoDto {
        private String termInfoId;
        private String agreeYn;
        private Integer termItemInfoId;
        private Integer version;
        private List<String> receiveTypes;
        // getter/setter 생략
    }
} 