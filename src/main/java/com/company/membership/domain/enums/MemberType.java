package com.company.membership.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberType {
    UNIFIED("UU", "통합회원"),
    REWARDS("R", "Rewards"),
    WEB("W", "Web");
    
    private final String code;
    private final String description;
    
    public static MemberType fromCode(String code) {
        for (MemberType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown member type code: " + code);
    }
    
    public boolean isUnified() {
        return this == UNIFIED;
    }
    
    public boolean isRewards() {
        return this == REWARDS;
    }
    
    public boolean isWeb() {
        return this == WEB;
    }
} 