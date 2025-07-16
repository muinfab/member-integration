package com.company.membership.api.dto;

import java.util.List;

public class TermsResponse {
    private String code;
    private String message;
    private TermsData data;
    // getter/setter 생략

    public static class TermsData {
        private List<TermInfoDto> termInfos;
        // getter/setter 생략
    }

    public static class TermInfoDto {
        private String termInfoId;
        private Integer termItemInfoId;
        private String applyDate;
        private String expireDate;
        private String requiredYn;
        private Integer version;
        private String title;
        private String subTitle;
        private String contents;
        private List<String> receiveMethods;
        // getter/setter 생략
    }
} 