package com.company.membership.api.dto;

import java.util.List;

public class RegisterRequest {
    private String rewardsMembershipId;
    private String webMemberId;
    private String unifiedId;
    private String unifiedPw;
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