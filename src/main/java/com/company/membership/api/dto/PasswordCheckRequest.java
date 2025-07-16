package com.company.membership.api.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PasswordCheckRequest {
    @NotBlank(message = "P001|회원 유형은 필수입니다.")
    private String memberType;

    @NotBlank(message = "P002|로그인 아이디는 필수입니다.")
    private String loginId;

    @NotBlank(message = "P003|로그인 패스워드는 필수입니다.")
    private String loginPw;
} 