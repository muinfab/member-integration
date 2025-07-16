package com.company.membership.api.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank(message = "E001|멤버십 아이디는 필수입니다.")
    private String rewardsMembershipId;

    @NotBlank(message = "E002|웹회원 아이디는 필수입니다.")
    private String webMemberId;

    @NotBlank(message = "E003|통합 아이디는 필수입니다.")
    private String unifiedId;

    @NotBlank(message = "E004|통합 비밀번호는 필수입니다.")
    private String unifiedPw;

    @Valid
    private List<TermInfoDto> termInfos;

    @Getter
    @Setter
    public static class TermInfoDto {
        @NotBlank(message = "E101|약관 유형 아이디는 필수입니다.")
        private String termInfoId;

        @NotBlank(message = "E102|동의 여부는 필수입니다.")
        private String agreeYn;

        private Integer termItemInfoId;
        private Integer version;
        private List<String> receiveTypes;
    }
} 