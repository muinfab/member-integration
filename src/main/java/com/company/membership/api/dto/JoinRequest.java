package com.company.membership.api.dto;

import java.util.List;

public class JoinRequest {
    private Integer joinType;
    private Integer joinMethod;
    private String joinId;
    private String joinPw;
    private Integer channelId;
    private String webMemberId;
    private String memberName;
    private String memberFirstName;
    private String memberMiddleName;
    private String memberLastName;
    private String memberMobile;
    private String memberEmail;
    private Integer memberGender;
    private String memberBirth;
    private String localYn;
    private String memberCi;
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