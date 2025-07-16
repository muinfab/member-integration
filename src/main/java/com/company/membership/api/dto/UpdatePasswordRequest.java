package com.company.membership.api.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdatePasswordRequest {
    @NotBlank(message = "PW001|회원 유형은 필수입니다.")
    private String memberType;

    @NotBlank(message = "PW002|로그인 아이디는 필수입니다.")
    private String loginId;

    @NotBlank(message = "PW003|기존 비밀번호는 필수입니다.")
    private String oldLoginPw;

    @NotBlank(message = "PW004|신규 비밀번호는 필수입니다.")
    private String newLoginPw;
} 