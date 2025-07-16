package com.company.membership.api.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
public class UpdateRequest {
    @NotBlank(message = "U001|회원 유형은 필수입니다.")
    private String memberType;

    private Integer membershipUserInfoId;
    private String membershipNo;
    private String membershipId;
    private String webMemberId;

    @NotBlank(message = "U002|한글 이름은 필수입니다.")
    private String memberName;

    @NotBlank(message = "U003|영문 이름은 필수입니다.")
    private String memberFirstName;

    @NotBlank(message = "U004|영문 중간 이름은 필수입니다.")
    private String memberMiddleName;

    @NotBlank(message = "U005|영문 성은 필수입니다.")
    private String memberLastName;

    @NotBlank(message = "U006|휴대전화는 필수입니다.")
    private String memberMobile;

    @NotBlank(message = "U007|이메일은 필수입니다.")
    private String memberEmail;

    @NotBlank(message = "U008|생년월일은 필수입니다.")
    private String memberBirth;

    private String zipCode;
    private String address1;
    private String address2;

    @Valid
    private List<TermInfoDto> termInfos;

    @Getter
    @Setter
    public static class TermInfoDto {
        @NotBlank(message = "U101|약관 유형 아이디는 필수입니다.")
        private String termInfoId;

        @NotBlank(message = "U102|동의 여부는 필수입니다.")
        private String agreeYn;

        @NotNull(message = "U103|약관 세부 아이디는 필수입니다.")
        private Integer termItemInfoId;

        @NotNull(message = "U104|약관 버전은 필수입니다.")
        private Integer version;

        @NotNull(message = "U105|수신방법 종류는 필수입니다.")
        private List<String> receiveTypes;
    }
} 