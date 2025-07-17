package com.company.membership.api.dto;

public class FindIdResponse {
    private String code;
    private String message;
    private FindIdData data;

    public static class FindIdData {
        private String memberType;
        private String loginId;

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
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FindIdData getData() {
        return data;
    }

    public void setData(FindIdData data) {
        this.data = data;
    }
} 