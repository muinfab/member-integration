package com.company.membership.api.dto;

public class EmailCertificationResponse {
    private String code;
    private String message;
    private EmailCertificationData data;

    public static class EmailCertificationData {
        private String rejectYn;

        public String getRejectYn() {
            return rejectYn;
        }

        public void setRejectYn(String rejectYn) {
            this.rejectYn = rejectYn;
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

    public EmailCertificationData getData() {
        return data;
    }

    public void setData(EmailCertificationData data) {
        this.data = data;
    }
} 