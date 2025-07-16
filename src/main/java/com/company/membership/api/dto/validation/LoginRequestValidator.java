package com.company.membership.api.dto.validation;

import com.company.membership.api.dto.LoginRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoginRequestValidator implements ConstraintValidator<ValidLoginRequest, LoginRequest> {

    @Override
    public boolean isValid(LoginRequest value, ConstraintValidatorContext context) {
        if (value == null) return true; // @NotNull로 처리

        boolean valid = true;
        context.disableDefaultConstraintViolation();

        if (value.getLoginType() == null) {
            context.buildConstraintViolationWithTemplate("loginType은 필수입니다.")
                    .addPropertyNode("loginType").addConstraintViolation();
            valid = false;
        } else if (value.getLoginType() == 0) { // 일반 로그인
            if (value.getLoginId() == null || value.getLoginId().trim().isEmpty()) {
                context.buildConstraintViolationWithTemplate("loginId는 필수입니다.")
                        .addPropertyNode("loginId").addConstraintViolation();
                valid = false;
            }
            if (value.getLoginPw() == null || value.getLoginPw().trim().isEmpty()) {
                context.buildConstraintViolationWithTemplate("loginPw는 필수입니다.")
                        .addPropertyNode("loginPw").addConstraintViolation();
                valid = false;
            }
        } else if (value.getLoginType() == 1) { // 간편 로그인
            if (value.getChannelId() == null || value.getChannelId().trim().isEmpty()) {
                context.buildConstraintViolationWithTemplate("channelId는 필수입니다.")
                        .addPropertyNode("channelId").addConstraintViolation();
                valid = false;
            }
            if (value.getMemberCi() == null || value.getMemberCi().trim().isEmpty()) {
                context.buildConstraintViolationWithTemplate("memberCi는 필수입니다.")
                        .addPropertyNode("memberCi").addConstraintViolation();
                valid = false;
            }
        }
        return valid;
    }
} 