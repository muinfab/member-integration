package com.company.membership.api.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
public class JoinRequest {
    @NotNull(message = "J001|가입 유형은 필수입니다.")
    private Integer joinType;

    @NotNull(message = "J002|가입 방법은 필수입니다.")
    private Integer joinMethod;

    @NotBlank(message = "J003|가입 아이디는 필수입니다.")
    private String joinId;

    @NotBlank(message = "J004|가입 패스워드는 필수입니다.")
    private String joinPw;

    private Integer channelId;
    private String webMemberId;

    @NotBlank(message = "J005|한글 이름은 필수입니다.")
    private String memberName;

    private String memberFirstName;
    private String memberMiddleName;
    private String memberLastName;

    @NotBlank(message = "J006|휴대전화는 필수입니다.")
    private String memberMobile;

    @NotBlank(message = "J007|이메일은 필수입니다.")
    private String memberEmail;

    @NotNull(message = "J008|성별은 필수입니다.")
    private Integer memberGender;

    @NotBlank(message = "J009|생년월일은 필수입니다.")
    private String memberBirth;

    @NotBlank(message = "J010|내국인/외국인 구분은 필수입니다.")
    private String localYn;

    private String memberCi;

    @Valid
    private List<TermInfoDto> termInfos;

    @Getter
    @Setter
    public static class TermInfoDto {
        @NotBlank(message = "J101|약관 유형 아이디는 필수입니다.")
        private String termInfoId;

        @NotBlank(message = "J102|동의 여부는 필수입니다.")
        private String agreeYn;

        @NotNull(message = "J103|약관 세부 아이디는 필수입니다.")
        private Integer termItemInfoId;

        @NotNull(message = "J104|약관 버전은 필수입니다.")
        private Integer version;

        @NotNull(message = "J105|수신방법 종류는 필수입니다.")
        private List<String> receiveTypes;
    }
} 