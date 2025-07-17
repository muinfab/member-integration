package com.company.membership.api.dto;

import java.util.List;

public class CommonCodeResponse {
    private String code;
    private String message;
    private List<CommonCodeData> data;

    public static class CommonCodeData {
        private String commonCode;
        private String commonName;

        public String getCommonCode() {
            return commonCode;
        }

        public void setCommonCode(String commonCode) {
            this.commonCode = commonCode;
        }

        public String getCommonName() {
            return commonName;
        }

        public void setCommonName(String commonName) {
            this.commonName = commonName;
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

    public List<CommonCodeData> getData() {
        return data;
    }

    public void setData(List<CommonCodeData> data) {
        this.data = data;
    }
} 